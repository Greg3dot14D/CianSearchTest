package ru.greg3d.matchers.date;

import org.hamcrest.Description;
import org.hamcrest.Factory;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;

import java.util.Date;

/**
 * Created by SBT-Konovalov-GV on 13.10.2017.
 */
public class DateMatchers <T extends Date> extends TypeSafeMatcher<T> {

    private abstract static class Matchings{
        public final static String AFTER = "is Arter";
        public final static String BEFORE = "is Before";
        public final static String EQUALS = "is Equal";

        public final static String AFTER_OR_EQUAL = "is Arter or Equal";
        public final static String BEFORE_OR_EQUAL = "is Before or Equal";
    }

    private Date date;
    private String matching;

    DateMatchers(Date date, String matching){
        this.date = date;
        this.matching = matching;
    }

    @Override
    protected boolean matchesSafely(Date dateToMatch) {
        switch (matching) {
            case Matchings.AFTER:
                return dateToMatch.getTime() > date.getTime();
            case Matchings.BEFORE:
                return dateToMatch.getTime() < date.getTime();
            case Matchings.EQUALS:
                return dateToMatch.getTime() == date.getTime();
            case Matchings.AFTER_OR_EQUAL:
                return dateToMatch.getTime() >= date.getTime();
            case Matchings.BEFORE_OR_EQUAL:
                return dateToMatch.getTime() <= date.getTime();
        }
        throw new RuntimeException(String.format("No such matching [%s] to match ", matching));
    }

    @Override
    public void describeTo(Description description) {
        description.appendText(String.format("%s [%s]", matching, date));
    }

    @Override
    public void describeMismatchSafely(T date, Description mismatchDescription) {
        mismatchDescription.appendText(String.format
                ("was [%s]", date)
        );
    }

    @Factory
    public static Matcher<Date> isAfter(Date date) {
        return new DateMatchers(date, Matchings.AFTER);
    }

    @Factory
    public static Matcher<Date> isBefore(Date date) {
        return new DateMatchers(date, Matchings.BEFORE);
    }

    @Factory
    public static Matcher<Date> isAfterOrEqual(Date date) {
        return new DateMatchers(date, Matchings.AFTER_OR_EQUAL);
    }

    @Factory
    public static Matcher<Date> isBeforeOrEqual(Date date) {
        return new DateMatchers(date, Matchings.BEFORE_OR_EQUAL);
    }
}
