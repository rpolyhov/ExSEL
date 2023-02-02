package org.exsel.ui.elements.menu;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.SelenideElement;
import org.exsel.ui.annotations.IParentable;
import org.exsel.ui.ElementsContainerWrapper;

import java.time.Duration;
import java.util.LinkedList;

public abstract class MenuAbstractSE extends ElementsContainerWrapper implements IParentable {
    private Object parent;
    protected abstract boolean isExpanded();
    protected abstract boolean isCollapsed();
    protected abstract String getXpathExpandElement();

    protected SelenideElement getExpandElement() {
        SelenideElement el= getElement(getXpathExpandElement());
        return el;
    }

    protected SelenideElement getElement(String xpath) {
        return this.getSelf().shouldBe(Condition.visible, Duration.ofMillis(10000)).$x(xpath).shouldBe(Condition.visible, Duration.ofMillis(10000));
    }

    public void expand(){
        if (!isExpanded()) getExpandElement().click();
    }
    protected void collaps() {
        if (!isCollapsed()) getExpandElement().click();
    }
    public void setParent(Object parent) {
        this.parent = parent;
    }
    public Object getParent() {
        return this.parent;
    }

    public void select() {
        LinkedList<MenuAbstractSE> list = new LinkedList<>();
        this.selectItems(list);
        for (MenuAbstractSE item : list) {
            if (item == list.getLast())
                item.click();
            else
                item.expand();
        }
    }

    protected <E extends MenuAbstractSE> void selectItems(LinkedList<E> list) {
        list.addFirst((E) this);
        if (this.getParent() instanceof MenuAbstractSE)
            ((E) this.getParent()).selectItems(list);
    }


}
