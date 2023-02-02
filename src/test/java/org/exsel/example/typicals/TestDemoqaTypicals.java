package org.exsel.example.typicals;


import com.codeborne.selenide.Condition;
import com.codeborne.selenide.Configuration;
import org.assertj.core.api.Assertions;
import org.exsel.example.BaseTest;
import org.exsel.example.typicals.Demoqa.elements.SortableDiv;
import org.exsel.helpers.DateHelper;
import org.testng.annotations.Test;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import static com.codeborne.selenide.Condition.attribute;
import static com.codeborne.selenide.Selenide.*;

public class TestDemoqaTypicals extends BaseTest {

    public TestDemoqaTypicals() {
        Configuration.baseUrl = "https://demoqa.com";
    }

    // @Test
    public void testTreeCheckBox() {
        open("/checkbox");
        app.demoqa.pages.elementsPage
                .checkBoxMenu
                .Home_Menu
                .Documents_menu
                .Office_menu
                .Private_menu
                .select();

        Assertions.assertThat(app.demoqa.pages.elementsPage
                        .checkBoxMenu
                        .Home_Menu
                        .Documents_menu
                        .Office_menu
                        .isCheck())
                .isEqualTo(true);

    }

    // @Test
    public void testSelectMenu() {
        open("/select-menu");
        app.demoqa.pages.selectMenuPage.selectValue.selectValue("Group 2, option 1");
        app.demoqa.pages.selectMenuPage.selectValue.shouldBe(Condition.text("Group 2, option 1"));

        app.demoqa.pages.selectMenuPage.selectOne.selectValue("Ms.");
        app.demoqa.pages.selectMenuPage.selectOne.shouldBe(Condition.text("Ms."));

        app.demoqa.pages.selectMenuPage.oldStyleSelectMenu.selectOption("Black");
        app.demoqa.pages.selectMenuPage.oldStyleSelectMenu.shouldBe(Condition.text("Black"));

        app.demoqa.pages.selectMenuPage.selectMultiDiv.selectValue("Green");
        app.demoqa.pages.selectMenuPage.selectMultiDiv.selectValue("Blue");
        app.demoqa.pages.selectMenuPage.selectMultiDiv.shouldBe(Condition.text("Blue")).shouldBe(Condition.text("Green"));

        app.demoqa.pages.selectMenuPage.multySelectMenu.selectOption("Opel", "Audi");
        app.demoqa.pages.selectMenuPage.multySelectMenu.shouldBe(Condition.text("Opel"), Condition.text("Audi"));

    }

    //@Test
    public void testSortable() {
        open("/sortable");
        String result = "";
        List<SortableDiv> list = Arrays.asList(
                app.demoqa.pages.sortablePage.one,
                app.demoqa.pages.sortablePage.two,
                app.demoqa.pages.sortablePage.three,
                app.demoqa.pages.sortablePage.four,
                app.demoqa.pages.sortablePage.five,
                app.demoqa.pages.sortablePage.six
        );
        for (SortableDiv el : list) {
            result = String.format("%s%s%s", el.getText(), "\n", result)
                    .replace("One\n", "One");
            actions().clickAndHold(el.getSelf())
                    .moveToElement(app.demoqa.pages.sortablePage.six.getSelf())
                    .release().build().perform();
        }
        app.demoqa.pages.sortablePage.content.shouldHave(Condition.text(result));
    }

   // @Test
    public void testDatePicker() {
        open("/date-picker");

        LocalDate date = DateHelper.getDateFromFormat(app.demoqa.pages.datePickerPage
                        .datePickerMonthYearInput.getText(),
                "MM/dd/yyyy");
       /* LocalDateTime datetime = DateHelper.getDateTimeFromFormat(app.demoqa.pages.datePickerPage
                        .dateAndTimePickerInput.getText(),"MMMM dd, yyyy hh:mm AA");*/

      /// SimpleDateHelper.getCurrentDate("MMMM dd, yyyy hh:mm AA");

        String val = DateHelper.addDays(date, 1, "MM/dd/yyyy");
        app.demoqa.pages.datePickerPage.datePickerMonthYearInput.setDate(val);

        app.demoqa.pages.datePickerPage.datePickerMonthYearInput
                .shouldHave(attribute("value", val));


    }
    @Test
    public void testSlider() {
       // int value=55;
        open("/slider");

   /*     executeJavaScript("$('[type=\"range\"]').val('50');" +
                "$('[type=\"range\"]').triggerHandler('slide');");
*/

        executeJavaScript(
                "$('[type=\"range\"]').value=arguments[0];" +
                        "$('[type=\"range\"]').triggerHandler('slide');",
                55
        );

/*       executeJavaScript(
                String.format("$('[type=\"%s\"]').val('%s'); $('[type=\"%s\"]').triggerHandler('slide');",
                        "range", "50","range")
        );*/

        // app.demoqa.pages.sliderPage.slider.


//input[@type='range']


    }






}
