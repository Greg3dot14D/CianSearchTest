package ru.greg3d.ciansearchtest;

import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import ru.greg3d.ciansearchtest.helpers.CianSearchTestHelper;
import ru.greg3d.ciansearchtest.helpers.IndexHolder;
import ru.greg3d.ciansearchtest.helpers.XlsxHelper;
import ru.greg3d.ciansearchtest.model.FilterModel;
import ru.greg3d.ciansearchtest.model.ResultModel;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class CianSearchTest {
    private CianSearchTestHelper testHelper;

    @BeforeClass
    public void setUp(){
        System.setProperty("webdriver.gecko.driver","../driver/geckodriver");
        System.setProperty("filters-file","./filters.xlsx");
        testHelper = new CianSearchTestHelper();
    }

    private List<FilterModel> getFilterList() throws IOException {
        XlsxHelper xlsReader = XlsxHelper.getInstance(new File(System.getProperty("filters-file")));

        List<FilterModel> resultList = new ArrayList<>();
        while (xlsReader.nextRow()) {
            resultList.add(xlsReader.fillModel(FilterModel.class));
        }
        return resultList;
    }

    @DataProvider
    public Object[][] getFilterListDataProvider() throws IOException {
        IndexHolder i = new IndexHolder();
        return this.getFilterList().stream()
                .map(e-> new Object[]{i.incrementIndex().getIndex(), e})
                .toArray(Object[][]::new);
    }

    @Test(dataProvider = "getFilterListDataProvider")
    public void searctTest(int index, FilterModel filter) throws IOException {
        XlsxHelper xlsWriter = XlsxHelper.newInstance(new File(String.format("./%s_out.xlsx", index)));

        // На первой странице выводим параметры фильтрации
        xlsWriter.addHeaders(filter.getClass());
        xlsWriter.addRow(filter);

        // На второй странице выводим результаты поиска
        xlsWriter.addSheet("Results");
        xlsWriter.addHeaders(ResultModel.class);

        for(ResultModel result : testHelper.getResultList(filter)) {
            xlsWriter.addRow(result);
        }
        xlsWriter.write();
    }
}
