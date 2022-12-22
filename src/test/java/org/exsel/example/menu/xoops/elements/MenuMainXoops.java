package org.exsel.example.menu.xoops.elements;

import org.exsel.ui.annotations.FindByPar;
import org.exsel.ui.elements.menu.MenuAbstractSE;
import org.openqa.selenium.support.FindBy;
import org.exsel.ui.ElementsContainerWrapper;

@FindBy(xpath ="//ul[@class='InstrTreeContainer']" )
public class MenuMainXoops extends ElementsContainerWrapper {
    @FindByPar(param1 = "Глава 1. CSS: основы основ")
    public Menu1Level Глава_1_CSS_основы_основ_Menu;

    public static class Menu1Level extends MenuExp {
        @FindByPar(param1 = "Определение стиля с помощью CSS")
        public Menu2Level Определение_стиля_с_помощью_CSS_menu;

        public static class Menu2Level extends MenuExp {
            @FindByPar(param1 = "Синтаксис CSS")
            public MenuExp Синтаксис_CSS_menu;
        }
    }


    @FindByPar(xpath =".//*[text()='%s']//ancestor::li[1]" )
    public static class MenuExp extends MenuAbstractSE {
        @Override
        protected boolean isExpanded() {
            String attr = getSelf().getAttribute("class");
            return attr.contains("Open") ;
        }
        @Override
        protected boolean isCollapsed() {
            String attr = getSelf().getAttribute("class");
            return attr.contains("Closed") ;
        }

        @Override
        protected String getXpathExpandElement() {
            return ".//div[@class='InstrTreeExpand']";
        }

        @Override
        public void click() {
            this.getSelf().$x(".//a").click();
        }


    }

}
