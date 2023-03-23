package org.exsel.example;

import com.automation.remarks.testng.UniversalVideoListener;
import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.WebDriverRunner;
import com.codeborne.selenide.testng.ScreenShooter;
import com.microsoft.playwright.*;
import io.qameta.allure.Allure;
import io.qameta.allure.Attachment;
import io.qameta.allure.Step;
import org.apache.commons.compress.utils.IOUtils;
import org.exsel.DriverFactory;
import org.exsel.MyUniversalVideoListener;
import org.exsel.example.typicals.TheInternet.MyWDProvider;
import org.exsel.helpers.WaitHelper;
import org.exsel.ui.config.TestConfigState;
import org.exsel.ui.listeners.TestNGListener;
import org.exsel.waitings.Until;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.events.WebDriverListener;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.Listeners;

import java.io.ByteArrayInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.lang.reflect.Method;
import java.nio.file.Paths;
import java.util.Arrays;

import static com.codeborne.selenide.WebDriverRunner.setWebDriver;
import static org.exsel.aspects.StepsAspects.maxTimeoutInt;
import static org.openqa.selenium.PageLoadStrategy.*;

@Listeners({ TestNGListener.class,ScreenShooter.class/*MyUniversalVideoListener.class*/})

public class BaseTest {
    protected AppManager app;
    protected Browser browser;
    protected Page page;
    @BeforeSuite
    public void beforeTest(){
       // DriverFactory.configurationDriverForSelenoid(this.getClass().getSimpleName());
   /*     Playwright playwright = Playwright.create();
        browser = playwright.chromium().launch(new BrowserType.LaunchOptions()
                .setHeadless(false)
                .setChromiumSandbox(false)
                .setChannel("chrome")
                .setArgs(Arrays.asList("--remote-debugging-port=9222")));
        BrowserContext context = browser.newContext(new Browser.NewContextOptions()
                .setRecordVideoDir(Paths.get("video1"))
                .setViewportSize(1920, 1080)
                .setRecordVideoSize(1280,720));
        page = context.newPage();*/
        //Configuration.browser = MyWDProvider.class.getName();
        app=new AppManager();
        Configuration.pageLoadStrategy=NONE.toString();
        WebDriverRunner.addListener(new WebDriverListener() {
             public void beforeAnyWebDriverCall(WebDriver driver, Method method, Object[] args)   {
                 Until.assertTimeout(120000, 500)
                         .assertThat("uiIsReady",()-> WaitHelper.uiIsReady(driver));

            }
        });

    }
    @BeforeClass
    public void beforeClass(){


    }
    @Attachment(type = "image/png")
    @Step
    public byte[] attachScreen() {
        return ((TakesScreenshot) WebDriverRunner.getWebDriver()).getScreenshotAs(OutputType.BYTES); }


    public static int getMaxTimeout() {
        try {
            return maxTimeoutInt.get();
        } catch (NullPointerException|NoSuchFieldError n) {
            return TestConfigState.getMaxTimeout();
        }

    }
}
