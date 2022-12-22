package org.exsel.example.menu.Aspro.elements;

import org.exsel.ui.annotations.FindByPar;
import org.exsel.ui.elements.menu.MenuAbstractSE;
import org.openqa.selenium.support.FindBy;
import org.exsel.ui.ElementsContainerWrapper;

@FindBy(xpath ="//ul[@class='main-menu']" )
public class MenuMainAspro extends ElementsContainerWrapper {
    @FindByPar(param1 = "Каталог")
    public Menu1Level Каталог_Menu;

    public static class Menu1Level extends MenuExp {
        @FindByPar(param1 = "Свойства ")
        public Menu2Level Свойства_menu;

        public static class Menu2Level extends MenuExp {
            @FindByPar(param1 = "Группировка свойств")
            public MenuExp Группировка_свойств_menu;
        }
    }


    @FindByPar(xpath =".//a[text()='%s']//ancestor::li[1]" )
    public static class MenuExp extends MenuAbstractSE {
        @Override
        protected boolean isExpanded() {
            String attr = getSelf().getAttribute("class");
            return attr.contains("open") ;
        }
        @Override
        protected boolean isCollapsed() {
            String attr = getSelf().getAttribute("class");
            return attr.contains("closed") ;
        }

        @Override
        protected String getXpathExpandElement() {
            return ".//span[@class='main-menu-arrow']";
        }


    }

}
