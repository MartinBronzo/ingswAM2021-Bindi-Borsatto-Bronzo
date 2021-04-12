package it.polimi.ingsw.DevCards;

import it.polimi.ingsw.ResourceType;
import it.polimi.ingsw.exceptions.NegativeQuantityException;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.*;

class DevSlotsTest {
    DevSlots devSlots;
    HashMap<ResourceType,Integer> hashMap;
    DevCard cardLevel1_1;
    DevCard cardLevel1_2;
    DevCard cardLevel1_3;
    DevCard cardLevel2_1;
    DevCard cardLevel2_2;
    DevCard cardLevel3_1;

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
        assertThrows(IllegalArgumentException.class, ()->devSlots.addDevCard(1, cardLevel3_1)); // is duplicated
        devSlots.addDevCard(1, newCard);
        assertEquals(newCard,devSlots.getDevSlot(1).getLastDevCard());
    }

    @Test
    void newConstructorTest() {
        DevSlots devSlots2 = new DevSlots(devSlots);
        assertEquals(cardLevel1_1, devSlots.getDevSlot(0).getDevCard(1));
        assertEquals(cardLevel1_2, devSlots.getDevSlot(1).getDevCard(1));
        assertEquals(cardLevel2_1, devSlots.getDevSlot(1).getDevCard(2));
        assertEquals(cardLevel1_3, devSlots.getDevSlot(2).getDevCard(1));
        assertEquals(cardLevel2_2, devSlots.getDevSlot(2).getDevCard(2));
        assertEquals(cardLevel3_1, devSlots.getDevSlot(2).getDevCard(3));

    }

    @BeforeEach
    void setUp() throws IllegalArgumentException, NegativeQuantityException {
        devSlots=new DevSlots();
        hashMap=new HashMap<>();
        cardLevel1_1=new DevCard(1, DevCardColour.GREEN,1,hashMap,hashMap,hashMap,"abc");
        cardLevel1_2=new DevCard(1, DevCardColour.GREEN,1,hashMap,hashMap,hashMap,"abc");
        cardLevel1_3=new DevCard(1, DevCardColour.GREEN,1,hashMap,hashMap,hashMap,"abc");
        cardLevel2_1=new DevCard(2,DevCardColour.GREEN,2,hashMap,hashMap,hashMap,"abc");
        cardLevel2_2=new DevCard(2,DevCardColour.GREEN,2,hashMap,hashMap,hashMap,"abc");
        cardLevel3_1=new DevCard(3,DevCardColour.GREEN,3,hashMap,hashMap,hashMap,"abc");
        devSlots.addDevCard(0,cardLevel1_1);
        devSlots.addDevCard(1,cardLevel1_2);
        devSlots.addDevCard(1,cardLevel2_1);
        devSlots.addDevCard(2,cardLevel1_3);
        devSlots.addDevCard(2,cardLevel2_2);
        devSlots.addDevCard(2,cardLevel3_1);
    }

}