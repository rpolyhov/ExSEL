package org.exsel.example.typicals.TheInternet.pages;

import com.codeborne.selenide.SelenideElement;
import org.exsel.WebPage;
import org.openqa.selenium.support.FindBy;

public class UploadPage implements WebPage {

    @FindBy(xpath ="//input[@id='file-upload']")
    public SelenideElement file_upload;

    @FindBy(xpath ="//input[@id='file-submit']")
    public SelenideElement file_submit;
    @FindBy(xpath ="//div[@class='example']")
    public SelenideElement uploadResult;


}
