package ru.greg3d.ciansearchtest.pages.typifiedelements;

import org.hamcrest.Matcher;
import org.hamcrest.core.StringContains;
import org.openqa.selenium.support.FindBy;
import ru.greg3d.ui.htmlelements.elements.HtmlElementExtended;
import ru.greg3d.ui.matchers.common.ElementMatchers;
import ru.greg3d.waitings.Until;
import ru.yandex.qatools.htmlelements.annotations.Name;

import java.util.List;
import java.util.NoSuchElementException;

import static org.hamcrest.core.Is.is;

public class DropDownCheckBoxList extends DropDownButton{

    @Name("Список действий")
    @FindBy(xpath = ".//div[contains(@class,'c-filters-operations-content-check')]//div[contains(@class,'c-filters-operations-radio-')]")
    private List<CheckBox> itemsList;

    @FindBy(xpath = ".//div[contains(@class,'c-filters-operations-content-check')]")
    private HtmlElementExtended content;

    protected List<CheckBox> getItemsList(){
        return this.itemsList;
    }

    protected HtmlElementExtended getContent(){
        return this.content;
    }

    @Override
    protected boolean isOpened(){
        return getItemsList().size() > 0;
    }

    public CheckBox getItemByValue(String value){
        this.open();
        Until.assertTimeout(500)
                .assertThat(getContent(), ElementMatchers.isDisplayed());
        try {
            return getItemsList().stream()
                    .filter(e -> value.equals(e.getText()))
                    .findFirst()
                    .get();
        }catch (NoSuchElementException e){
            throw new NoSuchElementException(String.format("Can't get item by value [%s]\n%s", value, e.getMessage()));
        }
    }

    public CheckBox getItemByValue(Matcher matcher){
        this.open();

        Until.assertTimeout(500)
                .assertThat(getContent(), ElementMatchers.isDisplayed());
        try {
            return getItemsList().stream()
                    .filter(e ->
                            matcher.matches(e.getText()))
                    .findFirst()
                    .get();
        }catch (NoSuchElementException e){
            throw new NoSuchElementException(String.format("Can't get item by value [%s]\n%s", matcher.toString(), e.getMessage()));
        }
    }

    public void checkByValue(Matcher matcher){
        this.getItemByValue(matcher).check();
    }

    public void unCheckByValue(Matcher matcher){
        this.getItemByValue(matcher).unCheck();
    }

    public void checkByValue(String value){
        this.checkByValue(is(value));
    }

    public void unCheckByValue(String value){
        this.unCheckByValue(is(value));
    }
}
