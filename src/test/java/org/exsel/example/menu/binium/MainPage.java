package org.exsel.example.menu.binium;

import org.exsel.ui.annotations.FindByParametrised;
import org.exsel.ui.elements.menu.MenuBlockSE;

public class MainPage {

    @FindByParametrised(param1 = "Брокеры")
    public MenuBrokery menuBrokery;

    public static class MenuBrokery extends MenuBlockSE {

   /*     @FindByParametrised(param1 = "Действия")
        public WidgetMenu_Block_0 Действия_WidgetMenu;

        public static class WidgetMenu_Block_0 extends WidgetMenuBlockSE {
            @FindByParametrised(param1 = "Печать материалов")
            public WidgetMenuSE печать_материалов_WidgetMenu1;
        }*/
    }
}
