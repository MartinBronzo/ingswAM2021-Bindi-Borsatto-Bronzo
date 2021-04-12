package it.polimi.ingsw.LeaderCardRequirementsTests;

import it.polimi.ingsw.Depot;
import it.polimi.ingsw.DevCards.DevCard;
import it.polimi.ingsw.DevCards.DevCardColour;
import it.polimi.ingsw.DevCards.DevSlots;
import it.polimi.ingsw.PlayerBoard;
import it.polimi.ingsw.PlayerResourcesAndCards;
import it.polimi.ingsw.ResourceType;
import it.polimi.ingsw.exceptions.IllegalActionException;
import it.polimi.ingsw.exceptions.NegativeQuantityException;
import it.polimi.ingsw.exceptions.NotEnoughSpaceException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Hashtable;
import java.util.LinkedList;

import static org.junit.jupiter.api.Assertions.*;

class CardRequirementResourceTest {
    CardRequirementResource requirement1;
    CardRequirementResource requirement2;
    CardRequirementResource requirement3;
    CardRequirementResource requirement4;
    PlayerResourcesAndCards playerResourcesAndCards;
    HashMap<ResourceType,Integer> hashMap;

    @BeforeEach
    void setUp() {
        requirement1 = new CardRequirementResource(ResourceType.STONE,3);
        requirement2 = new CardRequirementResource(ResourceType.SERVANT,4);
        requirement3 = new CardRequirementResource(ResourceType.SHIELD,3);
        requirement4 = new CardRequirementResource(ResourceType.COIN,300);
        hashMap = new HashMap<>();
        hashMap.put(ResourceType.STONE, 3);
        hashMap.put(ResourceType.SERVANT, 4);
        hashMap.put(ResourceType.SHIELD, 4);
        hashMap.put(ResourceType.COIN, 1);
        playerResourcesAndCards = new PlayerResourcesAndCards(hashMap, new LinkedList<>());

    }

    @Test
    void getQuantity() {
        assertEquals(3,requirement1.getQuantity());
        assertEquals(4,requirement2.getQuantity());
    }

    @Test
    void getResourceType() {
        assertEquals(ResourceType.SHIELD,requirement3.getResourceType());
        assertEquals(ResourceType.COIN,requirement4.getResourceType());
    }

    @Test
    void checkRequirement() {
        assertTrue(requirement1.checkRequirement(playerResourcesAndCards));
        assertTrue(requirement2.checkRequirement(playerResourcesAndCards));
        assertTrue(requirement3.checkRequirement(playerResourcesAndCards));
        assertFalse(requirement4.checkRequirement(playerResourcesAndCards));
    }

    @Test
    public void checkCloning() throws NegativeQuantityException {
        CardRequirementResource clone = (CardRequirementResource) requirement1.getClone();

        assertEquals(clone.getResourceType(), requirement1.getResourceType());
        assertEquals(clone.getQuantity(), requirement1.getQuantity());
        assertNotSame(clone, requirement1);
    }
}