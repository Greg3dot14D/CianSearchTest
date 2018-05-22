package ru.greg3d.ui.abstracts.typifiedelements.cell;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import ru.greg3d.ui.interfaces.ICheck;

/**
 * Created by sbt-konovalov-gv on 16.03.2018.
 */
public abstract class CheckBoxCellAbstract extends CellAbstract implements ICheck {

    // TODO - так НЕ прокатит, нужно создавать локатор для Ячейки
    //@FindBy(xpath = ".//div[contains(@class,'checkbox__')]")
    //private WebElement checkBoxElement;

    //@Override
    protected WebElement getCheckBoxElelemnt(){
        return this.getWrappedElement().findElement(By.xpath(".//div[contains(@class,'checkbox__')]"));
        // TODO - так НЕ прокатит, нужно создавать локатор для Ячейки
        //return this.checkBoxElement;
    }

    @Override
    public boolean isChecked(){
        return this.getCheckBoxElelemnt().getAttribute("class").contains("rowSelected");
    }

    @Override
    public boolean unChecked(){
        return !this.isChecked();
    }



    public void check(){
        if(!isChecked())
            this.getCheckBoxElelemnt().click();
    }

    public void unCheck(){
        if(isChecked())
            this.getCheckBoxElelemnt().click();
    }

    @Override
    public String getText() {
        return this.getWrappedElement().findElement(By.xpath(".//div")).getText();
    }
}