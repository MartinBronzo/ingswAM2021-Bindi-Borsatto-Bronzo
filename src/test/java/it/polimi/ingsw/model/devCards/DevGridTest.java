package it.polimi.ingsw.model.devCards;

import it.polimi.ingsw.exceptions.EmptyDeckException;
import it.polimi.ingsw.exceptions.NegativeQuantityException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;
import java.util.Collection;

import static org.junit.jupiter.api.Assertions.*;

class DevGridTest {

    DevGrid devGrid;
    DevDeck deckGreen1;
    File xmlDevCardsConfig;

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
    public void initialization() {
        for (int i = 0; i < 3; i++)
            for (int j = 0; j < 4; j++)
                assertEquals(4, devGrid.getDevDeckInTheGrid(i, j).size());
    }

    @Test
    void toStringTest() {
        System.out.println(devGrid.toString());
    }

    @Test
    void IllegalGetDeckTest() {
        assertThrows(IllegalArgumentException.class, () -> devGrid.getDevDeckInTheGrid(-1, 0));
        assertThrows(IllegalArgumentException.class, () -> devGrid.getDevDeckInTheGrid(3, 0));
        assertThrows(IllegalArgumentException.class, () -> devGrid.getDevDeckInTheGrid(0, 4));
        assertThrows(IllegalArgumentException.class, () -> devGrid.getDevDeckInTheGrid(0, -1));
    }

    @Test
    void IllegalGetCardTest() {
        assertThrows(IllegalArgumentException.class, () -> devGrid.getDevDeckInTheGrid(-1, 0));
        assertThrows(IllegalArgumentException.class, () -> devGrid.getDevDeckInTheGrid(3, 0));
        assertThrows(IllegalArgumentException.class, () -> devGrid.getDevDeckInTheGrid(0, 4));
        assertThrows(IllegalArgumentException.class, () -> devGrid.getDevDeckInTheGrid(0, -1));
    }

    @Test
    void GetCardTest() throws IllegalArgumentException {

        deckGreen1 = devGrid.getDevDeckInTheGrid(2, 0);
        int actualSize = deckGreen1.size();
        if (actualSize == 0) {
            assertNull(devGrid.getDevCardFromDeck(2, 0));
            assertNull(devGrid.getDevCardFromDeck(1, DevCardColour.GREEN));
        } else {
            assertEquals(devGrid.getDevCardFromDeck(2, 0), deckGreen1.getFirst());
            assertEquals(devGrid.getDevCardFromDeck(1, DevCardColour.GREEN), deckGreen1.getFirst());

        }
    }

    @Test
    void IllegalDrawCardTest() {
        assertThrows(IllegalArgumentException.class, () -> devGrid.drawDevCardFromDeck(-1, 0));
        assertThrows(IllegalArgumentException.class, () -> devGrid.drawDevCardFromDeck(3, 0));
        assertThrows(IllegalArgumentException.class, () -> devGrid.drawDevCardFromDeck(0, 4));
        assertThrows(IllegalArgumentException.class, () -> devGrid.drawDevCardFromDeck(0, -1));
    }

    @Test
    void drawCardTest() throws IllegalArgumentException, EmptyDeckException {

        DevCard actualDevCard;
        int actualSize = devGrid.getDevDeckInTheGrid(2, 0).size();
        while (actualSize > 0) {
            actualDevCard = devGrid.getDevCardFromDeck(2, 0);
            assertEquals(actualDevCard, devGrid.drawDevCardFromDeck(2, 0));
            assertEquals(actualSize - 1, devGrid.getDevDeckInTheGrid(2, 0).size());
            actualSize = devGrid.getDevDeckInTheGrid(2, 0).size();
        }
        assertThrows(EmptyDeckException.class, () -> devGrid.drawDevCardFromDeck(2, 0));
    }

    @Test
    void drawCardTestByColorAndLevel() throws IllegalArgumentException, EmptyDeckException {

        DevCard actualDevCard;
        int actualSize = devGrid.getDevDeckInTheGrid(2, 0).size();
        while (actualSize > 0) {
            actualDevCard = devGrid.getDevCardFromDeck(1, DevCardColour.GREEN);
            assertEquals(actualDevCard, devGrid.drawDevCardFromDeck(1, DevCardColour.GREEN));
            assertEquals(actualSize - 1, devGrid.getDevDeckInTheGrid(2, 0).size());
            actualSize = devGrid.getDevDeckInTheGrid(2, 0).size();
        }
        assertThrows(EmptyDeckException.class, () -> devGrid.drawDevCardFromDeck(1, DevCardColour.GREEN));
    }

    @Test
    void getDrawableCardsTest() throws IllegalArgumentException {
        Collection<DevCard> devCards = devGrid.getDrawableCards();
        assertTrue(devCards.stream().allMatch(card -> card.equals(devGrid.getDevCardFromDeck(card.getLevel(), card.getColour()))));
    }


    @BeforeEach
    void setUp() throws ParserConfigurationException, NegativeQuantityException, SAXException, IllegalArgumentException, IOException {
        xmlDevCardsConfig = new File("DevCardConfig.xsd.xml");
        devGrid = new DevGrid(xmlDevCardsConfig);
        deckGreen1 = devGrid.getDevDeckInTheGrid(2, 0);
        assertEquals(4, deckGreen1.size());
    }

    /*@Test
    //This methods uses the fakeEquals method which doesn't consider the order of the cards in the deck
    public void ctrlFakeEqualsTrue() throws NegativeQuantityException, ParserConfigurationException, IOException, SAXException {
        DevGrid d2 = devGrid.createDevGridWithoutShuffling(xmlDevCardsConfig);

        assertNotSame(devGrid, d2);
        assertTrue(devGrid.equalsFake(d2));
    }*/

    @Test
    //This methods uses the fakeEquals method which doesn't consider the order of the cards in the deck
    public void ctrlFakeEqualsFalse() throws EmptyDeckException, NegativeQuantityException, ParserConfigurationException, IOException, SAXException {
        DevGrid d2 = devGrid.createDevGridWithoutShuffling(xmlDevCardsConfig);
        d2.drawDevCardFromDeck(0, 1);
        assertNotSame(devGrid, d2);
        assertFalse(devGrid.equalsFake(d2)); //Because they don't contain the same cards anymore
    }

    @Test
    public void ctrlCloning() {
        DevGrid d2 = new DevGrid(devGrid);

        assertNotSame(devGrid, d2);
        assertEquals(devGrid, d2);
    }


}