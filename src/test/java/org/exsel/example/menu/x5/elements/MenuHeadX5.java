package org.exsel.example.menu.x5.elements;

import org.exsel.ui.annotations.FindByPar;
import org.exsel.ui.elements.menu.MenuAbstractSE;
import org.openqa.selenium.support.FindBy;
import org.exsel.ui.ElementsContainerWrapper;

@FindBy(xpath ="//ul[@class='header__nav-list']" )
public class MenuHeadX5 extends ElementsContainerWrapper {
    @FindByPar(param1 = "Инвесторам")
    public Menu1Level Инвесторам_Menu;

    public static class Menu1Level extends MenuExp {
        @FindByPar(param1 = "Акции")
        public Menu2Level Акции_menu;

        public static class Menu2Level extends MenuExp {
            @FindByPar(param1 = "Дивиденды")
            public MenuExp Дивиденды_menu;
        }
    }


    @FindByPar(xpath =".//a[contains(text(),'%s')]//parent::li" )
    public static class MenuExp extends MenuAbstractSE {
        @Override
        protected boolean isExpanded() {
            String attr = getExpandElement().$x("./*[contains(@class,'sub-menu')]").getAttribute("class");
            return attr.contains("active") ;
        }
        @Override
        protected boolean isCollapsed() {
            return !isExpanded();
        }

        @Override
        protected String getXpathExpandElement() {
            return ".//parent::li";
        }

        protected void expand(){
            if (!isExpanded()) getExpandElement().hover();
        }
    }

}
