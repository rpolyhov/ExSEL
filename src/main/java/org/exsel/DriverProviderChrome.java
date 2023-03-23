package org.exsel;

import com.codeborne.selenide.WebDriverProvider;
import org.openqa.selenium.Capabilities;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;

import javax.annotation.Nonnull;
import java.io.File;
import java.util.HashMap;

public class DriverProviderChrome implements WebDriverProvider {


    @Nonnull
    @Override
    public WebDriver createDriver(@Nonnull Capabilities capabilities) {
       // File adf = new File("drivers");
      //  System.setProperty("webdriver.chrome.driver", "drivers/chromedriver/win32/75.0.3770.90/chromedriver.exe");
       // capabilities = DesiredCapabilities.chrome();
     //   LoggingPreferences logs = new LoggingPreferences();
       // logs.enable(LogType.DRIVER, Level.ALL);

        HashMap<String, Object> chromePrefs = new HashMap<>();
        chromePrefs.put("plugins.always_open_pdf_externally", true);//  true не открывать pdf в preview
        chromePrefs.put("download.prompt_for_download", false);// не выводить диалоговое окно при сохранении

        chromePrefs.put("profile.default_content_setting_values.automatic_downloads", 1);
        chromePrefs.put("browser.helperApps.neverAsk.saveToDisk", "application/pdf");
        chromePrefs.put("browser.download.folderList", 2);
        chromePrefs.put("safebrowsing.enabled", true);





        //chromePrefs.put("plugins.plugins_disabled", new String []{"Chrome PDF Viewer"});

     /*   chromePrefs.put("profile.default_content_settings.popups", 0);
        chromePrefs.put("download.default_directory", new File("").getAbsolutePath());
        chromePrefs.put("download.prompt_for_download", false);
        chromePrefs.put("plugins.plugins_disabled", "Chrome PDF Viewer");*/


    //    ..chromePrefs.put("download.default_directory", new File("").getAbsolutePath());
 /*       chromePrefs.put("intl.accept_languages", "nl");
        chromePrefs.put("disable-popup-blocking", "true");
*/
        ChromeOptions chromeOptions = new ChromeOptions();
        chromeOptions.addArguments("--incognito");
        chromeOptions.addArguments("start-maximized");
        chromeOptions.addArguments("disable-infobars");
        chromeOptions.addArguments("browser.set_download_behavior", "allow");
        //chromeOptions.addArguments("--user-data-dir=C:\\Users\\AntonK\\AppData\\Local\\Google\\Chrome\\User Data\\Profile 1");
        //chromeOptions.setCapability("pdfjs.disabled", "true");
        chromeOptions.setExperimentalOption("prefs", chromePrefs);

        //capabilities.setCapability(ChromeOptions.CAPABILITY, chromeOptions);
       // TestBase_working.log(capabilities.getVersion());
        chromeOptions.merge(capabilities);
        return new ChromeDriver(chromeOptions);
    }
}