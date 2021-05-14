package it.polimi.ingsw.model.soloGame;

import it.polimi.ingsw.exceptions.LastVaticanReportException;
import it.polimi.ingsw.exceptions.NegativeQuantityException;
import it.polimi.ingsw.model.DevCards.DevGrid;
import it.polimi.ingsw.model.FaithTrack.FaithLevelBasic;
import it.polimi.ingsw.model.FaithTrack.FaithTrack;
import org.junit.jupiter.api.Test;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

class FaithPointTokenTest {

    @Test
    public void init1() {
        FaithPointToken faithPointToken = new FaithPointToken(2);
        assertEquals(faithPointToken.getFaithPoints(), 2);
        assertFalse(faithPointToken.isShuffleToken());
    }

    @Test
    public void init2() {
        FaithPointToken faithPointToken = new FaithPointToken(0);
        assertEquals(faithPointToken.getFaithPoints(), 0);
        assertFalse(faithPointToken.isShuffleToken());
    }

    @Test
    public void initNegativePoints() {
        Exception exception;
        exception = assertThrows(IllegalArgumentException.class, () -> new FaithPointToken(-1));
        assertEquals(exception.getMessage(), "Can't have negative faith points");
    }

    @Test
    public void attachObserver() throws LastVaticanReportException, ParserConfigurationException, NegativeQuantityException, SAXException, IOException {
        /*DevGrid devGrid = new DevGrid(new File("DevCardConfig.xsd.xml"));
        FaithLevelBasic faithLevelBasic = new FaithLevelBasic(FaithTrack.instance(new File("FaithTrackConfig.xml")));
        //FaithLevelBasic faithLevelBasic = new FaithLevelBasic();
        SoloActionDeck soloActionDeck = new SoloActionDeck(new File("SoloTokenConfig.xml"));*/
        SoloBoard soloBoard = new SoloBoard();

        FaithPointTokenObserver observer = new FaithPointTokenObserver(soloBoard);
        FaithPointToken faithPointToken = new FaithPointToken(2);

        assertTrue(faithPointToken.attach(observer));
        assertTrue(faithPointToken.playEffect());
        assertTrue(faithPointToken.detach(observer));

    }

}