package ru.greg3d.ui.htmlelements.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by sbt-konovalov-gv on 19.03.2018.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE, ElementType.FIELD})
public @interface FindByParametrised {
    String xpath() default ".";
    String param1() default "";
    String param2() default "";
    String param3() default "";
}
