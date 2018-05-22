package ru.greg3d.ui.interfaces;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.internal.WrapsElement;
import ru.greg3d.ui.abstracts.typifiedelements.cell.CellAbstract;
import ru.greg3d.ui.abstracts.typifiedelements.grid.GridAbstract;

/**
 * Created by sbt-konovalov-gv on 15.01.2018.
 */
public interface IGrid extends WrapsElement {

    IPagination getPagination();

    //HashMap<String, Integer> getHeadersMup();

    int getHeaderIndex(String headerName);

//    Cell getCellByHeaderName(String headerName);
//
//    Cell getCellByHeaderIndex(int index);

    //Cell initCell(Cell cell);

    WebElement getWrappedCellElement(CellAbstract cell);

    IGrid goToRow(int rowNum);

    IGrid selectRow(int index);

    int getRowsCount();

    int getCurrentRowNum();

    GridAbstract.Headers getHeaders();

    GridAbstract.Rows getRows();

    boolean nextRow();

    void resetRowIterator();

    @Override
    String toString();
}
