package ru.greg3d.ciansearchtest.pages.typifiedelements;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import ru.greg3d.ui.htmlelements.elements.HtmlElementExtended;
import ru.greg3d.ui.matchers.common.ElementMatchers;
import ru.greg3d.waitings.Until;
import ru.yandex.qatools.htmlelements.annotations.Name;

import java.util.List;
import java.util.NoSuchElementException;

public class DropDownSelect extends DropDownButton{

    @Name("Список действий")
    @FindBy(xpath = ".//div[contains(@class,'c-filters-operations-radio')]")
    private List<WebElement> itemsList;

    @FindBy(xpath = ".//div[contains(@class,'c-filters-operations-content')]")
    private HtmlElementExtended content;

    protected List<WebElement> getItemsList(){
        return this.itemsList;
    }

    protected HtmlElementExtended getContent(){
        return this.content;
    }

    @Override
    protected boolean isOpened(){
        return this.getItemsList().size() > 0;
    }

    public void selectByValue(String value){
        this.open();
        Until.assertTimeout(500)
                .assertThat(getContent(), ElementMatchers.isDisplayed());
        try {
            getItemsList().stream()
                    .filter(e -> value.equals(e.getText()))
                    .findFirst()
                    .get()
                    .click();
        }catch (NoSuchElementException e){
            throw new NoSuchElementException(String.format("Can't get item by value [%s]\n%s", value, e.getMessage()));
        }
    }
}
