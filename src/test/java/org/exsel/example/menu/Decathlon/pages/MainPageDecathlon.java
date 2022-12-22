package org.exsel.example.menu.Decathlon.pages;

import org.exsel.example.menu.Decathlon.elements.MenuHeadDecathlon;
import org.exsel.ui.annotations.FindByPar;
import org.exsel.ui.elements.menu.MenuAbstractSE;

import java.util.LinkedList;


public class MainPageDecathlon {

   //@FindByPar(param1 = "Брокеры")
    public MenuHeadDecathlon menuHeadDecathlon;

    // Два элемента ниже вставил сюда, чтобы не тратить время на создание структуры, она не нужна, т.к это пример
    @FindByPar(param1 = "Обувь")
    public MenuExp Обувь_menu;
    @FindByPar(param1 = "Ботинки")
    public MenuExp Ботинки_menu;

    @FindByPar(xpath ="//div[contains(@class,'MenuCategory')]//a[ text()='%s']" )
    public static class MenuExp extends MenuAbstractSE {
        @Override
        protected boolean isExpanded() {
            String attr = getExpandElement().getAttribute("class");
            return attr.contains("Active") ;
        }
        @Override
        protected boolean isCollapsed() {
            return !isExpanded();
        }

        @Override
        protected String getXpathExpandElement() {
            return ".";
        }

   /*     protected void expand(){
            if (!isExpanded()) getExpandElement().hover();
        }*/

        public void select() {
            LinkedList<MenuAbstractSE> list = new LinkedList<>();
            this.selectItems(list);
            for (MenuAbstractSE item : list) {
                    item.hover();

            }
        }
    }

}
