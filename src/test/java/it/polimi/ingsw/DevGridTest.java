package it.polimi.ingsw;

import it.polimi.ingsw.exceptions.EmptyDeckException;
import it.polimi.ingsw.exceptions.IllegalParameterException;
import it.polimi.ingsw.exceptions.NegativeQuantityException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

class DevGridTest {

    DevGrid devGrid;
    DevDeck deckGreen1;

    /*
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
    */

    @Test
    void toStringTest()  {
        System.out.println(devGrid.toString());
    }

    @Test
    void IllegalGetDeckTest()  {
        assertThrows(IllegalParameterException.class, ()->devGrid.getDevDeckInTheGrid(-1,0));
        assertThrows(IllegalParameterException.class, ()->devGrid.getDevDeckInTheGrid(3,0));
        assertThrows(IllegalParameterException.class, ()->devGrid.getDevDeckInTheGrid(0,4));
        assertThrows(IllegalParameterException.class, ()->devGrid.getDevDeckInTheGrid(0,-1));
    }

    @Test
    void IllegalGetCardTest()  {
        assertThrows(IllegalParameterException.class, ()->devGrid.getDevDeckInTheGrid(-1,0));
        assertThrows(IllegalParameterException.class, ()->devGrid.getDevDeckInTheGrid(3,0));
        assertThrows(IllegalParameterException.class, ()->devGrid.getDevDeckInTheGrid(0,4));
        assertThrows(IllegalParameterException.class, ()->devGrid.getDevDeckInTheGrid(0,-1));
    }

    @Test
    void GetCardTest() throws IllegalParameterException {

        deckGreen1=devGrid.getDevDeckInTheGrid(2,0);
        int actualSize = deckGreen1.size();
        if (actualSize==0)
            assertNull(devGrid.getDevCardFromDeck(2,0));
        else
            assertEquals(devGrid.getDevCardFromDeck(2,0),deckGreen1.getFirst());
    }

    @Test
    void IllegalDrawCardTest()  {
        assertThrows(IllegalParameterException.class, ()->devGrid.drawDevCardFromDeck(-1,0));
        assertThrows(IllegalParameterException.class, ()->devGrid.drawDevCardFromDeck(3,0));
        assertThrows(IllegalParameterException.class, ()->devGrid.drawDevCardFromDeck(0,4));
        assertThrows(IllegalParameterException.class, ()->devGrid.drawDevCardFromDeck(0,-1));
    }

    @Test
    void drawCardTest() throws IllegalParameterException, EmptyDeckException {

        DevCard actualDevCard;
        int actualSize = devGrid.getDevDeckInTheGrid(2,0).size();
        while (actualSize>0){
            actualDevCard = devGrid.getDevCardFromDeck(2,0);
            assertEquals(actualDevCard,devGrid.drawDevCardFromDeck(2,0));
            assertEquals(actualSize-1,devGrid.getDevDeckInTheGrid(2,0).size());
            actualSize = devGrid.getDevDeckInTheGrid(2,0).size();
        }
        assertThrows(EmptyDeckException.class, ()->devGrid.drawDevCardFromDeck(2,0));
    }

    @BeforeEach
    void setUp() throws ParserConfigurationException, NegativeQuantityException, SAXException, IllegalParameterException, IOException {
        File xmlDevCardsConfig = new File("DevCardConfig.xsd.xml");
        devGrid =new DevGrid(xmlDevCardsConfig);
        deckGreen1=devGrid.getDevDeckInTheGrid(2,0);
        assertEquals(4,deckGreen1.size());
    }

}