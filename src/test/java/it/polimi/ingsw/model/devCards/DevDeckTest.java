package it.polimi.ingsw.model.devCards;

import it.polimi.ingsw.exceptions.NegativeQuantityException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class DevDeckTest {
    List<DevCard> devCards;
    DevDeck devDeck;


    @Test
    void drawFromDeck() {
        int initialSize = devDeck.size();
        DevCard devCard = devDeck.drawFromDeck();
        if (initialSize == 0) {
            assertEquals(initialSize, devDeck.size());
            assertEquals(devCard, devDeck.getFirst());
        } else {
            assertEquals(initialSize - 1, devDeck.size());
            assertNotEquals(devCard, devDeck.getFirst());
        }
    }

    @Test
    void getFirstTest() {
        int initialSize = devDeck.size();
        DevCard devCard = devDeck.getFirst();
        assertEquals(initialSize, devDeck.size());
        assertEquals(devCard, devDeck.getFirst());

    }


    @Test
    void isEmpty() {
        if (devDeck.size() == 0)
            assertTrue(devDeck.isEmpty());
        else
            assertFalse(devDeck.isEmpty());


    }

    @BeforeEach
    void setUp() throws IllegalArgumentException, NegativeQuantityException {
        devCards = new LinkedList<>();
        for (int i = 0; i < 10; i++) {
            devCards.add(new DevCard(i % 3 + 1, DevCardColour.GREEN, i, new HashMap<>(), new HashMap<>(), new HashMap<>(), "abc"));
        }
        devDeck = new DevDeck(devCards);

        assertEquals(10, devDeck.size());
    }

    @Test
    public void ctrlEqualsTrue() {
        DevDeck d2 = new DevDeck(devCards);

        assertNotSame(devDeck, d2);
        assertEquals(devDeck, d2);
    }

    @Test
    //The decks differ because of only one card
    public void ctrlEqualsFalse() {
        devCards.remove(devCards.size() - 1);
        DevDeck d2 = new DevDeck(devCards);

        assertNotEquals(devDeck, d2);
    }

    @Test
    public void ctrlCloning() {
        DevDeck d2 = new DevDeck(devDeck);

        assertNotSame(devDeck, d2);
        assertEquals(devDeck, d2);
    }

    @Test
    //Now the deck we compare are not in the original configuration because a card has been drawn before the cloning operation
    public void ctrlCloningAfterDrawnCard() {
        devDeck.drawFromDeck();
        DevDeck d2 = new DevDeck(devDeck);

        assertNotSame(devDeck, d2);
        assertEquals(devDeck, d2);
    }

}