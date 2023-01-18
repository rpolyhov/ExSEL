package org.exsel.example.typicals.TheInternet;

import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.WebDriverProvider;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.Capabilities;
import org.openqa.selenium.PageLoadStrategy;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import javax.annotation.Nonnull;


public class MyWDProvider implements WebDriverProvider {
/*    @Nonnull
    @Override
    public WebDriver createDriver(@Nonnull Capabilities capabilities) {
        //System.setProperty("webdriver.chrome.driver", "chromedriver.exe");//++
        // ChromeOptions options = new ChromeOptions();
        //  options.setPageLoadStrategy(PageLoadStrategy.EAGER);

        Configuration.timeout = 60000;
        WebDriverManager.chromedriver().setup();
        ChromeOptions chromeOptions = new ChromeOptions();
        // Capabilities extraCapabilities
        chromeOptions.setPageLoadStrategy(PageLoadStrategy.EAGER);//++
        chromeOptions.setExperimentalOption("debuggerAddress", "127.0.0.1:9222");
        chromeOptions.setAcceptInsecureCerts(true);
        chromeOptions.merge(capabilities);
        return new ChromeDriver(chromeOptions);*/


        @Override
        public WebDriver createDriver(@Nonnull Capabilities capabilities) {
            Configuration.remoteConnectionTimeout = 60000;
            WebDriverManager.chromedriver().setup();
            ChromeOptions chromeOptions = new ChromeOptions();

            chromeOptions.setExperimentalOption("debuggerAddress", "127.0.0.1:9222");
            chromeOptions.merge(capabilities);

            return new ChromeDriver(chromeOptions);
        }


}
