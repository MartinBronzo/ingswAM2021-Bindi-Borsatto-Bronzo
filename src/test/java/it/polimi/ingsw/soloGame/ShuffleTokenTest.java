package it.polimi.ingsw.soloGame;

import it.polimi.ingsw.FaithTrack.ReportNum;
import it.polimi.ingsw.Interfaces.Observer;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ShuffleTokenTest {

    @Test
    public void init1(){
        ShuffleToken shuffleToken = new ShuffleToken(2);
        assertEquals(shuffleToken.getFaithPoints(), 2);
        assertTrue(shuffleToken.isShuffleToken());
    }

    @Test
    public void init2(){
        ShuffleToken shuffleToken = new ShuffleToken(0);
        assertEquals(shuffleToken.getFaithPoints(), 0);
        assertTrue(shuffleToken.isShuffleToken());
    }

    @Test
    public void initNegativePoints(){
        Exception exception;
        exception = assertThrows(IllegalArgumentException.class, () -> new ShuffleToken(-5));
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

        ShuffleToken shuffleToken = new ShuffleToken(2);

        assertTrue(shuffleToken.attach(observer));
        assertTrue(shuffleToken.playEffect());
        assertTrue(shuffleToken.detach(observer));
    }

}