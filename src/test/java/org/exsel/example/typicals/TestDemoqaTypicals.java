package org.exsel.example.typicals;

import com.codeborne.pdftest.PDF;
import com.codeborne.selenide.*;
import io.qameta.allure.Step;
import org.assertj.core.api.Assertions;
import org.exsel.SelenoidSettingFactory;

import org.exsel.example.BaseTest;
import org.exsel.example.typicals.Demoqa.elements.SortableDiv;
import org.exsel.helpers.DateHelper;
import org.testng.annotations.Test;

import java.io.File;
import java.io.IOException;
import java.time.Duration;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import static com.codeborne.pdftest.assertj.Assertions.assertThat;
import static com.codeborne.selenide.Condition.attribute;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.FileDownloadMode.FOLDER;
import static com.codeborne.selenide.FileDownloadMode.PROXY;
import static com.codeborne.selenide.Selenide.*;
import static com.codeborne.selenide.files.FileFilters.withExtension;
import static org.exsel.helpers.FilesHelper.getFilePath;
import static org.exsel.helpers.FilesHelper.uploadFile;

public class TestDemoqaTypicals extends BaseTest {

    public TestDemoqaTypicals() {
        Configuration.baseUrl = "https://demoqa.com";
    }

    //@Test
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

    //@Test
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

    //@Test
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

    @Test
    @Step
   // @Video
    public void testDownLoad() throws IOException {
        SelenoidSettingFactory.configuration(this.getClass().getSimpleName());
        open("http://172.30.48.50:8080/share/page/document?nodeRef=workspace://SpacesStore/57d4afcc-6b55-4dbe-8fa1-1569c02386f8");

        $x("//input[@name='username']").val("Smoke_user111");
        $x("//input[@name='password']").val("Test123456");
        $x("//button[@type='button' and text()='Войти']").click();
        //attachScreen();
        open("http://172.30.48.50:8080/share/page/document?nodeRef=workspace://SpacesStore/57d4afcc-6b55-4dbe-8fa1-1569c02386f8");
        $x("//a[@href='#approval']").click();
        $x("//span[@title='Печать']").click();
        $x("//div[@class='hd' and text()='Задание параметров отчета']").shouldBe(visible, Duration.ofSeconds(10));
        File report = $x("//button[contains(text(),'ОК') and @type='button' and not(ancestor::div[contains(@class,'hidden')])]")
                .download(DownloadOptions.using(Configuration.fileDownload).withFilter(withExtension("pdf")).withTimeout(20000));
        PDF pdf = new PDF(report);
        assertThat(pdf).containsExactText("Смокин112");
        report.delete();
         assert false;
    }


   // @Test
    public void testUploadAndDownLoadFileThroughProxy() throws IOException {

        Configuration.proxyHost = "127.0.0.1";
        Configuration.proxyPort = 8081;
        Configuration.proxyEnabled = true;
        open("https://webtopdf.com/html-to-pdf");
        String fileName = "test.html";
        uploadFile(getFilePath(fileName), $(".dropfilehere").shouldBe(visible, Duration.ofSeconds(30)));
        $(".tit_complete").shouldBe(Condition.text("Completed"), Duration.ofSeconds(30));

        File downloadfile = $(".downfile_over")
                .download(DownloadOptions.using(PROXY).withFilter(withExtension("pdf")));
        PDF pdf = new PDF(downloadfile);
        assertThat(pdf).containsExactText("FAIL");
    }

   // @Test
    @Step
    public void testUploadAndDownLoadFileInSelenoid() throws IOException {
        SelenoidSettingFactory.configuration(this.getClass().getSimpleName());
        open("https://webtopdf.com");
        $x("//a[@href='/html-to-pdf']").click();
        SelenideElement upload=$(".dropfilehere").shouldBe(visible, Duration.ofSeconds(30));
        String fileName = "test.html";
        uploadFile(getFilePath(fileName), upload);
        $(".tit_complete").shouldBe(Condition.text("Completed"), Duration.ofSeconds(30));
            File downloadfile = $(".downfile_over")
                .download(DownloadOptions.using(FOLDER).withFilter(withExtension("pdf")).withTimeout(30000));

        PDF pdf = new PDF(downloadfile);
        assertThat(pdf).containsExactText("FAIL");
    }

}



