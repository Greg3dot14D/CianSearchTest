DECLARE
  TYPE COL_TYPE IS RECORD (name VARCHAR2(50), type VARCHAR2(50), default_value VARCHAR2(100));
  TYPE COL_LIST_TYPE IS TABLE OF COL_TYPE;
  TYPE LIST_TYPE IS TABLE OF VARCHAR2(600);

  TAB VARCHAR2(4) := '    ';
  A VARCHAR(1) := chr(64);

  properties LIST_TYPE := LIST_TYPE();
  methods LIST_TYPE := LIST_TYPE();
  cols COL_LIST_TYPE := COL_LIST_TYPE();


  ---*-*---
    --*--
  pSHEME varchar2(50) DEFAULT 'SIEBEL';
  pTABLE varchar2(50) DEFAULT '&TABLE_NAME';
  pCLASS_NAME varchar2(50) DEFAULT '&CLASS_NAME';
    --*--
  ---*-*---


  --print helper
  PROCEDURE print(message VARCHAR2 DEFAULT '')
  IS
  BEGIN
    dbms_output.put_line(message);
  END;

  PROCEDURE printt(message VARCHAR2 DEFAULT '')
  IS
  BEGIN
    dbms_output.put(message);
  END;



  --convert name to camelCase (or CamelCase)
  FUNCTION clean_name(name VARCHAR2, upperCamelCase boolean := false) RETURN VARCHAR2
  IS
    cleanName VARCHAR2(50);
  BEGIN

    cleanName := REPLACE(INITCAP(name), '_', '');

    IF NOT upperCamelCase THEN
      cleanName := lower(SUBSTR(cleanName, 1, 1)) || SUBSTR(cleanName, 2);
    END IF;

    RETURN cleanName;
  END;



  --type convertion
  FUNCTION get_type(data_type VARCHAR2) RETURN VARCHAR2
  IS
    import_string VARCHAR2(70);
  BEGIN
    CASE data_type
      WHEN 'VARCHAR2' THEN
        import_string := 'String';
      WHEN 'NUMBER' THEN
        import_string := 'Long';
      WHEN 'CHAR' THEN
        import_string := 'String';
      WHEN 'CLOB' THEN
        import_string := 'String';
      WHEN 'DATE' THEN
        import_string := 'Timestamp';
    END CASE;

    return import_string;
  END;



  --property declaration generation
  FUNCTION get_property(name VARCHAR2, type VARCHAR2) RETURN VARCHAR2
  IS
  BEGIN
    RETURN 'private ' || get_type(type) || ' ' || clean_name(name) ||';';
  END;



  --getter generation
  FUNCTION get_getter(column_name VARCHAR2, type VARCHAR2, nullable VARCHAR2, length NUMBER) RETURN VARCHAR2
  IS
    getter VARCHAR2(400);
    java_type VARCHAR2(10);
  BEGIN
    java_type := get_type(type);

    getter := TAB || '@Basic' || chr(10) || TAB || '@Column(name = "' || column_name || '"';

    IF nullable = 'Y' THEN
      getter := getter || ', nullable = true';
    ELSE
      getter := getter || ', nullable = false';
    END IF;


    IF type <> 'NUMBER' AND type <> 'DATE' THEN
      getter := getter || ', length = ' || length;
    END IF;

    getter := getter || ')' || chr(10);

    getter := getter || TAB || 'public ' || java_type || ' get' || clean_name(column_name, true) || '(){' || chr(10);
    getter := getter || TAB || TAB || 'return ' || clean_name(column_name) || ';' || chr(10);
    getter := getter || TAB || '}';

    RETURN getter;
  END;



  --setter generation
  FUNCTION get_setter(column_name VARCHAR2, type VARCHAR2, returnThis BOOLEAN DEFAULT true) RETURN VARCHAR2
  IS
    setter VARCHAR2(400);
    name VARCHAR2(50);
  BEGIN
    name := clean_name(column_name);

    IF returnThis THEN
      setter := 'public ' || clean_name(pTABLE, true) || ' ';
    ELSE
      setter := 'public void ';
    END IF;

    setter := TAB || setter || 'set' || clean_name(column_name, true) || '(' || get_type(type) || ' ' || name || '){' || chr(10);
    setter := setter || TAB || TAB || 'this.' || name || ' = ' || name || ';';

    IF returnThis THEN
      setter := setter || chr(10) || TAB || TAB || 'return this;';
    END IF;

    setter := setter || chr(10) || TAB || '}';

    RETURN setter;
  END;


  --equals()
  PROCEDURE print_equals_method(columns_list COL_LIST_TYPE)
  IS
    class_name VARCHAR2(50);
    obj_var_name VARCHAR2(50);
    property_name VARCHAR(50);
    line VARCHAR2(200);
    col COL_TYPE;
  BEGIN
    class_name := clean_name(pTABLE, true);
    obj_var_name := clean_name(pTABLE);
    print();
    print(TAB || '@Override');
    print(TAB || 'public boolean equals(Object o) {');

    print(TAB || TAB || 'if (this == o) return true;');

    print(TAB || TAB || 'if (o == null || getClass() != o.getClass()) return false;');

    print(TAB || TAB || class_name || ' ' || obj_var_name || ' = (' || class_name || ') o;');
    --@todo добавить возможность не print'ить проверку по колонкам, специфичным для не Siebel'овых таблиц
    print(TAB || TAB || 'return Objects.equals(rowId, ' || obj_var_name || '.rowId) &&' || '&&');
    print(TAB || TAB || TAB || 'Objects.equals(created, ' || obj_var_name || '.created) &&' || '&&');
    print(TAB || TAB || TAB || 'Objects.equals(createdBy, ' || obj_var_name || '.createdBy) &&' || '&&');
    print(TAB || TAB || TAB || 'Objects.equals(lastUpd, ' || obj_var_name || '.lastUpd) &&' || '&&');
    print(TAB || TAB || TAB || 'Objects.equals(lastUpdBy, ' || obj_var_name || '.lastUpdBy) &&' || '&&');
    print(TAB || TAB || TAB || 'Objects.equals(conflictId, ' || obj_var_name || '.conflictId) &&' || '&&');
    print(TAB || TAB || TAB || 'Objects.equals(dbLastUpd, ' || obj_var_name || '.dbLastUpd) &&' || '&&');
    print(TAB || TAB || TAB || 'Objects.equals(dbLastUpdSrc, ' || obj_var_name || '.dbLastUpdSrc) &&' || '&&');
    print(TAB || TAB || TAB || 'modificationNum == ' || obj_var_name || '.modificationNum &&' || '&&');

    FOR i IN 1 .. columns_list.COUNT
    LOOP
      col := columns_list(i);
      property_name := clean_name(col.name);

      CASE col.type
        WHEN 'NUMBER' THEN
          line := property_name || ' == ' || obj_var_name || '.' || property_name;
        ELSE
          line := 'Objects.equals(' || property_name || ', ' || obj_var_name || '.' || property_name || ')';
      END CASE;

       IF i <> columns_list.COUNT THEN
         line := line || ' &&' || '&&';
       ELSE
         line := line || ';';
       END IF;

       print(TAB || TAB || TAB || line);
    END LOOP;


    print(TAB || '}');
  END;


  --hashCode()
  PROCEDURE print_hash_method(columns_list COL_LIST_TYPE)
  IS
    col COL_TYPE;
  BEGIN
    print();
    print(TAB || '@Override');
    print(TAB || 'public int hashCode() {');

    printt(TAB || TAB || TAB || 'return Objects.hash(rowId, created, createdBy, lastUpd, lastUpdBy, conflictId, dbLastUpd, dbLastUpdSrc, modificationNum, ');

    FOR i IN 1 .. columns_list.COUNT
    LOOP
      col := columns_list(i);
      printt(clean_name(col.name));

      IF i <> columns_list.LAST THEN
        printt(', ');
      ELSE
        printt(');' || chr(10));
      END IF;
    END LOOP;

    print(TAB || '}');
  END;


  --@PrePersist
  PROCEDURE print_onPrePersist_method(columns_list COL_LIST_TYPE)
  IS
    col COL_TYPE;
    property VARCHAR2(100);
  BEGIN
    print(TAB || '@PrePersist');
    print(TAB || 'public void onPrePersist(){');

    FOR i IN 1 .. columns_list.COUNT
    LOOP
      col := columns_list(i);

      IF col.default_value IS NOT NULL THEN
        property := clean_name(col.name);

        IF (col.type = 'VARCHAR2' OR col.type = 'CHAR') AND col.default_value <> 'NULL' THEN
          IF i > 1 THEN print(); END IF;

          print(TAB || TAB || 'if (' || property || ' == null || ' || property || '.equals("")) {');
          print(TAB || TAB || TAB || property || ' = ' || '"' || REGEXP_REPLACE(col.default_value, '['' ]', '') || '";');
          print(TAB || TAB || '}');
        END IF;
      END IF;
    END LOOP;

    print(TAB || '}');
  END;



BEGIN
  FOR col IN(
    SELECT
      column_name name
      , data_type type
      , data_length length
      , data_default /*значение по умолчанию*/
      , nullable
    FROM all_tab_cols
    WHERE owner = pSHEME
      AND table_name = pTABLE
      AND column_name NOT IN(
        'DB_LAST_UPD_SRC', 'DB_LAST_UPD', 'CONFLICT_ID', 'MODIFICATION_NUM', 'LAST_UPD_BY', 'LAST_UPD', 'CREATED_BY', 'CREATED', 'ROW_ID'
      )
      AND column_name NOT LIKE 'SYS_%'
    ORDER BY column_name
  ) LOOP


    cols.EXTEND(1);
    cols(cols.LAST).name := col.name;
    cols(cols.LAST).type := col.type;
    cols(cols.LAST).default_value := substr(col.data_default, 1, 4000);

    properties.EXTEND(1);
    properties(properties.LAST) := get_property(col.name, col.type);

    methods.EXTEND(1);
    methods(methods.LAST) := get_getter(col.name, col.type, col.nullable, col.length);
    methods.EXTEND(1);
    methods(methods.LAST) := get_setter(col.name, col.type);


  END LOOP;



  --CLASS DECLARATION
  print('package models.pojo;');
  print();
  print('import javax.persistence.*;');
  print('import java.sql.Timestamp;');
  print('import java.util.Objects;');
  print();
  print('@Entity');
  print('@Table(name = "' || pTABLE || '", schema = "' || pSHEME || '")');
  print('public class ' || clean_name(pTABLE, true)  || ' extends SiebelBaseEntity {');


  --PROPERTYES
  FOR i IN 1 .. properties.COUNT LOOP
    print(TAB || properties(i));
  END LOOP;

  --METHODS
  FOR i IN 1 .. methods.COUNT LOOP
    print();
    print(methods(i));
  END LOOP;

  print_equals_method(cols);

  print_hash_method(cols);

  print_onPrePersist_method(cols);

  --END CLASS
  print('}');
END;

