package ru.greg3d.ui.waitings.handlers;

import ru.greg3d.ui.waitings.interfaces.ISyncSystemCommand;
import ru.yandex.qatools.htmlelements.element.HtmlElement;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.List;

/**
 * Created by greg3d on 07.05.17.
 */
public class WaitHtmlElementListProxyHandler <T extends List<HtmlElement>> extends BaseWaitProxyHandler implements InvocationHandler {

    public WaitHtmlElementListProxyHandler(T elements, ISyncSystemCommand command){
        super(elements, command);
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        return super.invoke(proxy, method, args);
    }
}
