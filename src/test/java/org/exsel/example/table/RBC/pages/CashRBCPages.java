package org.exsel.example.table.RBC.pages;

import org.exsel.ui.ElementsContainerWrapper;
import org.exsel.ui.PageDecorator;

import static com.codeborne.selenide.Selenide.page;

public class CashRBCPages {
    public MainPageCashRBC mainPage;

    public CashRBCPages(){
        mainPage=new MainPageCashRBC();
        mainPage = page(MainPageCashRBC.class);
        PageDecorator.setParentToObject(mainPage, ElementsContainerWrapper.class);
    }


}
