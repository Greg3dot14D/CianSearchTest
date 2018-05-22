#if (${PACKAGE_NAME} && ${PACKAGE_NAME} != "")package ${PACKAGE_NAME};#end

import org.openqa.selenium.WebDriver;
import pagesandblocks.Page;
import ru.greg3d.siebel.factory.annotations.AppletCorrector;
import ru.greg3d.siebel.factory.annotations.Block;
import ru.greg3d.siebel.siebelelement.*;
import ru.greg3d.siebel.tools.WaitManager;

#parse("File Header.java")
public class ${NAME} extends Page{

    /////////////////////////////// ������ �� ������� �������� ////////////////////////////
    @Block
    public Views views;
    public static class Views{

    }

    /////////////////////////////// ������ � ���������� �������� ////////////////////////////
    @Block
    public Tabs tabs;
    public static class Tabs{

    }

    // ����������� ��������
    public ${NAME}(WebDriver driver){
        super(driver);
    }
}