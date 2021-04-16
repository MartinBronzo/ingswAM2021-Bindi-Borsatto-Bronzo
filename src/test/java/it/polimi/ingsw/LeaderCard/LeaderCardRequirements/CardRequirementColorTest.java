package it.polimi.ingsw.LeaderCard.LeaderCardRequirements;

import it.polimi.ingsw.DevCards.DevCard;
import it.polimi.ingsw.DevCards.DevCardColour;
import it.polimi.ingsw.PlayerResourcesAndCards;
import it.polimi.ingsw.ResourceType;
import it.polimi.ingsw.exceptions.NegativeQuantityException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class CardRequirementColorTest {
    CardRequirementColor requirement1;
    CardRequirementColor requirement2;
    PlayerResourcesAndCards playerResourcesAndCards;
    HashMap<ResourceType,Integer> hashMap;
    DevCard cardLevel1;
    DevCard cardLevel2;
    DevCard cardLevel3;
    List<DevCard> devCards;

    @BeforeEach
    void setUp() throws NegativeQuantityException {
        requirement1 = new CardRequirementColor(DevCardColour.GREEN,2);
        requirement2 = new CardRequirementColor(DevCardColour.BLUE,1);
        hashMap=new HashMap<>();
        cardLevel1=new DevCard(1, DevCardColour.GREEN,1,hashMap,hashMap,hashMap,"abc");
        cardLevel2=new DevCard(2,DevCardColour.GREEN,2,hashMap,hashMap,hashMap,"abc");
        cardLevel3=new DevCard(3,DevCardColour.GREEN,3,hashMap,hashMap,hashMap,"abc");
        devCards=new ArrayList<>();
        devCards.add(cardLevel1);
        devCards.add(cardLevel2);
        devCards.add(cardLevel3);
        playerResourcesAndCards = new PlayerResourcesAndCards(new HashMap<>(), devCards);
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
        assertTrue(requirement1.checkRequirement(playerResourcesAndCards));
        assertFalse(requirement2.checkRequirement(playerResourcesAndCards));
    }

    @Test
    public void checkCloning() throws NegativeQuantityException {
        CardRequirementColor clone = (CardRequirementColor) requirement1.getClone();

        assertEquals(clone.getCardColour(), requirement1.getCardColour());
        assertEquals(clone.getQuantity(), requirement1.getQuantity());
        assertNotSame(clone, requirement1);
        assertEquals(clone, requirement1);
    }
}