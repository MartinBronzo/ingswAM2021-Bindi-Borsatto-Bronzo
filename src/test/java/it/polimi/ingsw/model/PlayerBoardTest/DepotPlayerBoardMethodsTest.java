package it.polimi.ingsw.model.PlayerBoardTest;

import it.polimi.ingsw.exceptions.*;
import it.polimi.ingsw.model.devCards.DevCardColour;
import it.polimi.ingsw.model.leaderCard.LeaderCard;
import it.polimi.ingsw.model.leaderCard.LeaderCardRequirements.CardRequirementColor;
import it.polimi.ingsw.model.leaderCard.LeaderCardRequirements.Requirement;
import it.polimi.ingsw.model.leaderCard.leaderEffects.Effect;
import it.polimi.ingsw.model.leaderCard.leaderEffects.ExtraSlotLeaderEffect;
import it.polimi.ingsw.model.board.PlayerBoard;
import it.polimi.ingsw.model.resources.ResourceType;
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
        List<LeaderCard> leaderCards = new ArrayList<>();
        leaderCards.add(leaderCard);
        playerBoard = new PlayerBoard(leaderCards);
    }

    @Test
    public void addToDepot() throws IllegalActionException {
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
    public void removeFromDepot() throws NotEnoughResourcesException, IllegalActionException {
        playerBoard.addResourceToDepot(shield, 3, 3);
        playerBoard.removeResourceFromDepot(2, 3);
    }

    @Test
    public void removeFromDepotException() throws IllegalActionException {
        Exception exception;
        playerBoard.addResourceToDepot(shield, 2, 3);
        exception = assertThrows(IllegalActionException.class, () -> playerBoard.removeResourceFromDepot(3, 3));
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
    public void removeFromStrongbox() throws IllegalActionException {
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
        exception = assertThrows(IllegalActionException.class, () -> playerBoard.removeResourcesFromStrongbox(removeMap));
        assertEquals(exception.getMessage(), "Not enough resources to remove");
    }

    @Test
    public void moveBetweenShelves() throws NotEnoughSpaceException, IllegalActionException {
        playerBoard.addResourceToDepot(coin, 1, 1);
        playerBoard.addResourceToDepot(stone, 1, 3);

        assertTrue(playerBoard.moveBetweenShelves(1, 3));
        assertEquals(playerBoard.getResourceTypeFromShelf(3), coin);
        assertEquals(playerBoard.getResourceTypeFromShelf(1), stone);
        assertEquals(playerBoard.getResourceFromDepot(coin), 1);
        assertEquals(playerBoard.getResourceFromDepot(stone), 1);
    }

    @Test
    public void moveBetweenShelvesException1() throws IllegalActionException {
        Exception exception;
        playerBoard.addResourceToDepot(coin, 1, 1);
        playerBoard.addResourceToDepot(stone, 2, 3);

        exception = assertThrows(IllegalActionException.class, () -> playerBoard.moveBetweenShelves(1, 3));
        assertEquals(exception.getMessage(), "Not enough space in source shelf");
    }

    @Test
    public void moveBetweenShelvesException2() throws IllegalActionException {
        Exception exception;
        playerBoard.addResourceToDepot(stone, 2, 3);

        exception = assertThrows(NullPointerException.class, () -> playerBoard.moveBetweenShelves(2, 3));
        assertEquals(exception.getMessage(), "No source resource to move");
    }

    @Test
    public void addToLeader() throws FullExtraSlotException, UnmetRequirementException, IllegalActionException {
        assertTrue(playerBoard.activateLeaderCard(leaderCard));
        assertTrue(playerBoard.addResourceToLeader(coin, 2));
        assertEquals(playerBoard.getLeaderSlotResourceQuantity(coin), 2);
        assertEquals(playerBoard.getLeaderSlotLimitQuantity(coin), 2);
    }

    @Test
    public void removeFromLeader() throws FullExtraSlotException, NoExtraSlotException, NotEnoughResourcesException, UnmetRequirementException, IllegalActionException {
        playerBoard.activateLeaderCard(leaderCard);
        playerBoard.addResourceToLeader(coin, 2);
        assertTrue(playerBoard.removeResourceFromLeader(coin, 1));
        assertEquals(playerBoard.getLeaderSlotLimitQuantity(coin), 2);
        assertEquals(playerBoard.getLeaderSlotResourceQuantity(coin), 1);
    }

    @Test
    public void moveToLeader() throws NotEnoughSpaceException, FullExtraSlotException, NotEnoughResourcesException, NoExtraSlotException, UnmetRequirementException, IllegalActionException {
        playerBoard.activateLeaderCard(leaderCard);
        playerBoard.addResourceToDepot(coin, 2, 2);
        playerBoard.moveFromShelfToLeader(2, 1);
        assertEquals(playerBoard.getResourceFromDepot(coin), 1);
        assertEquals(playerBoard.getLeaderSlotResourceQuantity(coin), 1);
    }

    @Test
    public void moveToShelf() throws FullExtraSlotException, NotEnoughSpaceException, NoExtraSlotException, AlreadyInAnotherShelfException, NotEnoughResourcesException, UnmetRequirementException, IllegalActionException {
        playerBoard.activateLeaderCard(leaderCard);
        playerBoard.addResourceToLeader(coin, 2);
        assertTrue(playerBoard.moveFromLeaderToShelf(coin, 1, 1));
        assertEquals(playerBoard.getLeaderSlotResourceQuantity(coin), 1);
        assertEquals(playerBoard.getResourceFromDepot(coin), 1);
        assertEquals(playerBoard.getResourceTypeFromShelf(1), coin);
    }


}
