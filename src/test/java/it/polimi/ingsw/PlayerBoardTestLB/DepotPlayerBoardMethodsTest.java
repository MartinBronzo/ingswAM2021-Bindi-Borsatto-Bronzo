package it.polimi.ingsw.PlayerBoardTestLB;

import it.polimi.ingsw.PlayerBoard;
import it.polimi.ingsw.ResourceType;
import it.polimi.ingsw.exceptions.AlreadyInAnotherShelfException;
import it.polimi.ingsw.exceptions.NotEnoughResourcesException;
import it.polimi.ingsw.exceptions.NotEnoughSpaceException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.provider.EmptySource;

import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.*;

public class DepotPlayerBoardMethodsTest {
    PlayerBoard playerBoard;
    final ResourceType coin = ResourceType.COIN;
    final ResourceType shield = ResourceType.SHIELD;
    final ResourceType servant = ResourceType.SERVANT;
    final ResourceType stone = ResourceType.STONE;
    final ResourceType faithPoint = ResourceType.FAITHPOINT;

    @BeforeEach
    public void init(){
        playerBoard = new PlayerBoard();
    }

    @Test
    public void addToDepot() throws AlreadyInAnotherShelfException, NotEnoughSpaceException {
        playerBoard.addResourceToDepot(coin, 3, 3);
        assertEquals(playerBoard.getResourceFromDepot(coin), 3);
        assertEquals(playerBoard.getResourceTypeFromShelf(3), coin);
    }

    @Test
    public void addToDepotException(){
        Exception exception;
        exception = assertThrows(IllegalArgumentException.class, () -> playerBoard.addResourceToDepot(faithPoint, 1,2));
        assertEquals(exception.getMessage(), "Depot can't handle faith points");
    }

    @Test
    public void removeFromDepot() throws AlreadyInAnotherShelfException, NotEnoughSpaceException, NotEnoughResourcesException {
        playerBoard.addResourceToDepot(shield, 3, 3);
        playerBoard.removeResourceFromDepot(2, 3);
    }

    @Test
    public void removeFromDepotException() throws AlreadyInAnotherShelfException, NotEnoughSpaceException, NotEnoughResourcesException {
        Exception exception;
        playerBoard.addResourceToDepot(shield, 2, 3);
        exception = assertThrows(NotEnoughResourcesException.class, () -> playerBoard.removeResourceFromDepot(3, 3));
        assertEquals(exception.getMessage(), "Not enough resources to remove");
    }

    @Test
    public void addToStrongbox(){
        HashMap<ResourceType, Integer> resMap = new HashMap<>();
        resMap.put(coin, 3);
        resMap.put(shield, 10);

        playerBoard.addResourcesToStrongbox(resMap);
        assertEquals(playerBoard.getResourceFromStrongbox(coin), 3);
        assertEquals(playerBoard.getResourceFromStrongbox(shield), 10);
    }

    @Test
    public void addToStrongboxException(){
        Exception exception;
        HashMap<ResourceType, Integer> resMap = new HashMap<>();
        resMap.put(faithPoint, 3);
        resMap.put(shield, 10);

        exception = assertThrows(IllegalArgumentException.class, () -> playerBoard.addResourcesToStrongbox(resMap));
        assertEquals(exception.getMessage(), "Can't add Faith point");
    }

    @Test
    public void removeFromStrongbox() throws NotEnoughResourcesException {
        HashMap<ResourceType, Integer> addMap = new HashMap<>();
        HashMap<ResourceType, Integer> removeMap = new HashMap<>();

        addMap.put(servant, 50);
        removeMap.put(servant, 25);
        playerBoard.addResourcesToStrongbox(addMap);
        playerBoard.removeResourcesFromStrongbox(removeMap);
    }

    @Test
    public void removeFromStrongboxException(){
        Exception exception;
        HashMap<ResourceType, Integer> addMap = new HashMap<>();
        HashMap<ResourceType, Integer> removeMap = new HashMap<>();

        addMap.put(servant, 24);
        removeMap.put(servant, 25);
        playerBoard.addResourcesToStrongbox(addMap);
        exception = assertThrows(NotEnoughResourcesException.class, () -> playerBoard.removeResourcesFromStrongbox(removeMap));
        assertEquals(exception.getMessage(), "Not enough resources to remove");
    }

    @Test
    public void moveBetweenShelves() throws AlreadyInAnotherShelfException, NotEnoughSpaceException {
        playerBoard.addResourceToDepot(coin, 1, 1);
        playerBoard.addResourceToDepot(stone, 1, 3);

        playerBoard.moveBetweenShelves(1, 3);
        assertEquals(playerBoard.getResourceTypeFromShelf(3), coin);
        assertEquals(playerBoard.getResourceTypeFromShelf(1), stone);
        assertEquals(playerBoard.getResourceFromDepot(coin), 1);
        assertEquals(playerBoard.getResourceFromDepot(stone), 1);
    }

    @Test
    public void moveBetweenShelvesException1() throws AlreadyInAnotherShelfException, NotEnoughSpaceException {
        Exception exception;
        playerBoard.addResourceToDepot(coin, 1, 1);
        playerBoard.addResourceToDepot(stone, 2, 3);

        exception = assertThrows(NotEnoughSpaceException.class, () -> playerBoard.moveBetweenShelves(1, 3));
        assertEquals(exception.getMessage(), "Not enough space in source shelf");
    }

    @Test
    public void moveBetweenShelvesException2() throws AlreadyInAnotherShelfException, NotEnoughSpaceException {
        Exception exception;
        playerBoard.addResourceToDepot(stone, 2, 3);

        exception = assertThrows(NullPointerException.class, () -> playerBoard.moveBetweenShelves(2, 3));
        assertEquals(exception.getMessage(), "No source resource to move");
    }





































}
