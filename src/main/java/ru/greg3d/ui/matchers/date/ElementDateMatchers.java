package ru.greg3d.ui.matchers.date;

import org.hamcrest.Description;
import org.hamcrest.Factory;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import ru.greg3d.ui.interfaces.IDateFormated;
import ru.greg3d.ui.interfaces.IText;
import ru.greg3d.helpers.SimpleDateHelper;
import ru.yandex.qatools.htmlelements.element.Named;

import java.util.Date;

/**
 * Created by SBT-Konovalov-GV on 12.10.2017.
 */
public class ElementDateMatchers<T extends IDateFormated> extends TypeSafeMatcher<T> {

    private abstract static class Matchings{
        public final static String AFTER = "is Arter";
        public final static String BEFORE = "is Before";
        public final static String EQUALS = "is Equal";

        public final static String AFTER_OR_EQUAL = "is Arter or Equal";
        public final static String BEFORE_OR_EQUAL = "is Before or Equal";
    }

    private Date date;
    private String matching;
    private static IDateFormated element;

    ElementDateMatchers(Date date, String matching){
        this.date = date;
        this.matching = matching;
    }

    @Override
    protected boolean matchesSafely(T element) {
        if(element != null)
            this.element = element;
        SimpleDateHelper helper = new SimpleDateHelper(element.getDateFormat());
        switch (matching) {
            case Matchings.AFTER:
                return helper.setDateTime(((IText)element).getText()).toMillis() > helper.setDateTime(date).toMillis();
            case Matchings.BEFORE:
                return helper.setDateTime(((IText)element).getText()).toMillis() < helper.setDateTime(date).toMillis();
            case Matchings.EQUALS:
                return helper.setDateTime(((IText)element).getText()).toMillis() == helper.setDateTime(date).toMillis();
            case Matchings.AFTER_OR_EQUAL:
                return helper.setDateTime(((IText)element).getText()).toMillis() >= helper.setDateTime(date).toMillis();
            case Matchings.BEFORE_OR_EQUAL:
                return helper.setDateTime(((IText)element).getText()).toMillis() <= helper.setDateTime(date).toMillis();
        }
        throw new RuntimeException(String.format("No such matching [%s] to match ", matching));
    }

    @Override
    public void describeTo(Description description) {
        description.appendText(String.format("%s [%s] %s [%s]", element.getClass().getSimpleName(), ((Named) this.element).getName(), this.matching
                , SimpleDateHelper.getInstance(element.getDateFormat()).setDateTime(this.date).toString()
        ));
    }

    @Override
    public void describeMismatchSafely(T element, Description mismatchDescription) {
        mismatchDescription.appendText(String.format(
                "%s [%s] is [%s]", element.getClass().getSimpleName(), ((Named)element).getName(), ((IText)element).getText()));
                //" is [%s]", ((IText)element).getText()));
    }

    @Factory
    public static Matcher<IDateFormated> isAfter(Date date) {
        return new ElementDateMatchers(date, Matchings.AFTER);
    }

    @Factory
    public static Matcher<IDateFormated> isAfterOrEqual(Date date) {
        return new ElementDateMatchers(date, Matchings.AFTER_OR_EQUAL);
    }


    @Factory
    public static Matcher<IDateFormated> isBefore(Date date) {
        return new ElementDateMatchers(date, Matchings.BEFORE);
    }

    @Factory
    public static Matcher<IDateFormated> isBeforeOrEqual(Date date) {
        return new ElementDateMatchers(date, Matchings.BEFORE_OR_EQUAL);
    }

    @Factory
    public static Matcher<IDateFormated> isEqual(Date date) {
        return new ElementDateMatchers(date, Matchings.EQUALS);
    }
}
