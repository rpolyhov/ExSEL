package org.exsel.tools.allure;


/**
 * Created by sbt-hlopunov-va on 20.06.2017.
 */
public class Step {

    /**
     * Простой логгер для Allure
     * @param value
     */
    @io.qameta.allure.Step("{0}")
    public static void log(String value){}
}
