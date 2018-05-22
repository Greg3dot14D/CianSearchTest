package ru.greg3d.ui.abstracts.typifiedelements.grid;

import ru.greg3d.ui.interfaces.IPagination;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import ru.greg3d.ui.abstracts.typifiedelements.cell.CellAbstract;
import ru.greg3d.ui.interfaces.IGrid;
import ru.greg3d.ui.htmlelements.elements.HtmlElementDriverable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by sbt-konovalov-gv on 15.01.2018.
 */
public abstract class GridAbstract extends HtmlElementDriverable implements IGrid {
    private int currentRowNum = -1;

    private HashMap<String, Integer> headersMup;

    private Headers headers;

    protected abstract List<WebElement> getHeadersList();

    protected abstract List<WebElement> getRowsList();

    private Rows rows;

    public void resetRowIterator() {
        this.currentRowNum = -1;
    }

    public boolean nextRow() {
        if ((currentRowNum + 1) < getRowsCount()) {
            currentRowNum++;
            return true;
        }
        return false;
    }

    /*
    ** Контейнер по работе с пеерлистыванием страниц
    */

    @Override
    public abstract IPagination getPagination();
//
//    public static abstract class Pagination extends HtmlElementExtended implements IPagination {
////        private GridAbstract grid;
////
////        protected Pagination setGrid(GridAbstract grid){
////            this.grid = grid;
////            return this;
////        }
//
//        protected abstract HtmlElement getNextPage_ArrowElement();
//
//        protected abstract HtmlElement getPrevPage_ArrowElement();
//
//        protected abstract HtmlElement getActivePageNumberElement();
//
//        protected abstract List<HtmlElement> getNotActivePageNumbersElements();
//
//        protected abstract List<HtmlElement> getAllPageNumbersElements();
//
//        @Override
//        public void clickNumberActivePage() {
//            getActivePageNumberElement().click();
//        }
//
//        @Override
//        public boolean haveNext() {
//            return getActivePageNumber() < getMaxPageNumber();
//        }
//
//        @Override
//        public boolean havePrev() {
//            return getActivePageNumber() > 1;
//        }
//
//        private int maxPageNumber = 0;
//
//        @Override
//        public int getMaxPageNumber() {
//            if (maxPageNumber > 0)
//                return maxPageNumber;
//            int size = getAllPageNumbersElements().size();
//            if (size == 0)
//                return 1;
//            return Integer.parseInt(getAllPageNumbersElements().get(size - 1).getText());
//        }
//
//        @Override
//        public IPagination nextPage() {
//            this.getNextPage_ArrowElement().click();
////            this.grid.goToRecord(0);
//            return this;
//        }
//
//        @Override
//        public IPagination prevPage() {
//            this.getPrevPage_ArrowElement().click();
////            this.grid.goToRecord(0);
//            return this;
//        }
//
//        @Override
//        public IPagination toFirstPage() {
//            getAllPageNumbersElements().get(0).click();
//            return this;
//        }
//
//        @Override
//        public IPagination toLastPage() {
//            getAllPageNumbersElements().get(getAllPageNumbersElements().size() - 3).click();
//            return this;
//        }
//
//        @Override
//        public int getActivePageNumber() {
//            return Integer.parseInt(this.getActivePageNumberElement().getText());
//        }
//
//        @Override
//        public IPagination setNumberPage(String number) {
////            GridAbstract grid = new GridAbstract();
////            grid.getWrappedElement().findElement(By.xpath("")).
//            return this;
//        }
//    }

    //@Override
    protected HashMap<String, Integer> getHeadersMup() {
        if (headersMup != null)
            return headersMup;
        headersMup = new HashMap<String, Integer>();
        int index = 0;
        for (WebElement e : getHeadersList()) {
            headersMup.put(e.getText(), ++index);
        }
        return headersMup;
    }

    @Override
    public int getHeaderIndex(String headerName) {
        return getHeadersMup().get(headerName);
    }

    @Override
    public WebElement getWrappedCellElement(CellAbstract cell) {
        return this.getRowsList().get(this.currentRowNum).findElement(By.xpath(String.format("./td[%s]", getHeadersMup().get(cell.getName()))));
    }

    @Override
    public IGrid goToRow(int rowNum) {
        this.currentRowNum = rowNum;
        return this;
    }

    @Override
    public IGrid selectRow(int index) {
        this.getRowsList().get(index).click();
        return goToRow(index);
    }

    @Override
    public int getRowsCount() {
        return this.getRowsList().size();
    }

    @Override
    public int getCurrentRowNum() {
        return this.currentRowNum;
    }

    public static class Headers {

        private List<WebElement> headers;

        public Headers(List<WebElement> headers) {
            this.headers = headers;
        }

        public List<String> getStringList() {
            return this.headers.stream().map(e -> e.getText()).collect(Collectors.toList());
        }

        public List<WebElement> getList() {
            return this.headers;
        }
    }

    public static class Rows {

        private List<HtmlElementDriverable> rows;

        public Rows(List<WebElement> rows, WebDriver driver) {
            this.rows = new ArrayList<>();

            for(int i=0; i < rows.size(); i ++) {
                HtmlElementDriverable newElement = new HtmlElementDriverable();
                newElement.setWrappedElement(rows.get(i));
                newElement.setName(String.format("[%s] - row", i));
                newElement.setDriver(driver);
                this.rows.add(newElement);
            }
        }

        public List<String> getStringList() {
            return this.rows.stream().map(e -> e.getText()).collect(Collectors.toList());
        }

        public List<HtmlElementDriverable> getList() {
            return this.rows;
        }
    }

    @Override
    public Headers getHeaders() {
        if (this.headers == null)
            this.headers = new Headers(this.getHeadersList());
        return this.headers;
    }

    @Override
    public Rows getRows() {
        if (this.rows == null)
            this.rows = new Rows(this.getRowsList(), this.driver);
        return this.rows;
    }


    // TODO - частное решение
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        this.getHeadersList().stream().forEach(e -> sb.append(e.getText()).append("\t;"));

        this.getRowsList().stream().forEach(e ->
                {
                    sb.append("\n");
                    e.findElements(By.tagName("td"))
                            .stream().forEach(a -> sb.append(a.findElement(By
                            //.cssSelector(".table__cell-content")
                            .xpath(".//div[contains(@class,'table__cell-content')]")
                    ).getText())
                            .append("\t;"));
                }
        );
        sb.append("\n");
        return sb.toString();
    }
}
