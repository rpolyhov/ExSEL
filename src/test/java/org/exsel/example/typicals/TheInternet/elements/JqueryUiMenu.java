package org.exsel.example.typicals.TheInternet.elements;

import com.codeborne.selenide.SelenideElement;
import org.exsel.ui.ElementsContainerWrapper;
import org.exsel.ui.annotations.FindByPar;
import org.exsel.ui.elements.menu.MenuAbstractSE;
import org.openqa.selenium.By;
import org.openqa.selenium.support.FindBy;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.LinkedList;

@FindBy(xpath = "//ul[@id='menu']")
public class JqueryUiMenu extends ElementsContainerWrapper {
    @FindByPar(param1 = "Enabled")
    public Menu1Level Enabled_Menu;

    public static class Menu1Level extends MenuExp {
        @FindByPar(param1 = "Downloads")
        public Menu2Level Downloads_menu;

        public static class Menu2Level extends MenuExp {
            @FindByPar(param1 = "PDF")
            public MenuExp PDF_menu;
        }
    }

    @FindByPar(xpath = ".//a[text()='%s']//ancestor::li[1]")
    public static class MenuExp extends MenuAbstractSE {

        @FindBy(xpath = "./a")
        public SelenideElement link;


        protected boolean isExpanded() {
            String attr = getSelf().getAttribute("class");
            return attr.contains("ui-menu-item ui-state");
        }

        @Override
        protected boolean isCollapsed() {
            return !isExpanded();
        }

        @Override
        protected String getXpathExpandElement() {
            return ".";
        }

        public  File selectAndDownloadFile() {
            LinkedList<MenuExp> list = new LinkedList<>();
            this.selectItems(list);
            for (MenuExp item : list) {
                if (item == list.getLast()) {
                    File file;
                    try {
                        file = item.getSelf().find(By.xpath("./a")).download();
                    } catch (FileNotFoundException e) {
                        throw new RuntimeException(e);
                    }
                    item.hover();
                    item.click();
                    return file;

                } else item.expand();
            }
            return null;
        }

        @Override
        public void expand() {
            if (!isExpanded()) getExpandElement().hover();
        }

    }

}
