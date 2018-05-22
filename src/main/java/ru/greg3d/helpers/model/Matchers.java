package ru.greg3d.helpers.model;

import org.hamcrest.BaseMatcher;
import org.hamcrest.Matcher;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by SBT-Konovalov-GV on 12.10.2017.
 */
public class Matchers<T, M extends BaseModel>{

    private Map<String, Matcher<T>> fieldMatchers;

    private Map<String, Matcher<T>> getFieldMatchers(){
        if(this.fieldMatchers == null)
            this.fieldMatchers = new HashMap<>();
        return this.fieldMatchers;
    }

    public void putMatcher(String fieldAlias, Matcher<T> matcher){
        this.getFieldMatchers().put(fieldAlias, matcher);
    }

    public boolean match(M model, String fieldAlias){
        try {
            return ((BaseMatcher) getFieldMatchers().get(fieldAlias))
                    .matches(model.getFieldByAlias(fieldAlias)
                            .get(model));
        }catch(IllegalAccessException e){
            return false;
        }
    }

    @Override
    public Object clone(){
        try {
            return super.clone();
        } catch (CloneNotSupportedException e) {
            throw new RuntimeException(e.getMessage());
        }
    }
}

