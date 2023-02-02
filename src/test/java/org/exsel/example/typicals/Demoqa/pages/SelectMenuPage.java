package org.exsel.example.typicals.Demoqa.pages;

import com.codeborne.selenide.SelenideElement;
import org.exsel.example.WebPage;
import org.exsel.example.typicals.Demoqa.elements.SelectDiv;
import org.exsel.ui.annotations.FindByPar;
import org.openqa.selenium.support.FindBy;

public class SelectMenuPage implements WebPage {

    @FindByPar(param1 = "withOptGroup")
    public SelectDiv selectValue;
    @FindByPar(param1 = "selectOne")
    public SelectDiv selectOne;

    @FindBy(xpath = ".//select[@id='oldSelectMenu']")
    public SelenideElement oldStyleSelectMenu;

    @FindBy(xpath = ".//select[@id='cars']")
    public SelenideElement multySelectMenu;



    @FindBy(xpath = ".//b[text()='Multiselect drop down']//ancestor::div[1]/div")
    public SelectDiv selectMultiDiv;


}
