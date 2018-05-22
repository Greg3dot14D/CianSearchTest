package ru.greg3d.ui.annotations.handlers;

import org.openqa.selenium.By;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.FindBys;
import org.openqa.selenium.support.pagefactory.AbstractAnnotations;

/**
 * Created by SBT-Konovalov-GV on 31.05.2017.
 */
public class DefaultClassFindByAnnotationHandler <T> extends AbstractAnnotations{
    protected final Class<T> thisClazz;

    public DefaultClassFindByAnnotationHandler(Class<T> thisClazz){
        this.thisClazz = thisClazz;
    }

    @Override
    public By buildBy() {
        Class<?> clazz = thisClazz;
        while (clazz != Object.class) {
            if (clazz.isAnnotationPresent(FindBy.class)) {
                return buildByFromFindBy(clazz.getAnnotation(FindBy.class));
            }
            else if (clazz.isAnnotationPresent(FindBys.class)) {
                FindBys findBys = clazz.getAnnotation(FindBys.class);
                return buildByFromFindBys(findBys);
            }
            clazz = clazz.getSuperclass();
        }
        throw new RuntimeException(String.format("Cannot determine how to locate instance of %s", thisClazz));
    }

    @Override
    public boolean isLookupCached() {
        return false;
    }
}
