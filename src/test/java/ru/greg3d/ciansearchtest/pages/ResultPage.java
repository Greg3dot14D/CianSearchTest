package ru.greg3d.ciansearchtest.pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.FindBy;
import ru.greg3d.ciansearchtest.pages.widgets.ResultCell;
import ru.yandex.qatools.htmlelements.annotations.Name;

import java.util.List;

public class ResultPage extends BasePage{

    @Name("Список результатов поиска")
    @FindBy(xpath = ".//div[contains(@class,'-offer-container--')]")
    public List<ResultCell> resultCellList;

    public ResultPage(WebDriver driver) {
        super(driver);
    }
}
