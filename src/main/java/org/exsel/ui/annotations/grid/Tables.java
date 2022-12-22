package org.exsel.ui.annotations.grid;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;


@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE, ElementType.FIELD, ElementType.METHOD})
public @interface Tables {
    //String xpath() default "";
    String cssTable() default "";// тут указываем css таблицы
    String cssHeader() default "";// тут указываем css таблицы
    String cssRow() default "";
    String cssCell() default "";

}
