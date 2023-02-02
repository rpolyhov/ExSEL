package org.exsel.ui;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.ElementsContainer;
import com.codeborne.selenide.SelenideElement;
import org.openqa.selenium.WebElement;

import javax.annotation.CheckReturnValue;
import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
public abstract class ElementsContainerWrapper extends ElementsContainer {
    public ElementsContainerWrapper() {
    }

    public void click() {
        this.getSelf().click();
    }

    public void hover() {
        this.getSelf().hover();
    }

    public String getText() {
        return this.getSelf().getText();
    }

    public void setValue(String value) {this.getSelf().setValue(value);}
    public String val() {return this.getSelf().val();}
    public SelenideElement shouldBe(Condition condition) {return this.getSelf().shouldBe(condition);}
    public SelenideElement shouldHave(Condition condition) {return this.getSelf().shouldHave(condition);}
    public String getAttribute(String attribute) {return this.getSelf().getAttribute(attribute);}

    public void dragAndDropTo(WebElement element) {this.getSelf().dragAndDropTo(element);}


}
