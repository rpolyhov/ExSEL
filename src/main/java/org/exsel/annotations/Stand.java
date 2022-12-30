package org.exsel.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by sbt-konovalov-gv on 12.04.2018.
 *
 *  аннотация запускает тесты на указанном стенде, при локальном прогоне
 *
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE, ElementType.METHOD})
public @interface Stand {
    String standName() default "";
    String uiSettings() default "";
}
