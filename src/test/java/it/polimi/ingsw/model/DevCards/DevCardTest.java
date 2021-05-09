package it.polimi.ingsw.model.DevCards;

import it.polimi.ingsw.exceptions.NegativeQuantityException;
import it.polimi.ingsw.model.ResourceType;
import org.junit.jupiter.api.Test;

import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.*;


/**
 * DevCard is a simple immutable class, we tested its exceptions and if it is effectively immutable
 */
class DevCardTest {

    DevCard card;

    DevCardTest() throws IllegalArgumentException, NegativeQuantityException {
        HashMap<ResourceType, Integer> map = new HashMap<>();
        map.put(ResourceType.COIN, 3);
        card = new DevCard(1, DevCardColour.PURPLE, 5, map, map, map, "abc");
    }

    /*
    @Test
    public void ctrlEqualsTrue() throws NegativeQuantityException {
        DevCard d1 = new DevCard(2, DevCardColour.GREEN, 2, new HashMap<>(), new HashMap<>(), new HashMap<>(), "abc");
        DevCard d2 = new DevCard(2, DevCardColour.GREEN, 2, new HashMap<>(), new HashMap<>(), new HashMap<>(), "abc");

        assertNotSame(d1, d2);
        assertEquals(d1, d2);
    }*/

    @Test
    public void ctrlEqualsFake() throws NegativeQuantityException {
        DevCard d1 = new DevCard(2, DevCardColour.GREEN, 2, new HashMap<>(), new HashMap<>(), new HashMap<>(), "abc");
        DevCard d2 = new DevCard(2, DevCardColour.GREEN, 3, new HashMap<>(), new HashMap<>(), new HashMap<>(), "abc");

        assertNotSame(d1, d2);
        assertNotEquals(d1, d2);
    }

    @Test
    void IllegalParametersTest() {
        assertThrows(IllegalArgumentException.class, () -> new DevCard(4, DevCardColour.GREEN, 2, new HashMap<>(), new HashMap<>(), new HashMap<>(), "abc"));
        assertThrows(IllegalArgumentException.class, () -> new DevCard(3, DevCardColour.PURPLE, -3, new HashMap<>(), new HashMap<>(), new HashMap<>(), "abc"));
        assertDoesNotThrow(() -> new DevCard(2, DevCardColour.BLUE, 2, new HashMap<>(), new HashMap<>(), new HashMap<>(), "abc"));
    }

    @Test
    void NegativeQuantityTest() {

        HashMap<ResourceType, Integer> wrongHashMap = new HashMap<>();
        wrongHashMap.put(ResourceType.SERVANT, -3);
        assertThrows(NegativeQuantityException.class, () -> new DevCard(2, DevCardColour.GREEN, 2, wrongHashMap, new HashMap<>(), new HashMap<>(), "abc"));
        assertThrows(NegativeQuantityException.class, () -> new DevCard(3, DevCardColour.PURPLE, 2, new HashMap<>(), wrongHashMap, new HashMap<>(), "abc"));
        assertThrows(NegativeQuantityException.class, () -> new DevCard(3, DevCardColour.PURPLE, 2, new HashMap<>(), new HashMap<>(), wrongHashMap, "abc"));
    }


    @Test
    void getProductionInput() {
        HashMap<ResourceType, Integer> map1 = card.getProductionInput();
        map1.put(ResourceType.COIN, 1);
        map1.put(ResourceType.SERVANT, -1);
        HashMap<ResourceType, Integer> map2 = card.getProductionInput();
        assertEquals(3, map2.get(ResourceType.COIN));
        assertEquals(0, map2.getOrDefault(ResourceType.SERVANT, 0));
    }

    @Test
    void getProductionOutput() {
        HashMap<ResourceType, Integer> map1 = card.getProductionOutput();
        map1.put(ResourceType.COIN, 1);
        map1.put(ResourceType.SERVANT, -1);
        HashMap<ResourceType, Integer> map2 = card.getProductionOutput();
        assertEquals(3, map2.get(ResourceType.COIN));
        assertEquals(0, map2.getOrDefault(ResourceType.SERVANT, 0));
    }

    @Test
    void getCost() {
        HashMap<ResourceType, Integer> map1 = card.getCost();
        map1.put(ResourceType.COIN, 1);
        map1.put(ResourceType.SERVANT, -1);
        HashMap<ResourceType, Integer> map2 = card.getCost();
        assertEquals(3, map2.get(ResourceType.COIN));
        assertEquals(0, map2.getOrDefault(ResourceType.SERVANT, 0));
    }


}