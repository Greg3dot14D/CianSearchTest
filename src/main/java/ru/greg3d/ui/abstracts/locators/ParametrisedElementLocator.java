package ru.greg3d.ui.abstracts.locators;

import org.openqa.selenium.SearchContext;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.pagefactory.AbstractAnnotations;
import org.openqa.selenium.support.pagefactory.DefaultElementLocator;

import java.util.List;

/**
 * Created by sbt-konovalov-gv on 29.01.2018.
 */
public class ParametrisedElementLocator extends DefaultElementLocator {

    public ParametrisedElementLocator(SearchContext searchContext, AbstractAnnotations annotations) {
        super(searchContext, annotations);
    }

    // Ожидаем появления WebElement-а
    public WebElement findElement() { return super.findElement(); }

    // Ожидаем появления WebElement-ов
    public List<WebElement> findElements() {
        return super.findElements();
    }
}
