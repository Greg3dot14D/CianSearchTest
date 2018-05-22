package ru.greg3d.matchers.colections;

import org.hamcrest.Description;
import org.hamcrest.Factory;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;

import java.util.Collection;

/**
 * Created by SBT-Konovalov-GV on 18.10.2017.
 */
public class ListMatchers <E> extends TypeSafeMatcher<E> {

    private Collection<E> collection;

    private abstract static class Matchings{
        public final static String CONTAINS_IN = "contains in";
    }

    private String matching;

    public ListMatchers(Collection<E> collection, String matching){
        this.collection = collection;
        this.matching = matching;
    }

    @Override
    protected boolean matchesSafely(E o) {
        switch (matching) {
            case Matchings.CONTAINS_IN:
                return this.collection.contains(o);
        }
        throw new RuntimeException(String.format("No such matching [%s] to match ", matching));
    }

    @Override
    public void describeTo(Description description) {
        description.appendText(String.format("%s [%s]", matching, collection.toString()));
    }

    @Factory
    public static <E> Matcher<E> containsIn(Collection<E> collection) {
        return new ListMatchers(collection, Matchings.CONTAINS_IN);
    }
}
