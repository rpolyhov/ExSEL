package org.exsel.example.typicals.TheInternet.pages;

import com.codeborne.selenide.SelenideElement;
import org.exsel.example.WebPage;
import org.exsel.example.typicals.TheInternet.elements.DragAndDrop;
import org.exsel.ui.annotations.FindByPar;
import org.openqa.selenium.support.FindBy;

public class UploadPage implements WebPage {

    @FindBy(xpath ="//input[@id='file-upload']")
    public SelenideElement file_upload;

    @FindBy(xpath ="//input[@id='file-submit']")
    public SelenideElement file_submit;
    @FindBy(xpath ="//div[@class='example']")
    public SelenideElement uploadResult;


}
