package org.exsel.ui.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE, ElementType.FIELD})
public @interface FindByParametrised {
    String xpath() default ".";
    String param1() default "null";
    String param2() default "null";
    String param3() default "null";
}
