package ru.greg3d.ui.htmlelements.decorators;

import org.openqa.selenium.SearchContext;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.pagefactory.ElementLocator;
import ru.greg3d.ui.abstracts.locators.ParametrisedElementLocator;
import ru.greg3d.ui.htmlelements.annotations.FindByParametrised;
import ru.greg3d.ui.htmlelements.annotations.handlers.FindByParametrisedAnnotationHandler;
import ru.greg3d.ui.htmlelements.proxyhandlers.HtmlElementListNamedProxyHandler;
import ru.yandex.qatools.htmlelements.annotations.Name;
import ru.yandex.qatools.htmlelements.element.HtmlElement;
import ru.yandex.qatools.htmlelements.element.Named;
import ru.yandex.qatools.htmlelements.loader.decorator.HtmlElementDecorator;
import ru.yandex.qatools.htmlelements.loader.decorator.HtmlElementLocatorFactory;
import ru.yandex.qatools.htmlelements.loader.decorator.ProxyFactory;
import ru.yandex.qatools.htmlelements.loader.decorator.proxyhandlers.WebElementNamedProxyHandler;
import ru.yandex.qatools.htmlelements.utils.HtmlElementUtils;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationHandler;
import java.util.List;

import static ru.yandex.qatools.htmlelements.loader.decorator.ProxyFactory.createHtmlElementListProxy;
import static ru.yandex.qatools.htmlelements.utils.HtmlElementUtils.isHtmlElement;
import static ru.yandex.qatools.htmlelements.utils.HtmlElementUtils.isHtmlElementList;

/**
 * Created by sbt-konovalov-gv on 29.01.2018.
 */
public class ParametrisedElementDecorator {

    public static <T, S extends SearchContext> void setFindByParametrisedToObject(T object, S searchContext){
        if (object.getClass() == Object.class)
            return;
        Field[] fields = object.getClass().getDeclaredFields();

        try {
            for (Field f : fields) {
                f.setAccessible(true);

                if (isHtmlElement(f)) {
                    SearchContext context = setParametrisedElementDecorator(f, object, searchContext);
                    if(f.isAnnotationPresent(FindByParametrised.class))
                        setFindByParametrisedToObject(f.get(object), context);
                    else
                        setFindByParametrisedToObject(f.get(object), (SearchContext) f.get(object));
                }
                // TODO - вкрутить обработку листов
                if(isHtmlElementList(f)){
                    String name = getFieldName(f);

                    Class<?> elementClass = (Class<?>) HtmlElementUtils.getGenericParameterClass(f);

                    ElementLocator locator;
                    if(f.isAnnotationPresent(FindByParametrised.class))
                        locator = new ParametrisedElementLocator(searchContext, new FindByParametrisedAnnotationHandler(f));
                    else
                        locator = (new HtmlElementLocatorFactory(searchContext)).createLocator(f);

                    InvocationHandler handler = new HtmlElementListNamedProxyHandler(elementClass, locator, name);
                    ClassLoader loader = object.getClass().getClassLoader();

                    List<?> list = createHtmlElementListProxy(loader, handler);
                    f.set(object, list);
                }
            }
        }catch(IllegalAccessException | InstantiationException e){
            throw new RuntimeException(e);
        }
    }

    private static String getFieldName(Field f){
        if(f.isAnnotationPresent(Name.class))
            return f.getAnnotation(Name.class).value();
        return f.getName();
    }

    private static <T extends SearchContext> T setParametrisedElementDecorator(Field f, Object object, T searchContext) throws IllegalAccessException, InstantiationException {
        if(f.isAnnotationPresent(FindByParametrised.class)) {

            f.set(object, f.getType().newInstance());
            if(f.isAnnotationPresent(Name.class)){
                ((HtmlElement)f.get(object)).setName(f.getAnnotation(Name.class).value());
            }

            ElementLocator locator = new ParametrisedElementLocator(searchContext, new FindByParametrisedAnnotationHandler(f));
            ClassLoader loader = object.getClass().getClassLoader();
            InvocationHandler handler = new WebElementNamedProxyHandler(locator, ((Named)f.get(object)).getName());

            WebElement wrappedElement = ProxyFactory.createWebElementProxy(loader, handler);

            ((HtmlElement)f.get(object)).setWrappedElement(wrappedElement);

            PageFactory.initElements(
                    new HtmlElementDecorator(
                            new HtmlElementLocatorFactory(wrappedElement)), f.get(object));

            return (T)wrappedElement;
        }
        return searchContext;
    }
}
