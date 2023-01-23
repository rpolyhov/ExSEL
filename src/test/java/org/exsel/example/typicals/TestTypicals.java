package org.exsel.example.typicals;


import com.codeborne.selenide.Condition;
import org.assertj.core.api.Assertions;

import org.exsel.example.BaseTest;
import org.openqa.selenium.Keys;
import org.openqa.selenium.interactions.Actions;
import org.testng.annotations.Test;
import static com.codeborne.selenide.Selenide.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;


public class TestTypicals extends BaseTest {

   @Test
    public void testInputs() {
        open("http://the-internet.herokuapp.com/inputs");
        String text="123";
        app.theInternet.pages.mainPage.inputs.setValue(text);

        app.theInternet.pages.mainPage.inputs.shouldBe(Condition.exactValue(text));
        //или hamcrest
        assertThat(app.theInternet.pages.mainPage.inputs.val(),is(text));
       //или assertj
       Assertions.assertThat(app.theInternet.pages.mainPage.inputs.val())
                .isEqualTo(text);

    }
   @Test
    public void testDropDown() {
        String selectValue="Option 1";
        open("https://the-internet.herokuapp.com/dropdown");
        app.theInternet.pages.mainPage.dropdown.selectOption(selectValue);

        app.theInternet.pages.mainPage.dropdown.shouldBe(Condition.text(selectValue));
        //или
        assertThat(app.theInternet.pages.mainPage.dropdown.getSelectedOptionText(),is(selectValue));
       //или assertj
       Assertions.assertThat(app.theInternet.pages.mainPage.dropdown.getSelectedOptionText())
               .isEqualTo(selectValue);
    }
    @Test
    public void testCheckboxes()  {
        open("https://the-internet.herokuapp.com/checkboxes");
        app.theInternet.pages.mainPage.checkboxes.check(1);

        app.theInternet.pages.mainPage.checkboxes.uncheck(2);
       }
    @Test
       public void testHorizontal_slider()  {
        open("https://the-internet.herokuapp.com/horizontal_slider");
        double  value=4.5;
        double step = app.theInternet.pages.mainPage.horizontal_slider.getStep();
        double max = app.theInternet.pages.mainPage.horizontal_slider.getMax();
        Actions action= actions()
                .clickAndHold(app.theInternet.pages.mainPage.horizontal_slider.input)
                .moveByOffset(-60,0);
        for (double i=0.0;i<value&&i<max;i=i+step)
            action.keyDown(Keys.ARROW_RIGHT);
        action.perform();

        app.theInternet.pages.mainPage.horizontal_slider.value.shouldBe(Condition.text(Double.toString(value)));
        //или
        assertThat(app.theInternet.pages.mainPage.horizontal_slider.value.getText(),is(Double.toString(value)));
        //или assertj
        Assertions.assertThat(app.theInternet.pages.mainPage.horizontal_slider.value.getText())
                .isEqualTo(Double.toString(value));
    }
    @Test
    public void testInputsPlayWright() {
        open("http://the-internet.herokuapp.com/inputs");
        $x("//input[@type='number']").setValue("12345");
        page.locator("//input[@type='number']").fill("1234");
    }
}
