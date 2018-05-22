package ru.greg3d.ui.annotations.handlers;

import org.openqa.selenium.By;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.FindBys;
import org.openqa.selenium.support.pagefactory.AbstractAnnotations;

import java.lang.reflect.Field;

/**
 * Created by SBT-Konovalov-GV on 31.05.2017.
 */
public class DefaultFieldFindByAnnotationHandler extends AbstractAnnotations {

    protected Field field;

    public DefaultFieldFindByAnnotationHandler(Field field){
        this.field = field;
    }

    @Override
    public By buildBy() {
        if(field.isAnnotationPresent(FindBy.class))
            return buildByFromFindBy(field.getAnnotation(FindBy.class));
        else if (field.isAnnotationPresent(FindBys.class)) {
            FindBys findBys = field.getAnnotation(FindBys.class);
            return buildByFromFindBys(findBys);
        }
        throw new RuntimeException(String.format("Cannot determine how to locate field %s", field.getName()));
    }

    @Override
    public boolean isLookupCached() {
        return false;
    }
}
