package ru.greg3d.ui.waitings.decorators;

import org.openqa.selenium.WebElement;
import ru.greg3d.ui.waitings.interfaces.ISyncSystemCommand;
import ru.greg3d.ui.waitings.handlers.WaitWebElementProxyHandler;
import ru.yandex.qatools.htmlelements.loader.decorator.ProxyFactory;

import java.lang.reflect.InvocationHandler;

/**
 * Created by greg3d on 07.05.17.
 */
public class WaitWebElementDecorator {
    private WebElement element;
    private ISyncSystemCommand command;

    public WaitWebElementDecorator(WebElement element, ISyncSystemCommand command) {
        this.element = element;
        this.command = command;
    }

    public WebElement decorate(){
        InvocationHandler handler = new WaitWebElementProxyHandler(this.element, this.command);
        ClassLoader loader = element.getClass().getClassLoader();
        return ProxyFactory.createWebElementProxy(loader, handler);
    }
}
