package ru.greg3d.ciansearchtest.pages.typifiedelements;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import ru.greg3d.ui.htmlelements.elements.HtmlElementDriverable;
import ru.greg3d.ui.interfaces.IText;

public class DropDownButton extends HtmlElementDriverable implements IText{

    @FindBy(css = "button")
    private WebElement arrow;

    @FindBy(css = "button")
    private WebElement button;

    protected WebElement getArrow(){
        return this.arrow;
    }

    @Override
    public String getText(){
        return this.button.getText();
    }

    public void open(){
        if(isClosed())
            this.getArrow().click();
    }

    public void close(){
        if(isOpened())
            this.getArrow().click();
    }

    protected boolean isOpened(){
        return false;
    }

    protected boolean isClosed(){
        return !isOpened();
    }
}
