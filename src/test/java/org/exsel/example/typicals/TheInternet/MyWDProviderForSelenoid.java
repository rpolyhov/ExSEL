package org.exsel.example.typicals.TheInternet;

import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.WebDriverProvider;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.Capabilities;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;

import javax.annotation.Nonnull;

import java.net.MalformedURLException;
import java.net.URI;
import java.util.Arrays;
import java.util.HashMap;

import static com.codeborne.selenide.FileDownloadMode.FOLDER;


public class MyWDProviderForSelenoid implements WebDriverProvider {


        @Override
        public WebDriver createDriver(@Nonnull Capabilities capabilities) {
            //Configuration.fileDownload = FOLDER;
            //Configuration.remote = "http://192.168.1.79:4444/wd/hub";
            //Configuration.remote = "http://localhost:4444/wd/hub";
          //  Configuration.remoteConnectionTimeout = 60000;

            DesiredCapabilities capabilities1 = new DesiredCapabilities();
            //capabilities1.setCapability();
            capabilities1.setCapability("selenoid:options" +
                    "",new HashMap<String, Object>() {
                {
                    put("enableVNC", true);
                    put("enableVideo", true);
                    put("browserName", "chrome");
                    put("videoName", this.getClass().getSimpleName()/*+DateHelper.getCurrent("ddMMyyyy")*/);
                    put("env", Arrays.asList("LANG=ru_RU.UTF-8", "LANGUAGE=ru:ru", "LC_ALL=ru_RU.UTF-8"));
                    //put("browserVersion", "110");
                }
            });;

            try {
                return  new RemoteWebDriver(
                  URI.create("http://192.168.1.79:4444/wd/hub").toURL(),
                        capabilities1
          );
            } catch (MalformedURLException e) {
                throw new RuntimeException(e);
            }


        }


}
