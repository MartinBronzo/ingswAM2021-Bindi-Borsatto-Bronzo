package it.polimi.ingsw.model.soloGame;

import it.polimi.ingsw.exceptions.LastVaticanReportException;
import it.polimi.ingsw.exceptions.NegativeQuantityException;
import it.polimi.ingsw.model.DevCards.DevGrid;
import it.polimi.ingsw.model.FaithTrack.FaithLevelBasic;
import it.polimi.ingsw.model.FaithTrack.FaithTrack;
import it.polimi.ingsw.model.FaithTrack.ReportNumOrder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class FaithPointTokenObserverTest {
    SoloBoard soloBoard;

    @BeforeEach
    public void initSoloBoard() throws ParserConfigurationException, NegativeQuantityException, SAXException, IOException {
        /*DevGrid devGrid = new DevGrid(new File("DevCardConfig.xsd.xml"));
        FaithLevelBasic lorenzosFaith = new FaithLevelBasic(FaithTrack.instance(ReportNumOrder.instance()));
        SoloActionDeck soloActionDeck = new SoloActionDeck(new File("SoloTokenConfig.xml"));*/
        soloBoard = new SoloBoard();
    }

    @Test
    public void init() throws LastVaticanReportException {
        FaithPointTokenObserver observer = new FaithPointTokenObserver(soloBoard);
        FaithPointToken faithPointToken = new FaithPointToken(2);
        ShuffleToken shuffleToken = new ShuffleToken(1);

        assertTrue(faithPointToken.attach(observer));
        assertTrue(faithPointToken.playEffect());
        assertEquals(soloBoard.getLorenzoFaithTrackPosition(), 2);

        assertTrue(shuffleToken.attach(observer));
        assertTrue(shuffleToken.playEffect());
        assertEquals(soloBoard.getLorenzoFaithTrackPosition(), 3);
    }
}