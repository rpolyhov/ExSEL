package org.exsel.example.table.FINAM;
import com.codeborne.selenide.WebDriverRunner;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.exsel.example.BaseTest;
import org.exsel.helpers.RestAssuredHelpers;
import org.exsel.ui.config.TestConfigState;
import org.openqa.selenium.Cookie;
import org.testng.annotations.Test;
import org.testng.internal.thread.IThreadFactory;

import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import static com.codeborne.selenide.Selenide.$x;
import static com.codeborne.selenide.Selenide.open;

public class TestTableFinam extends BaseTest {
    RootFinam tableFinam,finalTableFinam;
    @Test
    public void test() throws JsonProcessingException, InterruptedException {
        open("https://www.finam.ru/api/quotesOnline?menuName=stocks_foreign&pageNumber=1");
        Thread.sleep(10000);
        Set<Cookie> cookie = WebDriverRunner.getWebDriver().manage().getCookies();
        Cookie cookie1 =cookie.stream().collect(Collectors.toList()).get(1);
        //  TestConfigState.setBaseUri("https://www.finam.ru");
        System.out.println(cookie1.getName()+"="+cookie1.getValue());
        tableFinam = new RestAssuredHelpers()
                .requestGet("https://www.finam.ru/api/quotesOnline?menuName=stocks_foreign&pageNumber=1",cookie1.getName()+"="+cookie1.getValue())
                .as(RootFinam.class);

       for (int i=1;i<=264;i++) {
           System.out.println(i);
           open("https://www.finam.ru/api/quotesOnline?menuName=stocks_foreign&pageNumber="+i);
           String json = $x("//pre[@style]").getText();
           tableFinam = new ObjectMapper().readValue(json, RootFinam.class);
           if (!tableFinam.status) break;
           if (i==1) finalTableFinam= new ObjectMapper().readValue(json, RootFinam.class);
           finalTableFinam.data.body.addAll(tableFinam.data.body);
       }
    }
}
