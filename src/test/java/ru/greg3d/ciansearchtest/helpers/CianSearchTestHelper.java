package ru.greg3d.ciansearchtest.helpers;

import org.hamcrest.core.StringContains;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import ru.greg3d.ciansearchtest.model.FilterModel;
import ru.greg3d.ciansearchtest.model.ResultModel;
import ru.greg3d.ciansearchtest.pages.ResultPage;
import ru.greg3d.ciansearchtest.pages.SearchPage;
import ru.greg3d.ciansearchtest.pages.typifiedelements.DropDownCheckBoxList;
import ru.greg3d.waitings.Until;
import ru.stqa.selenium.factory.WebDriverPool;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static ru.greg3d.ui.matchers.common.ElementMatchers.isExist;

public class CianSearchTestHelper {

    private WebDriver driver;

    public SearchPage searchPage;
    public ResultPage resultPage;

    public CianSearchTestHelper(){
        this.driver = WebDriverPool.DEFAULT.getDriver(new FirefoxOptions());
        searchPage = new SearchPage(this.driver);
        resultPage = new ResultPage(this.driver);
    }

    public void goToUrl(String url){
        this.driver.get(url);
    }

    public List<ResultModel> getResultList(FilterModel filter){
        this.driver.get("https://www.cian.ru/");

        Until.assertTimeout(2000).assertThat(searchPage.filterWidget, isExist());

        searchPage.filterWidget.operations.selectByValue(filter.action);
        searchPage.filterWidget.type.selectByValue(filter.type);

//        if(!"".equals(Optional.ofNullable(filter.category).orElse("")))
//            searchPage.filterWidget.category.selectByValue(filter.category);

        if(!"".equals(Optional.ofNullable(filter.roomsCount).orElse("")))
            selectByRoomsCount(searchPage.filterWidget.roomsCount, filter.roomsCount);
        searchPage.filterWidget.priceMin.setText(String.valueOf(filter.priceMin));
        searchPage.filterWidget.priceMax.setText(String.valueOf(filter.priceMax));
        searchPage.filterWidget.region.setText(filter.region);
        searchPage.search_Button.click();

        return resultPage.resultCellList.stream()
                .map(cell->{
                    ResultModel resultModel = new ResultModel();
                    resultModel.title = cell.title.getText();
                    resultModel.price = cell.price.getText();
                    resultModel.location = cell.location.getText();
                    resultModel.owner = cell.owner.getText();
                    resultModel.description = cell.description.getText();
                    return resultModel;})
                .collect(Collectors.toList());
    }

    private void selectByRoomsCount(DropDownCheckBoxList roomsCount, String searchText){

        for(int i=1; i < 7; i ++) {
            String matcher = String.format("%s-к", i);
            if (searchText.contains(matcher))
                roomsCount.checkByValue(StringContains.containsString(matcher));
            else
                roomsCount.unCheckByValue(StringContains.containsString(matcher));
        }
        if(searchText.toLowerCase().contains("студ"))
            roomsCount.checkByValue("Студия");
        else
            roomsCount.unCheckByValue("Студия");

        if(searchText.toLowerCase().contains("своб"))
            roomsCount.checkByValue("Свободная планировка");
        else
            roomsCount.unCheckByValue("Свободная планировка");
    }
}
