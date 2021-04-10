package it.polimi.ingsw.soloGame;

import it.polimi.ingsw.FaithTrack.ReportNum;
import it.polimi.ingsw.Interfaces.Observer;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class FaithPointTokenTest {

    @Test
    public void init1(){
        FaithPointToken faithPointToken = new FaithPointToken(2);
        assertEquals(faithPointToken.getFaithPoints(), 2);
        assertFalse(faithPointToken.isShuffleToken());
    }

    @Test
    public void init2(){
        FaithPointToken faithPointToken = new FaithPointToken(0);
        assertEquals(faithPointToken.getFaithPoints(), 0);
        assertFalse(faithPointToken.isShuffleToken());
    }

    @Test
    public void initNegativePoints(){
        Exception exception;
        exception = assertThrows(IllegalArgumentException.class, () -> new FaithPointToken(-1));
        assertEquals(exception.getMessage(), "Can't have negative faith points");
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

        FaithPointToken faithPointToken = new FaithPointToken(2);

        assertTrue(faithPointToken.attach(observer));
        assertTrue(faithPointToken.detach(observer));
        assertTrue(faithPointToken.playEffect());
    }

}