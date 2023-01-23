package org.exsel.example;

import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.WebDriverRunner;
import com.microsoft.playwright.*;
import org.exsel.example.typicals.TheInternet.MyWDProvider;
import org.exsel.helpers.WaitHelper;
import org.exsel.ui.config.TestConfigState;
import org.exsel.waitings.Until;
import org.openqa.selenium.By;
import org.openqa.selenium.PageLoadStrategy;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.events.WebDriverListener;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeSuite;

import java.lang.reflect.Method;
import java.nio.file.Paths;
import java.util.Arrays;

import static com.codeborne.selenide.WebDriverRunner.setWebDriver;
import static org.exsel.aspects.StepsAspects.maxTimeoutInt;
import static org.openqa.selenium.PageLoadStrategy.*;

public class BaseTest {
    protected AppManager app;
    protected Browser browser;
    protected Page page;
    @BeforeSuite
    public void beforeTest(){
        Playwright playwright = Playwright.create();
        browser = playwright.chromium().launch(new BrowserType.LaunchOptions()
                .setHeadless(false)
                .setChromiumSandbox(false)
                .setChannel("chrome")
                .setArgs(Arrays.asList("--remote-debugging-port=9222")));
        BrowserContext context = browser.newContext(new Browser.NewContextOptions()
                .setRecordVideoDir(Paths.get("video1"))
                .setViewportSize(1920, 1080)
                .setRecordVideoSize(1280,720));
        page = context.newPage();
        Configuration.browser = MyWDProvider.class.getName();
        app=new AppManager();
        Configuration.pageLoadStrategy=NONE.toString();
        WebDriverRunner.addListener(new WebDriverListener() {
             public void beforeAnyWebDriverCall(WebDriver driver, Method method, Object[] args)   {
                 System.out.println("method"+method.getName());
                 Until.assertTimeout(60000, 250)
                         .assertThat(()-> WaitHelper.uiIsReady(driver));

            }
        });

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
