package ru.greg3d.ui.decorators;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.FindBy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.greg3d.annotations.DateFormat;
import ru.greg3d.ui.interfaces.IDateFormated;
import ru.greg3d.ui.interfaces.IDriverable;
import ru.greg3d.ui.interfaces.IGridContainer;
import ru.greg3d.ui.interfaces.IParentable;
import ru.yandex.qatools.htmlelements.element.HtmlElement;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;

/**
 * Created by SBT-Konovalov-GV on 23.01.2017.
 *
 * PageDecorator создает и возвращает объект, содержащий поля с типом "Страница" и инициализирует страницы фабричным методом
 */
public class PageDecorator {

    private static Logger LOG = LoggerFactory.getLogger(PageDecorator.class);

    /**
     * Просаживаем WebDriver в объекты, реализующие интерфейс IDriverable
     *
     * @param object - рутовый объект, в котором ищем объекты, в которые можно просадить WebDriver
     * @param driver - экземпляр WebDriver
     * @param rootClass - для какого класса или наследника можно вызывать рекурсию
     * @param <T> - тип объекта, в котором производит поиск объектов для просадки
     */
    public static <T> void setDriverToObject(T object, WebDriver driver, Class rootClass){
        if(object.getClass() == Object.class)
            return;
        try {
            // Создаем и инициализируем страницы
            Field[] fields = object.getClass().getDeclaredFields();

            for(Field f: fields){
                f.setAccessible(true);
                if(f.get(object) instanceof IDriverable)
                    ((IDriverable) f.get(object)).setDriver(driver);

                if(rootClass.isInstance(f.get(object)))
                    setDriverToObject(f.get(object), driver, rootClass);
//                if(SiebelElementUtils.fieldTypeIsOrChildOfClass(f, clazz)){
//                    ((IDriverable) f.get(object)).setDriver(driver);
//                }
//                if(SiebelElementUtils.fieldTypeIsOrChildOfClass(f, rootClass))
//                    setDriverToObject(f.get(object), driver, clazz, rootClass);
            }
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    public static <T> void setDriverToObject(T object, WebDriver driver){
        if(object.getClass() == Object.class)
            return;
        try {
            // Создаем и инициализируем страницы
            Field[] fields = object.getClass().getDeclaredFields();

            for(Field f: fields){
                f.setAccessible(true);
                if(f.get(object) instanceof IDriverable)
                    ((IDriverable) f.get(object)).setDriver(driver);
                if(HtmlElement.class.isInstance(f.get(object)))
                    setDriverToObject(f.get(object), driver);
            }
        //} catch (Exception e) {
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    public static <T> void setParentToObject(T object, Class rootClass){
        if(object.getClass() == Object.class)
            return;
        try {
            // Создаем и инициализируем страницы
            Field[] fields = object.getClass().getDeclaredFields();

            for(Field f: fields){
                f.setAccessible(true);

                if(f.get(object) instanceof IParentable)
                    ((IParentable) f.get(object)).setParent(object);

                if(rootClass.isInstance(f.get(object)))
                    setParentToObject(f.get(object), rootClass);
            }
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

//    /**
//     * Просаживаем прокси с нужной ожидалкой в WebElement
//     * @param object - рутовый объект, в котором ищем объекты, в которые можно просадить WebDriver
//     * @param driver - экземпляр WebDriver
//     * @param rootClass - для какого класса или наследника можно вызывать рекурсию
//     * @param command - команда синхронизации, которая будет вызываться в прокси
//     * @param <T> - тип объекта, в котором производит поиск объектов для просадки
//     */
//
//    public static <T> void setWaitCommandToObject(T object, WebDriver driver, Class rootClass, ISyncSystemCommand command){
//        if(object.getClass() == Object.class)
//            return;
//        try {
//            Field[] fields = object.getClass().getDeclaredFields();
//
//            for(Field f: fields){
//                f.setAccessible(true);
//                if (isHtmlElement(f))
//                    ((HtmlElement) f.get(object)).setWrappedElement(
//                            new WaitWebElementDecorator(((HtmlElement) f.get(object)).getWrappedElement(), command).decorate());
//                    // TODO - Если прокатит, добавить обертку для типового элемента
//                else if (isTypifiedElement(f))
//                    f.set(object, new WaitTypifiedElementDecorator((TypifiedElement) f.get(object), command).decorate(f.getType()));
//
//                else if (isWebElement(f))
//                    f.set(object, new WaitWebElementDecorator(((WebElement) f.get(object)), command).decorate());
//
//                else if (isHtmlElementList(f))
//                    f.set(object, new WaitHtmlElementListDecorator(((List<HtmlElement>) f.get(object)), command).decorate());
//
//                else if (isTypifiedElementList(f))
//                    f.set(object, new WaitTypifiedElementListDecorator(((List<TypifiedElement>) f.get(object)), command).decorate());
//
//                else if (isWebElementList(f))
//                    f.set(object, new WaitWebElementListDecorator(((List<WebElement>) f.get(object)), command).decorate());
//                ////if(rootClass.isInstance(f.get(object)))
//                if (isHtmlElement(f))
//                    setWaitCommandToObject(f.get(object), driver, rootClass, command);
//
//            }
//        } catch (IllegalAccessException e) {
//            throw new RuntimeException(e.getMessage());
//        }
//    }

    public static <T> void setGrid(T page){
        Field[] fields = page.getClass().getFields();
        for (Field f: fields){
            f.setAccessible(true);
            try {
                Object o = f.get(page);
                if(o instanceof IGridContainer)
                    ((IGridContainer)o).setGrid();
                if(o instanceof HtmlElement)
                    setGrid(o);
            } catch (IllegalAccessException e) {
                throw new RuntimeException(e.getMessage());
            }
        }
    }

    public static <T> void setDateFormat(T page){
        Field[] fields = page.getClass().getDeclaredFields();
        for (Field f: fields){
            f.setAccessible(true);
            try {
                Object o = f.get(page);
                if(o != null) {
                    if (o instanceof IDateFormated)
                        if (f.isAnnotationPresent(DateFormat.class)) {
                            ((IDateFormated) o).setDateFormat(f.getAnnotation(DateFormat.class).value());
                        }
                    if (HtmlElement.class.isInstance(o))
                        setDateFormat(o);
                }
            } catch (IllegalAccessException e) {
                throw new RuntimeException(e.getMessage());
            }
        }
    }
}
