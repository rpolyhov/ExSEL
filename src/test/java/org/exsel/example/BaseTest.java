package org.exsel.example;

import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeSuite;

public class BaseTest {
    protected AppManager app;

    @BeforeSuite
    public void beforeTest(){
        app=new AppManager();

    }
    @BeforeClass
    public void beforeClass(){


    }
}
