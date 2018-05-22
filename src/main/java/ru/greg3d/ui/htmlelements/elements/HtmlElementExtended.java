package ru.greg3d.ui.htmlelements.elements;

import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.WebElement;
import ru.greg3d.ui.interfaces.IElement;
import ru.greg3d.ui.interfaces.IText;
import ru.yandex.qatools.htmlelements.element.HtmlElement;

/**
 * Created by SBT-Konovalov-GV on 25.11.2016.
 */
public class HtmlElementExtended extends HtmlElement implements IElement, IText {

    @Override
    public WebElement getWrappedElement(){
        try{
            WebElement element = super.getWrappedElement();
            if(element == null)
                throw new NullPointerException(String.format("Can't get element [%s]\n", this.getName()));
            return element;
        //}catch (NoSuchElementException e){
        }catch (WebDriverException e){
            throw new NoSuchElementException(String.format("Can't get element [%s]\n%s", this.getName(), e.getMessage()));
        }
    }

    public boolean isExist(){
        try {

            this.getWrappedElement().isDisplayed();
        }catch (NoSuchElementException e){
            return false;
        }
        return true;
    }
}
