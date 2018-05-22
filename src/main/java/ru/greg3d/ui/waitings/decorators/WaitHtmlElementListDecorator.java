package ru.greg3d.ui.waitings.decorators;

import ru.greg3d.ui.waitings.interfaces.ISyncSystemCommand;
import ru.greg3d.ui.waitings.handlers.WaitHtmlElementListProxyHandler;
import ru.yandex.qatools.htmlelements.element.HtmlElement;
import ru.yandex.qatools.htmlelements.loader.decorator.ProxyFactory;

import java.lang.reflect.InvocationHandler;
import java.util.List;

/**
 * Created by greg3d on 07.05.17.
 */
public class WaitHtmlElementListDecorator<T extends HtmlElement>{
    private List<T> elements;
    private ISyncSystemCommand command;

    public WaitHtmlElementListDecorator(List<T> elements, ISyncSystemCommand command) {
        this.elements = elements;
        this.command = command;
    }

    public List<T> decorate(){
        InvocationHandler handler = new WaitHtmlElementListProxyHandler(elements, this.command);
        ClassLoader loader = elements.getClass().getClassLoader();
        return ProxyFactory.createHtmlElementListProxy(loader, handler);
    }
}
