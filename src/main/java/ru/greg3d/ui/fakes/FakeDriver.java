package ru.greg3d.ui.fakes;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.HasInputDevices;
import org.openqa.selenium.interactions.Keyboard;
import org.openqa.selenium.interactions.Mouse;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

/**
 * Created by greg3d on 08.05.17.
 */
public class FakeDriver implements WebDriver, JavascriptExecutor, HasInputDevices {

    public static WebDriver newInstance(){
        return new FakeDriver()
                .setFakeWebElement(new FakeWebElement());
    }

    private WebElement fakeElement;

    public FakeDriver setFakeWebElement(WebElement element){
        this.fakeElement = element;
        return this;
    }

    private WebElement getFakeElement(){
        return this.fakeElement;
    }

    @Override
    public void get(String s) {

    }

    @Override
    public String getCurrentUrl() {
        return null;
    }

    @Override
    public String getTitle() {
        return null;
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
    public String getPageSource() {
        return null;
    }

    @Override
    public void close() {

    }

    @Override
    public void quit() {

    }

    @Override
    public Set<String> getWindowHandles() {
        return null;
    }

    @Override
    public String getWindowHandle() {
        return null;
    }

    @Override
    public TargetLocator switchTo() {
        return null;
    }

    @Override
    public Navigation navigate() {
        return null;
    }

    @Override
    public Options manage() {
        return null;
    }

    @Override
    public Object executeScript(String s, Object... objects) {
        switch(s){
            case "return SiebelApp.S_App.uiStatus.IsBusy();" :
                System.out.println("IsBusy done");
                return false;
            case "return ($.active == 0);" :
                System.out.println("active = 0 done");
                return true;
        }
        if(objects.length == 0)
            System.out.println(String.format("executed %s", s));
        else {
            StringBuilder sb = new StringBuilder("");
            Arrays.stream(objects).forEach(a-> sb.append("[").append(a.toString()).append("]"));
            System.out.println(String.format("executed %s %s", s, sb.toString()));
            //System.out.println(String.format(s, objects));
        }
        return true;
    }

    @Override
    public Object executeAsyncScript(String s, Object... objects) {
        return null;
    }

    @Override
    public Keyboard getKeyboard() {
        return null;
    }

    @Override
    public Mouse getMouse() {
        return null;
    }
}
