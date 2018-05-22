package ru.greg3d.ui.interfaces;

/**
 * Created by SBT-Konovalov-GV on 13.07.2017.
 */
public interface IPagination {
    boolean haveNext();

    boolean havePrev();

    int getMaxPageNumber();

    IPagination nextPage();

    IPagination prevPage();

    IPagination toFirstPage();

    IPagination toLastPage();

    int getActivePageNumber();

    IPagination setNumberPage(String number);

    void clickNumberActivePage();
}
