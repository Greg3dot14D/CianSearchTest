package ru.greg3d.ciansearchtest.pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.FindBy;
import ru.greg3d.ciansearchtest.pages.typifiedelements.*;
import ru.greg3d.ui.htmlelements.elements.HtmlElementExtended;
import ru.yandex.qatools.htmlelements.annotations.Name;
import ru.yandex.qatools.htmlelements.element.HtmlElement;

public class SearchPage extends BasePage{

    @Name("Группа фильтров")
    @FindBy(xpath = ".//div[contains(@class,'c-filters-form')][1]")
    public FilterWidget filterWidget;

    public static class FilterWidget extends HtmlElementExtended {

        @Name("Действие")
        @FindBy(xpath = ".//div[contains(@class,'c-filters-field-operations')]")
        public DropDownSelect operations;

        @Name("Тип недвижимости")
        @FindBy(xpath = ".//div[contains(@class,'c-filters-field-property')][1]")
        public DropDownItems type;

        @Name("Категория")
        @FindBy(xpath = ".//div[contains(@class,'c-filters-field-property')][2]")
        public DropDownItems category;

        @Name("Число комнат")
        @FindBy(xpath = ".//div[contains(@class,'c-filters-field-room')]")
        public DropDownCheckBoxList roomsCount;

        @Name("Минимальная цена")
        @FindBy(xpath = "(.//div[contains(@class,'undefined')]//input)[1]/..")
        public TextInput priceMin;

        @Name("Максимальная цена")
        @FindBy(xpath = "(.//div[contains(@class,'undefined')]//input)[2]/..")
        public TextInput priceMax;

        @Name("Регион")
        @FindBy(xpath = ".//div[contains(@class,'c-filters-field-region')]")
        public DropDownTextInput region;
    }

    @Name("Найти")
    @FindBy(xpath = ".//button[contains(@class,'c-filters-field-button')]")
    public HtmlElement search_Button;

    public SearchPage(WebDriver driver) {
        super(driver);
    }
}
