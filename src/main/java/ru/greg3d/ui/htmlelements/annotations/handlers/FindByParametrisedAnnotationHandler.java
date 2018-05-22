package ru.greg3d.ui.htmlelements.annotations.handlers;

import org.openqa.selenium.By;
import org.openqa.selenium.support.pagefactory.AbstractAnnotations;
import ru.greg3d.ui.htmlelements.annotations.FindByParametrised;
import ru.yandex.qatools.htmlelements.utils.HtmlElementUtils;

import java.lang.reflect.Field;
import java.util.List;

/**
 * Created by sbt-konovalov-gv on 29.01.2018.
 */
public class FindByParametrisedAnnotationHandler extends AbstractAnnotations {

    private Field field;

    public FindByParametrisedAnnotationHandler(Field field) {
        this.field = field;
    }

    @Override
    public By buildBy(){
        return handle(this.field);
    }

    @Override
    public boolean isLookupCached() {
        return false;
    }


    public static By handle(Field field){
        By by = null;
        // Если поле аннотировано FindBy - ем
        if(field.isAnnotationPresent(FindByParametrised.class))
            by = buildByImpl(field);
//            // Если класс аннотирован FindBy - ем
//        else if(field.getType().isAnnotationPresent(ReactFindBy.class))
//            by = buildByClassImpl(field);
        return by;
    }

    private static By buildByImpl(Field field) {
        if(field.isAnnotationPresent(FindByParametrised.class)) {
            String xpath = null;
            Class<?> clazz;
            if(List.class.isAssignableFrom(field.getType()))
                clazz = HtmlElementUtils.getGenericParameterClass(field);
            else
                clazz = field.getType();

            while(clazz != Object.class) {
                try {
                    xpath = clazz.getAnnotation(FindByParametrised.class).xpath();
                    break;
                }catch (Exception e){}
                clazz = clazz.getSuperclass();
            }
            String param1 = field.getAnnotation(FindByParametrised.class).param1();
            String param2 = field.getAnnotation(FindByParametrised.class).param2();
            String param3 = field.getAnnotation(FindByParametrised.class).param3();
            return By.xpath(String.format(xpath, param1, param2, param3));
        }
        throw new RuntimeException(String.format("Cannot determine how to locate field %s", field.getName()));
    }

//    private static By buildByClassImpl(Field field) {
//        Class<?> clazz = field.getType();
//        while (clazz != Object.class) {
//            if (clazz.isAnnotationPresent(ReactFindBy.class)) {
//                // TODO - создаеми By
//                //return buildByFromFindBy(clazz.getAnnotation(FindBy.class));
//                return null;
//            }
//            clazz = clazz.getSuperclass();
//        }
//        throw new RuntimeException(String.format("Cannot determine how to locate instance of %s", field.getType()));
//    }
}
