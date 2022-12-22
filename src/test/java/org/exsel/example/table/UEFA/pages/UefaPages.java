package org.exsel.example.table.UEFA.pages;

import org.exsel.ui.ElementsContainerWrapper;
import org.exsel.ui.PageDecorator;

import static com.codeborne.selenide.Selenide.page;

public class UefaPages {
    public MainPageUefa mainPage;

    public UefaPages(){
        mainPage=new MainPageUefa();
        mainPage = page(MainPageUefa.class);
        PageDecorator.setParentToObject(mainPage, ElementsContainerWrapper.class);
    }


}
