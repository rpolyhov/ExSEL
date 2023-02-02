package org.exsel.example.table.UEFA.helpers;

import com.codeborne.selenide.WebDriverRunner;
import org.exsel.annotations.MaxTimeOut;
import org.exsel.example.AppManager;
import org.exsel.example.table.UEFA.elements.СoefficientsTableUefa;
import org.exsel.ui.helpers.GridHelper;
import org.exsel.waitings.Until;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public   class UefaTableHelper {
    public static Logger LOGGER = LoggerFactory.getLogger(UefaTableHelper.class);
    List<СoefficientsTableUefa> table;
    int size;

    @MaxTimeOut(seconds = 123123)
    public <T> Integer getSize()  {
        LOGGER.info("getSize");
        table = getTable(new СoefficientsTableUefa());// считываем таблицу
        while (true) {
            size = table.size();// определяем кол-во строк
            if (size!=0) table.get(size-1).position.scrollIntoView(true);// Скролируем до последней строки
            if (!Until.isTrueStatic(5000, () ->
                    (table = getTable(new СoefficientsTableUefa())).size() != size)) break; // Если в течении таймаута новые не загрузились, тогда считаем, что вся таблица загружена
        }
        return table.size();
    }

    private  <T> List<T> getTable(T table) {
        return (List<T>) new GridHelper()
                .toModelList(WebDriverRunner.getWebDriver(), table.getClass());

    }


}
