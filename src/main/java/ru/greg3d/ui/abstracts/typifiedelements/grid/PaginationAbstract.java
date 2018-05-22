package ru.greg3d.ui.abstracts.typifiedelements.grid;

import ru.greg3d.ui.interfaces.IPagination;
import ru.greg3d.ui.htmlelements.elements.HtmlElementExtended;
import ru.yandex.qatools.htmlelements.element.HtmlElement;

import java.util.List;

/**
 * Created by sbt-konovalov-gv on 05.04.2018.
 */
public abstract class PaginationAbstract extends HtmlElementExtended implements IPagination {
//        private GridAbstract grid;
//
//        protected Pagination setGrid(GridAbstract grid){
//            this.grid = grid;
//            return this;
//        }

        protected abstract HtmlElement getNextPage_ArrowElement();

        protected abstract HtmlElement getPrevPage_ArrowElement();

        protected abstract HtmlElement getActivePageNumberElement();

        protected abstract List<HtmlElement> getNotActivePageNumbersElements();

        protected abstract List<HtmlElement> getAllPageNumbersElements();

        @Override
        public void clickNumberActivePage() {
            getActivePageNumberElement().click();
        }

        @Override
        public boolean haveNext() {
            return getActivePageNumber() < getMaxPageNumber();
        }

        @Override
        public boolean havePrev() {
            return getActivePageNumber() > 1;
        }

        private int maxPageNumber = 0;

        @Override
        public int getMaxPageNumber() {
            if (maxPageNumber > 0)
                return maxPageNumber;
            int size = getAllPageNumbersElements().size();
            if (size == 0)
                return 1;
            return Integer.parseInt(getAllPageNumbersElements().get(size - 1).getText());
        }

        @Override
        public IPagination nextPage() {
            this.getNextPage_ArrowElement().click();
            return this;
        }

        @Override
        public IPagination prevPage() {
            this.getPrevPage_ArrowElement().click();
            return this;
        }

        @Override
        public IPagination toFirstPage() {
            getAllPageNumbersElements().get(0).click();
            return this;
        }

        @Override
        public IPagination toLastPage() {
            getAllPageNumbersElements().get(getAllPageNumbersElements().size() - 1).click();
            return this;
        }

        @Override
        public int getActivePageNumber() {
            return Integer.parseInt(this.getActivePageNumberElement().getText());
        }

        @Override
        public IPagination setNumberPage(String number) {
//            GridAbstract grid = new GridAbstract();
//            grid.getWrappedElement().findElement(By.xpath("")).
            return this;
        }
    }
