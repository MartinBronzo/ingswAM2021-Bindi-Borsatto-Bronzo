package it.polimi.ingsw.model.LeaderCardTest;

import it.polimi.ingsw.exceptions.NegativeQuantityException;
import it.polimi.ingsw.model.LeaderCard.LeaderCardDeck;
import org.junit.jupiter.api.Test;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

public class LeaderCardDeckTest {

    @Test
    public void ctrlEqualsTrue() throws NegativeQuantityException, ParserConfigurationException, IOException, SAXException {
        LeaderCardDeck l1 = new LeaderCardDeck(new File("LeaderCardConfig.xml"));
        LeaderCardDeck l2 = new LeaderCardDeck(new File("LeaderCardConfig.xml"));

        assertNotSame(l1, l2);
        assertEquals(l1, l2);
    }

    @Test
    public void ctrlEqualsFalse() throws NegativeQuantityException, ParserConfigurationException, IOException, SAXException {
        LeaderCardDeck l1 = new LeaderCardDeck(new File("LeaderCardConfig.xml"));
        LeaderCardDeck l2 = new LeaderCardDeck(new File("LeaderCardConfig.xml"));

        l2.drawFromDeck();

        assertNotSame(l1, l2);
        assertNotEquals(l1, l2);
    }

    @Test
    public void ctrlCloning() throws NegativeQuantityException, ParserConfigurationException, IOException, SAXException {
        LeaderCardDeck l1 = new LeaderCardDeck(new File("LeaderCardConfig.xml"));
        LeaderCardDeck l2 = new LeaderCardDeck(l1);

        assertNotSame(l1, l2);
        assertEquals(l1, l2);
    }

    @Test
    //Now the deck we compare are not in the original configuration because a card has been drawn before the cloning operation
    public void ctrlCloningAfterDrawnCard() throws NegativeQuantityException, ParserConfigurationException, IOException, SAXException {
        LeaderCardDeck l1 = new LeaderCardDeck(new File("LeaderCardConfig.xml"));
        l1.drawFromDeck();
        LeaderCardDeck l2 = new LeaderCardDeck(l1);

        assertNotSame(l1, l2);
        assertEquals(l1, l2);
    }
}
