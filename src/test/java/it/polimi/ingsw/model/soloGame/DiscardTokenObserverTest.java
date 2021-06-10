package it.polimi.ingsw.model.soloGame;

import it.polimi.ingsw.exceptions.EmptyDevColumnException;
import it.polimi.ingsw.exceptions.NegativeQuantityException;
import it.polimi.ingsw.model.DevCards.DevCardColour;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertTrue;

class DiscardTokenObserverTest {
    SoloBoard soloBoard;

    @BeforeEach
    public void initSoloBoard() throws ParserConfigurationException, NegativeQuantityException, SAXException, IOException {
        /*DevGrid devGrid = new DevGrid(new File("DevCardConfig.xsd.xml"));
        FaithLevelBasic lorenzosFaith = new FaithLevelBasic(FaithTrack.instance(ReportNumOrder.instance()));
        SoloActionDeck soloActionDeck = new SoloActionDeck(new File("SoloTokenConfig.xml"));*/
        soloBoard = new SoloBoard();
    }

    @Test
    public void init() throws EmptyDevColumnException {
        DiscardTokenObserver observer = new DiscardTokenObserver(soloBoard);
        DiscardToken discardToken = new DiscardToken(DevCardColour.GREEN, 1, "name");

        assertTrue(discardToken.attach(observer));
        assertTrue(discardToken.playEffect());
    }

}