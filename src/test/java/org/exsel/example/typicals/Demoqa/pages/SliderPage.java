package org.exsel.example.typicals.Demoqa.pages;

import com.codeborne.selenide.SelenideElement;
import org.exsel.example.WebPage;
import org.exsel.example.typicals.Demoqa.elements.CheckBoxMenu;
import org.openqa.selenium.support.FindBy;

public class SliderPage implements WebPage {
    @FindBy(xpath=".//input[@type='range']")
    public SelenideElement slider;
}
