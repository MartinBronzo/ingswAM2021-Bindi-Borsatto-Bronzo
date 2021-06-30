package it.polimi.ingsw.model.soloGame;

import it.polimi.ingsw.model.devCards.DevCardColour;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;


import static org.junit.jupiter.api.Assertions.*;

class DiscardTokenTest {

    @Test
    public void init1() {
        DiscardToken discardToken = new DiscardToken(DevCardColour.BLUE, 2, "name");
        Assertions.assertEquals(discardToken.getCardColour(), DevCardColour.BLUE);
        assertEquals(discardToken.getNumCards(), 2);
    }

    @Test
    public void init2() {
        DiscardToken discardToken = new DiscardToken(DevCardColour.GREEN, 0, "name");
        Assertions.assertEquals(discardToken.getCardColour(), DevCardColour.GREEN);
        assertEquals(discardToken.getNumCards(), 0);
    }

    @Test
    public void initNegativeNumCard() {
        Exception exception;
        exception = assertThrows(IllegalArgumentException.class, () -> new DiscardToken(DevCardColour.GREEN, -1, "name"));
        assertEquals(exception.getMessage(), "Can't discard negative cards");
    }
}