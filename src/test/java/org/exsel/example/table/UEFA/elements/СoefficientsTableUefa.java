package org.exsel.example.table.UEFA.elements;

import com.codeborne.selenide.SelenideElement;
import org.exsel.ui.annotations.grid.TableCell;
import org.exsel.ui.annotations.grid.Tables;

@Tables(cssTable = "pk-table[table-id='coefficients_table']",
        cssHeader = " pk-table-header-col",
        cssRow = " pk-table-body>pk-table-row",
        cssCell = ">pk-table-cell")
public class СoefficientsTableUefa {
    @TableCell(columnOrder = 1)
    public SelenideElement position;
    @TableCell(columnOrder = 2)
    public String country;
    @TableCell(columnOrder = 3)
    public String year1;
    @TableCell(columnOrder = 4)
    public String year2;
    @TableCell(columnOrder = 5)
    public String year3;
    @TableCell(columnOrder = 6)
    public String year4;
    @TableCell(columnOrder = 7)
    public String year5;
    @TableCell(columnOrder = 8)
    public String sum;
    @TableCell(columnOrder = 9)
    public String clubs;
}
