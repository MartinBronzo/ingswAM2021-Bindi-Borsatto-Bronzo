package it.polimi.ingsw.soloGame;

import it.polimi.ingsw.DevCards.DevCardColour;
import it.polimi.ingsw.FaithTrack.ReportNum;
import it.polimi.ingsw.Interfaces.Observer;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class DiscardTokenTest {

    @Test
    public void init1(){
        DiscardToken discardToken = new DiscardToken(DevCardColour.BLUE, 2);
        assertEquals(discardToken.getCardColour(), DevCardColour.BLUE);
        assertEquals(discardToken.getNumCards(), 2);
    }

    @Test
    public void init2(){
        DiscardToken discardToken = new DiscardToken(DevCardColour.GREEN, 0);
        assertEquals(discardToken.getCardColour(), DevCardColour.GREEN);
        assertEquals(discardToken.getNumCards(), 0);
    }

    @Test
    public void initNegativeNumCard(){
        Exception exception;
        exception = assertThrows(IllegalArgumentException.class, () -> new DiscardToken(DevCardColour.GREEN, -1));
        assertEquals(exception.getMessage(), "Can't discard negative cards");
    }

    @Test
    public void attachObserver(){
        Observer observer = new Observer() {
            @Override
            public String update() {
                return null;
            }

            @Override
            public boolean update(boolean tmp, ReportNum reportNum) {
                return true;
            }

            @Override
            public boolean update(Object object) {
                return true;
            }
        };

        DiscardToken discardToken = new DiscardToken(DevCardColour.PURPLE, 1);

        assertTrue(discardToken.attach(observer));
        assertTrue(discardToken.playEffect());
        assertTrue(discardToken.detach(observer));
    }
}