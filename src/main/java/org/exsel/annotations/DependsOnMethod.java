package org.exsel.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/*
* @author Greg3D
*         Date: 09.09.16
*
* Аннотация указывает, что аннотированный Класс или Метод зависит от других методов
*
* Примеры использования:
*
* 1) тесты класса SomeClass зависят от метода "ru.box.tests.FirstTestClass.test7_Ok"
*
* @DependsOnMethod(methodName = "ru.box.tests.FirstTestClass.test7_Ok")
* public class SomeClass{
* 	...
* }
*
* 2) тест someTest зависит от методов "test6" и "test7_Ok" класса "FirstTestClass"
*
* @Test
* @DependsOnMethod(clazz = FirstTestClass.class, methodName = {"test6", "test7_Ok"})
* public void someTest(){ .. }
*
*/
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE, ElementType.METHOD})
public @interface DependsOnMethod {
	String[] methodName() default "";
	Class clazz() default Object.class;
}
