package org.exsel.example.table.UEFA.helpers;

import com.codeborne.selenide.WebDriverRunner;
import org.exsel.example.AppManager;
import org.exsel.example.table.UEFA.elements.СoefficientsTableUefa;
import org.exsel.ui.helpers.GridHelper;

import java.util.List;

public class UefaTableHelper {
    AppManager app;

    UefaTableHelper(AppManager app) {
        this.app = app;
    }

    public Integer getSize() throws InterruptedException {
        List<СoefficientsTableUefa> table=new GridHelper()
                .toModelList(WebDriverRunner.getWebDriver(), СoefficientsTableUefa.class);
        int size = table.size();

        while (true) {
            Thread.sleep(5000);
            table.get(table.size()-1).position.scrollIntoView(true);
            table = new GridHelper()
                    .toModelList(WebDriverRunner.getWebDriver(), СoefficientsTableUefa.class);
            if (table.size() == size) break;
            else size = table.size();
        }
        return table.size();

    }
}
