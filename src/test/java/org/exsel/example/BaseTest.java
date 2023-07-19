package org.exsel.example;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.WebDriverRunner;
import com.codeborne.selenide.testng.ScreenShooter;
import com.microsoft.playwright.*;
import io.qameta.allure.Attachment;
import io.qameta.allure.Step;
import org.exsel.SelenoidSettingFactory;
import org.exsel.helpers.WaitHelper;
import org.exsel.ui.config.TestConfigState;
import org.exsel.ui.listeners.TestNGListener;
import org.exsel.waitings.Until;
import org.openqa.selenium.*;
import org.openqa.selenium.support.events.WebDriverListener;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.Listeners;

import java.lang.reflect.Method;

import static com.codeborne.selenide.Selenide.$x;
import static com.codeborne.selenide.WebDriverRunner.setWebDriver;
import static org.exsel.aspects.StepsAspects.maxTimeoutInt;
import static org.openqa.selenium.PageLoadStrategy.*;
import static org.testng.util.Strings.isNotNullAndNotEmpty;
import static org.testng.util.Strings.isNullOrEmpty;

@Listeners({TestNGListener.class, ScreenShooter.class/*MyUniversalVideoListener.class*/})

public class BaseTest {
    protected AppManager app;
    protected Browser browser;
    protected Page page;
    public static String loadSelector;

    @BeforeSuite
    public void beforeTest() {
        //SelenoidSettingFactory.configurationDriverForSelenoid(this.getClass().getSimpleName());
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
        app = new AppManager();
        Configuration.pageLoadStrategy = EAGER.toString();
        System.out.println("!!!!!!!!!!!!!!!!!!!!!!!");
        WebDriverRunner.addListener(new WebDriverListener() {
            public void beforeAnyWebDriverCall(WebDriver driver, Method method, Object[] args) {
                Until.assertTimeout(120000, 500)
                        .assertThat("uiIsReady", () ->
                                {
                                    if (isNotNullAndNotEmpty(loadSelector))
                                        return WaitHelper.uiIsReady(driver,loadSelector);
                                    else return WaitHelper.uiIsReady(driver);


                                }

                        );


            }


        });

    }

    @BeforeClass
    public void beforeClass() {


    }

    @Attachment(type = "image/png")
    @Step
    public byte[] attachScreen() {
        return ((TakesScreenshot) WebDriverRunner.getWebDriver()).getScreenshotAs(OutputType.BYTES);
    }


    public static int getMaxTimeout() {
        try {
            return maxTimeoutInt.get();
        } catch (NullPointerException | NoSuchFieldError n) {
            return TestConfigState.getMaxTimeout();
        }

    }
}
