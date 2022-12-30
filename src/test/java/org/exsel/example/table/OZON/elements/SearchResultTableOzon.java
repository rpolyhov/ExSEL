package org.exsel.example.table.OZON.elements;

import com.codeborne.selenide.SelenideElement;
import org.exsel.ui.annotations.grid.TableCell;
import org.exsel.ui.annotations.grid.Tables;

@Tables(cssTable = "div[class='k8z']",
        cssRow = ">div[class='kx6 x6k']")
public class SearchResultTableOzon {
    @TableCell(cssCell = " a[class='tile-hover-target uk3']>span>span")
    public String name;
    @TableCell(cssCell = " div[class='k7x'] span[@class='_32-a2']")
    public String price;
    @TableCell(cssCell = " div[class='k7x'] div[class='_33-a0']")
    public String ozonprice;
    @TableCell(cssCell = " div[class='k7x'] div[class='vk6 k7v'] button[class='_4-a1']")
    public SelenideElement inBasket;
}
