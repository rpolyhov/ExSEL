package org.exsel.example.typicals.Demoqa.elements;

import com.codeborne.selenide.SelenideElement;
import org.exsel.ui.ElementsContainerWrapper;
import org.exsel.ui.annotations.FindByPar;
import org.openqa.selenium.Keys;
import org.openqa.selenium.support.FindBy;

@FindByPar(xpath = ".//div[@id='%s']")
public class SelectDiv extends ElementsContainerWrapper {

    @FindBy(xpath = ".//input")
    public SelenideElement input;

    public void selectValue(String value) {
        input.val(value).sendKeys(Keys.ENTER);
    }




}
