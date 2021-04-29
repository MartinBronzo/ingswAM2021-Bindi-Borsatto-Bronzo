package it.polimi.ingsw.model.soloGame;

import it.polimi.ingsw.model.DevCards.DevCardColour;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class DiscardTokenTest {

    @Test
    public void init1() {
        DiscardToken discardToken = new DiscardToken(DevCardColour.BLUE, 2);
        Assertions.assertEquals(discardToken.getCardColour(), DevCardColour.BLUE);
        assertEquals(discardToken.getNumCards(), 2);
    }

    @Test
    public void init2() {
        DiscardToken discardToken = new DiscardToken(DevCardColour.GREEN, 0);
        Assertions.assertEquals(discardToken.getCardColour(), DevCardColour.GREEN);
        assertEquals(discardToken.getNumCards(), 0);
    }

    @Test
    public void initNegativeNumCard() {
        Exception exception;
        exception = assertThrows(IllegalArgumentException.class, () -> new DiscardToken(DevCardColour.GREEN, -1));
        assertEquals(exception.getMessage(), "Can't discard negative cards");
    }

    //TODO: da scommentare una volta che ci sono tutte le devCard nell'XML
   /*@Test
    public void attachObserver() throws IOException, SAXException, ParserConfigurationException, NegativeQuantityException, EmptyDevColumnException {
       DevGrid devGrid = new DevGrid(new File("DevCardConfig.xsd.xml"));
       FaithLevelBasic faithLevelBasic = new FaithLevelBasic(FaithTrack.instance(new File("FaithTrackConfig.xml")));
       //FaithLevelBasic faithLevelBasic = new FaithLevelBasic();
       SoloActionDeck soloActionDeck = new SoloActionDeck(new File("SoloTokenConfig.xml"));
       SoloBoard soloBoard = new SoloBoard(devGrid, faithLevelBasic,  soloActionDeck);

       DiscardTokenObserver observer = new DiscardTokenObserver(soloBoard);

        DiscardToken discardToken = new DiscardToken(DevCardColour.PURPLE, 1);

        assertTrue(discardToken.attach(observer));
        assertTrue(discardToken.playEffect());
        assertTrue(discardToken.detach(observer));
    }*/
}