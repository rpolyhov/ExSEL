package org.exsel.example.typicals;


import com.codeborne.selenide.*;

import com.codeborne.selenide.testng.SoftAsserts;
import org.assertj.core.api.Assertions;

import org.assertj.core.api.SoftAssertions;
import org.awaitility.core.ConditionEvaluationListener;
import org.awaitility.core.EvaluatedCondition;
import org.exsel.annotations.MaxTimeOut;
import org.exsel.example.BaseTest;

import org.exsel.helpers.FilesHelper;
import org.exsel.waitings.Until;
import org.openqa.selenium.Keys;
import org.openqa.selenium.interactions.Actions;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;

import java.io.File;
import java.io.FileNotFoundException;

import static com.codeborne.selenide.AssertionMode.SOFT;
import static com.codeborne.selenide.AssertionMode.STRICT;
import static com.codeborne.selenide.Selenide.*;
import static java.util.concurrent.TimeUnit.MINUTES;
import static java.util.concurrent.TimeUnit.SECONDS;

import static org.awaitility.Awaitility.await;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

@Listeners(SoftAsserts.class)
public class TestTheInternetTypicals extends BaseTest {

    public TestTheInternetTypicals() {
        Configuration.baseUrl = "http://the-internet.herokuapp.com";
    }

    //@Test
    public void testInputs() {
        open("/inputs");
        String text = "123";
        app.theInternet.pages.inputsPage.inputs.setValue(text);

        app.theInternet.pages.inputsPage.inputs.shouldBe(Condition.exactValue(text));
        //или hamcrest
        assertThat(app.theInternet.pages.inputsPage.inputs.val(), is(text));
        //или assertj
        Assertions.assertThat(app.theInternet.pages.inputsPage.inputs.val())
                .isEqualTo(text);

    }

    //@Test
    public void testDropDown() {
        String selectValue = "Option 1";
        open("https://the-internet.herokuapp.com/dropdown");
        app.theInternet.pages.mainPage.dropdown.selectOption(selectValue);

        app.theInternet.pages.mainPage.dropdown.shouldBe(Condition.text(selectValue));
        //или
        assertThat(app.theInternet.pages.mainPage.dropdown.getSelectedOptionText(), is(selectValue));
        //или assertj
        Assertions.assertThat(app.theInternet.pages.mainPage.dropdown.getSelectedOptionText())
                .isEqualTo(selectValue);
    }

    //@Test
    public void testCheckboxes() {
        open("https://the-internet.herokuapp.com/checkboxes");
        app.theInternet.pages.mainPage.checkboxes.check(1);

        app.theInternet.pages.mainPage.checkboxes.uncheck(2);
    }

    // @Test
    public void testHorizontal_slider() {
        open("https://the-internet.herokuapp.com/horizontal_slider");
        double value = 4.5;
        double step = app.theInternet.pages.mainPage.horizontal_slider.getStep();
        double max = app.theInternet.pages.mainPage.horizontal_slider.getMax();
        Actions action = actions()
                .clickAndHold(app.theInternet.pages.mainPage.horizontal_slider.input)
                .moveByOffset(-60, 0);
        for (double i = 0.0; i < value && i < max; i = i + step)
            action.keyDown(Keys.ARROW_RIGHT);
        action.perform();

        app.theInternet.pages.mainPage.horizontal_slider.value.shouldBe(Condition.text(Double.toString(value)));
        //или
        assertThat(app.theInternet.pages.mainPage.horizontal_slider.value.getText(), is(Double.toString(value)));
        //или assertj
        Assertions.assertThat(app.theInternet.pages.mainPage.horizontal_slider.value.getText())
                .isEqualTo(Double.toString(value));
    }

    //  @Test
    public void testInputsPlayWright() {
        open("http://the-internet.herokuapp.com/inputs");
        $x("//input[@type='number']").setValue("12345");
        page.locator("//input[@type='number']").fill("1234");
    }

    //@Test
    public void drag_and_drop() {
        open("http://the-internet.herokuapp.com/drag_and_drop");
        app.theInternet.pages.dragAndDropPage.a.dragAndDropTo(app.theInternet.pages.dragAndDropPage.b.getSelf());

        app.theInternet.pages.dragAndDropPage.a.shouldBe(Condition.text("B"));
        app.theInternet.pages.dragAndDropPage.b.shouldBe(Condition.text("A"));
    }

    //@Test
    @MaxTimeOut(seconds = 54000)
    public void upload() {
        String result = "File Uploaded!";
        String fileName = "adobeid.pdf";

        open("http://the-internet.herokuapp.com/upload");
        app.theInternet.pages.uploadPage.file_upload.uploadFile(FilesHelper.getFile(fileName));
        app.theInternet.pages.uploadPage.file_submit.click();
        // Softassert in Selenide
        Configuration.assertionMode = SOFT;
        app.theInternet.pages.uploadPage.uploadResult.shouldHave(Condition.text(result));
        app.theInternet.pages.uploadPage.uploadResult.shouldHave(Condition.text(fileName));
        Configuration.assertionMode = STRICT;

        // или
        // но тут совт ассерт не работает
        app.theInternet.pages.uploadPage.uploadResult.shouldHave(Condition.text(result), Condition.text(fileName));
        // Configuration.assertionMode = STRICT;

        //или Until
        Until.assertTimeout(getMaxTimeout())
                .setSoftAssert()
                .assertThat(String.format("Отображается %s", result), app.theInternet.pages.uploadPage.uploadResult.getText(), containsString(result))
                .assertThat(String.format("Отображается имя загруженного файла %s", fileName), app.theInternet.pages.uploadPage.uploadResult.getText(), containsString(fileName))
                .assertAll();

        // или assertj с SoftAssertions
        SoftAssertions softly = new SoftAssertions();
        softly.assertThat(app.theInternet.pages.uploadPage.uploadResult.getText()).contains(result);
        softly.assertThat(app.theInternet.pages.uploadPage.uploadResult.getText()).contains(fileName);
        softly.assertAll();

        // или просто assertj, но сот ассерт не работает
        Assertions.assertThat(app.theInternet.pages.uploadPage.uploadResult.getText())
                .contains(result).contains(fileName);
        // или assertj c awaitility
        await("Здесь указывается алиас").
                conditionEvaluationListener( beforeEvaluation -> {})

                .pollInSameThread()
                .during(19, SECONDS)
                .atMost(20, SECONDS)
                .pollInterval(0, SECONDS)
                .pollDelay(0, SECONDS)
                //.catchUncaughtExceptions()
                .ignoreException(NullPointerException.class)
                .untilAsserted(
                        () -> {
                            Assertions.assertThat(app.theInternet.pages.uploadPage.uploadResult.getText())
                                    .contains(result).contains(fileName);
                        });
    }

    //@Test
    @MaxTimeOut(seconds = 60000)
    public void jqueryui_menu() {
        open("/jqueryui/menu");
        File file = app.theInternet.pages.jqueryUIMenuPage
                .menu
                .Enabled_Menu
                .Downloads_menu
                .PDF_menu
                .selectAndDownloadFile();
        await("Проверка сохранения файла")
                .pollInSameThread()
                .atMost(getMaxTimeout(), SECONDS)
                .untilAsserted(() -> file.exists());
        file.delete();
    }

    //@Test
    @MaxTimeOut(seconds = 60000)
    public void tinymce() {
        open("/tinymce");
        switchTo().frame(app.theInternet.pages.tinymcePage.frame.getSelf());
        app.theInternet.pages.tinymcePage.body.shouldBe(Condition.text("Your content goes here."));
        switchTo().defaultContent();
    }

    @Test
    public void download_secure() throws FileNotFoundException {
        open("/download_secure",
                AuthenticationType.BASIC,
                new BasicAuthCredentials("admin", "admin"));
        app.theInternet.pages.downloadSecure.linkText_txt.click();
        File file = app.theInternet.pages.downloadSecure.linkText_txt.download();

        await("Проверка сохранения файла").pollInSameThread()
                .atMost(getMaxTimeout(), SECONDS)
                .untilAsserted(() -> file.exists());
        file.delete();


    }
}
