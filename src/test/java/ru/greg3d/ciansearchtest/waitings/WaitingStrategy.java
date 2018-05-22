package ru.greg3d.ciansearchtest.waitings;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriverException;
import java.util.function.Supplier;

/**
 * Created by sbt-konovalov-gv on 29.03.2018.
 */
public class WaitingStrategy {

    /**
     * То же самое, что и systemIsReady, только пока ни на что не реагирует;
     * @param driver - экземпляр текущего WebDriver-а
     * @param f - метод, который нужно выполнить
     * @param errorPrefix - префикс текста ошибки, в случае, если метод не будет выполнен
     * @param <T> - тип возвращаемого значения метода f
     * @return - возвращаемое значение метода f
     */
    public static <T> T isActive(WebDriver driver, Supplier<T> f, String errorPrefix){
        String errorMessage = "";
        for(int i = 0; i < 2; i ++){
            try {
                return f.get();
            }catch(WebDriverException e){
                    sleep(500);
                errorMessage = e.getMessage();
            }
        }
        throw new RuntimeException(String.format("%s\n%s", errorPrefix, errorMessage));
    }

    public static <T> T isActive(WebDriver driver, Supplier<T> f){
        return isActive(driver, f, "");
    }

    private static void sleep(long millis){
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
