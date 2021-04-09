package it.polimi.ingsw.DevCards;

import it.polimi.ingsw.ResourceType;
import it.polimi.ingsw.exceptions.NegativeQuantityException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.*;

class DevSlotsTest {
    DevSlots devSlots;
    HashMap<ResourceType,Integer> hashMap;
    DevCard cardLevel1;
    DevCard cardLevel2;
    DevCard cardLevel3;

    @Test
    void getPointsTest() {
        assertEquals(10,devSlots.getPoints());
    }

    @Test
    void getCardsTest() {
        assertEquals(6,devSlots.getAllDevCards().size());
    }

    @Test
    void getIllegalDevSlotTest() {
        assertThrows(IndexOutOfBoundsException.class, ()->devSlots.getDevSlot(-1));
        assertThrows(IndexOutOfBoundsException.class, ()->devSlots.getDevSlot(3));
    }
    @Test
    void AddDevCardTest() throws NegativeQuantityException {
        DevCard newCard=new DevCard(3, DevCardColour.BLUE,7,hashMap,hashMap,hashMap,"abc");
        assertThrows(IndexOutOfBoundsException.class, ()->devSlots.addDevCard(-1, newCard));
        assertThrows(IndexOutOfBoundsException.class, ()->devSlots.addDevCard(3, newCard));
        assertThrows(NullPointerException.class, ()->devSlots.addDevCard(1, null));
        assertThrows(IllegalArgumentException.class, ()->devSlots.addDevCard(1, cardLevel3));
        devSlots.addDevCard(1, newCard);
        assertEquals(newCard,devSlots.getDevSlot(1).getLastDevCard());

    }

    @BeforeEach
    void setUp() throws IllegalArgumentException, NegativeQuantityException {
        devSlots=new DevSlots();
        hashMap=new HashMap<>();
        cardLevel1=new DevCard(1, DevCardColour.GREEN,1,hashMap,hashMap,hashMap,"abc");
        cardLevel2=new DevCard(2,DevCardColour.GREEN,2,hashMap,hashMap,hashMap,"abc");
        cardLevel3=new DevCard(3,DevCardColour.GREEN,3,hashMap,hashMap,hashMap,"abc");
        devSlots.getDevSlot(0).addDevCard(cardLevel1);
        devSlots.getDevSlot(1).addDevCard(cardLevel1);
        devSlots.getDevSlot(1).addDevCard(cardLevel2);
        devSlots.getDevSlot(2).addDevCard(cardLevel1);
        devSlots.getDevSlot(2).addDevCard(cardLevel2);
        devSlots.getDevSlot(2).addDevCard(cardLevel3);
    }

}