package org.exsel.ui;

import com.codeborne.selenide.ElementsContainer;
import com.codeborne.selenide.SelenideElement;

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

}
