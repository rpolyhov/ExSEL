package org.exsel.example.typicals.TheInternet.pages;

import org.exsel.WebPage;
import org.exsel.example.typicals.TheInternet.elements.DragAndDrop;
import org.exsel.ui.annotations.FindByPar;

public class DragAndDropPage implements WebPage {

    @FindByPar(param1 ="a")
    public DragAndDrop a;

    @FindByPar(param1 ="b")
    public DragAndDrop b;

}
