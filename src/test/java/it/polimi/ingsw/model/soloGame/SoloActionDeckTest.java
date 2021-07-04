package it.polimi.ingsw.model.soloGame;

import org.junit.jupiter.api.Test;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import static org.junit.jupiter.api.Assertions.assertEquals;

class SoloActionDeckTest {
    @Test
    public void init() throws IOException, SAXException, ParserConfigurationException {
        SoloBoard soloBoard = new SoloBoard();
        DiscardTokenObserver discardTokenObserver = new DiscardTokenObserver(soloBoard);
        FaithPointTokenObserver faithPointTokenObserver = new FaithPointTokenObserver(soloBoard);

        InputStream xmlTokenConfig = this.getClass().getResourceAsStream("/XMLs/SoloTokenConfig.xml");
        SoloActionDeck deck = new SoloActionDeck(xmlTokenConfig, discardTokenObserver, faithPointTokenObserver);

        assertEquals(deck.size(), 6);
    }

}