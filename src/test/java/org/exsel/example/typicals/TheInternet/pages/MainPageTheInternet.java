package org.exsel.example.typicals.TheInternet.pages;


import com.codeborne.selenide.SelenideElement;

import org.exsel.example.typicals.TheInternet.elements.Checkboxes;
import org.exsel.example.typicals.TheInternet.elements.HorizontalSlider;
import org.exsel.example.typicals.TheInternet.elements.Inputs;
import org.openqa.selenium.support.FindBy;

public class MainPageTheInternet {

    public Inputs inputs;
    @FindBy(xpath =".//select[@id='dropdown']")
    public SelenideElement dropdown;
    @FindBy(xpath =".//form[@id='checkboxes']")
    public Checkboxes checkboxes;

    @FindBy(xpath =".//div[@class='sliderContainer']")
    public HorizontalSlider horizontal_slider;
}
