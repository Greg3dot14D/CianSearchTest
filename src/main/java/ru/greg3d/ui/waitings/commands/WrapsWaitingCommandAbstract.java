package ru.greg3d.ui.waitings.commands;

import org.openqa.selenium.WebDriver;
import ru.greg3d.ui.waitings.interfaces.ISyncSystemCommand;

import java.util.function.Supplier;

/**
 * Created by sbt-konovalov-gv on 29.03.2018.
 */
public abstract class WrapsWaitingCommandAbstract implements ISyncSystemCommand{

    protected WebDriver driver;

    public WrapsWaitingCommandAbstract(WebDriver driver){
        this.driver = driver;
    }

    @Override
    public abstract <T> T supply(Supplier<T> s);
}
