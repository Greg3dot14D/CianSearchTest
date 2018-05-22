package ru.greg3d.ui.matchers.common;

import org.hamcrest.Description;
import org.hamcrest.Factory;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import ru.greg3d.ui.interfaces.ICheck;
import ru.yandex.qatools.htmlelements.element.Named;

/**
 * Created by SBT-Konovalov-GV on 07.11.2016.
 * Checks if element is enabled or not.
 */

public class CheckMatchers<T extends ICheck> extends TypeSafeMatcher<T> {

    private abstract static class Actions{
        public final static String CHECKED = "Checked";
        public final static String UNCHECKED = "UnChecked";
        public final static String CHECKED_STATE = "Checked state";
    }

    private String actionName;
    private ICheck element;
    private Object [] args;

    public CheckMatchers(String actionName, Object [] args){
        this.actionName = actionName;
        this.args = args;
    }

    public CheckMatchers(String actionName){
        this.actionName = actionName;
    }

    @Override
    protected boolean matchesSafely(T element) {
        this.element = element;
        switch (actionName) {
            case Actions.CHECKED:
                return element.isChecked();
            case Actions.UNCHECKED:
                return element.unChecked();
            case Actions.CHECKED_STATE:
                this.actionName = String.format("%s is(%s)",this.actionName, args[0]);
                return args[0].equals(element.isChecked());
        }
        throw new RuntimeException(String.format("No such action to match [%s]",actionName));
    }

    @Override
    public void describeTo(Description description) {
        description.appendText(String.format("%s [%s] %s", element.getClass().getSimpleName(), ((Named)this.element).getName(), this.actionName));
    }

    @Override
    public void describeMismatchSafely(T element, Description mismatchDescription) {
            mismatchDescription.appendText(String.format
                    //("%s [%s] not %s", element.getClass().getSimpleName(), ((Named)element).getName(), this.actionName)
                    ("not %s", this.actionName)
            );
    }

    /**
     * Creates matcher that checks if element is currently enabled.
     */
    @Factory
    public static Matcher<ICheck> isChecked() {
        return new CheckMatchers(Actions.CHECKED);
    }

    @Factory
    public static Matcher<ICheck> unChecked() {
        return new CheckMatchers(Actions.UNCHECKED);
    }

    @Factory
    public static Matcher<ICheck> checkState(boolean state) {
        return new CheckMatchers(Actions.CHECKED_STATE, new Object[] {state});
    }
}
