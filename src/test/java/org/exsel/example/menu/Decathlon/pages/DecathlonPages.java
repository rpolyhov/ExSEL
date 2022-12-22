package org.exsel.example.menu.Decathlon.pages;

import org.exsel.ui.ElementsContainerWrapper;
import org.exsel.ui.PageDecorator;

import static com.codeborne.selenide.Selenide.page;

public class DecathlonPages {
    public MainPageDecathlon mainPage;

    public DecathlonPages(){
        mainPage=new MainPageDecathlon();
        mainPage = page(MainPageDecathlon.class);
        PageDecorator.setParentToObject(mainPage, ElementsContainerWrapper.class);
    }


}
