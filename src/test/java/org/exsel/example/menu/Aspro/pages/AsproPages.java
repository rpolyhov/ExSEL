package org.exsel.example.menu.Aspro.pages;

import org.exsel.ui.ElementsContainerWrapper;
import org.exsel.ui.PageDecorator;

import static com.codeborne.selenide.Selenide.page;

public class AsproPages {
    public MainPageAspro mainPage;

    public AsproPages(){
        mainPage=new MainPageAspro();
        mainPage = page(MainPageAspro.class);
        PageDecorator.setParentToObject(mainPage, ElementsContainerWrapper.class);
    }


}
