package it.polimi.ingsw.soloGame;

import it.polimi.ingsw.DevCards.DevCardColour;
import it.polimi.ingsw.DevCards.DevGrid;
import it.polimi.ingsw.FaithTrack.FaithLevelBasic;
import it.polimi.ingsw.FaithTrack.FaithTrack;
import it.polimi.ingsw.FaithTrack.ReportNumOrder;
import it.polimi.ingsw.exceptions.EmptyDevColumnException;
import it.polimi.ingsw.exceptions.LastVaticanReportException;
import it.polimi.ingsw.exceptions.NegativeQuantityException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.Test;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

class SoloBoardTest {
    SoloBoard soloBoard;

    @BeforeEach
    public void init() throws ParserConfigurationException, NegativeQuantityException, SAXException, IOException {
        DevGrid devGrid = new DevGrid(new File("DevCardConfig.xsd.xml"));
        FaithLevelBasic lorenzosFaith = new FaithLevelBasic(FaithTrack.instance(ReportNumOrder.instance()));
        SoloActionDeck soloActionDeck = new SoloActionDeck(new File("SoloTokenConfig.xml"));
        soloBoard = new SoloBoard(devGrid, lorenzosFaith, soloActionDeck);
    }

    @Test
    public void moveLorenzosFaith() throws LastVaticanReportException {
        assertTrue(soloBoard.moveLorenzosFaith(2));
        assertEquals(soloBoard.getFaithTrackPosition(), 2);
    }

    @RepeatedTest(2)
    public void discardCard() throws EmptyDevColumnException {
        assertTrue(soloBoard.discardDevCards(DevCardColour.GREEN, 2));
    }

    @Test
    public void shuffle(){
        assertTrue(soloBoard.shuffleTokenDeck());
    }

    @Test
    public void testEmptyColumn(){
        assertFalse(soloBoard.isDevColumnEmpty(DevCardColour.GREEN));
    }















}