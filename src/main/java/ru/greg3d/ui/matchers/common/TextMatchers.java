package ru.greg3d.ui.matchers.common;

import ru.greg3d.ui.interfaces.IText;
import org.hamcrest.Description;
import org.hamcrest.Factory;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import ru.yandex.qatools.htmlelements.element.Named;

import static org.hamcrest.core.Is.is;

/**
 * Created by SBT-Konovalov-GV on 20.07.2017.
 */
public class TextMatchers<T extends IText> extends TypeSafeMatcher<T> {

    private String actionName;
    private String text;

    private Matcher<?> m;

    public TextMatchers(Matcher<?> m){
        this.m = m;
    }

    @Override
    protected boolean matchesSafely(T element) {
        if(m != null) {
            this.actionName = String.format
                    ("%s [%s] text ", element.getClass().getSimpleName(), ((Named)element).getName());
            this.text = m.toString().replace("\"", "'");
            return m.matches(element.getText());
        }

        throw new RuntimeException(String.format("No such action to match [%s]",actionName));
    }

    @Override
    public void describeTo(Description description) {
        description.appendText(String.format("%s ", actionName )).appendValue(this.text);
    }

    @Override
    public void describeMismatchSafely(T element, Description mismatchDescription) {
        if(m == null)
        mismatchDescription.appendText(String.format
                ("%s [%s] %s", element.getClass().getSimpleName(), ((Named)element).getName(), this.actionName))
                .appendValue(element.getText());
        else
            mismatchDescription.appendText(this.actionName)
                    .appendValue(element.getText());
    }

    /**
     * Creates matcher that checks if element is currently enabled.
     */

    @Factory
    public static <T extends IText> Matcher<T> text(Matcher<?> m) {
        return new TextMatchers(m);
    }

    @Factory
    public static <T extends IText> Matcher<T> text(String text) {
        return new TextMatchers(is(text));
    }
}
