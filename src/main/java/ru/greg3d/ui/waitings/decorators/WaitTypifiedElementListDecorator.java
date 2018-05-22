package ru.greg3d.ui.waitings.decorators;

import ru.greg3d.ui.waitings.interfaces.ISyncSystemCommand;
import ru.greg3d.ui.waitings.handlers.WaitTypifiedElementListProxyHandler;
import ru.yandex.qatools.htmlelements.element.TypifiedElement;
import ru.yandex.qatools.htmlelements.loader.decorator.ProxyFactory;

import java.lang.reflect.InvocationHandler;
import java.util.List;

/**
 * Created by greg3d on 07.05.17.
 */
public class WaitTypifiedElementListDecorator <T extends TypifiedElement>{
    private List<T> elements;
    private ISyncSystemCommand command;

    public WaitTypifiedElementListDecorator(List<T> elements, ISyncSystemCommand command) {
        this.elements = elements;
        this.command = command;
    }

    public List<T> decorate(){
        InvocationHandler handler = new WaitTypifiedElementListProxyHandler(elements, this.command);
        ClassLoader loader = elements.getClass().getClassLoader();
        return ProxyFactory.createTypifiedElementListProxy(loader, handler);
    }
}
