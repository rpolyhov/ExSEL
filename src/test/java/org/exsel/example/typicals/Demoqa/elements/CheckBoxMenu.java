package org.exsel.example.typicals.Demoqa.elements;

import com.codeborne.selenide.SelenideElement;
import org.exsel.ui.ElementsContainerWrapper;
import org.exsel.ui.annotations.FindByPar;
import org.exsel.ui.elements.menu.MenuAbstractSE;
import org.openqa.selenium.support.FindBy;

import java.util.LinkedList;

@FindBy(xpath = "//div[@class='check-box-tree-wrapper']")
public class CheckBoxMenu extends ElementsContainerWrapper {
    @FindByPar(param1 = "Home")
    public Menu1Level Home_Menu;

    public static class Menu1Level extends MenuExp {
        @FindByPar(param1 = "Documents")
        public Menu2Level Documents_menu;

        public static class Menu2Level extends MenuExp {
            @FindByPar(param1 = "Office")
            public Menu3Level Office_menu;

            public static class Menu3Level extends MenuExp {
                @FindByPar(param1 = "Private")
                public MenuExp Private_menu;

            }
        }
    }

    @FindByPar(xpath = ".//span[@class='rct-title' and text()='%s']//ancestor::li[contains(@class,'rct-node')][1]")
    public static class MenuExp extends MenuAbstractSE {

        @FindBy(xpath = ".//label")
        public Point point;

        public static class Point extends ElementsContainerWrapper {
            @FindBy(xpath = ".//span[@class='rct-checkbox']//*[contains(@class,'rct-icon')][1]")
            public SelenideElement checkBox;

        }



        protected boolean isExpanded() {
            String attr = getSelf().getAttribute("class");
            return attr.contains("expanded");
        }
        protected boolean isCollapsed() {
            String attr = getSelf().getAttribute("class");
            return attr.contains("collapsed");
        }


        @Override
        protected String getXpathExpandElement() {
            return ".//button[@title='Toggle']";
        }

        @Override
        public void click() {
            if (!isCheckPosition())
                this.point.click();
        }
        public Boolean isCheckPosition() {
            return (this.point.checkBox.getAttribute("class").contains("-check"));
        }

        public Boolean isCheck() {
            boolean result=true;
            LinkedList<MenuExp> list = new LinkedList<>();
            this.selectItems(list);
            for (MenuExp item : list) {
                 result=result&&item.isCheckPosition();
            }
            return result;
        }

    }

}
