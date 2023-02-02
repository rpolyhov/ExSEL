package org.exsel.example.typicals.Demoqa;


import org.exsel.example.Service;
import org.exsel.example.typicals.Demoqa.pages.DemoqaPages;

public class DemoqaManager {
    public DemoqaPages pages;
    public DemoqaManager() {
        pages = Service.initPage(DemoqaPages.class);

    }

}
