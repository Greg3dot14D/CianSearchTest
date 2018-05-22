package ru.greg3d.ui.abstracts.locators;

import org.openqa.selenium.By;
import org.openqa.selenium.SearchContext;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.pagefactory.ElementLocator;

import java.util.List;

public class ByElementLocator implements ElementLocator {

    private final SearchContext searchContext;
    private final By by;

    public ByElementLocator(SearchContext searchContext, By by) {
        this.searchContext = searchContext;
        this.by = by;
    }

    public WebElement findElement() {
        return this.searchContext.findElement(this.by);
    }

    public List<WebElement> findElements() {
        return  this.searchContext.findElements(this.by);
    }

    public String toString() {
        return this.getClass().getSimpleName() + " '" + this.by + "'";
    }
}