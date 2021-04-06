package it.polimi.ingsw.marble;

import it.polimi.ingsw.leaderEffects.Effect;
import it.polimi.ingsw.ResourceType;
import it.polimi.ingsw.exceptions.NegativeQuantityException;
import org.junit.jupiter.api.Test;

import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.*;

class BlueMarbleTest {
    final ResourceType coin = ResourceType.COIN;
    final ResourceType shield = ResourceType.SHIELD;
    final ResourceType servant = ResourceType.SERVANT;
    final ResourceType stone = ResourceType.STONE;
    final ResourceType faith = ResourceType.FAITHPOINT;



    @Test
    void onActivateLegalTest() throws NegativeQuantityException {
        HashMap<ResourceType, Integer> resourceMap = new HashMap<>();
        Effect effect = new Effect();
        BlueMarble marble = new BlueMarble();

        for (Integer c=0; c<100; c++){
            assertEquals(c,resourceMap.getOrDefault(shield,0));
            marble.onActivate(resourceMap,effect);
        }
        assertEquals(100,resourceMap.get(shield));
        assertNull(resourceMap.get(coin));
        assertNull(resourceMap.get(servant));
        assertNull(resourceMap.get(stone));
        assertNull(resourceMap.get(faith));
    }

    @Test
    void onActivateIllegalTest() throws NegativeQuantityException {
        HashMap<ResourceType, Integer> resourceMap = new HashMap<>();
        Effect effect = new Effect();
        BlueMarble marble = new BlueMarble();

        marble.onActivate(resourceMap,effect);
        assertEquals(1,resourceMap.get(shield));
        resourceMap.put(shield, -3);
        assertThrows(NegativeQuantityException.class, () -> marble.onActivate(resourceMap,effect));
        assertNull(resourceMap.get(coin));
        assertNull(resourceMap.get(servant));
        assertNull(resourceMap.get(stone));
        assertNull(resourceMap.get(faith));
    }
}