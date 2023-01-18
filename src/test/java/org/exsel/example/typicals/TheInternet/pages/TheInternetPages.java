package org.exsel.example.typicals.TheInternet.pages;

import org.exsel.ui.ElementsContainerWrapper;
import org.exsel.ui.PageDecorator;

import static com.codeborne.selenide.Selenide.page;

public class TheInternetPages {
    public MainPageTheInternet mainPage;
    public TheInternetPages(){
        mainPage=new MainPageTheInternet();
        mainPage = page(MainPageTheInternet.class);
        PageDecorator.setParentToObject(mainPage, ElementsContainerWrapper.class);
    }


}
