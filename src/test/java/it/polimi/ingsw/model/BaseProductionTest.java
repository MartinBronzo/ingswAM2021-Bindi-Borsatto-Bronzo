package it.polimi.ingsw.model;

import it.polimi.ingsw.model.resources.ResourceType;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class BaseProductionTest {
    private BaseProduction baseProduction;
    private List<ResourceType> inputResources;
    private List<ResourceType> outputResources;
    private BaseProduction baseProduction2;
    private List<ResourceType> inputResources2;
    private List<ResourceType> outputResources2;

    @BeforeEach
    void setUp() {
        baseProduction = new BaseProduction();
        inputResources = new LinkedList<>();
        inputResources.add(ResourceType.STONE);
        inputResources.add(ResourceType.COIN);
        outputResources = new LinkedList<>();
        outputResources.add(ResourceType.SHIELD);
        baseProduction.setBaseProduction(inputResources, outputResources);
    }

    @BeforeAll
    void setUp2() {
        inputResources2 = new LinkedList<>();
        inputResources2.add(ResourceType.STONE);
        inputResources2.add(ResourceType.COIN);
        outputResources2 = new LinkedList<>();
        outputResources2.add(ResourceType.SERVANT);
        outputResources2.add(ResourceType.SHIELD);
        baseProduction2 = new BaseProduction(inputResources2, outputResources2, 3, 3);
    }

    @Test
    void setInputHashMapOnProduction2() {
        inputResources = new LinkedList<>();
        inputResources.add(ResourceType.STONE);
        assertThrows(IllegalArgumentException.class, () -> baseProduction2.setInputHashMap(inputResources));
        inputResources.add(ResourceType.COIN);
        inputResources.add(ResourceType.COIN);
        baseProduction2.setInputHashMap(inputResources);
    }

    @Test
    void setOutputHashMapOnProduction2() {
        outputResources = new LinkedList<>();
        outputResources.add(ResourceType.SERVANT);
        outputResources.add(ResourceType.SERVANT);
        outputResources.add(ResourceType.SERVANT);
        assertThrows(IllegalArgumentException.class, () -> baseProduction2.setOutputHashMap(outputResources));
        outputResources.remove(ResourceType.SERVANT);
        outputResources.add(ResourceType.SHIELD);
        baseProduction2.setOutputHashMap(outputResources);
    }

    @Test
    void setInputHashMapTest1() {
        inputResources = new LinkedList<>();
        inputResources.add(ResourceType.STONE);
        inputResources.add(ResourceType.FAITHPOINT);
        assertThrows(IllegalArgumentException.class, () -> baseProduction.setInputHashMap(inputResources));
    }

    @Test
    void setInputHashMapTest2() {
        inputResources = new LinkedList<>();
        inputResources.add(ResourceType.STONE);
        assertThrows(IllegalArgumentException.class, () -> baseProduction.setInputHashMap(inputResources));
    }

    @Test
    void setOutputHashMapTest1() {
        outputResources = new LinkedList<>();
        outputResources.add(ResourceType.FAITHPOINT);
        assertThrows(IllegalArgumentException.class, () -> baseProduction.setOutputHashMap(outputResources));
    }

    @Test
    void setOutputHashMapTest2() {
        outputResources = new LinkedList<>();
        outputResources.add(ResourceType.STONE);
        outputResources.add(ResourceType.COIN);
        assertThrows(IllegalArgumentException.class, () -> baseProduction.setOutputHashMap(outputResources));
    }

    @Test
    void getQuantityInputResources() {
        assertEquals(2, baseProduction.getQuantityInputResources());
    }

    @Test
    void getQuantityOutputResources() {
        assertEquals(1, baseProduction.getQuantityOutputResources());
    }

    @Test
    void getInputForcedResources() {
        assertEquals(0, baseProduction.getInputForcedResources().size());
        assertEquals(0, baseProduction.getOutputForcedResources().size());
    }

    @Test
    void getInputHashMap() {
        HashMap<ResourceType, Integer> inputMap = baseProduction.getInputHashMap();
        assertEquals(1, inputMap.getOrDefault(ResourceType.STONE, 0));
        assertEquals(1, inputMap.getOrDefault(ResourceType.COIN, 0));
        assertEquals(0, inputMap.getOrDefault(ResourceType.SHIELD, 0));
    }

    @Test
    void getOutputHashMap() {
        HashMap<ResourceType, Integer> outputMap = baseProduction.getOutputHashMap();
        assertEquals(0, outputMap.getOrDefault(ResourceType.STONE, 0));
        assertEquals(0, outputMap.getOrDefault(ResourceType.COIN, 0));
        assertEquals(1, outputMap.getOrDefault(ResourceType.SHIELD, 0));
    }

}