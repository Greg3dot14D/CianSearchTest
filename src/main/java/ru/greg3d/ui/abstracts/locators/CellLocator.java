package ru.greg3d.ui.abstracts.locators;

import ru.greg3d.ui.abstracts.typifiedelements.cell.CellAbstract;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.pagefactory.DefaultElementLocator;
import ru.greg3d.ui.annotations.handlers.FakeAnnotationHandler;
import ru.greg3d.ui.interfaces.IGrid;

/**
 * Created by SBT-Konovalov-GV on 08.08.2017.
 */
public class CellLocator <G extends IGrid> extends DefaultElementLocator {

    private G grid;
    private CellAbstract cell;

    public CellLocator(G grid, CellAbstract cell) {
        super(null, new FakeAnnotationHandler());
        this.grid = grid;
        this.cell = cell;
    }

    private WebElement getWrappedElement(){
        return grid.getWrappedCellElement(this.cell);
    }

    public WebElement findElement() {
        return getWrappedElement();
    }
}
