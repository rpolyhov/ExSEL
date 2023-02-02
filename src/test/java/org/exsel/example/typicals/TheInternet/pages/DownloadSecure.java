package org.exsel.example.typicals.TheInternet.pages;
import com.codeborne.selenide.SelenideElement;
import org.exsel.example.WebPage;
import static com.codeborne.selenide.Selenide.$x;

public class DownloadSecure implements WebPage {

    public SelenideElement linkText_txt=
            $x("//a[contains(@href,'file.txt')]");


}
