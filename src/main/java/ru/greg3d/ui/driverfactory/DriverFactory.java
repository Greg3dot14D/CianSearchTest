package ru.greg3d.ui.driverfactory;

import org.openqa.selenium.UnexpectedAlertBehaviour;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;

/**
 * Created by Greg3D on 06.12.2016.
 */
public class DriverFactory {

    /**
     * Создаем вебдрайвер без указания файла с пропертями (просаживаются проперти по-умолчанию)
     * @return - экземпляр созданного вебдрайвера
     */
    public static WebDriver getDriver(String pathToDriver){
        DesiredCapabilities capabilities = DesiredCapabilities.firefox();
        /* 	Под Linux Firefox не умеет корректно обрабатывать нативные события
        cap.setCapability(CapabilityType.HAS_NATIVE_EVENTS, false);
            Под Виндой - должно быть норм
        */
        capabilities.setCapability(CapabilityType.UNEXPECTED_ALERT_BEHAVIOUR, UnexpectedAlertBehaviour.IGNORE);
        capabilities.setCapability(CapabilityType.HAS_NATIVE_EVENTS, true);
        return new FirefoxDriver(capabilities);
    }
}
