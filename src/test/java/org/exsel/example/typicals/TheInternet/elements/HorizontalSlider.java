package org.exsel.example.typicals.TheInternet.elements;


import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.SelenideElement;
import org.exsel.ui.ElementsContainerWrapper;
import org.openqa.selenium.support.FindBy;

@FindBy(xpath =".//div[@class='sliderContainer']")
public class HorizontalSlider extends ElementsContainerWrapper {

    @FindBy(xpath =".//input[@type='range']")
    public SelenideElement input;
    @FindBy(xpath =".//span[@id='range']")
    public SelenideElement value;

    public Double getStep() {
        return Double.valueOf(input.getAttribute("step"));
    }
    public Double getMax() {
        return Double.valueOf(input.getAttribute("max"));
    }

    public Double getAttributeValue(String att) {
        return Double.valueOf(input.getAttribute(att));
    }
}
