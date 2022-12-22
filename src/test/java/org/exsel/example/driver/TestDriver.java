package org.exsel.example.driver;

import org.exsel.example.BaseTest;
import org.openqa.selenium.PageLoadStrategy;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import static com.codeborne.selenide.Selenide.open;
import static com.codeborne.selenide.WebDriverRunner.setWebDriver;

public class TestDriver extends BaseTest {

   // @Test
    // Настройка собственного драйвера
    public void testDriver()  {
        System.setProperty("webdriver.chrome.driver", "chromedriver.exe");
        ChromeOptions options = new ChromeOptions();
        options.setPageLoadStrategy(PageLoadStrategy.EAGER);//eager : This stategy causes Selenium to wait for the DOMContentLoaded event (html content downloaded and parsed only).
        WebDriver webDriver = new ChromeDriver(options);
        setWebDriver(webDriver);
    }
}
