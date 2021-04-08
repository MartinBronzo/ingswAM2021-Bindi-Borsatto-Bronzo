package it.polimi.ingsw.marble;

import it.polimi.ingsw.LeaderCard.leaderEffects.Effect;
import it.polimi.ingsw.ResourceType;
import it.polimi.ingsw.exceptions.NegativeQuantityException;
import org.junit.jupiter.api.Test;

import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.*;

class RedMarbleTest {
    final ResourceType coin = ResourceType.COIN;
    final ResourceType shield = ResourceType.SHIELD;
    final ResourceType servant = ResourceType.SERVANT;
    final ResourceType stone = ResourceType.STONE;
    final ResourceType faith = ResourceType.FAITHPOINT;

    @Test
    void onActivateLegalTest() throws NegativeQuantityException {
        HashMap<ResourceType, Integer> resourceMap = new HashMap<>();
        Effect effect = new Effect();
        Marble marble = new RedMarble();

        for (Integer c=0; c<100; c++){
            assertEquals(c,resourceMap.getOrDefault(faith,0));
            marble.onActivate(resourceMap,effect);
        }
        assertEquals(100,resourceMap.get(faith));
        assertNull(resourceMap.get(coin));
        assertNull(resourceMap.get(servant));
        assertNull(resourceMap.get(stone));
        assertNull(resourceMap.get(shield));
    }

    @Test
    void onActivateIllegalTest() throws NegativeQuantityException {
        HashMap<ResourceType, Integer> resourceMap = new HashMap<>();
        Effect effect = new Effect();
        Marble marble = new RedMarble();

        marble.onActivate(resourceMap,effect);
        assertEquals(1,resourceMap.get(faith));
        resourceMap.put(faith, -3);
        assertThrows(NegativeQuantityException.class, () -> marble.onActivate(resourceMap,effect));
        assertNull(resourceMap.get(coin));
        assertNull(resourceMap.get(servant));
        assertNull(resourceMap.get(stone));
        assertNull(resourceMap.get(shield));
    }
}