package org.exsel.ui.elements.menu;

import com.codeborne.selenide.Condition;
import org.openqa.selenium.support.FindBy;


@FindBy(xpath = ".//span[text()='%s']//ancestor::li[@id]")
public  class MenuBlockSE extends MenuAbstractSE  {
    @Override
    public String getXpathExpandElement() {
        return ".";
    }

    protected boolean isExpanded() {
        return getExpandElement().$x("/ul").is(Condition.visible);
    }

    protected boolean isCollapsed() {
        return isExpanded();
    }
    protected void expand(){
        if (!isExpanded()) getExpandElement().hover();
    }

}