package ru.greg3d.ui.waitings.decorators;

import org.openqa.selenium.WebElement;
import ru.greg3d.ui.waitings.interfaces.ISyncSystemCommand;
import ru.greg3d.ui.waitings.handlers.WaitWebElementListProxyHandler;
import ru.yandex.qatools.htmlelements.loader.decorator.ProxyFactory;

import java.lang.reflect.InvocationHandler;
import java.util.List;

/**
 * Created by greg3d on 07.05.17.
 */
public class WaitWebElementListDecorator {
    private List<WebElement> elements;
    private ISyncSystemCommand command;

    public WaitWebElementListDecorator(List<WebElement> elements, ISyncSystemCommand command) {
        this.elements = elements;
        this.command = command;
    }

    public List<WebElement> decorate(){
        InvocationHandler handler = new WaitWebElementListProxyHandler(elements, this.command);
        ClassLoader loader = elements.getClass().getClassLoader();
        return ProxyFactory.createWebElementListProxy(loader, handler);
    }
}
