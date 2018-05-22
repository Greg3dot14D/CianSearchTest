package ru.greg3d.helpers.model;

import ru.greg3d.annotations.Alias;

import java.lang.reflect.Field;
import java.util.*;

/**
 * Created by SBT-Konovalov-GV on 12.10.2017.
 */
public class BaseModel implements Cloneable{

    private Matchers matchers;

    public Matchers matchers(){
        if(this.matchers == null)
            this.matchers = new Matchers();
        return this.matchers;
    }

    private Map<String, Field> fieldMap;

    @Override
    public Object clone(){
        try {
            Object o = super.clone();
            ((BaseModel)o).matchers = null;
            return o;
        } catch (CloneNotSupportedException e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    private void clearMatchers(){
        this.matchers = null;
    }

    public void clear(){
        this.clearMatchers();
        Field [] fields = this.getClass().getDeclaredFields();
        for(Field f: fields){
            try {
                if(f.isAnnotationPresent(Alias.class))
                    f.set(this, null);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
    }

    public Map<String, Field> getFieldMap() {
        if (this.fieldMap == null) {
            this.fieldMap = new HashMap<>();
            for (Field f : this.getClass().getDeclaredFields()) {
                if (f.isAnnotationPresent(Alias.class))
                    this.fieldMap.put(f.getAnnotation(Alias.class).value(), f);
                else
                    this.fieldMap.put(f.getName(), f);
            }
        }
        return this.fieldMap;
    }

    public List<String> toList() {
        List<String> list = new ArrayList<>();

        Set<String> keys = this.getFieldMap().keySet();

        for (String key : keys)
            list.add(this.getFieldValueToStringByAlias(key));
        return list;
    }

    public Map<String, String> toMap() {
        Map<String, String> result = new HashMap<>();

        Set<String> keys = this.getFieldMap().keySet();

        for (String key : keys) {
            result.put(key, Optional.ofNullable(this.getFieldValueToStringByAlias(key)).orElse("null"));
        }
        return result;
    }

    public String getFieldValueToStringByAlias(String aliasName) {
        return Optional.ofNullable(getFieldValueByAlias(aliasName)).orElse("").toString();
    }

    public Object getFieldValueByAlias(String aliasName){
        Field[] fields = this.getClass().getDeclaredFields();

        try {
            for (Field f : fields) {
                if (f.isAnnotationPresent(Alias.class)) {
                    if (aliasName.equals(f.getAnnotation(Alias.class).value()))
                        return f.get(this);
                }
                else{
                    try{
                        if(aliasName.equals(f.getName()))
                            return f.get(this);
                    }catch (NullPointerException e){
                        System.err.println(e.toString());
                    }
                }
            }
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e.getStackTrace().toString());
        }
        throw new RuntimeException(String.format("No such Alias name [%s] exception", aliasName));
    }

    public Field getFieldByAlias(String aliasName) {
        return this.getFieldMap().get(aliasName);
    }

    public void setValueByAlias(String aliasName, Object object){
        try {
            //System.out.println(String.format("[%s] = [%s] %s", aliasName, object, object.getClass()));
            this.getFieldByAlias(aliasName).set(this, object);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e.getMessage());
        }
    }


    private String equalsErrorText = " ";

    public String getEqualsErrorText() {
        return this.equalsErrorText;
    }

    public <T extends BaseModel> boolean equals(T m) {
        if (this.hashCode() == m.hashCode())
            return true;
        StringBuilder sb = new StringBuilder("");
        Set<String> keys = this.getFieldMap().keySet();
        if (keys != null)
            for (String key : keys) {
                Object object = this.getFieldValueByAlias(key);
                Object matcher = m.getFieldValueByAlias(key);
                if (object != null && matcher == null) {
                    sb.append(String.format("\nЗначения поля [%s]  не совпадают, o1 = [%s] o2 = null", key, object));
                }
                if (object != null && matcher != null) {
                    if (!object.equals(matcher)) {
                        sb.append(String.format("\nЗначения поля [%s]  не совпадают, o1 = [%s] o2 =[%s]", key, object, matcher));
                    }
                }
            }
        this.equalsErrorText = sb.toString();
        return "".equals(this.equalsErrorText);
    }

    public <T extends BaseModel> boolean match(T m) {
        if (this.hashCode() == m.hashCode())
            return true;
        Set<String> keys = this.getFieldMap().keySet();
        if (keys != null)
            for (String key : keys) {
                Object object = this.getFieldValueByAlias(key);
                Object matcher = m.getFieldValueByAlias(key);
                try {
                    if (!m.matchers().match(this, key))
                        return false;
                }catch(NullPointerException e){}
                if (object != null && matcher != null)
                    if (!object.equals(matcher))
                        return false;
                if(matcher != null && object == null)
                    return false;
            }
        return true;
    }
}

