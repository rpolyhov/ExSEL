package org.exsel.example.table.RBC.elements;

import org.exsel.ui.annotations.grid.TableCell;
import org.exsel.ui.annotations.grid.Tables;

@Tables(cssTable = "div[class='quote__left__height js-float-set-parent-height']",
        cssHeader = " div[class='js-float-table_head quote__float__table_head']",
        cssRow = " div[class='quote__office__one js-one-office']",
        cssCell = ">div")
public class CashRBCTable {
    @TableCell(columnOrder = 2)
    public String name;
    @TableCell(columnOrder = 4)
    public Double buy;
    @TableCell(columnOrder = 5)
    public Double sell;

    public static int compareBuy(CashRBCTable p1, CashRBCTable p2){
        if(p1.buy > p2.buy)
            return 1;
        return -1;
    }
    public static int compareSell (CashRBCTable p1, CashRBCTable p2){
        if(p1.sell > p2.sell)
            return 1;
        return -1;
    }



}
