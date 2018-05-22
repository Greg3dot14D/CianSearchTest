package ru.greg3d.ciansearchtest.pages.typifiedelements;

import ru.greg3d.ui.htmlelements.elements.HtmlElementExtended;
import ru.greg3d.ui.interfaces.ICheck;

public class CheckBox extends HtmlElementExtended implements ICheck {

    public boolean isChecked(){
        return this.getWrappedElement().getAttribute("class").contains("isc ");
    }

    public boolean unChecked(){
        return !isChecked();
    }

    public void check(){
        if(unChecked())
            this.click();
    }

    public void unCheck(){
        if(isChecked())
            this.click();
    }
}
