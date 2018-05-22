package ru.greg3d.waitings;

import org.hamcrest.Matcher;
import org.hamcrest.MatcherAssert;

import java.lang.reflect.InvocationTargetException;
import java.util.function.Supplier;

import static org.hamcrest.core.Is.is;

/**
 * Created by SBT-Konovalov-GV on 28.11.2016.
 */
public class Until {

    public static boolean isTrueStatic(Object o, String methodName, int timeOut){
        return isTrueStatic(o, methodName, timeOut, 1000);
    }

    private static boolean isTrueStatic(Object o, String methodName, int timeOut, int pullingInterval){
        long timeToFinish = System.currentTimeMillis() + timeOut;
        while(timeToFinish > System.currentTimeMillis()){
            try {
                //if ((Boolean) o.getClass().getDeclaredMethod(methodName).invoke(o))
                if ((Boolean) o.getClass().getMethod(methodName).invoke(o))
                    return true;
            } catch (IllegalAccessException e) {
                e.printStackTrace();
                return false;
            } catch (InvocationTargetException e) {
                e.printStackTrace();
                return false;
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
                return false;
            }
            try{
                Thread.sleep(pullingInterval);
            }catch(Exception e){}
        }
        return false;
    }


    /**
     * Ожидаем, когда функция f вернет true, в случае ошибки или по таймауту, возвращается false
     * @param f - метод, который должен вернуть true
     * @param timeOut - время ожидания события true
     * @return - дождались события или нет
     */
    public static boolean isTrueStatic(Supplier<Boolean> f, int timeOut){
        return isTrueStatic(f, timeOut, 1000);
    }

    /**
     * Ожидаем, когда функция f вернет true, в случае ошибки или по таймауту, возвращается false
     * @param f - метод, который должен вернуть true
     * @param timeOut - время ожидания события true
     * @param pullingInterval - интервал опроса события
     * @return - дождались события или нет
     */
    public static boolean isTrueStatic(Supplier<Boolean> f, int timeOut, int pullingInterval){
        Until until = new Until();
        until.timeout = timeOut;
        until.pullingInterval = pullingInterval;

        return isTrueStatic(until, f);
    }

    /**
     * Ожидаем, когда функция f вернет true, в случае ошибки или по таймауту, возвращается false
     * @param until - объект содержит параметры ожидания
     * @param f - метод, который должен вернуть true
     * @return - дождались события или нет
     */
    private static boolean isTrueStatic(Until until, Supplier<Boolean> f){
        long timeToFinish = System.currentTimeMillis() + until.timeout;
        while(timeToFinish > System.currentTimeMillis()){
            try {
                if (f.get())
                    return true;
            }catch (Throwable e){
                if(until.throwable)
                    if(until.toThrow.isAssignableFrom(e.getClass()))
                        throw e;
            }
            try {
                Thread.sleep(until.pullingInterval);
            } catch (Exception e) {}
        }
        return false;
    }

    public static Until assertTimeout(int millis){
        Until until = new Until();
        until.timeout = millis;
        return until;
    }

    public static Until assertTimeout(int millis, int pullingInterval){
        Until until = new Until();
        until.timeout = millis;
        until.pullingInterval = pullingInterval;
        return until;
    }

    private int timeout = 1000;
    private int pullingInterval = 500;
    private boolean throwable = false;
    private Class toThrow = Object.class;

    public Until throwable(){
        this.throwable = true;
        return this;
    }

    public Until throwable(Class toThrow){
        this.throwable = true;
        this.toThrow = toThrow;
        return this;
    }

    public boolean isTrue(Supplier<Boolean> f, int timeOut, int pullingInterval){
        this.timeout = timeOut;
        this.pullingInterval = pullingInterval;
        return isTrueStatic(this, f);
    }

    public boolean isTrue(Supplier<Boolean> f){
        return isTrue(f, this.timeout, this.pullingInterval);
    }

    public <T> void assertThat(T actual, Matcher<? super T> matcher){
        isTrueStatic(this, ()-> matcher.matches(actual));
        MatcherAssert.assertThat(String.format("timeout %s ms", this.timeout), actual, matcher);
    }

    public <T> void assertThat(String reason, T actual, Matcher<? super T> matcher){
        isTrueStatic(this, ()-> matcher.matches(actual));
        MatcherAssert.assertThat(String.format("timeout %s ms %s", this.timeout, reason), actual, matcher);
    }

    public <T> void assertThat(T actual){
        Matcher<? super T> matcher = is(true);
        isTrueStatic(this, ()-> matcher.matches(actual));
        MatcherAssert.assertThat(String.format("timeout %s ms", this.timeout), actual, matcher);
    }

    public void assertThat(Supplier<Boolean> f){
        MatcherAssert.assertThat(String.format("timeout %s ms", this.timeout), isTrueStatic(this, f), is(true));
    }

    public void assertThat(String reason, Supplier<Boolean> f){
        MatcherAssert.assertThat(String.format("%s timeout %s ms", reason, this.timeout), isTrueStatic(this, f), is(true));
    }
}
