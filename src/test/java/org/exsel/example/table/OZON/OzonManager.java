package org.exsel.example.table.OZON;


import org.exsel.example.AppManager;
import org.exsel.example.table.OZON.helpers.OzonHelpers;
import org.exsel.example.table.OZON.pages.OzonPages;

public class OzonManager {
    public OzonPages pages;
    public OzonHelpers helpers;
    public OzonManager() {
        pages = new OzonPages();
        helpers=new OzonHelpers();
    }

}
