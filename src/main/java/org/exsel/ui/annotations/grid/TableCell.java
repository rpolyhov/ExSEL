package org.exsel.ui.annotations.grid;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE, ElementType.FIELD, ElementType.METHOD})
public @interface TableCell {
    //String xpath() default "";
    String cssCell() default "";
    int columnOrder() default 0;
    String columnName() default "";
    String getAttr() default "";
    String containsText() default "";
}
