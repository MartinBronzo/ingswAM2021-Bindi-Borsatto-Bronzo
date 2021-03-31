package it.polimi.ingsw;

import it.polimi.ingsw.exceptions.IllegalActionException;
import it.polimi.ingsw.exceptions.IllegalParameterException;
import it.polimi.ingsw.exceptions.NegativeQuantityException;
import java.util.HashMap;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

public class StrongboxTest {
    final ResourceType coin = ResourceType.COIN;
    final ResourceType shield = ResourceType.SHIELD;
    final ResourceType servant = ResourceType.SERVANT;
    final ResourceType stone = ResourceType.STONE;
    final ResourceType faithPoint = ResourceType.FAITHPOINT;

    @Test
    public void addRes1() throws IllegalParameterException, IllegalActionException {
        HashMap<ResourceType, Integer> addMap = new HashMap<>();
        addMap.put(coin, 5);
        addMap.put(stone, 0);
        addMap.put(shield, 0);
        addMap.put(servant, 0);
        Strongbox strongbox = new Strongbox();
        assertTrue(strongbox.addResource(addMap));
        assertEquals(strongbox.getResource(coin),5);
        assertEquals(strongbox.getResource(stone),0);
        assertEquals(strongbox.getResource(shield),0);
        assertEquals(strongbox.getResource(servant),0);
    }

    @Test
    public void addRes2() throws IllegalParameterException, IllegalActionException {
        HashMap<ResourceType, Integer> addMap = new HashMap<>();
        addMap.put(coin, 2);
        addMap.put(stone, 0);
        addMap.put(shield, 1);
        addMap.put(servant, 100);
        Strongbox strongbox = new Strongbox();
        assertTrue(strongbox.addResource(addMap));
        assertEquals(strongbox.getResource(coin),2);
        assertEquals(strongbox.getResource(stone),0);
        assertEquals(strongbox.getResource(shield),1);
        assertEquals(strongbox.getResource(servant),100);
    }

    @Test
    public void multipleAdds() throws IllegalParameterException, IllegalActionException {
        HashMap<ResourceType, Integer> addMap = new HashMap<>();
        Strongbox strongbox = new Strongbox();

        addMap.put(coin, 2);
        strongbox.addResource(addMap);
        strongbox.addResource(addMap);

        assertEquals(strongbox.getResource(coin),4);
    }

    @Test
    public void addNegativeResource(){
        HashMap<ResourceType, Integer> addMap = new HashMap<>();
        addMap.put(coin, -3);
        Strongbox strongbox = new Strongbox();
        assertThrows(IllegalParameterException.class, () -> strongbox.addResource(addMap));
    }

    @Test
    public void addFaithPoint(){
        HashMap<ResourceType, Integer> addMap = new HashMap<>();
        addMap.put(faithPoint, 1);
        Strongbox strongbox = new Strongbox();
        assertThrows(IllegalActionException.class, () -> strongbox.addResource(addMap));
    }


    @Test
    public void removeResource() throws IllegalParameterException, IllegalActionException {
        HashMap<ResourceType, Integer> addMap = new HashMap<>();
        HashMap<ResourceType, Integer> removeMap = new HashMap<>();

        addMap.put(stone, 7);
        Strongbox strongbox = new Strongbox();
        strongbox.addResource(addMap);

        removeMap.put(stone, 3);
        strongbox.removeResource(removeMap);
    }

    @Test
    public void removeResources2() throws IllegalParameterException, IllegalActionException {
        HashMap<ResourceType, Integer> addMap = new HashMap<>();
        Strongbox strongbox = new Strongbox();

        addMap.put(coin, 5);
        addMap.put(stone, 0);
        addMap.put(shield, 2);
        addMap.put(servant, 20);
        strongbox.addResource(addMap);

        assertTrue(strongbox.removeResource(addMap));
        assertEquals(strongbox.getResource(coin),0);
        assertEquals(strongbox.getResource(stone),0);
        assertEquals(strongbox.getResource(shield),0);
        assertEquals(strongbox.getResource(servant),0);
    }

    @Test
    public void removeTooMuchResources(){
        Exception exception;
        HashMap<ResourceType, Integer> removeMap = new HashMap<>();
        Strongbox strongbox = new Strongbox();

        removeMap.put(servant, 1);
        exception = assertThrows(IllegalActionException.class, ()-> strongbox.removeResource(removeMap));
        assertEquals("Not enough resources to remove",exception.getMessage());
    }

    @Test
    public void removeNegativeResources(){
        Exception exception;
        HashMap<ResourceType, Integer> removeMap = new HashMap<>();
        Strongbox strongbox = new Strongbox();

        removeMap.put(stone, -5);
        exception = assertThrows(IllegalParameterException.class, () -> strongbox.removeResource(removeMap));
        assertEquals("Negative quantity",exception.getMessage());
    }
    @Test
    public void multipleRemoves() throws IllegalParameterException, IllegalActionException {
        HashMap<ResourceType, Integer> addMap = new HashMap<>();
        HashMap<ResourceType, Integer> removeMap = new HashMap<>();
        Strongbox strongbox = new Strongbox();

        addMap.put(stone, 5);
        strongbox.addResource(addMap);

        removeMap.put(stone, 1);
        strongbox.removeResource(removeMap);
        assertEquals(strongbox.getResource(stone),4);
        strongbox.removeResource(removeMap);
        assertEquals(strongbox.getResource(stone),3);
    }

    @Test
    public void removeFaithPoint(){
        HashMap<ResourceType, Integer> removeMap = new HashMap<>();
        removeMap.put(faithPoint, 1);
        Strongbox strongbox = new Strongbox();
        assertThrows(IllegalActionException.class, () -> strongbox.addResource(removeMap));
    }


}