package org.exsel.example.table.RBC;


import org.exsel.example.table.RBC.helpers.CashRBCHelpers;
import org.exsel.example.table.RBC.pages.CashRBCPages;
import org.exsel.example.table.UEFA.helpers.UefaHelpers;
import org.exsel.example.table.UEFA.pages.UefaPages;

public class RBCManager {
    public CashRBCPages pages;
    public CashRBCHelpers helpers;
    public RBCManager() {
        pages = new CashRBCPages();
        helpers=new CashRBCHelpers();
    }

}
