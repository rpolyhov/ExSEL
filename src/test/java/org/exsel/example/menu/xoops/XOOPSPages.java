package org.exsel.example.menu.xoops;

import org.exsel.ui.ElementsContainerWrapper;
import org.exsel.ui.PageDecorator;

import static com.codeborne.selenide.Selenide.page;

public class XOOPSPages {
    public MainPageXoops mainPage;

    XOOPSPages(){
        mainPage=new MainPageXoops();
        mainPage = page(MainPageXoops.class);
        PageDecorator.setParentToObject(mainPage, ElementsContainerWrapper.class);
    }


}
