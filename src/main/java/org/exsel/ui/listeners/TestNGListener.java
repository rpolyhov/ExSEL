package org.exsel.ui.listeners;

import com.codeborne.selenide.WebDriverRunner;
import io.qameta.allure.Allure;
import io.qameta.allure.model.Link;
import lombok.SneakyThrows;
import org.aeonbits.owner.ConfigFactory;
import org.exsel.DriverFactory;
import org.exsel.ServerConfig;
import org.exsel.annotations.DependsOnGroup;
import org.exsel.annotations.DependsOnMethod;
import org.exsel.annotations.Skip;
import org.exsel.annotations.Stand;
import org.exsel.helpers.SimpleDateHelper;
import org.exsel.tools.ScreenShots;
import org.exsel.tools.allure.Attach;
import org.exsel.ui.config.TestConfigState;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.logging.LogEntry;
import org.openqa.selenium.logging.LogType;
import org.openqa.selenium.remote.Augmenter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.*;
import org.testng.annotations.Test;


import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import static org.exsel.tools.allure.Attachments.*;
import static org.testng.util.Strings.isNullOrEmpty;

/**
 * Created by Greg3D on 26.12.2016.
 * <p>
 * слушаеи и запоминаем состояние наших тестов, аннотации DependsOnMethod и DependsOnGroup работают ТОЛЬКО через этот лиссенер
 */
public class TestNGListener extends TestListenerAdapter implements IInvokedMethodListener {

    private static ThreadLocal<WebDriver> localThreadDriver = new ThreadLocal<WebDriver>();

    private static Logger log = LoggerFactory.getLogger(TestNGListener.class);

    public static boolean doScreenShots = true;

    public static String videoFolder = null;
    //private static VideoRecorder videoRecorder;

    //    private static long TEST_TIMEOUT = 600_000L; // по умолчанию - 600 секунд
    private static long TEST_TIMEOUT = TimeUnit.MILLISECONDS.toMinutes(10); // по умолчанию - 10 минут(600сек)

    public synchronized static void setDriver(WebDriver driver) {
        localThreadDriver.set(driver);
    }

    public static WebDriver getDriver() {
        return localThreadDriver.get();
    }


    @Override
    public void onTestFailure(ITestResult result) {
        Set<Method> s = new HashSet();
        super.onTestFailure(result);
        try {
           Allure.addLinks(new Link().setName(String.format("video"))
                   .setUrl(ConfigFactory.create(ServerConfig.class)
                           .remoteVideos()+ DriverFactory.videoName.get()));
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println(e.getMessage());
        }
    }

    public static void doScreenShot() {
        if (doScreenShots)
            doScreenShot("ScreenShot");
    }

    static void doScreenShot(String title) {
        try {
            if ((getDriver() == null) && (WebDriverRunner.getWebDriver()==null))
                savePngAttachment(title, ScreenShots.getScreenShot());
            else
                savePngAttachment(title, ScreenShots.getDriverScreenShotToBytes(getDriver()==null?WebDriverRunner.getWebDriver():getDriver()));
        } catch (Exception e) {
            log.error(e.getMessage());
        }
    }



    public void beforeInvocation(IInvokedMethod m, ITestResult r) {
        if (log != null)
            log.info(">>> @ru.sibintek.ksed.Test " + m.getTestMethod().getMethodName());

        log.info("Timeout: " + m.getTestMethod().getTimeOut());

        if (m.getTestMethod().getTimeOut() == 0) // если timeOut не проставлен, т.е. default 0
            m.getTestMethod().setTimeOut(TEST_TIMEOUT);

        this.skipAnnotationHandler(m);

        // Отлавливаем зависимость теста от другого метода
        try {
            String className = m.getTestMethod().getMethod().getAnnotation(DependsOnMethod.class).clazz().getName();
            for (String methodName : m.getTestMethod().getMethod().getAnnotation(DependsOnMethod.class).methodName())
                skippedByMethod(methodName, className, m, r);
        } catch (NullPointerException e) {
        }

        // Отлавливаем зависимоть теста от группы
        try {
            for (String groupName : m.getTestMethod().getMethod().getAnnotation(DependsOnGroup.class).groupName())
                skippedByGroup(groupName, r);
        } catch (NullPointerException e) {
        }


        // Отлавливаем зависимость класса от метода (актуально, если не запускаем before/after class и прочие настройки)
        try {
            String className = m.getTestMethod().getMethod().getDeclaringClass().getAnnotation(DependsOnMethod.class).clazz().getName();
            for (String methodName : m.getTestMethod().getMethod().getDeclaringClass().getAnnotation(DependsOnMethod.class).methodName())
                skippedByMethod(methodName, className, m, r);
        } catch (NullPointerException e) {
            //TODO - обработать exception
            //e.printStackTrace();
        }

        // Отлавливаем зависимоть класса от группы
        try {
            for (String groupName : m.getTestMethod().getMethod().getDeclaringClass().getAnnotation(DependsOnGroup.class).groupName())
                skippedByGroup(groupName, r);
        } catch (NullPointerException e) {
            //TODO - обработать exception
            //e.printStackTrace();
        }
        //startVideoRecord();
    }
    private void skipAnnotationHandler(IInvokedMethod m) {
        if (m.getTestMethod().getMethod().isAnnotationPresent(Skip.class)) {
            String skippedMessage = m.getTestMethod().getMethod().getAnnotation(Skip.class).value();
            throw new SkipException(skippedMessage);
        }
    }

    private static void skippedByGroup(final String groupName, ITestResult tr) {

        boolean skipped = false;

        // Проверяем список заваленных тестов
        if (mapContainsGroup(groupName, tr.getTestContext().getFailedTests())
                ||
                // Проверяем список пропущенных тестов
                mapContainsGroup(groupName, tr.getTestContext().getSkippedTests())
        )
            skipped = true;

        if (skipped) {
            String skipMessage = String.format("Was skipped by group [%s]", groupName);
            log.warn(skipMessage);
            attachMessage(skipMessage);
            throw new SkipException(skipMessage);
        }
    }

    private static void skippedByMethod(String methodName, String className, IInvokedMethod m, ITestResult r) {
        boolean skipped = false;

        if (className.equals("java.lang.Object"))
            className = m.getTestMethod().getTestClass().getName();
        if (!methodName.contains("."))
            methodName = className + "." + methodName;

        // Проверяем список заваленных тестов
        if (mapContainsMethodName(methodName, r.getTestContext().getFailedTests())
                ||
                // Проверяем список пропущенных тесто
                mapContainsMethodName(methodName, r.getTestContext().getSkippedTests()))
            skipped = true;

        if (skipped) {
            StringBuilder sb = new StringBuilder("Was skipped by method")
                    .append("\n\n");
            if (m != null) {
                try {
                    String description = m.getTestMethod().getRealClass().getMethod(Arrays.stream(methodName.split("\\.")).skip(methodName.split("\\.").length - 1).findFirst().get()).getAnnotation(Test.class).description();
                    if (!"".equals(description))
                        sb.append(String.format("Description : [%s]", description))
                                .append("\n");
                } catch (NoSuchMethodException e) {
                }
            }

            sb.append(String.format("Method Name : [%s]", methodName));
            log.warn(sb.toString());
            //attachMessage("Skipped by method", sb.toString());
            throw new SkipException(sb.toString());
        }
    }
    private static boolean mapContainsMethodName(String methodName, IResultMap map) {
        return map.getAllMethods().stream()
                .filter(a -> (a.getTestClass().getName() + "." + a.getMethodName()).equals(methodName)).count() > 0;
    }

    private static boolean mapContainsGroup(String groupName, IResultMap map) {
        return map.getAllMethods().stream()
                .filter(a -> Arrays.stream(a.getGroups()).filter(g -> g.contains(groupName)).count() > 0).count() > 0;
    }

    @Override
    public void afterInvocation(IInvokedMethod m, ITestResult r) {
        String logResult = String.format("<<< @Class [%2s] @ru.sibintek.ksed.Test [%2s] delay: %2d ms", m.getTestMethod().getTestClass().getName(), m.getTestMethod().getMethodName(), (m.getTestResult().getEndMillis() - m.getTestResult().getStartMillis()));
        // насильно срубаем процесс, если время выполнения теста больше параметра timeOut
        //if(testRunTime >= TimeConstants.TEST_EXECUTION_TIMEOUT)

        String videoRecordName = String.format("%s.%s.avi", m.getTestMethod().getTestClass().getName(), m.getTestMethod().getMethodName());

        if (failedByTimeOut(m)) {
            //	делаем скриншот рабочего стола
            doScreenShot();
            // отправляем команду - срубить IE
            //killProcess(new ResourcesHelper().getPathToFile("kill_IE.bat"));
//            stopVideoRecord(videoRecordName);
            return;
        }

        switch (m.getTestResult().getStatus()) {
            case 2:
                try {
//                    if(stopVideoRecord(videoRecordName))
//                        Attachments.saveAviAttachment(videoRecorder.getResultFile().getAbsolutePath());

                    //attachMessage(m.getTestMethod().getTestClass().getName() +"."+ m.getTestMethod().getMethodName());
                    //Attachments.savePngAttachment(ScreenShots.getScreenShot());
                    //Attach.executeIfError();
                    // делаем скриншоты ? (для интеграционных проверок этого не надо, станет заметно, когда тестов дофига, по умолчанию делаем)

                    Attach.executeIfError();
                    //if (doScreenShots)
                    if (!m.getTestResult().getThrowable().getClass().equals(SkipException.class)) {
                        //doScreenShot();
                        attachPageSource();
                        //attachHyperLink();
                        attachBrowserLog();
                        //attachLog();
                    }
                    //attachPageSource(getDriver());
                    log.error(String.format("%s <<< failed <<<\n%s\n<<< message <<<", logResult, r.getThrowable()));
                } catch (Exception e) {
                    log.error(String.format("%s <<< failed <<<\n%s\n<<< message <<<", logResult, r.getThrowable()));
                }
                break;
            case 3:
                log.warn(logResult + " <<< skipped");
                break;
            default:
                log.warn(logResult + " <<< passed");
                break;
        }
        Attach.clearCommands();
    }
    private void attachBrowserLog() {
        StringBuilder sb = new StringBuilder();
        for (LogEntry entry : getDriver().manage().logs().get(LogType.BROWSER))
            sb
                    .append(SimpleDateHelper.getInstance("HH:mm:ss").setDateTime(entry.getTimestamp()).toString()).append(" - ")
                    .append(entry.getLevel()).append(": ").append(entry.getMessage()).append("\n\n");
        attachText("Browser Log", sb.toString());
    }
    private boolean failedByTimeOut(IInvokedMethod m) {
        try {
            long testRunTime = (m.getTestResult().getEndMillis() - m.getTestResult().getStartMillis());
            long timeOut = Class.forName(m.getTestMethod().getTestClass().getName()).getDeclaredMethod(m.getTestMethod().getMethodName()).getAnnotation(Test.class).timeOut();
            if (timeOut > 0)
                return (testRunTime >= timeOut);
        } catch (Exception e) {
        }
        return false;
    }
    private void attachPageSource() {
        try {
            attachXml("source-html", getDriver().getPageSource());
            //if (getDriver() != null)
            //    attachMessage("pageHtml", getDriver().getPageSource(), "image/svg+xml");
        } catch (Exception e) {
            log.error(e.getMessage());
        }
    }
}
