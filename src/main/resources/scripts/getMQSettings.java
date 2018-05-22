package modtests.tests.callcenter.ERKC.flood;

import modtests.constants.QueryNames;
import modtests.tests.Base.BaseTest;
import org.testng.annotations.Test;
import ru.sbt.crmretail.tools.MqTools;


public class getSettings extends BaseTest {

    String path = "C:\\HermesJMS\\bindings\\ldtest\\.bindings";  //Путь до биндингов
    String query = "ESBMS.CRMR";  // QueryNames.ESB_CRM_RET;  // Название очереди

    // Получить настройки очередей
    @Test
    public void getMQ(){
        String settings = MqTools.getMqSettings(path, query);
        System.out.println(settings);
    }
}

