package org.exsel.example.restApi;

import io.restassured.response.Response;
import org.exsel.example.BaseTest;
import org.exsel.example.restApi.CashRBC.Root;
import org.exsel.example.restApi.CashRBC.Root.Bank;
import org.exsel.helpers.RestAssuredHelpers;
import org.exsel.ui.config.TestConfigState;
import org.testng.annotations.Test;
import java.util.Objects;

public class TestApi extends BaseTest {
    Root root;
    @Test
    public void restAssure(){
        TestConfigState.setBaseUri("https://cash.rbc.ru");
        root = new RestAssuredHelpers()
                .requestGet("/cash/json/cash_rates_with_volumes/?city=1&currency=3").getBody()
                .as(Root.class);

        System.out.println("Минимальная цена покупки: " +
                root.banks.stream()
                        .filter(e-> Objects.nonNull(e.rate.buy))
                        .min(Bank::compareBuy).get().rate.buy);
        System.out.println("Максимальная цена покупки: " +
                root.banks.stream()
                        .filter(e->Objects.nonNull(e.rate.buy))
                        .max(Bank::compareBuy).get().rate.buy);

        System.out.println("Минимальная цена продажи: " +
                root.banks.stream()
                        .filter(e->Objects.nonNull(e.rate.sell))
                        .min(Bank::compareSell).get().rate.sell);

        System.out.println("Максимальная цена продажи: " +
                root.banks
                        .stream()
                        .filter(e->Objects.nonNull(e.rate.sell))
                        .max(Bank::compareSell).get().rate.sell);
        System.out.println("Всего записей: " + root.banks.stream().count());


    }

}
