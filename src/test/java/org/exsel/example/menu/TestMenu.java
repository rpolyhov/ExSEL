package org.exsel.example.menu;

import org.exsel.example.BaseTest;
import org.openqa.selenium.PageLoadStrategy;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.testng.annotations.Test;

import static com.codeborne.selenide.Selenide.open;
import static com.codeborne.selenide.WebDriverRunner.setWebDriver;

public class TestMenu extends BaseTest {
    //@Test
    public void testMenuX5(){
       // многоуровневое меню  без кликов "Инвесторам(hover)- Акции (hover) -Дивиденты (click)"
       open("https://www.x5.ru");
       app.x5.pages.mainPage.menuHeadX5.Инвесторам_Menu.Акции_menu.Дивиденды_menu.select();
    }
   // @Test
    public void testMenuDecathlon() throws InterruptedException {
        // многоуровневое меню без кликов и без вложения элементов "Детям->Обувь->Ботинки"
        open("https://www.decathlon.ru/");
        app.decathlonManager.pages.mainPage.menuHeadDecathlon.Детям_Menu.select();
        app.decathlonManager.pages.mainPage.Обувь_menu.select();
        app.decathlonManager.pages.mainPage.Ботинки_menu.click();
    }

   // @Test

    public void testMenuAspro() throws InterruptedException {
        //раскрывающее по клику меню
        open("https://aspro.ru/docs/course/course46/chapter027/?LESSON_PATH=2154.27");
        app.aspro.pages.mainPage.menuMainAspro.Каталог_Menu.Свойства_menu.Группировка_свойств_menu.select();
     }

   // @Test
    public void testMenuXoops() throws InterruptedException {
        //http://xoops.ws/modules/instruction/page.php?id=857
        System.setProperty("webdriver.chrome.driver", "chromedriver.exe");
        ChromeOptions options = new ChromeOptions();
        options.setPageLoadStrategy(PageLoadStrategy.EAGER);
        WebDriver webDriver = new ChromeDriver(options);
        setWebDriver(webDriver);

        open("http://xoops.ws/modules/instruction/page.php?id=857");
        app.xoops.pages.mainPage.menuMainXOOPS.Глава_1_CSS_основы_основ_Menu.Определение_стиля_с_помощью_CSS_menu.Синтаксис_CSS_menu.select();
        Thread.sleep(10000);
    }
    // // https://www.championat.com/news/1.html?utm_source=button&utm_medium=news# интересно сделать "Еще->Другие-Шахматы"
}
