package org.exsel.example.typicals.TheInternet.elements;


import org.exsel.ui.ElementsContainerWrapper;
import org.exsel.ui.annotations.FindByPar;


@FindByPar(xpath =".//div[@draggable='true' and @id='column-%s']")
public class DragAndDrop extends ElementsContainerWrapper {
}
