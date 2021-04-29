package it.polimi.ingsw.model.soloGame;

import org.junit.jupiter.api.Test;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;

class SoloActionDeckTest {
    @Test
    public void init() throws IOException, SAXException, ParserConfigurationException {
        File xmlTokenConfig = new File("SoloTokenConfig.xml");
        SoloActionDeck deck = new SoloActionDeck(xmlTokenConfig);

        assertEquals(deck.size(), 6);
    }

}