package org.exsel.example.table;

import io.qameta.allure.Step;
import org.exsel.annotations.Assume;
import org.exsel.annotations.Bug;
import org.exsel.annotations.MaxTimeOut;
import org.exsel.example.BaseTest;
import org.openqa.selenium.PageLoadStrategy;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.testng.annotations.Test;

import static com.codeborne.selenide.Selenide.open;
import static com.codeborne.selenide.WebDriverRunner.setWebDriver;
import static org.hamcrest.MatcherAssert.assertThat;

public class TestTable extends BaseTest {

    @Test
    @MaxTimeOut(seconds = 90000)
    public void testTable()  {
        System.out.println(getMaxTimeout());
        System.setProperty("webdriver.chrome.driver", "chromedriver.exe");
        ChromeOptions options = new ChromeOptions();
        options.setPageLoadStrategy(PageLoadStrategy.EAGER);
        WebDriver webDriver = new ChromeDriver(options);
        setWebDriver(webDriver);
        // 1. Простая таблица c дозагрузкой при прокрутке
        //https://ru.uefa.com/nationalassociations/uefarankings/country/#/yr/2023
        open("https://ru.uefa.com/nationalassociations/uefarankings/country/#/yr/2023");
        app.uefa.helpers.uefaTableHelper.getSize();
        //Thread.sleep(10000);
    }

    // @Test
    public void testTableOzon() {
        System.setProperty("webdriver.chrome.driver", "chromedriver.exe");
        ChromeOptions options = new ChromeOptions();
        options.setPageLoadStrategy(PageLoadStrategy.EAGER);
        WebDriver webDriver = new ChromeDriver(options);
        setWebDriver(webDriver);
        // 1. Простая таблица c дозагрузкой при прокрутке
        //https://ru.uefa.com/nationalassociations/uefarankings/country/#/yr/2023
        open("https://www.ozon.ru/category/knigi-16500/?category_was_predicted=true&deny_category_prediction=true&from_global=true&text=%D0%BA%D0%BD%D0%B8%D0%B3%D0%B0");
        app.ozon.helpers.ozonTableHelper.getSize();
        //Thread.sleep(10000);
    }


    @Step(value = "Тест пест")
    @MaxTimeOut(seconds = 120001)
    public void test1() {

    }


    @Assume(failTest = true)
    @MaxTimeOut(seconds = 60001)
    @Bug(value = "баги задолбали_1_1")
    @Step
    public void test_1_1() {
        System.out.println("start test_1_1");
        System.out.println(getMaxTimeout());
        assertThat("test1_1", false);
        System.out.println("finish test_1_1");
    }

    @Assume(failTest = false)
    @MaxTimeOut(seconds = 90001)
    @Bug(value = "баги задолбали_1_2")
    @Step
    public void test_1_2() {
        System.out.println("start test_1_2");
        System.out.println(getMaxTimeout());
        assertThat("test1_2", false);
        System.out.println("finish test_1_2");
    }


    //https://www.ozon.ru/category/knigi-16500/?category_was_predicted=true&deny_category_prediction=true&from_global=true&text=%D0%BA%D0%BD%D0%B8%D0%B3%D0%B0

    // 1. Простая таблица c дозагрузкой при прокрутке
    // 2. Таблица с постраничным выводом данных
    // 3. Таблица обновляемая онлайн
    // 4. Таблица в таблице и со сложными элементами

    //
    //https://ru.uefa.com/nationalassociations/uefarankings/country/#/yr/2023
    //https://ru.investing.com/currencies/exchange-rates-table
}
