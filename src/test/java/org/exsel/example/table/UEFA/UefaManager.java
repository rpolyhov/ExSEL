package org.exsel.example.table.UEFA;


import org.exsel.example.table.UEFA.helpers.UefaHelpers;
import org.exsel.example.table.UEFA.pages.UefaPages;

public class UefaManager {
    public UefaPages pages;
    public UefaHelpers helpers;
    public UefaManager() {
        pages = new UefaPages();
        helpers=new UefaHelpers();
    }

}
