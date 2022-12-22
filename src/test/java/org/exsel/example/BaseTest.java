package org.exsel.example;

import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeSuite;

import static com.codeborne.selenide.Selenide.open;

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
}
