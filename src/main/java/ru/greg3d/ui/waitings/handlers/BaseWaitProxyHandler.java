package ru.greg3d.ui.waitings.handlers;

import org.openqa.selenium.WebDriverException;
import ru.greg3d.ui.waitings.interfaces.ISyncSystemCommand;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * Created by greg3d on 08.05.17.
 */
public class BaseWaitProxyHandler<T> implements InvocationHandler{

        private T element;
        private ISyncSystemCommand command;

    public BaseWaitProxyHandler(T element, ISyncSystemCommand command){
        this.element = element;
        this.command = command;
    }

        @Override
        public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
            return command.supply(() -> {
                try {
                    return method.invoke(element, args);
                } catch (InvocationTargetException | IllegalAccessException e) {
                    ///throw e.getCause();
                    throw new WebDriverException(String.format("[%s] try invoke exception for element [%s] method [%s]\n%s", this.getClass().getSimpleName(), element.toString(), method.getName(), e.getCause().getMessage()));
                }
            });
    }
}
