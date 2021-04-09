package it.polimi.ingsw.LeaderCardRequirementsTests;

import it.polimi.ingsw.Depot;
import it.polimi.ingsw.DevCards.DevCard;
import it.polimi.ingsw.DevCards.DevCardColour;
import it.polimi.ingsw.DevCards.DevSlots;
import it.polimi.ingsw.PlayerBoard;
import it.polimi.ingsw.ResourceType;
import it.polimi.ingsw.exceptions.IllegalActionException;
import it.polimi.ingsw.exceptions.NotEnoughSpaceException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Hashtable;

import static org.junit.jupiter.api.Assertions.*;

class CardRequirementResourceTest {
    CardRequirementResource requirement1;
    CardRequirementResource requirement2;
    CardRequirementResource requirement3;
    CardRequirementResource requirement4;
    PlayerBoard playerBoard;
    HashMap<ResourceType,Integer> hashMap;

    @BeforeEach
    void setUp() throws IllegalActionException, NotEnoughSpaceException {
        requirement1 = new CardRequirementResource(ResourceType.STONE,3);
        requirement2 = new CardRequirementResource(ResourceType.SERVANT,4);
        requirement3 = new CardRequirementResource(ResourceType.SHIELD,3);
        requirement4 = new CardRequirementResource(ResourceType.COIN,300);
        playerBoard = new PlayerBoard();
        playerBoard.getDepot().addToShelf(ResourceType.STONE, 3, 3);
        playerBoard.getDepot().addToShelf(ResourceType.SERVANT, 2, 2);
        hashMap = new HashMap<>();
        hashMap.put(ResourceType.SERVANT, 2);
        hashMap.put(ResourceType.SHIELD, 4);
        hashMap.put(ResourceType.COIN, 1);
        playerBoard.getStrongbox().addResource(hashMap);

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
        assertTrue(requirement1.checkRequirement(playerBoard));
        assertTrue(requirement2.checkRequirement(playerBoard));
        assertTrue(requirement3.checkRequirement(playerBoard));
        assertFalse(requirement4.checkRequirement(playerBoard));
    }
}