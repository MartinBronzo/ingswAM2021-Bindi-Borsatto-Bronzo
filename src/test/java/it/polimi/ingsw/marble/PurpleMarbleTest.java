package it.polimi.ingsw.marble;

import it.polimi.ingsw.LeaderCard.leaderEffects.Effect;
import it.polimi.ingsw.ResourceType;
import it.polimi.ingsw.exceptions.NegativeQuantityException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class PurpleMarbleTest {
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
    void setUp(){
        resourceMap = new HashMap<>();
        effect = new Effect();
        effects = new LinkedList<>();
        effects.add(effect);
        marble = new PurpleMarble();
    }

    @Test
    void onActivateLegalTest() throws NegativeQuantityException {

        for (Integer c=0; c<100; c++){
            assertEquals(c,resourceMap.getOrDefault(servant,0));
            marble.onActivate(resourceMap, effects);
        }
        assertEquals(100,resourceMap.get(servant));
        assertNull(resourceMap.get(coin));
        assertNull(resourceMap.get(shield));
        assertNull(resourceMap.get(stone));
        assertNull(resourceMap.get(faith));
    }

    @Test
    void onActivateIllegalTest() throws NegativeQuantityException {
        HashMap<ResourceType, Integer> resourceMap = new HashMap<>();
        Effect effect = new Effect();
        Marble marble = new PurpleMarble();

        marble.onActivate(resourceMap, effects);
        assertEquals(1,resourceMap.get(servant));
        resourceMap.put(servant, -3);
        assertThrows(NegativeQuantityException.class, () -> marble.onActivate(resourceMap, effects));
        assertNull(resourceMap.get(coin));
        assertNull(resourceMap.get(shield));
        assertNull(resourceMap.get(stone));
        assertNull(resourceMap.get(faith));
    }
}