package org.exsel;

import com.codeborne.selenide.Configuration;
import org.aeonbits.owner.ConfigFactory;
import org.exsel.helpers.DateHelper;
import org.openqa.selenium.remote.DesiredCapabilities;

import java.util.Arrays;
import java.util.HashMap;

public class SelenoidSettingFactory {
    private static String remoteAddress;
    public static ThreadLocal<String> videoName = new ThreadLocal<>();
    public static void setRemoteAdress(String address) {
        remoteAddress = address;
    }

    public static String getRemoteAdress() {
        return remoteAddress;
    }
    public static ServerSelenoidConfig serverSelenoidConfig = ConfigFactory.create(ServerSelenoidConfig.class);
    public static void configuration(String nameClass) {

        setConfiguration();
        videoName.set(nameClass+ DateHelper.getCurrentDateTime(" yyyy-MMM-dd hh:mm:ss")+".mp4");
        Configuration.browserCapabilities = getDesiredCapabilities(true);
    }
    public static void configuration(String remoteAddress,String nameClass, Boolean enableVideo) {
        setRemoteAdress(remoteAddress);
        setConfiguration();
        videoName.set(nameClass+ DateHelper.getCurrentDateTime(" yyyy-MMM-dd hh:mm:ss")+".mp4");
        Configuration.browserCapabilities = getDesiredCapabilities(enableVideo);
    }
    public static void configuration(String remoteAddress,String nameClass) {
        setRemoteAdress(remoteAddress);
        setConfiguration();
        videoName.set(nameClass+ DateHelper.getCurrentDateTime(" yyyy-MMM-dd hh:mm:ss")+".mp4");
        Configuration.browserCapabilities = getDesiredCapabilities(true);
    }


    private static DesiredCapabilities getDesiredCapabilities(Boolean enableVideo){
        DesiredCapabilities capabilities = new DesiredCapabilities();
        capabilities.setCapability("selenoid:options" +
                "", new HashMap<String, Object>() {
            {
                put("enableVNC", serverSelenoidConfig.enableVNC());
                put("enableVideo", enableVideo&&serverSelenoidConfig.enableVideo());
                put("browserName", serverSelenoidConfig.defaultBrowser());
                //put("videoName", videoName.get());
                put("env", Arrays.asList("LANG=ru_RU.UTF-8", "LANGUAGE=ru:ru", "LC_ALL=ru_RU.UTF-8"));
            }
        });

        return capabilities;
    }

    private static void setConfiguration(){
        ServerSelenoidConfig serverSelenoidConfig = ConfigFactory.create(ServerSelenoidConfig.class);
        Configuration.fileDownload = serverSelenoidConfig.fileDownload();
        Configuration.baseUrl="";
        Configuration.remote = (getRemoteAdress() != null) ? getRemoteAdress() : serverSelenoidConfig.remoteAddress();
        Configuration.remoteConnectionTimeout = 60000;
    }


}
