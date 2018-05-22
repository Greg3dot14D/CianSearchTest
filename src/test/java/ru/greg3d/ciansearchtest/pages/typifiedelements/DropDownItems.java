package ru.greg3d.ciansearchtest.pages.typifiedelements;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import ru.greg3d.ui.htmlelements.elements.HtmlElementExtended;
import ru.greg3d.ui.matchers.common.ElementMatchers;
import ru.greg3d.waitings.Until;
import ru.yandex.qatools.htmlelements.annotations.Name;

import java.util.List;
import java.util.NoSuchElementException;

public class DropDownItems extends DropDownSelect{

    @Name("Список действий")
    @FindBy(xpath = ".//div[contains(@class,'c-filters-property-content')]//li")
    private List<WebElement> itemsList;

    @FindBy(xpath = ".//div[contains(@class,'c-filters-property-content')]")
    private HtmlElementExtended content;

    protected List<WebElement> getItemsList(){
        return this.itemsList;
    }

    protected HtmlElementExtended getContent(){
        return this.content;
    }
}
