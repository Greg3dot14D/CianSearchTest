package ru.greg3d.ui.matchers.common;

import org.hamcrest.Description;
import org.hamcrest.Factory;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import ru.greg3d.ui.interfaces.IElement;
import ru.yandex.qatools.htmlelements.element.Named;

/**
 * Created by SBT-Konovalov-GV on 07.11.2016.
 * Checks if element is enabled or not.
 */

public class ElementMatchers<T extends IElement> extends TypeSafeMatcher<T> {

    private abstract static class Actions{
        public final static String ENABLED = "Enabled";
        public final static String DISPLAYED = "Displayed";
        public final static String EXIST = "Exist";
    }

    protected String actionName;
    protected Object element;

    public ElementMatchers(String actionName){
        this.actionName = actionName;
    }

    @Override
    protected boolean matchesSafely(T element) {
        this.element = element;
        switch (actionName) {
            case Actions.ENABLED:
                 return element.isEnabled();
            case Actions.DISPLAYED:
                return element.isDisplayed();
            case Actions.EXIST:
                return element.isExist();
        }
        throw new RuntimeException(String.format("No such action to match [%s]", this.actionName));
    }

    @Override
    public void describeTo(Description description) {
        description.appendText(String.format("%s [%s] %s ", element.getClass().getSimpleName(), ((Named)this.element).getName(), this.actionName));
    }

    @Override
    public void describeMismatchSafely(T element, Description mismatchDescription) {
        mismatchDescription.appendText(String.format
                ("not %s", this.actionName)
        );
    }

    /**
     * Creates matcher that checks if element is currently enabled.
     */
    @Factory
    public static Matcher<IElement> isEnabled() {
        return new ElementMatchers(Actions.ENABLED);
    }

    @Factory
    public static Matcher<IElement> isDisplayed() {
        return new ElementMatchers(Actions.DISPLAYED);
    }

    @Factory
    public static Matcher<IElement> isExist() {
        return new ElementMatchers(Actions.EXIST);
    }

}