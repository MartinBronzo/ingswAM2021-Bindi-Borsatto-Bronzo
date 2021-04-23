package it.polimi.ingsw.marble;

import it.polimi.ingsw.LeaderCard.leaderEffects.Effect;
import it.polimi.ingsw.LeaderCard.leaderEffects.WhiteMarbleLeaderEffect;
import it.polimi.ingsw.ResourceType;
import it.polimi.ingsw.exceptions.NegativeQuantityException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class WhiteMarbleTest {
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
        effect = new WhiteMarbleLeaderEffect(ResourceType.COIN);
        effects = new LinkedList<>();
        effects.add(effect);
        marble = new WhiteMarble();
    }

    @Test
    void onActivateLegalTest() throws NegativeQuantityException {
        for (Integer c=0; c<100; c++){
            assertEquals(c,resourceMap.getOrDefault(coin,0));
            marble.onActivate(resourceMap, effects);
            effects.add(effect);
            assertEquals(1, effects.size());
        }
        assertEquals(100,resourceMap.get(coin));
        assertNull(resourceMap.get(shield));
        assertNull(resourceMap.get(servant));
        assertNull(resourceMap.get(stone));
        assertNull(resourceMap.get(faith));
    }

    @Test
    void onActivateIllegalTest() throws NegativeQuantityException {

        marble.onActivate(resourceMap, effects);
        assertEquals(1,resourceMap.get(coin));
        resourceMap.put(coin, -3);
        effects.add(effect);
        assertThrows(NegativeQuantityException.class, () -> marble.onActivate(resourceMap, effects));
        assertNull(resourceMap.get(shield));
        assertNull(resourceMap.get(servant));
        assertNull(resourceMap.get(stone));
        assertNull(resourceMap.get(faith));
    }
}