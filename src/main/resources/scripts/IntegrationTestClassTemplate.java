#if (${PACKAGE_NAME} && ${PACKAGE_NAME} != "")package ${PACKAGE_NAME};#end

import com.ibm.mq.MQException;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import ru.greg3d.helpers.xml.XmlUnit;
import ru.sbt.qa.MQManager;
import javax.xml.xpath.XPathExpressionException;

#parse("File Header.java")
public class ${NAME}{
    protected SimpleLog log = new SimpleLog("${NAME}");
    private ResourcesHelper resourcesHelper = new ResourcesHelper();
    private String xmlFile = "";
    private MqManagerHelper mqHelper;
    
    @Test
    // 
    public void allTestsRunner() throws MQException, XPathExpressionException {
        log.setLevel(2);
        this.init();
    }

    private void init() {

        // TODO, ���������� ���� � ����� ��������
        resourcesHelper.setResourcesSubFolder("resources/_");
        // TODO, ���������� ���� � ����������� ����������
        mqHelper = new MqManagerHelper(resourcesHelper.getPathToFile("_.integration.properties"));
        // TODO, ���������� ���� � ������� �������� xml
        xmlFile = resourcesHelper.getPathToFile("xml/_");
    }


    // ��������� ��� ��������������� �������� ������, ������� ����� ����� ��������� � ��������� �������
    private TestData testData = new TestData();
    private class TestData {
        // TODO �������� �������� ������, ���������� �� ����� �����
    }

    // ������ ������ � ��������������� �����������
    @Test(dataProvider = "some_DataProvider")
    public void test1_CRM_56005_step3_FieldContainsIncorrectSimbol(String fieldName, String xpath, String fieldValue) throws MQException, XPathExpressionException {
        //log.info("������� ���� '" + fieldName + "'");
        mqHelper.getParameters().clear();
        mqHelper.getParameters().put(xpath, fieldValue);

        XmlUnit x = mqHelper.parametriseXml(xmlFile);
        x.replaceAttributeValue("//HeaderInfo", "RqTm", MQManager.generateRqTime());
        x.replaceAttributeValue("//HeaderInfo", "RqUID", mqHelper.getRqUID());

        String xml = x.getXml();
        log.debug("xmlSend -> " + xml);

        Assert.assertTrue(mqHelper.connect());
        mqHelper.sendMessage(xml);
        String xmlReceived = mqHelper.getMessage();
        log.debug("xmlReceived -> " + xmlReceived);

        x = new XmlUnit().serializeFromString(xmlReceived);
        Assert.assertEquals(x.getValueOfAttributeByXpath("//HeaderInfo","ErrorCode"),"1", fieldName);
        Assert.assertEquals(x.getValueOfAttributeByXpath("//HeaderInfo","ErrorText"),"����������� ��������� ������������ ����", fieldName);
    }

    // ���� ���������, ��� ��������� �������� ������
    @DataProvider
    public static Object[][] some_DataProvider() {
        return new Object[][] {
                {"�������","//Offer/LastName","a1a"},
                {"���","//Offer/FirstName","b1b"},
                {"��������","//Offer/MiddleName","b.b"}
        };
    }
}
