package org.exsel.example.iframe.elements;

import com.codeborne.selenide.SelenideElement;
import org.exsel.ui.ElementsContainerWrapper;
import org.openqa.selenium.support.FindBy;

import java.util.List;

import static com.codeborne.selenide.Selenide.$$x;

@FindBy(xpath = "//iframe[@name='chat']")
public class IframeSmatrLab extends ElementsContainerWrapper {
    public List<SelenideElement> comments=
            $$x(".//div[@id='comments-list']//div[@comment]");
}
