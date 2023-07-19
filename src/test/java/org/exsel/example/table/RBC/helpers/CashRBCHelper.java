package org.exsel.example.table.RBC.helpers;

import com.codeborne.selenide.WebDriverRunner;
import org.exsel.annotations.MaxTimeOut;
import org.exsel.example.table.RBC.elements.CashRBCTable;
import org.exsel.ui.helpers.GridHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

import static com.codeborne.selenide.Selenide.$x;
import static com.codeborne.selenide.Selenide.open;

public   class CashRBCHelper {
    public static Logger LOGGER = LoggerFactory.getLogger(CashRBCHelper.class);
    List<CashRBCTable> table;
    List<CashRBCTable> finaltable= new ArrayList<>() ;
    int size;

    @MaxTimeOut(seconds = 123123)
    public <T> Integer getSize()  {
        table = getTable(new CashRBCTable());
        return table.size();
    }

    @MaxTimeOut(seconds = 123123)
    public <T>    List<CashRBCTable> getTable()  {
        finaltable.clear();
        Integer countPage=Integer.valueOf($x("//div[@class='pagination__item js-pagination-item'][last()]")
                .getText());
        for (int i=1;i<=countPage;i++) {
            open(String.format("https://cash.rbc.ru/cash/?currency=3&city=1&diapason=all&page=%s",i));
            table = getTable(new CashRBCTable());
            System.out.println(i+"p -"+table.size());
            finaltable.addAll(table);
            table.clear();
        }
        return finaltable;
    }
    private  <T> List<T> getTable(T table) {
        return (List<T>) new GridHelper()
                .toModelList(WebDriverRunner.getWebDriver(), table.getClass());

    }


}
