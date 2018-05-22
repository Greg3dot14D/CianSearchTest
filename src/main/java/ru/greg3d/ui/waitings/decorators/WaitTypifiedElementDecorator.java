package ru.greg3d.ui.waitings.decorators;

import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.WebElement;
import ru.greg3d.ui.waitings.interfaces.ISyncSystemCommand;
import ru.yandex.qatools.htmlelements.element.TypifiedElement;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

/**
 * Created by greg3d on 07.05.17.
 */
public class WaitTypifiedElementDecorator <T extends TypifiedElement>{
    private T element;
    private ISyncSystemCommand command;

    public WaitTypifiedElementDecorator(T element, ISyncSystemCommand command) {
        this.element = element;
        this.command = command;
    }

//    public <T> T decorate(){
//        InvocationHandler handler = new WaitTypifiedElementProxyHandler(element, this.command);
//
//        ClassLoader loader = element.getClass().getClassLoader();
//        // TODO - ХЗ, может и ен прокатит с интерфейсами урезанными
//        return SiebelProxyFactory.createTypifiedElementProxy(loader, handler);
//    }

    public <O> O decorate(O clazz){
        //InvocationHandler handler = new WaitTypifiedElementProxyHandler(element, this.command);
        //ClassLoader loader = element.getClass().getClassLoader();
        // TODO - ХЗ, может и ен прокатит с интерфейсами урезанными
        try {
            Constructor c = this.element.getClass().getConstructor(new Class[]{WebElement.class});
            WebElement decoratedElement = new WaitWebElementDecorator(this.element.getWrappedElement(), command).decorate();
            O object = (O)c.newInstance(new Object[]{decoratedElement});
            ((T)object).setName(element.getName());
            return object;
        } catch (NoSuchMethodException e) {
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }
        throw new WebDriverException("decorate typified element exception");
    }
}
