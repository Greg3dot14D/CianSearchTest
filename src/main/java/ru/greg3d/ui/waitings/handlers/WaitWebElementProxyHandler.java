package ru.greg3d.ui.waitings.handlers;

import org.openqa.selenium.WebElement;
import ru.greg3d.ui.waitings.interfaces.ISyncSystemCommand;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

/**
 * Created by greg3d on 07.05.17.
 */
public class WaitWebElementProxyHandler <T extends WebElement> extends BaseWaitProxyHandler implements InvocationHandler {

    public WaitWebElementProxyHandler(T element, ISyncSystemCommand command){
        super(element, command);
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        return super.invoke(proxy, method, args);
    }
}
