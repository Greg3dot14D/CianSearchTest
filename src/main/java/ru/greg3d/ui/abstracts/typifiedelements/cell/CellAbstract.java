package ru.greg3d.ui.abstracts.typifiedelements.cell;

import org.hamcrest.Matcher;
import org.hamcrest.core.Is;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import ru.greg3d.ui.abstracts.locators.CellLocator;
import ru.greg3d.ui.interfaces.*;
import ru.greg3d.constants.DateFormats;
import ru.greg3d.ui.htmlelements.elements.HtmlElementDriverable;
import ru.yandex.qatools.htmlelements.loader.decorator.ProxyFactory;
import ru.yandex.qatools.htmlelements.loader.decorator.proxyhandlers.WebElementNamedProxyHandler;

import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

/**
 * Created by sbt-konovalov-gv on 16.03.2018.
 */
@FindBy(xpath=".")
public abstract class CellAbstract extends HtmlElementDriverable implements IText, IDateFormated, ILocaled, IGridable {

    private IGrid grid;
    private int index = -1;
    private String dateFormat = DateFormats.DEFAULT;
    private Locale locale = Locale.ENGLISH;

    //private Column column;

    public IGrid getGrid(){
        return this.grid;
    }

    public static class Column{

        private CellAbstract cell;
        private Column(CellAbstract cell){
            this.cell = cell;
        }

        public List<String> toListOfStrings(){
            return toListOfWebElements().stream().map(e->e.getText()).collect(Collectors.toList());
        }

//        public List<Boolean> toListOfBoolean(){
//            // Это под чекбокс к стрингу
//            // .findElement(By.xpath(".//div[contains(@class,'checkbox__')]")
//            return toListOfWebElements().stream().map(e->e.findElement(By.xpath(".//div[contains(@class,'checkbox__')]")).getAttribute("class").contains("checked")).collect(Collectors.toList());
//        }

        public <T extends CellAbstract> List<T> toList(Class<T> clazz ){
            return toListOfWebElements().stream().map(e->
                    {
                        T c = null;
                        try {
                            c = clazz.newInstance();
                        } catch (InstantiationException | IllegalAccessException e1) {
                            e1.printStackTrace();
                        }
                        c.setWrappedElement(e);
                        c.setName(cell.getName());
                        return c;}

            ).collect(Collectors.toList());
        }

        private List<WebElement> toListOfWebElements(){
            return
                    cell.getGridElement()
                            .findElements(By
                                    .xpath(String.format(".//tr[contains(@class,'table__row')]/td[not(contains(@class,'table__cell_header'))][%s]", cell.getIndex())));
        }

        public boolean goToRecord(String textToSearch) {
            return goToRecord(Is.is(textToSearch));
        }

        public boolean goToRecord(Matcher<String> matcher) {
            List<?> list = toList(cell.getClass());
            for(int i=0; i < list.size(); i ++){
                if(matcher.matches(((IText)list.get(i)).getText())) {
                    this.cell.grid.goToRow(i);
                    return true;
                }
            }
            return false;
        }

//        public boolean goToRecord(Matcher<String> matcher) {
//            List<String> list = toListOfStrings();
//            for(int i=0; i < list.size(); i ++){
//                if(matcher.matches(list.get(i))) {
//                    this.cell.grid.goToRow(i);
//                    return true;
//                }
//            }
//            return false;
//        }
    }

    public Column getColumn(){
        return new Column(this);
    }

    @Override
    public void setGrid(IGrid grid){
        this.grid = grid;
        this.setWrappedElement(ProxyFactory.createWebElementProxy(this.getClass().getClassLoader(), new WebElementNamedProxyHandler(new CellLocator(grid, this), this.getName())));
    }

    public abstract String getText();

    private int getIndex(){
        if(index < 0)
            index = grid.getHeaderIndex(this.getName());
        return index;
    }

    private WebElement getGridElement(){
        return this.grid.getWrappedElement();
    }


    @Override
    public void setDateFormat(String dateFormat) {
        this.dateFormat = dateFormat;
    }

    @Override
    public String getDateFormat() {
        return this.dateFormat;
    }

    @Override
    public Locale getLocale() {
        return this.locale;
    }

    @Override
    public void setLocale(Locale locale) {
        this.locale = locale;
    }
}

