package it.polimi.ingsw;

import org.junit.jupiter.api.Test;
import java.io.File;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class DevGridTest {

    @Test
    void createConfigurationList(){
        DevGrid devGrid = new DevGrid();
        List<DevCard> devCardList;
        File xmlDevCardsConfig = new File("DevCardConfig.xsd.xml");
        try {
            devCardList=devGrid.createConfigurationList(xmlDevCardsConfig);
            System.out.println(devCardList.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}