package org.exsel.example.typicals.Demoqa.pages;

import com.codeborne.selenide.SelenideElement;
import org.exsel.example.WebPage;
import org.exsel.example.typicals.Demoqa.elements.SelectDiv;
import org.exsel.example.typicals.Demoqa.elements.SortableDiv;
import org.exsel.ui.annotations.FindByPar;
import org.openqa.selenium.support.FindBy;

public class SortablePage implements WebPage {

    @FindByPar(param1 = "One")
    public SortableDiv one;

    @FindByPar(param1 = "Two")
    public SortableDiv two;

    @FindByPar(param1 = "Three")
    public SortableDiv three;

    @FindByPar(param1 = "Four")
    public SortableDiv four;

    @FindByPar(param1 = "Five")
    public SortableDiv five;

    @FindByPar(param1 = "Six")
    public SortableDiv six;


    @FindBy(xpath = ".//div[@class='tab-content']")
    public SelenideElement content;


}
