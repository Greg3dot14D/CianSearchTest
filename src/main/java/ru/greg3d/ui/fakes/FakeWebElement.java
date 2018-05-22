package ru.greg3d.ui.fakes;

import org.openqa.selenium.*;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by sbt-konovalov-gv on 20.03.2018.
 */
public class FakeWebElement implements WebElement{

    private WebElement fakeElement;

    public FakeWebElement setFakeWebElement(WebElement element){
        this.fakeElement = element;
        return this;
    }

    private WebElement getFakeElement(){
        if(this.fakeElement == null)
            this.fakeElement = new FakeWebElement();
        return this.fakeElement;
    }

    @Override
    public void click() {

    }

    @Override
    public void submit() {

    }

    @Override
    public void sendKeys(CharSequence... charSequences) {

    }

    @Override
    public void clear() {

    }

    @Override
    public String getTagName() {
        return "der Tag";
    }

    @Override
    public String getAttribute(String s) {
        return "Der attribute";
    }

    @Override
    public boolean isSelected() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    @Override
    public String getText() {
        return "DerText";
    }

    @Override
    public List<WebElement> findElements(By by) {
        List<WebElement> list = new ArrayList<>();
        list.add(this.getFakeElement());
        return list;
    }

    @Override
    public WebElement findElement(By by) {
        return this.getFakeElement();
    }

    @Override
    public boolean isDisplayed() {
        return true;
    }

    @Override
    public Point getLocation() {
        return null;
    }

    @Override
    public Dimension getSize() {
        return null;
    }

    @Override
    public Rectangle getRect() {
        return null;
    }

    @Override
    public String getCssValue(String s) {
        return null;
    }

    @Override
    public <X> X getScreenshotAs(OutputType<X> outputType) throws WebDriverException {
        return null;
    }
}
