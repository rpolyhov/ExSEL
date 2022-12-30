package io.qameta.allure.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by Greg3D on 03.07.2019.
 */
@Retention(RetentionPolicy.RUNTIME)
//@Target({ElementType.TYPE, ElementType.METHOD})
@Target({ElementType.TYPE})
public @interface DescriptionLink {
    String name() default "Remote Link";
    String link() default "https://ya.ru";
}
