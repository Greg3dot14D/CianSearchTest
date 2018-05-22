package ru.greg3d.ui.htmlelements.elements;

import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.interactions.Actions;
import ru.greg3d.ui.interfaces.IDriverable;

/**
 * Created by SBT-Konovalov-GV on 17.07.2017.
 */
public class HtmlElementDriverable extends HtmlElementExtended implements IDriverable {
    protected WebDriver driver;

    public void setDriver(WebDriver driver){
        this.driver = driver;
    }

    public WebDriver getDriver(){
        return this.driver;
    }
}
