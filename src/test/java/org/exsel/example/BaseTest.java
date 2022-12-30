package org.exsel.example;

import org.exsel.ui.config.TestConfigState;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeSuite;

import static io.qameta.allure.aspects.StepsAspects.maxTimeoutInt;

public class BaseTest {
    protected AppManager app;

    @BeforeSuite
    public void beforeTest(){
       // open("ya.ru");
        app=new AppManager();

    }
    @BeforeClass
    public void beforeClass(){


    }
    public static int getMaxTimeout() {
        try {
            return maxTimeoutInt.get();
        } catch (NullPointerException|NoSuchFieldError n) {
            return TestConfigState.getMaxTimeout();
        }

    }
}
