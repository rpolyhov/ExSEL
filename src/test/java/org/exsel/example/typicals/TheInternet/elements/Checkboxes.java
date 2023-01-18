package org.exsel.example.typicals.TheInternet.elements;


import com.codeborne.selenide.Condition;
import com.codeborne.selenide.SelenideElement;
import org.exsel.ui.ElementsContainerWrapper;
import org.openqa.selenium.support.FindBy;

import java.util.Collection;
import java.util.stream.Collectors;

import static com.codeborne.selenide.Selenide.$;

@FindBy(xpath = ".//form[@id='checkboxes']")
public class Checkboxes extends ElementsContainerWrapper {
    public void check(int number) {
        SelenideElement el = getElement(number);
        if (!el.isSelected()) el.click();
        el.shouldBe(Condition.selected);
    }

    public void uncheck(int number) {
        SelenideElement el = getElement(number);
        if (el.isSelected()) el.click();
        el.shouldNotBe(Condition.selected);
    }
    private SelenideElement getElement(int number) {
        Collection<SelenideElement> sel =
                getSelf().$$x("./text()//preceding-sibling::input");
        SelenideElement el =
                sel.stream().collect(Collectors.toList()).get(number - 1);
        return el;
    }
}
