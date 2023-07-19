package org.exsel.example.iframe.pages;

import org.exsel.ui.ElementsContainerWrapper;
import org.exsel.ui.PageDecorator;
import static com.codeborne.selenide.Selenide.page;
public class SmartLabPages {
    public MainPageSmartLab mainPage;


    public SmartLabPages(){
        mainPage =new MainPageSmartLab();
        mainPage = page(MainPageSmartLab .class);
        PageDecorator.setParentToObject(mainPage, ElementsContainerWrapper .class);
    }


}
