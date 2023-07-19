package org.exsel.example.table;

import com.codeborne.selenide.Configuration;
import io.qameta.allure.Step;
import org.exsel.annotations.Assume;
import org.exsel.annotations.Bug;
import org.exsel.annotations.MaxTimeOut;
import org.exsel.example.BaseTest;
import org.exsel.example.table.RBC.elements.CashRBCTable;
import org.openqa.selenium.PageLoadStrategy;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.testng.annotations.Test;

import java.util.List;
import java.util.Objects;

import static com.codeborne.selenide.Selenide.open;
import static com.codeborne.selenide.WebDriverRunner.setWebDriver;
import static org.hamcrest.MatcherAssert.assertThat;

public class TestTable extends BaseTest {
        {
            loadSelector="div[class*='quote__preloader js-office-preloader-in-list'][style!='display: none;']";
        }
    //@Test
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


    @Test
    @MaxTimeOut(seconds = 90000)
    public void testTableCashRBC()  {
      //  System.setProperty("webdriver.chrome.silentOutput", "true");
        //System.out.println(getMaxTimeout());
/*        System.setProperty("webdriver.chrome.driver", "chromedriver.exe");
        ChromeOptions options = new ChromeOptions();
        options.setPageLoadStrategy(PageLoadStrategy.EAGER);
        options.addArguments("--start-maximized");
        options.addArguments("--remote-allow-origins=*");
        WebDriver webDriver = new ChromeDriver(options);
        setWebDriver(webDriver);*/
        // 1. Простая таблица c дозагрузкой при прокрутке
        //https://ru.uefa.com/nationalassociations/uefarankings/country/#/yr/2023

        ChromeOptions options = new ChromeOptions();
        options.addArguments("--start-maximized");
        options.addArguments("--remote-allow-origins=*");

        Configuration.browserCapabilities = options;

        open("https://cash.rbc.ru/cash/?currency=3&city=1&diapason=all");

       // app.rbcManager.helpers.cashRBCHelper.getSize();
       // System.out.println("Всего - "+app.rbcManager.helpers.cashRBCHelper.getTable().size());

        List<CashRBCTable> tables=app.rbcManager.helpers.cashRBCHelper.getTable();
        System.out.println("Минимальная цена покупки: " +
                tables.stream()
                        .filter(e-> Objects.nonNull(e.buy))
                        .min(CashRBCTable::compareBuy).get().buy);
        System.out.println("Максимальная цена покупки: " +
                tables.stream()
                        .filter(e-> Objects.nonNull(e.buy))
                        .max(CashRBCTable::compareBuy).get().buy);

        System.out.println("Минимальная цена продажи: " +
                tables.stream()
                        .filter(e-> Objects.nonNull(e.sell))
                        .min(CashRBCTable::compareSell).get().sell);

        System.out.println("Максимальная цена продажи: " +
                tables.stream()
                        .filter(e-> Objects.nonNull(e.sell))
                        .max(CashRBCTable::compareSell).get().sell);

        System.out.println("Всего записей: " + tables.size());

        //Thread.sleep(10000);
    }
}
