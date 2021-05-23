package it.polimi.ingsw.model.PlayerBoardTest;

import it.polimi.ingsw.exceptions.EndOfGameException;
import it.polimi.ingsw.exceptions.IllegalActionException;
import it.polimi.ingsw.exceptions.LastVaticanReportException;
import it.polimi.ingsw.exceptions.NegativeQuantityException;
import it.polimi.ingsw.model.DevCards.DevCard;
import it.polimi.ingsw.model.DevCards.DevCardColour;
import it.polimi.ingsw.model.FaithTrack.FaithTrack;
import it.polimi.ingsw.model.FaithTrack.ReportNum;
import it.polimi.ingsw.model.FaithTrack.ReportNumOrder;
import it.polimi.ingsw.model.LeaderCard.LeaderCard;
import it.polimi.ingsw.model.LeaderCard.LeaderCardRequirements.Requirement;
import it.polimi.ingsw.model.LeaderCard.leaderEffects.Effect;
import it.polimi.ingsw.model.LeaderCard.leaderEffects.ExtraProductionLeaderEffect;
import it.polimi.ingsw.model.PlayerBoard;
import it.polimi.ingsw.model.ResourceType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class productionTest {
    PlayerBoard playerBoard;
    List<LeaderCard> leaderCards;
    List<Requirement> requirements;
    List<DevCard> devCards;
    LeaderCard leaderCard1;
    LeaderCard leaderCard2;
    LeaderCard leaderCard3;
    Effect effect;
    DevCard cardLevel1_1;
    DevCard cardLevel1_2;
    DevCard cardLevel1_3;
    DevCard cardLevel2_1;
    DevCard cardLevel2_2;
    DevCard cardLevel3_1;
    HashMap<ResourceType, Integer> hashMap;
    HashMap<ResourceType, Integer> resourcesInStrongbox;
    ReportNumOrder reportNumOrder;
    FaithTrack ft;

    @BeforeEach
    void setUp() throws NegativeQuantityException, EndOfGameException, IllegalActionException {
        requirements = new LinkedList<>();
        leaderCards = new LinkedList<>();
        effect = new ExtraProductionLeaderEffect(ResourceType.SERVANT, 1);
        leaderCard1 = new LeaderCard(2, requirements, effect);
        leaderCards.add(leaderCard1);
        effect = new ExtraProductionLeaderEffect(ResourceType.STONE, 1);
        leaderCard2 = new LeaderCard(3, requirements, effect);
        leaderCards.add(leaderCard2);
        effect = new ExtraProductionLeaderEffect(ResourceType.COIN, 1);
        leaderCard3 = new LeaderCard(4, requirements, effect);
        leaderCards.add(leaderCard3);
        playerBoard = new PlayerBoard(leaderCards);
        hashMap = new HashMap<>();
        hashMap.put(ResourceType.COIN, 2);
        hashMap.put(ResourceType.SERVANT, 2);
        cardLevel1_1 = new DevCard(1, DevCardColour.GREEN, 1, hashMap, hashMap, hashMap, "abc");
        cardLevel1_2 = new DevCard(1, DevCardColour.GREEN, 1, hashMap, hashMap, hashMap, "abc");
        cardLevel1_3 = new DevCard(1, DevCardColour.GREEN, 1, hashMap, hashMap, hashMap, "abc");
        cardLevel2_1 = new DevCard(2, DevCardColour.GREEN, 2, hashMap, hashMap, hashMap, "abc");
        cardLevel2_2 = new DevCard(2, DevCardColour.GREEN, 2, hashMap, hashMap, hashMap, "abc");
        cardLevel3_1 = new DevCard(3, DevCardColour.GREEN, 3, hashMap, hashMap, hashMap, "abc");
        playerBoard.addCardToDevSlot(0, cardLevel1_1);
        playerBoard.addCardToDevSlot(1, cardLevel1_2);
        playerBoard.addCardToDevSlot(1, cardLevel2_1);
        playerBoard.addCardToDevSlot(2, cardLevel1_3);
        playerBoard.addCardToDevSlot(2, cardLevel2_2);
        playerBoard.addCardToDevSlot(2, cardLevel3_1);
        devCards = new ArrayList<>();
        devCards.add(cardLevel1_1);
        devCards.add(cardLevel2_1);
        devCards.add(cardLevel3_1);
        playerBoard.activateLeaderCard(leaderCard1);
        playerBoard.activateLeaderCard(leaderCard2);
        resourcesInStrongbox = new HashMap<>();
        resourcesInStrongbox.put(ResourceType.COIN, 7);
        resourcesInStrongbox.put(ResourceType.SERVANT, 7);
        resourcesInStrongbox.put(ResourceType.STONE, 2);
        resourcesInStrongbox.put(ResourceType.SHIELD, 0);
        playerBoard.addResourcesToStrongbox(resourcesInStrongbox);
        FaithTrack.deleteState();
        reportNumOrder = ReportNumOrder.instance();
        reportNumOrder.addElementInOrder(ReportNum.REPORT1);
        reportNumOrder.addElementInOrder(ReportNum.REPORT2);
        reportNumOrder.addElementInOrder(ReportNum.REPORT3);
        ft = FaithTrack.instance(reportNumOrder);
        playerBoard.setPlayerFaithLevelFaithTrack(ft);

    }

    @Test
    void activateProduction() throws LastVaticanReportException {
        leaderCards = new LinkedList<>();
        leaderCards.add(leaderCard1);
        leaderCards.add(leaderCard2);
        HashMap<LeaderCard, ResourceType> leaderProductionMap = new HashMap<>();
        for (LeaderCard leadercard : leaderCards) {
            leaderProductionMap.put(leadercard, ResourceType.COIN);
        }
        HashMap<ResourceType, Integer> allResources = playerBoard.getAllResources();
        playerBoard.activateProduction(devCards, leaderProductionMap, false);
        HashMap<ResourceType, Integer> newAllResources = playerBoard.getAllResources();
        assertEquals(allResources.getOrDefault(ResourceType.COIN, 0) + 8, newAllResources.getOrDefault(ResourceType.COIN, 0));
        assertEquals(allResources.getOrDefault(ResourceType.SERVANT, 0) + 6, newAllResources.getOrDefault(ResourceType.SERVANT, 0));
        assertEquals(allResources.getOrDefault(ResourceType.SHIELD, 0), newAllResources.getOrDefault(ResourceType.SHIELD, 0));
        assertEquals(allResources.getOrDefault(ResourceType.STONE, 0), newAllResources.getOrDefault(ResourceType.STONE, 0));
        assertEquals(2, playerBoard.getPositionOnFaithTrack());
    }

    @Test
    void activateProductionWithBaseProduction() throws LastVaticanReportException {
        leaderCards = new LinkedList<>();
        leaderCards.add(leaderCard1);
        leaderCards.add(leaderCard2);
        HashMap<LeaderCard, ResourceType> leaderProductionMap = new HashMap<>();
        for (LeaderCard leadercard : leaderCards) {
            leaderProductionMap.put(leadercard, ResourceType.COIN);
        }
        List<ResourceType> input = new LinkedList<>();
        input.add(ResourceType.COIN);
        input.add(ResourceType.STONE);
        List<ResourceType> output = new LinkedList<>();
        output.add(ResourceType.SHIELD);
        playerBoard.setBaseProduction(input, output);

        HashMap<ResourceType, Integer> allResources = playerBoard.getAllResources();
        playerBoard.activateProduction(devCards, leaderProductionMap, true);
        HashMap<ResourceType, Integer> newAllResources = playerBoard.getAllResources();
        assertEquals(allResources.getOrDefault(ResourceType.COIN, 0) + 8, newAllResources.getOrDefault(ResourceType.COIN, 0));
        assertEquals(allResources.getOrDefault(ResourceType.SERVANT, 0) + 6, newAllResources.getOrDefault(ResourceType.SERVANT, 0));
        assertEquals(allResources.getOrDefault(ResourceType.SHIELD, 0) + 1, newAllResources.getOrDefault(ResourceType.SHIELD, 0));
        assertEquals(allResources.getOrDefault(ResourceType.STONE, 0), newAllResources.getOrDefault(ResourceType.STONE, 0));
        assertEquals(2, playerBoard.getPositionOnFaithTrack());
    }

    @Test
    void activateProductionNotActionableDevCard() throws NegativeQuantityException {
        leaderCards = new LinkedList<>();
        leaderCards.add(leaderCard1);
        leaderCards.add(leaderCard2);
        HashMap<ResourceType, Integer> cardMap = new HashMap<>();
        cardMap.put(ResourceType.COIN, 275);
        DevCard cardLevel3_2 = new DevCard(3, DevCardColour.GREEN, 3, cardMap, cardMap, cardMap, "abc");
        try {
            playerBoard.addCardToDevSlot(1, cardLevel3_2);
        } catch (EndOfGameException ignored) {
        }
        HashMap<LeaderCard, ResourceType> leaderProductionMap = new HashMap<>();
        for (LeaderCard leadercard : leaderCards) {
            leaderProductionMap.put(leadercard, ResourceType.COIN);
        }
        assertThrows(IllegalArgumentException.class, () -> playerBoard.activateProduction(devCards, leaderProductionMap, false));
    }

    @Test
    void activateProductionNotActionableLeaderCardCard() {
        leaderCards = new ArrayList<>();
        leaderCards.add(leaderCard3);
        HashMap<LeaderCard, ResourceType> leaderProductionMap = new HashMap<>();
        for (LeaderCard leadercard : leaderCards) {
            leaderProductionMap.put(leadercard, ResourceType.COIN);
        }
        assertThrows(IllegalArgumentException.class, () -> playerBoard.activateProduction(devCards, leaderProductionMap, false));
    }

    @Test
    void getProductionCost() throws IllegalActionException {
        leaderCards = new LinkedList<>();
        leaderCards.add(leaderCard1);
        leaderCards.add(leaderCard2);
        HashMap<ResourceType, Integer> costMap;
        costMap = playerBoard.getProductionCost(devCards, leaderCards, false);
        assertEquals(6, costMap.getOrDefault(ResourceType.COIN, 0));
        assertEquals(7, costMap.getOrDefault(ResourceType.SERVANT, 0));
        assertEquals(0, costMap.getOrDefault(ResourceType.SHIELD, 0));
        assertEquals(1, costMap.getOrDefault(ResourceType.STONE, 0));
    }

    @Test
    void getProductionCostWithBaseProduction() throws IllegalActionException {
        leaderCards = new LinkedList<>();
        leaderCards.add(leaderCard1);
        leaderCards.add(leaderCard2);
        List<ResourceType> input = new LinkedList<>();
        input.add(ResourceType.COIN);
        input.add(ResourceType.STONE);
        List<ResourceType> output = new LinkedList<>();
        output.add(ResourceType.SHIELD);
        playerBoard.setBaseProduction(input, output);
        HashMap<ResourceType, Integer> costMap;
        costMap = playerBoard.getProductionCost(devCards, leaderCards, true);
        assertEquals(7, costMap.getOrDefault(ResourceType.COIN, 0));
        assertEquals(7, costMap.getOrDefault(ResourceType.SERVANT, 0));
        assertEquals(0, costMap.getOrDefault(ResourceType.SHIELD, 0));
        assertEquals(2, costMap.getOrDefault(ResourceType.STONE, 0));
    }

    @Test
    void getProductionNotEnoughResources() throws NegativeQuantityException {
        leaderCards = new LinkedList<>();
        leaderCards.add(leaderCard1);
        leaderCards.add(leaderCard2);
        HashMap<ResourceType, Integer> cardMap = new HashMap<>();
        cardMap.put(ResourceType.COIN, 275);
        DevCard cardLevel3_2 = new DevCard(3, DevCardColour.GREEN, 3, cardMap, cardMap, cardMap, "abc");
        try {
            playerBoard.addCardToDevSlot(1, cardLevel3_2);
        } catch (EndOfGameException e) {
        }
        devCards = new ArrayList<>();
        devCards.add(cardLevel3_2);
        assertThrows(IllegalActionException.class, () -> playerBoard.getProductionCost(devCards, leaderCards, false));
    }

    @Test
    void getProductionNotActionableDevCard() throws NegativeQuantityException {
        leaderCards = new LinkedList<>();
        leaderCards.add(leaderCard1);
        leaderCards.add(leaderCard2);
        HashMap<ResourceType, Integer> cardMap = new HashMap<>();
        cardMap.put(ResourceType.COIN, 275);
        DevCard cardLevel3_2 = new DevCard(3, DevCardColour.GREEN, 3, cardMap, cardMap, cardMap, "abc");
        try {
            playerBoard.addCardToDevSlot(1, cardLevel3_2);
        } catch (EndOfGameException e) {
        }
        assertThrows(IllegalArgumentException.class, () -> playerBoard.getProductionCost(devCards, leaderCards, false));
    }

    @Test
    void getProductionNotActionableLeaderCard() {
        leaderCards = new ArrayList<>();
        leaderCards.add(leaderCard3);
        assertThrows(IllegalArgumentException.class, () -> playerBoard.getProductionCost(devCards, leaderCards, false));
    }


}
