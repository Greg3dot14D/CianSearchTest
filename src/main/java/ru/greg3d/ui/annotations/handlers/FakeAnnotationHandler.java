package ru.greg3d.ui.annotations.handlers;

import org.openqa.selenium.By;
import org.openqa.selenium.support.pagefactory.AbstractAnnotations;

/**
 * Created by SBT-Konovalov-GV on 08.08.2017.
 */
public class FakeAnnotationHandler extends AbstractAnnotations {
    @Override
    public By buildBy() {
        return By.xpath(".");
    }

    @Override
    public boolean isLookupCached() {
        return false;
    }
}
