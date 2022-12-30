package org.exsel.example.table.OZON.pages;

import org.exsel.ui.ElementsContainerWrapper;
import org.exsel.ui.PageDecorator;

import static com.codeborne.selenide.Selenide.page;

public class OzonPages {
    public MainPageOzon mainPage;

    public OzonPages(){
        mainPage=new MainPageOzon();
        mainPage = page(MainPageOzon.class);
        PageDecorator.setParentToObject(mainPage, ElementsContainerWrapper.class);
    }


}
