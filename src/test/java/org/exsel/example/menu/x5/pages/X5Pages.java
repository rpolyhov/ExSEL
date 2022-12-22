package org.exsel.example.menu.x5.pages;

import org.exsel.example.menu.x5.pages.MainPageX5;
import org.exsel.ui.ElementsContainerWrapper;
import org.exsel.ui.PageDecorator;

import static com.codeborne.selenide.Selenide.page;

public class X5Pages {
    public MainPageX5 mainPage;

    public X5Pages(){

        mainPage=new MainPageX5();
        mainPage = page(MainPageX5.class);
        PageDecorator.setParentToObject(mainPage, ElementsContainerWrapper.class);
    }


}
