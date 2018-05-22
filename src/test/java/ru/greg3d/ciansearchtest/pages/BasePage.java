package ru.greg3d.ciansearchtest.pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.PageFactory;
import ru.greg3d.ciansearchtest.waitings.IsActiveCommand;
import ru.greg3d.ui.decorators.PageDecorator;
import ru.greg3d.ui.htmlelements.decorators.HtmlDecorators;
import ru.greg3d.ui.htmlelements.decorators.ParametrisedElementDecorator;
import ru.greg3d.ui.waitings.interfaces.ISyncSystemCommand;
import ru.yandex.qatools.htmlelements.element.HtmlElement;
import ru.yandex.qatools.htmlelements.loader.decorator.HtmlElementDecorator;
import ru.yandex.qatools.htmlelements.loader.decorator.HtmlElementLocatorFactory;

public class BasePage {
    protected WebDriver driver;

    public BasePage(WebDriver driver) {
        this.driver = driver;
        this.initElements(this);
    }

    private <T> T initElements(T page) {
        PageFactory.initElements(
                new HtmlElementDecorator(
                        new HtmlElementLocatorFactory(driver)), page);

        ParametrisedElementDecorator.setFindByParametrisedToObject(page, this.driver);

        PageDecorator.setDriverToObject(page, driver);
        PageDecorator.setParentToObject(page, HtmlElement.class);
        PageDecorator.setGrid(page);
        PageDecorator.setDateFormat(page);

        ISyncSystemCommand command = new IsActiveCommand(this.driver);
        HtmlDecorators.setWaitCommandToObject(page, this.driver, command);

        return page;
    }
}
