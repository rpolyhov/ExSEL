package io.qameta.allure.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by Greg3D on 17.08.2018.
 *
 * display order:
 *
 * 1. @SuiteName
 * 2. @DisplayedName
 * 3. @TestName
 **/
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE, ElementType.METHOD})
public @interface TestName {
    String value() default "";
}
