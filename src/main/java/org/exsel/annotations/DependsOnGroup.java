package org.exsel.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/*
* @author Greg3D
*         Date: 26.12.2016
*
* Аннотация указывает, что аннотированный Класс или Метод зависит от группы тестов
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
* 2) тест someTest зависит от групп "testGroup" и "testGroup_3"
*
* @Test(groups = "testGroup_3")
* public void someAnotherTest(){...}
*
* ...
*
* @Test(groups = {"testGroup", "testGroup_3"})
* public void oneMoreTest(){...}
*
* @DependsOnGroup(groupName = {"testGroup", "testGroup_3"})
* @Test
* public void someTest(){ .. }
*
*/
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE, ElementType.METHOD})
public @interface DependsOnGroup {
    String[] groupName() default "";
}
