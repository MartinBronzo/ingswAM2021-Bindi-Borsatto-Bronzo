package it.polimi.ingsw.model.LeaderCardTest;

import it.polimi.ingsw.exceptions.NegativeQuantityException;
import it.polimi.ingsw.exceptions.UnmetRequirementException;
import it.polimi.ingsw.model.devCards.DevCard;
import it.polimi.ingsw.model.devCards.DevCardColour;
import it.polimi.ingsw.model.leaderCard.LeaderCard;
import it.polimi.ingsw.model.leaderCard.LeaderCardRequirements.CardRequirementColor;
import it.polimi.ingsw.model.leaderCard.LeaderCardRequirements.CardRequirementColorAndLevel;
import it.polimi.ingsw.model.leaderCard.LeaderCardRequirements.CardRequirementResource;
import it.polimi.ingsw.model.leaderCard.LeaderCardRequirements.Requirement;
import it.polimi.ingsw.model.leaderCard.leaderEffects.Effect;
import it.polimi.ingsw.model.leaderCard.leaderEffects.ExtraProductionLeaderEffect;
import it.polimi.ingsw.model.leaderCard.leaderEffects.WhiteMarbleLeaderEffect;
import it.polimi.ingsw.model.board.PlayerResourcesAndCards;
import it.polimi.ingsw.model.resources.ResourceType;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

public class LeaderCardTest {
    private static final ResourceType[] resources = new ResourceType[]{ResourceType.COIN, ResourceType.STONE, ResourceType.SERVANT, ResourceType.FAITHPOINT, ResourceType.SHIELD};

    @Test
    //Tests correct creation of the LeaderCard (the default creation): bad parameters
    public void ctrlLeaderCardCreationBadParameters() throws NegativeQuantityException {
        List<Requirement> requirements = new ArrayList<>();

        requirements.add(new CardRequirementColor(DevCardColour.BLUE, 3));
        requirements.add(new CardRequirementColorAndLevel(2, DevCardColour.GREEN, 2));

        Effect effect = new WhiteMarbleLeaderEffect(ResourceType.SERVANT);

        NullPointerException exception = assertThrows(NullPointerException.class, () -> new LeaderCard(2, null, effect));
        assertEquals(exception.getMessage(), "The requirement list can't be a null pointer!");
        exception = assertThrows(NullPointerException.class, () -> new LeaderCard(2, null, null));
        assertTrue(exception.getMessage().equals("The requirement list can't be a null pointer!") || exception.getMessage().equals("The effect can't be a null pointer!"));
        exception = assertThrows(NullPointerException.class, () -> new LeaderCard(2, requirements, null));
        assertEquals(exception.getMessage(), "The effect can't be a null pointer!");

    }

    @Test
    //Tests correct creation of the LeaderCard (the default creation): good parameters
    public void ctrlLeaderCardCreationGoodParameters() throws NegativeQuantityException {
        List<Requirement> requirements = new ArrayList<>();

        requirements.add(new CardRequirementColor(DevCardColour.BLUE, 3));
        requirements.add(new CardRequirementColorAndLevel(2, DevCardColour.GREEN, 2));

        Effect effect = new WhiteMarbleLeaderEffect(ResourceType.SERVANT);

        LeaderCard leaderCard = new LeaderCard(3, requirements, effect);

        List<Requirement> tmp = leaderCard.getRequirementsListSafe();

        assertNotSame(requirements, tmp);
        assertTrue(requirements.containsAll(tmp));
        assertTrue(tmp.containsAll(requirements));

        assertEquals(leaderCard.getVictoryPoints(), 3);

        assertEquals(leaderCard.getEffect(), effect);

        HashMap<ResourceType, Integer> output = leaderCard.getOutputWhenDiscarded();
        assertFalse(output.isEmpty());
        assertEquals(output.size(), 1);
        for (ResourceType rT : resources)
            if (rT.equals(ResourceType.FAITHPOINT))
                assertEquals(output.get(rT), 1);
            else
                assertEquals(output.get(rT), null);
    }

    @Test
    //Tests if the LeaderCard correctly checks for its requirements: all requirements met
    public void ctrlCardRequirementsAllMet() throws NegativeQuantityException {
        CardRequirementColor requirement1;
        CardRequirementColor requirement2;
        PlayerResourcesAndCards playerResourcesAndCards;
        HashMap<ResourceType, Integer> hashMap;
        DevCard cardLevel1;
        DevCard cardLevel2;
        DevCard cardLevel3;
        List<DevCard> devCards;
        requirement1 = new CardRequirementColor(DevCardColour.GREEN, 2); //Met
        requirement2 = new CardRequirementColor(DevCardColour.BLUE, 1); //Unmet
        hashMap = new HashMap<>();
        cardLevel1 = new DevCard(1, DevCardColour.GREEN, 1, hashMap, hashMap, hashMap, "abc");
        cardLevel2 = new DevCard(2, DevCardColour.GREEN, 2, hashMap, hashMap, hashMap, "abc");
        cardLevel3 = new DevCard(3, DevCardColour.GREEN, 3, hashMap, hashMap, hashMap, "abc");
        devCards = new ArrayList<>();
        devCards.add(cardLevel1);
        devCards.add(cardLevel1);
        devCards.add(cardLevel2);
        devCards.add(cardLevel1);
        devCards.add(cardLevel2);
        devCards.add(cardLevel3);
        playerResourcesAndCards = new PlayerResourcesAndCards(new HashMap<>(), devCards);

        CardRequirementColorAndLevel req1;
        CardRequirementColorAndLevel req2;
        CardRequirementColorAndLevel req3;
        req1 = new CardRequirementColorAndLevel(3, DevCardColour.GREEN, 2); //Unmet
        req2 = new CardRequirementColorAndLevel(1, DevCardColour.BLUE, 1); //Unmet
        req3 = new CardRequirementColorAndLevel(2, DevCardColour.GREEN, 2); //Met

        Effect effect = new WhiteMarbleLeaderEffect(ResourceType.SERVANT);
        LeaderCard leaderCard;
        List<Requirement> requirements = new ArrayList<>();

        //All the requirements are met
        requirements.add(requirement1);
        requirements.add(req3);
        leaderCard = new LeaderCard(3, requirements, effect);
        try {
            assertTrue(leaderCard.checkRequirements(playerResourcesAndCards));
        } catch (UnmetRequirementException e) {
            e.printStackTrace();
        }

        //One requirement is unmet
        requirements.add(req1);
        Collections.shuffle(requirements);
        leaderCard = new LeaderCard(3, requirements, effect);
        LeaderCard finalLeaderCard = leaderCard;
        UnmetRequirementException exception = assertThrows(UnmetRequirementException.class, () -> finalLeaderCard.checkRequirements(playerResourcesAndCards));
        //assertEquals(exception.getUnmetRequirement(), req1);
        CardRequirementColorAndLevel unmetReq = (CardRequirementColorAndLevel) exception.getUnmetRequirement();
        assertEquals(unmetReq.getLevel(), req1.getLevel());
        assertEquals(unmetReq.getCardColour(), req1.getCardColour());
        assertEquals(unmetReq.getQuantity(), req1.getQuantity());

        //Two requirements are unmet
        requirements.add(requirement2);
        Collections.shuffle(requirements);
        leaderCard = new LeaderCard(3, requirements, effect);
        LeaderCard finalLeaderCard1 = leaderCard;
        exception = assertThrows(UnmetRequirementException.class, () -> finalLeaderCard1.checkRequirements(playerResourcesAndCards));
        Requirement tmp = exception.getUnmetRequirement();
        if (tmp instanceof CardRequirementColorAndLevel) {
            assertEquals(((CardRequirementColorAndLevel) tmp).getLevel(), req1.getLevel());
            assertEquals(((CardRequirementColorAndLevel) tmp).getCardColour(), req1.getCardColour());
            assertEquals(((CardRequirementColorAndLevel) tmp).getQuantity(), req1.getQuantity());
        } else {
            assertEquals(((CardRequirementColor) tmp).getQuantity(), requirement2.getQuantity());
            assertEquals(((CardRequirementColor) tmp).getCardColour(), requirement2.getCardColour());
        }

        //All the requirements are unmet
        requirements = new ArrayList<>();
        requirements.add(requirement2);
        requirements.add(req1);
        requirements.add(req2);
        Collections.shuffle(requirements);
        leaderCard = new LeaderCard(3, requirements, effect);
        LeaderCard finalLeaderCard2 = leaderCard;
        exception = assertThrows(UnmetRequirementException.class, () -> finalLeaderCard2.checkRequirements(playerResourcesAndCards));
        //assertTrue(exception.getUnmetRequirement().equals(req1) || exception.getUnmetRequirement().equals(requirement2) || exception.getUnmetRequirement().equals(req2));
        tmp = exception.getUnmetRequirement();
        if (tmp instanceof CardRequirementColorAndLevel) {
            if (((CardRequirementColorAndLevel) tmp).getLevel() == 3) {
                assertEquals(((CardRequirementColorAndLevel) tmp).getLevel(), req1.getLevel());
                assertEquals(((CardRequirementColorAndLevel) tmp).getCardColour(), req1.getCardColour());
                assertEquals(((CardRequirementColorAndLevel) tmp).getQuantity(), req1.getQuantity());
            } else {
                assertEquals(((CardRequirementColorAndLevel) tmp).getLevel(), req2.getLevel());
                assertEquals(((CardRequirementColorAndLevel) tmp).getCardColour(), req2.getCardColour());
                assertEquals(((CardRequirementColorAndLevel) tmp).getQuantity(), req2.getQuantity());
            }

        } else {
            assertEquals(((CardRequirementColor) tmp).getQuantity(), requirement2.getQuantity());
            assertEquals(((CardRequirementColor) tmp).getCardColour(), requirement2.getCardColour());
        }
    }

    @Test
    //Tests if the LeaderCard correctly checks for its requirements: all unmet
    public void ctrlCardRequirementsAllUnmet() throws NegativeQuantityException {
        CardRequirementColor requirement1;
        CardRequirementColor requirement2;
        PlayerResourcesAndCards playerResourcesAndCards;
        HashMap<ResourceType, Integer> hashMap;
        DevCard cardLevel1;
        DevCard cardLevel2;
        DevCard cardLevel3;
        List<DevCard> devCards;
        requirement2 = new CardRequirementColor(DevCardColour.BLUE, 1); //Unmet
        hashMap = new HashMap<>();
        cardLevel1 = new DevCard(1, DevCardColour.GREEN, 1, hashMap, hashMap, hashMap, "abc");
        cardLevel2 = new DevCard(2, DevCardColour.GREEN, 2, hashMap, hashMap, hashMap, "abc");
        cardLevel3 = new DevCard(3, DevCardColour.GREEN, 3, hashMap, hashMap, hashMap, "abc");
        devCards = new ArrayList<>();
        devCards.add(cardLevel1);
        devCards.add(cardLevel1);
        devCards.add(cardLevel2);
        devCards.add(cardLevel1);
        devCards.add(cardLevel2);
        devCards.add(cardLevel3);
        playerResourcesAndCards = new PlayerResourcesAndCards(new HashMap<>(), devCards);

        CardRequirementColorAndLevel req1;
        CardRequirementColorAndLevel req2;
        CardRequirementColorAndLevel req3;
        req1 = new CardRequirementColorAndLevel(3, DevCardColour.GREEN, 2); //Unmet
        req2 = new CardRequirementColorAndLevel(1, DevCardColour.BLUE, 1); //Unmet

        Effect effect = new WhiteMarbleLeaderEffect(ResourceType.SERVANT);
        LeaderCard leaderCard;

        //All the requirements are unmet
        List<Requirement> requirements = new ArrayList<>();
        requirements.add(requirement2);
        requirements.add(req1);
        requirements.add(req2);
        Collections.shuffle(requirements);
        leaderCard = new LeaderCard(3, requirements, effect);
        LeaderCard finalLeaderCard1 = leaderCard;
        UnmetRequirementException exception = assertThrows(UnmetRequirementException.class, () -> finalLeaderCard1.checkRequirements(playerResourcesAndCards));
        assertTrue(exception.getUnmetRequirement().equals(req1) || exception.getUnmetRequirement().equals(requirement2) || exception.getUnmetRequirement().equals(req2));
    }

    @Test
    //Tests if the LeaderCard correctly checks for its requirements: Two unmet
    public void ctrlCardRequirementsTwoUnmet() throws NegativeQuantityException {
        CardRequirementColor requirement1;
        CardRequirementColor requirement2;
        PlayerResourcesAndCards playerResourcesAndCards;
        HashMap<ResourceType, Integer> hashMap;
        DevCard cardLevel1;
        DevCard cardLevel2;
        DevCard cardLevel3;
        List<DevCard> devCards;
        requirement1 = new CardRequirementColor(DevCardColour.GREEN, 2); //Met
        requirement2 = new CardRequirementColor(DevCardColour.BLUE, 1); //Unmet
        hashMap = new HashMap<>();
        cardLevel1 = new DevCard(1, DevCardColour.GREEN, 1, hashMap, hashMap, hashMap, "abc");
        cardLevel2 = new DevCard(2, DevCardColour.GREEN, 2, hashMap, hashMap, hashMap, "abc");
        cardLevel3 = new DevCard(3, DevCardColour.GREEN, 3, hashMap, hashMap, hashMap, "abc");
        devCards = new ArrayList<>();
        devCards.add(cardLevel1);
        devCards.add(cardLevel1);
        devCards.add(cardLevel2);
        devCards.add(cardLevel1);
        devCards.add(cardLevel2);
        devCards.add(cardLevel3);
        playerResourcesAndCards = new PlayerResourcesAndCards(new HashMap<>(), devCards);

        CardRequirementColorAndLevel req1;
        CardRequirementColorAndLevel req2;
        CardRequirementColorAndLevel req3;
        req1 = new CardRequirementColorAndLevel(3, DevCardColour.GREEN, 2); //Unmet
        req3 = new CardRequirementColorAndLevel(2, DevCardColour.GREEN, 2); //Met

        Effect effect = new WhiteMarbleLeaderEffect(ResourceType.SERVANT);
        LeaderCard leaderCard;
        List<Requirement> requirements = new ArrayList<>();

        //Two requirements are unmet
        requirements.add(requirement1);
        requirements.add(req3);
        requirements.add(req1);
        requirements.add(requirement2);
        Collections.shuffle(requirements);
        leaderCard = new LeaderCard(3, requirements, effect);
        LeaderCard finalLeaderCard1 = leaderCard;
        UnmetRequirementException exception = assertThrows(UnmetRequirementException.class, () -> finalLeaderCard1.checkRequirements(playerResourcesAndCards));
        Requirement tmp = exception.getUnmetRequirement();

        assertTrue(tmp.equals(req1) || tmp.equals(requirement2));
    }

    @Test
    //Tests if the LeaderCard correctly checks for its requirements: one unmet
    public void ctrlCardRequirementsOneUnmet() throws NegativeQuantityException {
        CardRequirementColor requirement1;
        CardRequirementColor requirement2;
        PlayerResourcesAndCards playerResourcesAndCards;
        HashMap<ResourceType, Integer> hashMap;
        DevCard cardLevel1;
        DevCard cardLevel2;
        DevCard cardLevel3;
        List<DevCard> devCards;
        requirement1 = new CardRequirementColor(DevCardColour.GREEN, 2); //Met
        hashMap = new HashMap<>();
        cardLevel1 = new DevCard(1, DevCardColour.GREEN, 1, hashMap, hashMap, hashMap, "abc");
        cardLevel2 = new DevCard(2, DevCardColour.GREEN, 2, hashMap, hashMap, hashMap, "abc");
        cardLevel3 = new DevCard(3, DevCardColour.GREEN, 3, hashMap, hashMap, hashMap, "abc");
        devCards = new ArrayList<>();
        devCards.add(cardLevel1);
        devCards.add(cardLevel1);
        devCards.add(cardLevel2);
        devCards.add(cardLevel1);
        devCards.add(cardLevel2);
        devCards.add(cardLevel3);
        playerResourcesAndCards = new PlayerResourcesAndCards(new HashMap<>(), devCards);

        CardRequirementColorAndLevel req1;
        CardRequirementColorAndLevel req2;
        CardRequirementColorAndLevel req3;
        req1 = new CardRequirementColorAndLevel(3, DevCardColour.GREEN, 2); //Unmet
        req3 = new CardRequirementColorAndLevel(2, DevCardColour.GREEN, 2); //Met

        Effect effect = new WhiteMarbleLeaderEffect(ResourceType.SERVANT);
        LeaderCard leaderCard;
        List<Requirement> requirements = new ArrayList<>();

        //One requirement is unmet
        requirements.add(requirement1);
        requirements.add(req3);
        requirements.add(req1);
        Collections.shuffle(requirements);

        leaderCard = new LeaderCard(3, requirements, effect);
        LeaderCard finalLeaderCard = leaderCard;
        UnmetRequirementException exception = assertThrows(UnmetRequirementException.class, () -> finalLeaderCard.checkRequirements(playerResourcesAndCards));
        assertEquals(exception.getUnmetRequirement(), req1);
    }


    @Test
    //Tests the cloning of LeaderCards
    public void ctrlLeaderCardCloning() throws NegativeQuantityException {
        List<Requirement> tmp = new ArrayList<>();
        tmp.add(new CardRequirementResource(ResourceType.COIN, 3));
        tmp.add(new CardRequirementColorAndLevel(2, DevCardColour.GREEN, 4));
        LeaderCard original = new LeaderCard(3, tmp, new WhiteMarbleLeaderEffect(ResourceType.SERVANT));

        LeaderCard clone = new LeaderCard(original);

        assertNotSame(clone, original);
        assertEquals(clone.getVictoryPoints(), original.getVictoryPoints());

        HashMap<ResourceType, Integer> outputOriginal = original.getOutputWhenDiscarded();
        HashMap<ResourceType, Integer> outputClone = clone.getOutputWhenDiscarded();

        for (ResourceType rT : resources)
            assertEquals(outputClone.get(rT), outputOriginal.get(rT));
        assertNotSame(outputClone, outputOriginal);

        List<Requirement> reqOriginal = original.getRequirementsList();
        List<Requirement> reqClone = original.getRequirementsList();
        assertNotSame(reqClone, reqOriginal);

        for (Requirement req : reqOriginal)
            assertEquals(req, reqClone.get(reqClone.indexOf(req)));

        assertEquals(reqClone, reqOriginal);

        WhiteMarbleLeaderEffect effectOriginal = (WhiteMarbleLeaderEffect) original.getEffect();
        WhiteMarbleLeaderEffect effectClone = (WhiteMarbleLeaderEffect) clone.getEffect();

        assertEquals(effectClone.getExtraResourceAmount(), effectOriginal.getExtraResourceAmount());
        Assertions.assertEquals(effectClone.getExtraResourceType(), effectOriginal.getExtraResourceType());
        assertNotSame(effectClone, effectOriginal);

        assertEquals(clone, original);

    }

    @Test
    public void ctrlRequirementCloning() throws NegativeQuantityException {
        List<Requirement> tmp = new ArrayList<>();
        CardRequirementColorAndLevel pos0 = new CardRequirementColorAndLevel(1, DevCardColour.GREEN, 2);
        tmp.add(pos0);
        CardRequirementColor pos1 = new CardRequirementColor(DevCardColour.GREEN, 2);
        tmp.add(pos1);
        CardRequirementResource pos2 = new CardRequirementResource(ResourceType.COIN, 3);
        tmp.add(pos2);
        List<Requirement> newList = LeaderCard.getCloneList(tmp);

        CardRequirementColorAndLevel npos0 = (CardRequirementColorAndLevel) newList.get(0);
        assertEquals(pos0.getLevel(), npos0.getLevel());
        assertEquals(pos0.getCardColour(), npos0.getCardColour());
        assertEquals(pos0.getQuantity(), npos0.getQuantity());
        CardRequirementColor npos1 = (CardRequirementColor) newList.get(1);
        assertEquals(pos1.getCardColour(), npos1.getCardColour());
        assertEquals(pos1.getQuantity(), npos1.getQuantity());
        CardRequirementResource npos2 = (CardRequirementResource) newList.get(2);
        assertEquals(pos2.getQuantity(), npos2.getQuantity());
        assertEquals(pos2.getResourceType(), npos2.getResourceType());
        assertEquals(newList.size(), tmp.size());

    }

    @Test
    //Tests whether LeaderCards return a copy of their RequirementList when asked so
    public void ctrlRequirementCloningSafe() throws NegativeQuantityException {
        Requirement requirement1 = new CardRequirementColor(DevCardColour.GREEN, 2); //Met
        Requirement requirement2 = new CardRequirementColorAndLevel(1, DevCardColour.BLUE, 1); //Unmet
        Requirement requirement3 = new CardRequirementResource(ResourceType.COIN, 3);
        List<Requirement> list = new ArrayList<>();
        list.add(requirement1);
        list.add(requirement2);
        list.add(requirement3);
        LeaderCard lD = new LeaderCard(4, list, new Effect());

        List<Requirement> copy = lD.getRequirementsListSafe();
        assertNotSame(copy, lD);
        assertEquals(copy.size(), list.size());
        for (int i = 0; i < list.size(); i++) {
            assertEquals(copy.get(i), list.get(i));
            assertNotSame(copy.get(i), list.get(i));
        }
    }

    @Test
    public void ctrlEquals() {
        Effect e1 = new ExtraProductionLeaderEffect(ResourceType.SERVANT, 1);
        Effect e2 = new ExtraProductionLeaderEffect(ResourceType.COIN, 1);
        LeaderCard l1 = new LeaderCard(2, new LinkedList<>(), e1);
        LeaderCard l2 = new LeaderCard(2, new LinkedList<>(), e2);

        assertNotEquals(l1, l2);
        assertNotEquals(e1, e2);
        Effect e1bis = new ExtraProductionLeaderEffect(ResourceType.SERVANT, 1);
        assertEquals(e1, e1bis);
        assertNotSame(e1, e1bis);
    }

    @Test
    public void ctrlEqualsRequirementsFalse() throws NegativeQuantityException {
        List<Requirement> req1 = new LinkedList<>();
        List<Requirement> req2 = new LinkedList<>();

        req1.add(new CardRequirementResource(ResourceType.COIN, 1));
        req2.add(new CardRequirementResource(ResourceType.COIN, 1));
        req1.add(new CardRequirementColor(DevCardColour.YELLOW, 2));
        req2.add(new CardRequirementColor(DevCardColour.BLUE, 2));
        LeaderCard l1 = new LeaderCard(2, req1, new ExtraProductionLeaderEffect(ResourceType.SERVANT, 1));
        LeaderCard l2 = new LeaderCard(2, req2, new ExtraProductionLeaderEffect(ResourceType.SERVANT, 1));

        assertNotEquals(l1, l2);
        assertFalse(req1.containsAll(req2));
        assertFalse(req2.containsAll(req1));
    }

    @Test
    public void ctrlEqualsRequirementsTrue() throws NegativeQuantityException {
        List<Requirement> req1 = new LinkedList<>();
        req1.add(new CardRequirementResource(ResourceType.COIN, 1));
        req1.add(new CardRequirementColor(DevCardColour.YELLOW, 2));
        LeaderCard l1 = new LeaderCard(2, req1, new ExtraProductionLeaderEffect(ResourceType.SERVANT, 1));
        LeaderCard l2 = new LeaderCard(2, req1, new ExtraProductionLeaderEffect(ResourceType.SERVANT, 1));

        assertEquals(l1, l2);
    }
}
