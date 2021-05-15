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

class ShuffleTokenTest {

    @Test
    public void init1() {
        ShuffleToken shuffleToken = new ShuffleToken(2);
        assertEquals(shuffleToken.getFaithPoints(), 2);
        assertTrue(shuffleToken.isShuffleToken());
    }

    @Test
    public void init2() {
        ShuffleToken shuffleToken = new ShuffleToken(0);
        assertEquals(shuffleToken.getFaithPoints(), 0);
        assertTrue(shuffleToken.isShuffleToken());
    }

    @Test
    public void initNegativePoints() {
        Exception exception;
        exception = assertThrows(IllegalArgumentException.class, () -> new ShuffleToken(-5));
        assertEquals(exception.getMessage(), "Can't have negative faith points");
    }

    @Test
    public void attachObserver() throws LastVaticanReportException, IOException, SAXException, ParserConfigurationException, NegativeQuantityException {
        /*DevGrid devGrid = new DevGrid(new File("DevCardConfig.xsd.xml"));
        FaithLevelBasic faithLevelBasic = new FaithLevelBasic(FaithTrack.instance(new File("FaithTrackConfig.xml")));
        //FaithLevelBasic faithLevelBasic = new FaithLevelBasic();
        SoloActionDeck soloActionDeck = new SoloActionDeck(new File("SoloTokenConfig.xml"));*/
        SoloBoard soloBoard = new SoloBoard();

        FaithPointTokenObserver observer = new FaithPointTokenObserver(soloBoard);

        ShuffleToken shuffleToken = new ShuffleToken(2);

        assertTrue(shuffleToken.attach(observer));
        assertTrue(shuffleToken.playEffect());
        assertTrue(shuffleToken.detach(observer));
    }

}