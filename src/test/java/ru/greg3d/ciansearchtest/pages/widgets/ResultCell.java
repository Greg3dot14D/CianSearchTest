package ru.greg3d.ciansearchtest.pages.widgets;

import org.openqa.selenium.support.FindBy;
import ru.greg3d.ui.htmlelements.elements.HtmlElementExtended;
import ru.yandex.qatools.htmlelements.annotations.Name;

public class ResultCell extends HtmlElementExtended {

    // Полное описание
    //div[contains(@class,'-offer-container--')]//div[starts-with(@class,'c6e8ba5398-info--')]

    // Подробнее ...

    // 1-комн. кв., 35 м², 1/23 этаж
    //
    // Параметры объекта
    //div[contains(@class,'-offer-container--')]//div[contains(@class,'-title--')]

    @Name("Параметры объекта")
    @FindBy(xpath = ".//div[contains(@class,'-title--')]")
    public HtmlElementExtended title;

    // 2 328 750 ₽
    //
    // Стоимость
    //div[contains(@class,'-offer-container--')]//div[contains(@class,'-header-')]
    @Name("Стоимость")
    @FindBy(xpath = ".//div[contains(@class,'-header-')]")
    public HtmlElementExtended price;

    // М Улица Дыбенко ...
    //
    // Локация
    //div[contains(@class,'-offer-container--')]//div[contains(@class,'-info-section--')][2]
    @Name("Локация")
    @FindBy(xpath = ".//div[contains(@class,'-info-section--')][2]")
    public HtmlElementExtended location;


    // Продается 1-к...
    //
    // Описание
    //div[contains(@class,'-offer-container--')]//div[contains(@class,'-info-section--')][3]
    @Name("Описание")
    @FindBy(xpath = ".//div[contains(@class,'-info-section--')][3]")
    public HtmlElementExtended description;


    // ПИК
    //
    // Владелец заявки
    //div[contains(@class,'-offer-container--')]//div[contains(@class,'-user_info--')]//div[contains(@class,'-name--')]
    @Name("Владелец заявки")
    @FindBy(xpath = ".//div[contains(@class,'-user_info--')]//div[contains(@class,'-name--')]")
    public HtmlElementExtended owner;
}
