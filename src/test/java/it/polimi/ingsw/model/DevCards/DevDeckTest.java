package it.polimi.ingsw.model.DevCards;

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

}