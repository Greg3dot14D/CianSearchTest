package ru.greg3d.ciansearchtest.waitings;

import org.openqa.selenium.WebDriver;
import ru.greg3d.ui.waitings.commands.WrapsWaitingCommandAbstract;

import java.util.function.Supplier;

public class IsActiveCommand extends WrapsWaitingCommandAbstract {

	public IsActiveCommand(WebDriver driver){
		super(driver);
	}

	@Override
    public <T> T supply(Supplier<T> s){
    	return WaitingStrategy.isActive(driver, s);
    }
}
