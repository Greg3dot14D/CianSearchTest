package ru.greg3d.ui.htmlelements.decorators;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import ru.greg3d.ui.waitings.decorators.*;
import ru.greg3d.ui.waitings.interfaces.ISyncSystemCommand;
import ru.yandex.qatools.htmlelements.element.HtmlElement;
import ru.yandex.qatools.htmlelements.element.TypifiedElement;

import java.lang.reflect.Field;
import java.util.List;

import static ru.yandex.qatools.htmlelements.utils.HtmlElementUtils.*;

/**
 * Created by SBT-Konovalov-GV on 08.08.2017.
 */
public class HtmlDecorators {
    /**
     * Просаживаем прокси с нужной ожидалкой в WebElement
     * @param object - рутовый объект, в котором ищем объекты, в которые можно просадить WebDriver
     * @param driver - экземпляр WebDriver
    //* @param command - команда синхронизации, которая будет вызываться в прокси
     * @param <T> - тип объекта, в котором производит поиск объектов для просадки
     */

    public static <T> void setWaitCommandToObject(T object, WebDriver driver, ISyncSystemCommand command){
        if(object.getClass() == Object.class)
            return;
        try {
            Field[] fields = object.getClass().getDeclaredFields();

            for(Field f: fields){
                f.setAccessible(true);
                if (isHtmlElement(f))
                    ((HtmlElement) f.get(object)).setWrappedElement(
                            new WaitWebElementDecorator(((HtmlElement) f.get(object)).getWrappedElement(), command).decorate());
                    // TODO - Если прокатит, добавить обертку для типового элемента
                else if (isTypifiedElement(f))
                    f.set(object, new WaitTypifiedElementDecorator((TypifiedElement) f.get(object), command).decorate(f.getType()));

                else if (isWebElement(f))
                    f.set(object, new WaitWebElementDecorator(((WebElement) f.get(object)), command).decorate());

                else if (isHtmlElementList(f))
                    f.set(object, new WaitHtmlElementListDecorator(((List<HtmlElement>) f.get(object)), command).decorate());

                else if (isTypifiedElementList(f))
                    f.set(object, new WaitTypifiedElementListDecorator(((List<TypifiedElement>) f.get(object)), command).decorate());

                else if (isWebElementList(f))
                    f.set(object, new WaitWebElementListDecorator(((List<WebElement>) f.get(object)), command).decorate());
                if (isHtmlElement(f))
                    setWaitCommandToObject(f.get(object), driver, command);
            }
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    public static <T> void setWaitCommandToInstance(T object, WebDriver driver, ISyncSystemCommand command){
        if (object instanceof HtmlElement)
            ((HtmlElement) object).setWrappedElement(
                    new WaitWebElementDecorator(((HtmlElement) object).getWrappedElement(), command).decorate());
            // TODO - Если прокатит, добавить обертку для типового элемента
        else if (TypifiedElement.class.isAssignableFrom(object.getClass()))
            object = (T) new WaitTypifiedElementDecorator((TypifiedElement) object, command).decorate(object.getClass());

        else if (object.getClass().isInstance(WebElement.class))
            object = (T) new WaitWebElementDecorator(((WebElement) object), command).decorate();
        // TODO - Добавить работу со списками
//        else if (isHtmlElementList(object))
//            object = (T) new WaitHtmlElementListDecorator(((List<HtmlElement>) object), command).decorate();
//
//        else if (isTypifiedElementList(f))
//            object = (T) new WaitTypifiedElementListDecorator(((List<TypifiedElement>) object), command).decorate();
//
//        else if (List.class.isAssignableFrom(object.getClass()))
//            object = (T) new WaitWebElementListDecorator(((List<WebElement>) object), command).decorate();
//
        if (object.getClass().isInstance(HtmlElement.class))
            setWaitCommandToObject(object, driver, command);
    }


    private static <T> boolean isHtmlElementList(T object){
        if(isList(object))
            return true;
        return false;
    }

    private static <T> boolean isList(T object) {
        return List.class.isAssignableFrom(object.getClass());
    }
}

