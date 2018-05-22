package ru.greg3d.ui.waitings.handlers;

import org.openqa.selenium.WebElement;
import ru.greg3d.ui.waitings.interfaces.ISyncSystemCommand;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.List;

/**
 * Created by greg3d on 07.05.17.
 */

public class WaitWebElementListProxyHandler <T extends List<WebElement>> extends BaseWaitProxyHandler implements InvocationHandler {

    public WaitWebElementListProxyHandler(T elements, ISyncSystemCommand command){
        super(elements,command);
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        return super.invoke(proxy, method, args);
    }
}
