package io.qameta.allure.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by SBT-Konovalov-GV on 16.03.2017.
 *
 * display order:
 *
 * 1. @SuiteName
 * 2. @DisplayedName
 * 3. @TestName
 **/
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE, ElementType.METHOD})
public @interface DisplayedName {
    String value() default "";
}
