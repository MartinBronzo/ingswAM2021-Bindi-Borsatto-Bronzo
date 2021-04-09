package it.polimi.ingsw.LeaderCardRequirementsTests;

import it.polimi.ingsw.DevCards.DevCard;
import it.polimi.ingsw.DevCards.DevCardColour;
import it.polimi.ingsw.DevCards.DevSlots;
import it.polimi.ingsw.PlayerBoard;
import it.polimi.ingsw.ResourceType;
import it.polimi.ingsw.exceptions.NegativeQuantityException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.*;

class CardRequirementColorTest {
    CardRequirementColor requirement1;
    CardRequirementColor requirement2;
    PlayerBoard playerBoard;
    HashMap<ResourceType,Integer> hashMap;
    DevCard cardLevel1;
    DevCard cardLevel2;
    DevCard cardLevel3;
    DevSlots devSlots;

    @BeforeEach
    void setUp() throws NegativeQuantityException {
        requirement1 = new CardRequirementColor(DevCardColour.GREEN,2);
        requirement2 = new CardRequirementColor(DevCardColour.BLUE,1);
        playerBoard = new PlayerBoard();
        hashMap=new HashMap<>();
        cardLevel1=new DevCard(1, DevCardColour.GREEN,1,hashMap,hashMap,hashMap,"abc");
        cardLevel2=new DevCard(2,DevCardColour.GREEN,2,hashMap,hashMap,hashMap,"abc");
        cardLevel3=new DevCard(3,DevCardColour.GREEN,3,hashMap,hashMap,hashMap,"abc");
        devSlots=playerBoard.getDevSlots();
        devSlots.getDevSlot(0).addDevCard(cardLevel1);
        devSlots.getDevSlot(1).addDevCard(cardLevel1);
        devSlots.getDevSlot(1).addDevCard(cardLevel2);
        devSlots.getDevSlot(2).addDevCard(cardLevel1);
        devSlots.getDevSlot(2).addDevCard(cardLevel2);
        devSlots.getDevSlot(2).addDevCard(cardLevel3);

    }

    @Test
    void getQuantity() {
        assertEquals(2,requirement1.getQuantity());
        assertEquals(1,requirement2.getQuantity());
    }

    @Test
    void getCardColour() {
        assertEquals(DevCardColour.GREEN,requirement1.getCardColour());
        assertEquals(DevCardColour.BLUE,requirement2.getCardColour());
    }

    @Test
    void checkRequirement() {
        assertTrue(requirement1.checkRequirement(playerBoard));
        assertFalse(requirement2.checkRequirement(playerBoard));
    }
}