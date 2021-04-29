package it.polimi.ingsw.model.LeaderCard.LeaderCardRequirements;

import it.polimi.ingsw.exceptions.NegativeQuantityException;
import it.polimi.ingsw.model.DevCards.DevCard;
import it.polimi.ingsw.model.DevCards.DevCardColour;
import it.polimi.ingsw.model.PlayerResourcesAndCards;
import it.polimi.ingsw.model.ResourceType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class CardRequirementColorAndLevelTest {
    CardRequirementColorAndLevel requirement1;
    CardRequirementColorAndLevel requirement2;
    CardRequirementColorAndLevel requirement3;
    PlayerResourcesAndCards playerResourcesAndCards;
    HashMap<ResourceType, Integer> hashMap;
    DevCard cardLevel1;
    DevCard cardLevel2;
    DevCard cardLevel3;
    List<DevCard> devCards;

    @BeforeEach
    void setUp() throws NegativeQuantityException {
        requirement1 = new CardRequirementColorAndLevel(3, DevCardColour.GREEN, 2);
        requirement2 = new CardRequirementColorAndLevel(1, DevCardColour.BLUE, 1);
        requirement3 = new CardRequirementColorAndLevel(2, DevCardColour.GREEN, 2);
        hashMap = new HashMap<>();
        cardLevel1 = new DevCard(1, DevCardColour.GREEN, 1, hashMap, hashMap, hashMap, "abc");
        cardLevel2 = new DevCard(2, DevCardColour.GREEN, 2, hashMap, hashMap, hashMap, "abc");
        cardLevel3 = new DevCard(3, DevCardColour.GREEN, 3, hashMap, hashMap, hashMap, "abc");
        devCards = new ArrayList<>();
        devCards.add(cardLevel1);
        devCards.add(cardLevel1);
        devCards.add(cardLevel2);
        devCards.add(cardLevel1);
        devCards.add(cardLevel2);
        devCards.add(cardLevel3);
        playerResourcesAndCards = new PlayerResourcesAndCards(new HashMap<>(), devCards);

    }

    @Test
    void getQuantity() {
        assertEquals(2, requirement1.getQuantity());
        assertEquals(1, requirement2.getQuantity());
    }

    @Test
    void getLevel() {
        assertEquals(3, requirement1.getLevel());
        assertEquals(1, requirement2.getLevel());
    }

    @Test
    void getCardColour() {
        assertEquals(DevCardColour.GREEN, requirement1.getCardColour());
        assertEquals(DevCardColour.BLUE, requirement2.getCardColour());
    }

    @Test
    void checkRequirement() {
        assertFalse(requirement1.checkRequirement(playerResourcesAndCards));
        assertFalse(requirement2.checkRequirement(playerResourcesAndCards));
        assertTrue(requirement3.checkRequirement(playerResourcesAndCards));
    }

    @Test
    public void checkCloning() throws NegativeQuantityException {
        CardRequirementColorAndLevel clone = (CardRequirementColorAndLevel) requirement1.getClone();

        assertEquals(clone.getLevel(), requirement1.getLevel());
        assertEquals(clone.getCardColour(), requirement1.getCardColour());
        assertEquals(clone.getQuantity(), requirement1.getQuantity());
        assertNotSame(clone, requirement1);
        assertEquals(clone, requirement1);
    }
}