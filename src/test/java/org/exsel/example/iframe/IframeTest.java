package org.exsel.example.iframe;

import org.exsel.example.BaseTest;
import org.testng.annotations.Test;

import static com.codeborne.selenide.Selenide.*;

public class IframeTest extends BaseTest {

    @Test
    public void test() throws InterruptedException {
        open("https://smart-lab.ru/gr/MOEX.POLY");
        switchTo().frame(app.smartLab.pages.mainPage.iframeSmatrLab.getSelf());
        for (int i=0;i<100;i++){
            Thread.sleep(500);
            app.smartLab.pages.mainPage.iframeSmatrLab.comments
                    .get(0).scrollIntoView(false);
        }
        switchTo().defaultContent();

        //Thread.sleep(10000);
    }
}
