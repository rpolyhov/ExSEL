package org.exsel.ui.config;

import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Provider;
import org.aeonbits.owner.Config;
import org.aeonbits.owner.ConfigFactory;

import static io.qameta.allure.aspects.StepsAspects.maxTimeoutInt;

public class TestConfigState {

    private interface StendConfig extends Config {

        @Key("browserName")
        @DefaultValue("CHROME")
        String getBrowserName();

        @Key("baseUri")
        @DefaultValue("http://172.30.48.40:8080")
        String getBaseUri();

        @Key("selenoidLink")
        //@DefaultValue("http://moon.sibintek.ru:15230/wd/hub")
        @DefaultValue("http://172.30.49.62:8090/wd/hub")
            //@DefaultValue("http://172.30.48.80:7770/wd/hub")
            // http://172.30.48.80:7770/wd/hub
            // @DefaultValue("http://localhost:8080/wd/hub")
        String getSelenoidLink();

        @Key("useSelenoid")
        // @DefaultValue("true")
        @DefaultValue("false")
        boolean getUseSelenoid();

        @Key("useDataBaseH2")
        @DefaultValue("false")
        boolean getUseDataBaseH2();

        @Key("useDataGenerator")
        @DefaultValue("true")
        boolean getUseDataGenerator();

        @Key("maxTimeout")
        @DefaultValue("300000")
        int getMaxTimeout();

        @Key("hoursOffset")
        @DefaultValue("0")
        int getHoursOffset();

        @Key("headless")
        @DefaultValue("false")
        String getHeadless();

        @Key("retry")
        @DefaultValue("0")
        String getRetry();

        @Key("login")
        @DefaultValue("Smoke_user211")
        String getLogin();

        @Key("password")
        @DefaultValue("vHVF8pLErowZkrDcMMa1BA==11")
        String getPassword();

        @Key("portablePath")
        @DefaultValue("")
        String getPorablePath();


    }

    private static class StendConfigProvider implements Provider<StendConfig> {
        @Override
        public StendConfig get() {
            return ConfigFactory.create(StendConfig.class, System.getProperties());
        }
    }

    public static class StendConfigModule extends AbstractModule {
        @Override
        protected void configure() {
            bind(StendConfig.class).toProvider(StendConfigProvider.class);
        }
    }

    private static StendConfig init() {
        Injector injector = Guice.createInjector(new StendConfigModule());
        //return injector.getInstance(StendConfig.class);
        StendConfig config = injector.getInstance(StendConfig.class);

        // TODO - для обратной совместимости частей кода, куда НЕДОТЯГИВАЮТСЯ геттеры, приходится просаживать системные переменные
        System.setProperty("baseUri", config.getBaseUri());
        return config;

    }

    private static StendConfig stendConfig = init();

    private static void setProperty(String propertyName, Object o) {
        System.setProperty(propertyName, String.valueOf(o));
        stendConfig = init();
    }

    /***
     **     GETTERS -- SETTERS
     ***/

    public static boolean getUseSelenoid() {
        return stendConfig.getUseSelenoid();
    }

    public static String getLogin() {
        return stendConfig.getLogin();
    }

    public static String getPassword() {
        return stendConfig.getPassword();
    }


    public static String getBaseUri() {
        return stendConfig.getBaseUri();
    }

    public static void setBaseUri(String url) {
        setProperty("baseUri", url);
    }


    public static String getAppUrl() {
        return stendConfig.getBaseUri() + "/share/";
    }
    public static String getAppUrlLogin() {
        return stendConfig.getBaseUri() + "/share/page?pt=login";
    }

    public static String getSelenoidLink() {
        return stendConfig.getSelenoidLink();
    }

    public static void setSelenoidLink(String link) {
        setProperty("selenoidLink", link);
    }

    public static String getSelenoidUrl() {
        return getSelenoidLink().replace("/wd/hub", "");
    }

    public static boolean getUseDataGenerator() {
        return stendConfig.getUseDataGenerator();
    }

    public static void setUseDataGenerator(boolean use) {
        setProperty("useDataGenerator", use);
    }

    public static boolean getUseDataBaseH2() {
        return stendConfig.getUseDataBaseH2();
    }

    public static int getMaxTimeout() {
        try {
            if (maxTimeoutInt.get() == null)
                return getMaxTimeoutDefault();
            else return maxTimeoutInt.get();
        } catch (Error e) {
            return getMaxTimeoutDefault();
        }
    }

    public static int getMaxTimeoutDefault() {
        return stendConfig.getMaxTimeout();

    }

    public static void setMaxTimeOut(Integer maxTimeout) {
        setProperty("maxTimeout", maxTimeout);
    }

    public static String getBrowserName() {
        return stendConfig.getBrowserName();
    }

    public static void setBrowserName(String browserName) {
        setProperty("browserName", browserName);
    }

    public static int getHoursOffset() {
        return stendConfig.getHoursOffset();
    }

    public static String getHeadless() {
        return stendConfig.getHeadless();
    }

    public static void setHeadless(String headless) {
        setProperty("headless", headless);
    }

    public static String getRetry() {
        return stendConfig.getRetry();
    }

    public static void setRetry(Integer retry) {
        setProperty("retry", retry);
    }

    public static void setPassword(String password) {
        setProperty("password", password);
    }

    public static void setPortablePath(String portablePath) {
        setProperty("portablePath", portablePath);
    }

    public static String getPortablePath() {
        return stendConfig.getPorablePath();
    }

}
