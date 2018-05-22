package ru.greg3d.asserts;

import org.hamcrest.Matcher;
import org.hamcrest.MatcherAssert;
import org.testng.asserts.IAssert;

public class SoftHamcrestAssert extends SoftAssertExtended {

    public <T> void assertThat(final T actual, final Matcher<? super T> matcher) {
        doAssert(new IAssert() {
            @Override
            public void doAssert() {
                MatcherAssert.assertThat(actual, matcher);
            }

            @Override
            public Object getActual() {
                return actual;
            }

            @Override
            public Object getExpected() {
                return null;
            }

            @Override
            public String getMessage() {
                return null;
            }
        });
    }

    public <T> void assertThat(final String reason, final T actual, final Matcher<? super T> matcher) {
        doAssert(new IAssert() {
            @Override
            public void doAssert() {
                MatcherAssert.assertThat(reason, actual, matcher);
            }

            @Override
            public Object getActual() {
                return actual;
            }

            @Override
            public Object getExpected() {
                return null;
            }

            @Override
            public String getMessage() {
                return reason;
            }
        });
    }

    @Override
    public void assertAll(){
        try {
            super.assertAll();
        }catch(AssertionError e){
            super.clearErrorMap();
            throw e;
        }
        //super.clearErrorMap();
    }
}
