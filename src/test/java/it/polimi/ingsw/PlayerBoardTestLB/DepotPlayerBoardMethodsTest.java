package it.polimi.ingsw.PlayerBoardTestLB;

import it.polimi.ingsw.DevCards.DevCardColour;
import it.polimi.ingsw.LeaderCard.LeaderCard;
import it.polimi.ingsw.LeaderCard.leaderEffects.Effect;
import it.polimi.ingsw.LeaderCard.leaderEffects.ExtraSlotLeaderEffect;
import it.polimi.ingsw.LeaderCardRequirementsTests.CardRequirementColor;
import it.polimi.ingsw.LeaderCardRequirementsTests.Requirement;
import it.polimi.ingsw.PlayerBoard;
import it.polimi.ingsw.ResourceType;
import it.polimi.ingsw.exceptions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class DepotPlayerBoardMethodsTest {
    PlayerBoard playerBoard;
    LeaderCard leaderCard;
    final ResourceType coin = ResourceType.COIN;
    final ResourceType shield = ResourceType.SHIELD;
    final ResourceType servant = ResourceType.SERVANT;
    final ResourceType stone = ResourceType.STONE;
    final ResourceType faithPoint = ResourceType.FAITHPOINT;

    @BeforeEach
    public void init() throws NegativeQuantityException {
        List<Requirement> requirementList = new ArrayList<>();
        requirementList.add(new CardRequirementColor(DevCardColour.BLUE, 0));
        Effect effect = new ExtraSlotLeaderEffect(coin, 2);
        leaderCard = new LeaderCard(2, requirementList, effect);
        playerBoard = new PlayerBoard();
    }

    @Test
    public void addToDepot() throws AlreadyInAnotherShelfException, NotEnoughSpaceException {
        playerBoard.addResourceToDepot(coin, 3, 3);
        assertEquals(playerBoard.getResourceFromDepot(coin), 3);
        assertEquals(playerBoard.getResourceTypeFromShelf(3), coin);
    }

    @Test
    public void addToDepotException() {
        Exception exception;
        exception = assertThrows(IllegalArgumentException.class, () -> playerBoard.addResourceToDepot(faithPoint, 1, 2));
        assertEquals(exception.getMessage(), "Depot can't handle faith points");
    }

    @Test
    public void removeFromDepot() throws AlreadyInAnotherShelfException, NotEnoughSpaceException, NotEnoughResourcesException {
        playerBoard.addResourceToDepot(shield, 3, 3);
        playerBoard.removeResourceFromDepot(2, 3);
    }

    @Test
    public void removeFromDepotException() throws AlreadyInAnotherShelfException, NotEnoughSpaceException {
        Exception exception;
        playerBoard.addResourceToDepot(shield, 2, 3);
        exception = assertThrows(NotEnoughResourcesException.class, () -> playerBoard.removeResourceFromDepot(3, 3));
        assertEquals(exception.getMessage(), "Not enough resources to remove");
    }

    @Test
    public void addToStrongbox() {
        HashMap<ResourceType, Integer> resMap = new HashMap<>();
        resMap.put(coin, 3);
        resMap.put(shield, 10);

        playerBoard.addResourcesToStrongbox(resMap);
        assertEquals(playerBoard.getResourceFromStrongbox(coin), 3);
        assertEquals(playerBoard.getResourceFromStrongbox(shield), 10);
    }

    @Test
    public void addToStrongboxException() {
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
    public void removeFromStrongboxException() {
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

        assertTrue(playerBoard.moveBetweenShelves(1, 3));
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

    @Test
    public void addToLeader() throws NoExtraSlotException, FullExtraSlotException, NotEnoughSpaceException {
        assertTrue(playerBoard.activateExtraLeaderCard(leaderCard));
        assertTrue(playerBoard.addResourceToLeader(coin, 2));
        assertEquals(playerBoard.getLeaderSlotResourceQuantity(coin),2);
        assertEquals(playerBoard.getLeaderSlotLimitQuantity(coin),2);
    }

    @Test
    public void removeFromLeader() throws FullExtraSlotException, NotEnoughSpaceException, NoExtraSlotException, NotEnoughResourcesException {
        playerBoard.activateExtraLeaderCard(leaderCard);
        playerBoard.addResourceToLeader(coin, 2);
        assertTrue(playerBoard.removeResourceFromLeader(coin, 1));
        assertEquals(playerBoard.getLeaderSlotLimitQuantity(coin), 2);
        assertEquals(playerBoard.getLeaderSlotResourceQuantity(coin), 1);
    }

    @Test
    public void moveToLeader() throws AlreadyInAnotherShelfException, NotEnoughSpaceException, FullExtraSlotException, NotEnoughResourcesException, NoExtraSlotException {
        playerBoard.activateExtraLeaderCard(leaderCard);
        playerBoard.addResourceToDepot(coin, 2,2);
        playerBoard.moveFromShelfToLeader(2,1);
        assertEquals(playerBoard.getResourceFromDepot(coin), 1);
        assertEquals(playerBoard.getLeaderSlotResourceQuantity(coin),1);
    }

    @Test
    public void moveToShelf() throws FullExtraSlotException, NotEnoughSpaceException, NoExtraSlotException, AlreadyInAnotherShelfException, NotEnoughResourcesException {
        playerBoard.activateExtraLeaderCard(leaderCard);
        playerBoard.addResourceToLeader(coin,2);
        assertTrue(playerBoard.moveFromLeaderToShelf(coin, 1,1));
        assertEquals(playerBoard.getLeaderSlotResourceQuantity(coin),1);
        assertEquals(playerBoard.getResourceFromDepot(coin), 1);
        assertEquals(playerBoard.getResourceTypeFromShelf(1), coin);
    }




}
