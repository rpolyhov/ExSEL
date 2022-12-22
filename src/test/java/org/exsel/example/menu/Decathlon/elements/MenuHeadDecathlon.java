package org.exsel.example.menu.Decathlon.elements;

import org.exsel.ui.annotations.FindByPar;
import org.exsel.ui.elements.menu.MenuAbstractSE;
import org.openqa.selenium.support.FindBy;
import org.exsel.ui.ElementsContainerWrapper;

@FindBy(xpath ="//nav[contains(@class,'DesktopCatalogMenu')]" )
public class MenuHeadDecathlon extends ElementsContainerWrapper {

    @FindByPar(param1 = "Детям")
    public CatalogMenuExp Детям_Menu;

    @FindByPar(xpath =".//ul[contains(@class,'CatalogMenu')]//a[ text()='%s']" )
    public static class CatalogMenuExp extends MenuAbstractSE {
        @Override
        protected boolean isExpanded() {
            String attr = getExpandElement().getAttribute("class");
            return attr.contains("hover") ;
        }
        @Override
        protected boolean isCollapsed() {
            return !isExpanded();
        }

        @Override
        protected String getXpathExpandElement() {
            return ".//parent::li";
        }

        public void click(){
            if (!isExpanded()) getExpandElement().hover();
        }
    }

}
