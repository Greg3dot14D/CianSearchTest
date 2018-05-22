package ru.greg3d.utils;

/**
 * Created by sbt-konovalov-gv on 05.04.2018.
 */
public class XpathUtils {

    /*
    ** get String like "substring(@id, string-length(@id) - string-length('attributeValue') +1) = 'attributeValue'"
    *  to search tag like
    *  <div id="somePrefix_attributeValue"/>
    **
    */
    public static String getEndsWith(String attributeName, String endsWith){
        return String.format("substring(%s, string-length(%s) - string-length('%s') +1) = '%s'"
                , attributeName
                , attributeName
                , endsWith
                , endsWith);
    }
}
