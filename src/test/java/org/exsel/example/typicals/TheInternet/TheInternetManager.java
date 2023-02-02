package org.exsel.example.typicals.TheInternet;


import org.exsel.example.Service;
import org.exsel.example.typicals.TheInternet.helpers.TheInternetHelpers;
import org.exsel.example.typicals.TheInternet.pages.TheInternetPages;

public class TheInternetManager {
    public TheInternetPages pages;
    public TheInternetHelpers helpers;
    public TheInternetManager() {
        pages = Service.initPage(TheInternetPages.class);
        helpers=new TheInternetHelpers();
    }

}
