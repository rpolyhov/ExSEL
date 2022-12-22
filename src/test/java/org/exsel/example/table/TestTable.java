package org.exsel.example.table;

import org.exsel.example.BaseTest;
import org.openqa.selenium.PageLoadStrategy;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.testng.annotations.Test;

import static com.codeborne.selenide.Selenide.open;
import static com.codeborne.selenide.WebDriverRunner.setWebDriver;

public class TestTable extends BaseTest {

    @Test
    public void testTable() throws InterruptedException {
        // 1. Простая таблица c дозагрузкой при прокрутке
        //https://ru.uefa.com/nationalassociations/uefarankings/country/#/yr/2023
        open("https://ru.uefa.com/nationalassociations/uefarankings/country/#/yr/2023");
        app.uefaManager.helpers.uefaTableHelper.getSize();
        //Thread.sleep(10000);
    }

    // 1. Простая таблица c дозагрузкой при прокрутке
    // 2. Таблица с постраничным выводом данных
    // 3. Таблица обновляемая онлайн
    // 4. Таблица подгружаемая при прокрутке
    // 5. Таблица в таблице и со сложными элементами

    //
    //https://ru.uefa.com/nationalassociations/uefarankings/country/#/yr/2023
    //https://ru.investing.com/currencies/exchange-rates-table
}
