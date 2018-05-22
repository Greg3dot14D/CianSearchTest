package ru.greg3d.ciansearchtest.pages.typifiedelements;

import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.HasInputDevices;
import org.openqa.selenium.interactions.Keyboard;
import org.openqa.selenium.support.FindBy;
import ru.greg3d.ui.htmlelements.elements.HtmlElementDriverable;
import ru.greg3d.ui.interfaces.IText;
import ru.greg3d.ui.interfaces.ITextEdit;

import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.ClipboardOwner;
import java.awt.datatransfer.StringSelection;
import java.util.NoSuchElementException;
import java.util.Optional;

public class TextInput extends HtmlElementDriverable implements IText, ITextEdit {

    @FindBy(css = "input")
    private WebElement input;

    protected WebElement getInput(){
        return  this.input;
    }

    /**
     * Возвращает текст из поля
     * @return - текст из input
     */
    @Override
    public String getText(){
        try {
            return Optional.of(this.getInput().getAttribute("value")).orElse("");
        }catch(NullPointerException | NoSuchElementException e){
            return this.getInput().getText();
        }
    }

    /**
     * Метод просаживает текст в TextInput
     * @param text текст для заполнения
     */
    @Override
    public void setText(String text) {
        //this.getInput().click();
        this.getInput().clear();
        this.getInput().sendKeys(text);
        //this.copyPaste(text);
    }

    /**
     * Метод по очистке текстового поля
     */
    public void clear(){
        this.getInput().clear();
    }

    /**
     * Выполняем копирование текста в поле из буфера обмена
     * @param text - текст для ввода
     */
    private void copyPaste(String text){
        setClipboardContents(text, null);
        Keyboard kb = ((HasInputDevices)this.driver).getKeyboard();
        //kb.pressKey(Keys.chord(Keys.CONTROL,"v"));
        kb.sendKeys(Keys.chord(Keys.SHIFT, Keys.INSERT));
    }

    // это нужно для реализации CopyPaste-а
    private static void setClipboardContents(String text2Copy, ClipboardOwner owner) {
        StringSelection stringSelection = new StringSelection(text2Copy);
        Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
        clipboard.setContents(stringSelection, owner);
    }
}
