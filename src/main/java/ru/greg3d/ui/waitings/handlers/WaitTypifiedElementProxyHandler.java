package ru.greg3d.ui.waitings.handlers;

import ru.greg3d.ui.waitings.interfaces.ISyncSystemCommand;
import ru.yandex.qatools.htmlelements.element.TypifiedElement;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

/**
 * Created by greg3d on 07.05.17.
 */

// TODO - аттовизм (тянется от WebElementProxy)
public class WaitTypifiedElementProxyHandler <T extends TypifiedElement> extends BaseWaitProxyHandler implements InvocationHandler {

    public WaitTypifiedElementProxyHandler(T element, ISyncSystemCommand command){
        super(element, command);
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        return super.invoke(proxy, method, args);
    }
}
