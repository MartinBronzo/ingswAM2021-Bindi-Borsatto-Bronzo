package it.polimi.ingsw.DevCards;

import it.polimi.ingsw.DevCards.DevCard;
import it.polimi.ingsw.DevCards.DevCardColour;
import it.polimi.ingsw.DevCards.DevSlot;
import it.polimi.ingsw.ResourceType;
import it.polimi.ingsw.exceptions.NegativeQuantityException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.*;

class DevSlotTest {
    DevSlot devSlot;
    HashMap<ResourceType,Integer> hashMap;
    DevCard cardLevel1;
    DevCard cardLevel2;
    DevCard cardLevel3;



    @Test
    void addDevCardsSequentially() throws IllegalArgumentException {
        devSlot=new DevSlot();
        devSlot.addDevCard(cardLevel1);
        devSlot.addDevCard(cardLevel2);
        devSlot.addDevCard(cardLevel3);
        assertEquals(cardLevel1,devSlot.getDevCard(1));
        assertEquals(cardLevel2,devSlot.getDevCard(2));
        assertEquals(cardLevel3,devSlot.getDevCard(3));
        assertEquals(6,devSlot.getPoints());
        assertEquals(cardLevel3,devSlot.getLastDevCard());
    }

    @Test
    void get0PointTest() {
        devSlot=new DevSlot();
        assertEquals(0,devSlot.getPoints());
    }

    @Test
    void getNotValidDevCard() {
        devSlot=new DevSlot();
        devSlot.addDevCard(cardLevel1);
        assertNull(devSlot.getDevCard(2));
    }

    @Test
    void IllegalAddDevCard() {
        devSlot=new DevSlot();
        assertThrows(NullPointerException.class, ()->devSlot.addDevCard(null));
        assertThrows(java.lang.IllegalArgumentException.class, ()->devSlot.addDevCard(cardLevel2));
        assertThrows(java.lang.IllegalArgumentException.class, ()->devSlot.addDevCard(cardLevel3));

        devSlot.addDevCard(cardLevel1);
        assertThrows(java.lang.IllegalArgumentException.class, ()->devSlot.addDevCard(cardLevel1));
        assertThrows(java.lang.IllegalArgumentException.class, ()->devSlot.addDevCard(cardLevel3));


        devSlot.addDevCard(cardLevel2);
        assertThrows(java.lang.IllegalArgumentException.class, ()->devSlot.addDevCard(cardLevel1));
        assertThrows(java.lang.IllegalArgumentException.class, ()->devSlot.addDevCard(cardLevel2));

        devSlot.addDevCard(cardLevel3);
        assertThrows(java.lang.IllegalArgumentException.class, ()->devSlot.addDevCard(cardLevel1));
        assertThrows(java.lang.IllegalArgumentException.class, ()->devSlot.addDevCard(cardLevel2));
        assertThrows(java.lang.IllegalArgumentException.class, ()->devSlot.addDevCard(cardLevel3));

    }

    @BeforeEach
    void setUp() throws IllegalArgumentException, NegativeQuantityException {
        hashMap=new HashMap<>();
        cardLevel1=new DevCard(1, DevCardColour.GREEN,1,hashMap,hashMap,hashMap,"abc");
        cardLevel2=new DevCard(2,DevCardColour.GREEN,2,hashMap,hashMap,hashMap,"abc");
        cardLevel3=new DevCard(3,DevCardColour.GREEN,3,hashMap,hashMap,hashMap,"abc");


    }
}