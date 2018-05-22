package ru.greg3d.utils;

import java.lang.reflect.Field;

/**
 * Created by SBT-Konovalov-GV on 31.05.2017.
 */
public class InstanceUtils {

    public static boolean isOrChildOfClass(Class clazz, Class classOf){
        while (clazz != Object.class) {
            if(clazz == null)
                return false;
            if (clazz.equals(classOf)){
                return true;
            }
            clazz = clazz.getSuperclass();
        }
        return false;
    }

    public static boolean fieldTypeIsOrChildOfClass(Field field, Class classOf){
        return isOrChildOfClass(field.getType(), classOf);
    }
}
