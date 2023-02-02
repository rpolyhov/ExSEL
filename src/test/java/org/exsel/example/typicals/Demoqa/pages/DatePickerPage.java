package org.exsel.example.typicals.Demoqa.pages;

import com.codeborne.selenide.ElementsContainer;
import com.codeborne.selenide.SelenideElement;
import org.exsel.example.WebPage;
import org.exsel.ui.ElementsContainerWrapper;
import org.openqa.selenium.support.FindBy;

import static com.codeborne.selenide.Selenide.executeJavaScript;

public class DatePickerPage implements WebPage {

    @FindBy(xpath = ".//input[@id='datePickerMonthYearInput']")
    public DataEl datePickerMonthYearInput;

    @FindBy(xpath = ".//input[@id='dateAndTimePickerInput']")
    public DataEl dateAndTimePickerInput;



    public static class DataEl extends ElementsContainerWrapper {

        public void setDate(String date) {
            executeJavaScript(
                    String.format("$('[id=\"%s\"]').val('%s')",
                            "datePickerMonthYearInput", date)
            );
            executeJavaScript(
                    "$('.react-datepicker-popper').hide();"
            );


        }
        public String getText(){
            return this.getAttribute("value");
        }
    }

}
