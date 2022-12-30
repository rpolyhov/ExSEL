package org.exsel.ui.listeners;

import io.qameta.allure.Allure;
import io.qameta.allure.model.Link;
import lombok.SneakyThrows;
import org.exsel.annotations.DependsOnGroup;
import org.exsel.annotations.DependsOnMethod;
import org.exsel.annotations.Skip;
import org.exsel.annotations.Stand;
import org.exsel.helpers.SimpleDateHelper;
import org.exsel.tools.ScreenShots;
import org.exsel.tools.allure.Attach;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.logging.LogEntry;
import org.openqa.selenium.logging.LogType;
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

    /**
     * Метод вызывается после прохождениея теста, сохраняем его состояние, выводим в ЛОГ, делаем скриншот (если надо и можем)
     *
     * @param m - тест
     * @param r - результат теста
     */
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

 /*   @SneakyThrows
    private void attachLog() {
        Allure.addAttachment("api-debug.log",new FileInputStream("target//api-debug.log"));

    }*/

    private void attachBrowserLog() {
        StringBuilder sb = new StringBuilder();
        for (LogEntry entry : getDriver().manage().logs().get(LogType.BROWSER))
            sb
                    .append(SimpleDateHelper.getInstance("HH:mm:ss").setDateTime(entry.getTimestamp()).toString()).append(" - ")
                    .append(entry.getLevel()).append(": ").append(entry.getMessage()).append("\n\n");
        attachText("Browser Log", sb.toString());
    }

    static void attachHyperLink() {
        try {

        } catch (Throwable e) {
            String url = getDriver().getCurrentUrl();
            System.out.println(url);
            attachMessage(url);
        }

    }
/*
    static void doScreenShot() {
        if (doScreenShots)
            doScreenShot("Error");
    }*/
    public static void doScreenShot() {
        if (doScreenShots)
            doScreenShot("ScreenShot");
    }

    static void doScreenShot(String title) {
        try {
            if (getDriver() == null)
                savePngAttachment(title, ScreenShots.getScreenShot());
            else
                savePngAttachment(title, ScreenShots.getDriverScreenShotToBytes(getDriver()));
        } catch (Exception e) {
            log.error(e.getMessage());
        }
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

//    private boolean startVideoRecord(){
//        if(driver != null && videoFolder != null) {
//            videoRecorder = new VideoRecorder(driver, videoFolder);
//            videoRecorder.startRecording();
//            return true;
//        }
//        videoRecorder = null;
//        return false;
//    }

//    private boolean stopVideoRecord(String fileName) {
//        if (driver != null && videoRecorder != null) {
//            videoRecorder.stopRecording(fileName);
//            return true;
//        }
//        return false;
//    }

    @SuppressWarnings({"deprecation"})
    private void skipAnnotationHandler(IInvokedMethod m) {
        if (m.getTestMethod().getMethod().isAnnotationPresent(Skip.class)) {
            String skippedMessage = m.getTestMethod().getMethod().getAnnotation(Skip.class).value();
            throw new SkipException(skippedMessage);
        }
    }

    private void skipAnnotationHandler(Class clazz) {
        if (clazz.isAnnotationPresent(Skip.class)) {
            String skippedMessage = ((Skip) clazz.getAnnotation(Skip.class)).value();
            throw new SkipException(skippedMessage);
        }
    }

    /**
     * Операции перед запуском теста (скипуем, если найдены заваленные зависимости)
     *
     * @param m - имя теста
     * @param r - статус выполнения теста
     */
    @Override
    @SuppressWarnings({"deprecation"})
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

    /**
     * тест с именем m упал по таймауту
     *
     * @param m - имя проверяемого теста
     * @return - упал ?
     */
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

    /**
     * Запускаем сторонний скрипт
     *
     * @param fileToRun - имя скрипта на винте
     */
    private void killProcess(String fileToRun) {
        try {
            //String command = "cmd /c start cmd.exe";
            Runtime.getRuntime().exec("cmd /c start " + fileToRun);
        } catch (Exception e) {
        }
    }


    @SneakyThrows
    @Override
    public void onTestSkipped(ITestResult tr) {
        super.onTestSkipped(tr);

//        String failedTestName = tr.getInstanceName() +"."+  tr.getMethod().getMethodName();
//
//        String skipMessage = skippedByMethod(tr);
//        if(skipMessage != null)
//            attachMessage(skipMessage);
//        else{
//            skipMessage = skippedByGroup(tr);
//            if(skipMessage != null)
//                attachMessage(skipMessage);
//        }

        String skipMessage = skippedByGroup(tr);
        if (skipMessage != null)
            attachMessage(skipMessage);
        //attachMessage(failedTestName);
        Attach.executeIfError();


    }

    // Выполняется перед конфигурацией, типа beforeClass, afterClass
    @Override
    public void beforeConfiguration(ITestResult tr) {
        super.beforeConfiguration(tr);

        Class testClazz;
        try {
            testClazz = Class.forName(tr.getTestClass().getName());
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e.getMessage());
        }

        this.skipAnnotationHandler(testClazz);

        // Обрабатываем аннотацию @Stand - для запуска тестов на выбранном стенде, при локальных прогонах
        this.handleStendAnnotation(testClazz);

        // Чистим аттачменты перед/после группы тестов
        Attach.clearManualCommands();

        // Отлавливаем зависимоть конфигурации от группы
        try {
            try {
                for (String groupName : Class.forName(tr.getTestClass().getName()).getAnnotation(DependsOnGroup.class).groupName())
                    skippedByGroup(groupName, tr);
            } catch (ClassNotFoundException e) {
                //e.printStackTrace();
            }
        } catch (NullPointerException e) {
        }

        // Отлавливаем зависимоть конфигурации от метода
        try {
            try {
                String className = Class.forName(tr.getTestClass().getName()).getAnnotation(DependsOnMethod.class).clazz().getName();
                for (String methodName : Class.forName(tr.getTestClass().getName()).getAnnotation(DependsOnMethod.class).methodName())
                    skippedByMethod(methodName, className, tr);
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        } catch (NullPointerException e) {
        }
    }


    private void handleStendAnnotation(Class clazz) {
        //if (System.getProperty("stand") != null){
        //    this.setUp(System.getProperty("stand"), System.getProperty("ui"), getBrowserName());
        if (System.getProperty("stand") == null)
            if (clazz.isAnnotationPresent(Stand.class)) {
                Annotation stand = clazz.getAnnotation(Stand.class);
                if ("".equals(((Stand) stand).standName()))
                    throw new RuntimeException("Параметр standName должен быть указан");
                else
                    System.setProperty("stand", ((Stand) stand).standName());
                if (!"".equals(((Stand) stand).uiSettings()) && ((Stand) stand).uiSettings() != null)
                    System.setProperty("ui", ((Stand) stand).uiSettings());
            }
    }

    @Override
    public void onConfigurationFailure(ITestResult tr) {
        super.onConfigurationFailure(tr);
        attachPageSource();
        doScreenShot("Config");
    }

    @Override
    public void onConfigurationSkip(ITestResult tr) {
        super.onConfigurationSkip(tr);
        doScreenShot("Config");
    }

//    @Override
//    public void onConfigurationSuccess(ITestResult tr){
//        try {
//            if(videoRecorder != null && driver != null)
//                if(isAnnotatedBy(tr, AfterClass.class)) {
//                    videoRecorder.stopRecording();
//                    videoRecorder.getResultFile().delete();
//                    return;
//                }
//        }catch(SessionNotFoundException e){}
//        if(isAnnotatedBy(tr, BeforeClass.class))
//            startVideoRecord();
//    }

    private boolean isAnnotatedBy(ITestResult tr, Class clazz) {
        Method[] methods = tr.getTestClass().getRealClass().getMethods();

        for (Method method : methods) {
            if (method.isAnnotationPresent(clazz)) {
                if (method.getName().equals(tr.getMethod().getMethodName()))
                    return true;
            }
        }
        return false;
    }

    // IInvokedMethod m, ITestResult res

    //                 if (className.equals("java.lang.Object"))
    //                    className = m.getTestMethod().getTestClass().getName();
    //                if (!methodName.contains("."))
    //                    methodName = className + "." + methodName;

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

    private static void skippedByMethod(String methodName, String className, ITestResult r) {
        skippedByMethod(methodName, className, null, r);
    }

    /**
     * Заваливаем тест зависимым методом
     *
     * @param tr - результат теста
     * @return - текст сообщения, содержащий ЗАВАЛИВАЮЩИЙ тест
     */
    private static String skippedByMethod(ITestResult tr) {

        boolean skipped = false;
        String methodName = "";
        String[] methods = tr.getMethod().getMethodsDependedUpon();

        for (String m : methods) {
            // Проверяем список заваленных тестов
            if (mapContainsMethodName(m, tr.getTestContext().getFailedTests())
                    ||
                    // Проверяем список пропущенных тестов
                    mapContainsMethodName(m, tr.getTestContext().getSkippedTests())) {
                skipped = true;
                methodName = m;
                break;
            }
        }

        if (skipped) {
            String skipMessage = String.format("Was skipped by method [%s]", methodName);
            log.warn(skipMessage);
            return skipMessage;
        }
        return null;
    }

    private static boolean mapContainsMethodName(String methodName, IResultMap map) {
        return map.getAllMethods().stream()
                .filter(a -> (a.getTestClass().getName() + "." + a.getMethodName()).equals(methodName)).count() > 0;
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


//    private static void skippedByConfiguration(ITestResult tr){
//        tr.getTestContext().getFailedConfigurations().getAllMethods();
//    }

    private static String skippedByGroup(ITestResult tr) {

        boolean skipped = false;
        String groupName = "";
        String[] groups = tr.getMethod().getGroupsDependedUpon();

        for (String g : groups) {

            // Проверяем список заваленных тестов
            if (mapContainsGroup(g, tr.getTestContext().getFailedTests())
                    ||
                    // Проверяем список пропущенных тестов
                    mapContainsGroup(g, tr.getTestContext().getSkippedTests())
            ) {
                skipped = true;
                groupName = g;
                break;
            }
        }

        if (skipped) {
            String skipMessage = String.format("Was skipped by group [%s]", groupName);
            log.warn(skipMessage);
            return skipMessage;
        }
        return null;
    }

    private static boolean mapContainsGroup(String groupName, IResultMap map) {
        return map.getAllMethods().stream()
                .filter(a -> Arrays.stream(a.getGroups()).filter(g -> g.contains(groupName)).count() > 0).count() > 0;
    }

//    /**
//     * Заваливаем тест зависимым методом
//     * @param methodName - имя заваливающего метода
//     */
//    private static void skippedByMethod(String methodName){
//        boolean skipped = testResultSet.stream()
//                .filter(a->a.methodName.equals(methodName) && a.skipped)
//                .count() > 0;
//        if(skipped){
//            String skipMessage = String.format("Was skipped by method [%s]", methodName);
//            log.warn(skipMessage);
//            Attachments.attachMessage(skipMessage);
//            throw new SkipException(skipMessage);
//        }
//    }

    //    private static String skippedByMethod(String methodName, ITestResult tr) {
//
//        boolean skipped = false;
//        // Проверяем список заваленных тестов
//        if(mapContainsMethodName(methodName, tr.getTestContext().getFailedTests())
//                    ||
//                    // Проверяем список пропущенных тесто
//           mapContainsMethodName(methodName, tr.getTestContext().getSkippedTests()))
//                skipped = true;
//
//        if(skipped){
//            String skipMessage = String.format("Was skipped by method [%s]", methodName);
//            log.warn(skipMessage);
//            return skipMessage;
//        }
//        return null;
//    }

//    /**
//     * Заваливаем тест зависимой группой
//     * @param groupName - имя заваливающей группы
//     */
//    private static void skippedByGroup(String groupName){
//
//        boolean skipped = testResultSet.stream()
//                .filter(a-> a.groupName.equals(groupName) && a.skipped)
//                .count() > 0;
//        if(skipped){
//            String skipMessage = String.format("Was skipped by group [%s]", groupName);
//            log.warn(skipMessage);
//            Attachments.attachMessage(skipMessage);
//            throw new SkipException(skipMessage);
//        }
//    }

    // result.getTestContext().getFailedTests().getAllMethods().stream().map(a->a.getGroups())
//    /**
//     * Добавляем метод/группу/состояние в ХашСет
//     * @param m - тест
//     * @param doSkip - скипуем/не скипуем
//     */
//    private static void skipMethod(IInvokedMethod m, boolean doSkip){
//        String methodName = m.getTestMethod().getTestClass().getName() + "." + m.getTestMethod().getMethodName();
//
//        if(m.getTestMethod().getGroups().length > 0)
//            for(String groupName: m.getTestMethod().getGroups()) {
//                testResultSet.add(new TestResult().setGroupName(groupName).setMethodName(methodName).setSkipped(doSkip));
//            }
//        else
//            testResultSet.add(new TestResult().setGroupName("").setMethodName(methodName).setSkipped(doSkip));
//        return;
//    }
//
//    /**
//     * Добавляем метод/группу/состояние в ХашСет
//     * @param methodName - тест
//     * @param doSkip - скипуем/не скипуем
//     */
//    private static void skipMethod(String methodName, boolean doSkip){
//        testResultSet.add(new TestResult().setGroupName("").setMethodName(methodName).setSkipped(doSkip));
//        return;
//    }
//
//    // хашсет со списком методов/групп/состояний теста
//    private static Set<TestResult> testResultSet = new HashSet<>();
//
//    /**
//     * Класс описывает объект, который хранит информацию о имени теста, группе теста и состоянии - скипнут/ не скипнут
//     */
//    private static class TestResult{
//        public String methodName;
//        public String groupName;
//        public boolean skipped;
//
//        public TestResult(){}
//
//        public TestResult setMethodName(String methodName){
//            this.methodName = methodName;
//            return this;
//        }
//
//        public TestResult setGroupName(String groupName){
//            this.groupName = groupName;
//            return this;
//        }
//
//        public TestResult setSkipped(boolean skipped){
//            this.skipped = skipped;
//            return this;
//        }
//    }
    @Override
    public void onTestFailure(ITestResult result) {
        Set<Method> s = new HashSet();
        super.onTestFailure(result);
        //attachPageSource();
        try {
            String error = result.getThrowable().getMessage();
            String url = System.getProperty(getDriver().getWindowHandle());
            if ((isNullOrEmpty(url)) || (!url.contains("workspace")))
                url = getDriver().getCurrentUrl();
            Allure.addLinks(new Link().setName(String.format("Link to document - %s", url)).setUrl(url));
            //Allure.addAttachment("api-debug.log",new FileInputStream("api-debug.log"));
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println(e.getMessage());
        }
    }

    public void onTestSuccess(ITestResult result) {
        String url = System.getProperty(getDriver().getWindowHandle());
        if ((isNullOrEmpty(url)) || (!url.contains("workspace")))
            url = getDriver().getCurrentUrl();

        Allure.addLinks(new Link().setName(String.format("Link to document - %s", url)).setUrl(url));
    }
}
