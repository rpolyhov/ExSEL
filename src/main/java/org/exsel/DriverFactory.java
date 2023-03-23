package org.exsel;

import com.codeborne.selenide.Configuration;
import org.aeonbits.owner.ConfigFactory;
import org.exsel.helpers.DateHelper;
import org.openqa.selenium.remote.DesiredCapabilities;

import java.util.Arrays;
import java.util.HashMap;

public class DriverFactory {
    private static String remoteAddress;
    public static ThreadLocal<String> videoName = new ThreadLocal<>();
    public static void setRemoteAdress(String address) {
        remoteAddress = address;
    }

    public static String getRemoteAdress() {
        return remoteAddress;
    }

    public static void configurationDriverForSelenoid(String nameClass) {
        ServerConfig serverConfig = ConfigFactory.create(ServerConfig.class);
        Configuration.fileDownload = serverConfig.fileDownload();
        Configuration.remote = (getRemoteAdress() != null) ? getRemoteAdress() : serverConfig.remoteAddress();
        Configuration.remoteConnectionTimeout = 60000;
        DesiredCapabilities capabilities = new DesiredCapabilities();
        videoName.set(nameClass+ DateHelper.getCurrentDateTime(" yyyy-MMM-dd hh:mm:ss")+".avi");
        capabilities.setCapability("selenoid:options" +
                "", new HashMap<String, Object>() {
            {
                put("enableVNC", serverConfig.enableVNC());
                put("enableVideo", serverConfig.enableVideo());
                put("browserName", serverConfig.defaultBrowser());
                put("videoName", videoName.get());
                put("env", Arrays.asList("LANG=ru_RU.UTF-8", "LANGUAGE=ru:ru", "LC_ALL=ru_RU.UTF-8"));
            }
        });
        Configuration.browserCapabilities = capabilities;
    }
}
