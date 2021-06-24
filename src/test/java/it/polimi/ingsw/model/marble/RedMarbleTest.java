package it.polimi.ingsw.model.marble;

import it.polimi.ingsw.exceptions.NegativeQuantityException;
import it.polimi.ingsw.model.leaderCard.leaderEffects.Effect;
import it.polimi.ingsw.model.resources.ResourceType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class RedMarbleTest {
    final ResourceType coin = ResourceType.COIN;
    final ResourceType shield = ResourceType.SHIELD;
    final ResourceType servant = ResourceType.SERVANT;
    final ResourceType stone = ResourceType.STONE;
    final ResourceType faith = ResourceType.FAITHPOINT;
    HashMap<ResourceType, Integer> resourceMap;
    Effect effect;
    List<Effect> effects;
    Marble marble;


    @BeforeEach
    void setUp() {
        resourceMap = new HashMap<>();
        effect = new Effect();
        effects = new LinkedList<>();
        effects.add(effect);
        marble = new RedMarble();
    }

    @Test
    void onActivateLegalTest() throws NegativeQuantityException {
        for (Integer c = 0; c < 100; c++) {
            assertEquals(c, resourceMap.getOrDefault(faith, 0));
            marble.onActivate(resourceMap, effects);
        }
        assertEquals(100, resourceMap.get(faith));
        assertNull(resourceMap.get(coin));
        assertNull(resourceMap.get(servant));
        assertNull(resourceMap.get(stone));
        assertNull(resourceMap.get(shield));
    }

    @Test
    void onActivateIllegalTest() throws NegativeQuantityException {

        marble.onActivate(resourceMap, effects);
        assertEquals(1, resourceMap.get(faith));
        resourceMap.put(faith, -3);
        assertThrows(NegativeQuantityException.class, () -> marble.onActivate(resourceMap, effects));
        assertNull(resourceMap.get(coin));
        assertNull(resourceMap.get(servant));
        assertNull(resourceMap.get(stone));
        assertNull(resourceMap.get(shield));
    }
}