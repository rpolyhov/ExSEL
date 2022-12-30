package org.exsel.example.table.OZON.helpers;

import com.codeborne.selenide.WebDriverRunner;
import org.exsel.example.AppManager;
import org.exsel.example.table.OZON.elements.SearchResultTableOzon;
import org.exsel.ui.helpers.GridHelper;
import org.exsel.waitings.Until;

import java.util.List;

public   class OzonTableHelper {
    List<SearchResultTableOzon> table;
    int size;
    OzonTableHelper() {
    }
    public <T> Integer getSize()  {
        getTable(new SearchResultTableOzon()).get(2).inBasket.click();
        table = getTable(new SearchResultTableOzon());// считываем таблицу
   /*     while (true) {
            size = table.size();// определяем кол-во строк
            if (size!=0) table.get(size-1).position.scrollIntoView(true);// Скролируем до последней строки
            if (!Until.isTrueStatic(5000, () ->
                    (table = getTable(new SearchResultTableOzon())).size() != size)) break; // Если в течении таймаута новые не загрузились, тогда считаем, что вся таблица загружена
        }*/
        return table.size();
    }

    private  <T> List<T> getTable(T table) {
        return (List<T>) new GridHelper()
                .toModelList(WebDriverRunner.getWebDriver(), table.getClass());

    }


}
