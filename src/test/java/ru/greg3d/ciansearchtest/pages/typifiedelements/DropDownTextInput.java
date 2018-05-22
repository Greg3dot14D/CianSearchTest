package ru.greg3d.ciansearchtest.pages.typifiedelements;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import ru.greg3d.ui.htmlelements.elements.HtmlElementExtended;
import ru.greg3d.ui.interfaces.ITextEdit;
import ru.greg3d.ui.matchers.common.ElementMatchers;
import ru.greg3d.waitings.Until;
import ru.yandex.qatools.htmlelements.annotations.Timeout;

import java.util.List;

public class DropDownTextInput extends DropDownButton implements ITextEdit{

    @FindBy(css = "input")
    private WebElement input;

    protected WebElement getInput(){
        return  this.input;
    }

    @FindBy(css = "button")
    private WebElement arrow;

    @FindBy(xpath = ".//div[contains(@class,'suggested_items')]")
    private List<WebElement> itemsList;

    protected List<WebElement> getItemsList(){
        return this.itemsList;
    }

    @Timeout(10)
    @FindBy(xpath = ".//div[contains(@class,'popup-content')]")
    private HtmlElementExtended popupContainer;

    @FindBy(xpath = ".//div[contains(@class,'suggested_items')][1]")
    private WebElement firstItem;

    @Override
    protected WebElement getArrow(){
        return this.arrow;
    }

    @Override
    public void setText(String text){
        this.getInput().clear();
        this.getInput().sendKeys(text);

        this.popupContainer.isExist();

        //Until.assertTimeout(10000).assertThat(this.popupContainer, ElementMatchers.isExist());
        this.firstItem.click();
    }
}
