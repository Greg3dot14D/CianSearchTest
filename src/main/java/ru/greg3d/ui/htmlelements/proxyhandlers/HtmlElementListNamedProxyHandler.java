package ru.greg3d.ui.htmlelements.proxyhandlers;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.pagefactory.ElementLocator;
import ru.yandex.qatools.htmlelements.element.HtmlElement;
import ru.yandex.qatools.htmlelements.loader.decorator.HtmlElementDecorator;
import ru.yandex.qatools.htmlelements.loader.decorator.HtmlElementLocatorFactory;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

/**
 * Modified by sbt-konovalov-gv on 20.02.2018.
 */
public class HtmlElementListNamedProxyHandler<T extends HtmlElement> implements InvocationHandler {
    private final Class<T> htmlElementClass;
    private final ElementLocator locator;
    private final String name;

    public HtmlElementListNamedProxyHandler(Class<T> htmlElementClass, ElementLocator locator, String name) {
        this.htmlElementClass = htmlElementClass;
        this.locator = locator;
        this.name = name;
    }

    public Object invoke(Object o, Method method, Object[] objects) throws Throwable {
        if("toString".equals(method.getName())) {
            return this.name;
        } else {
            LinkedList htmlElements = new LinkedList();
            List elements = this.locator.findElements();

            Iterator e = elements.iterator();

            while(e.hasNext()) {
                WebElement element = (WebElement)e.next();

                HtmlElement htmlElement = this.htmlElementClass.newInstance();
                htmlElement.setWrappedElement(element);
                String htmlElementName = String.format("%s [%d]", new Object[]{this.name, Integer.valueOf(elements.indexOf(element))});
                htmlElement.setName(htmlElementName);

                PageFactory.initElements(new HtmlElementDecorator(new HtmlElementLocatorFactory(element)), htmlElement);

                //ParametrisedElementDecorator.setFindByParametrisedToObject(htmlElement, element);

                htmlElements.add(htmlElement);
            }

            try {
                return method.invoke(htmlElements, objects);
            } catch (InvocationTargetException ex) {
                throw ex.getCause();
            }
        }
    }
}
