package org.exsel.example.typicals.TheInternet.pages;


import com.codeborne.selenide.SelenideElement;

import org.exsel.WebPage;
import org.exsel.example.typicals.TheInternet.elements.Checkboxes;
import org.exsel.example.typicals.TheInternet.elements.HorizontalSlider;
import org.openqa.selenium.support.FindBy;

public class MainPageTheInternet implements WebPage {

    @FindBy(xpath =".//select[@id='dropdown']")
    public SelenideElement dropdown;
    @FindBy(xpath =".//form[@id='checkboxes']")
    public Checkboxes checkboxes;

    @FindBy(xpath =".//div[@class='sliderContainer']")
    public HorizontalSlider horizontal_slider;

}
