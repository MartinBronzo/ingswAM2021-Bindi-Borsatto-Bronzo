package it.polimi.ingsw.LeaderCardTest;

import it.polimi.ingsw.DevCards.DevCard;
import it.polimi.ingsw.DevCards.DevCardColour;
import it.polimi.ingsw.DevCards.DevSlots;
import it.polimi.ingsw.LeaderCard.*;
import it.polimi.ingsw.LeaderCard.leaderEffects.Effect;
import it.polimi.ingsw.LeaderCard.leaderEffects.WhiteMarbleLeaderEffect;
import it.polimi.ingsw.PlayerBoard;
import it.polimi.ingsw.ResourceType;
import it.polimi.ingsw.exceptions.NegativeQuantityException;
import it.polimi.ingsw.exceptions.UnmetRequirementException;
import it.polimi.ingsw.LeaderCardRequirementsTests.*;
import it.polimi.ingsw.marble.WhiteMarble;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class LeaderCardsTest {

    @Test
    //Tests the creation of LeaderCards
    public void ctrlCreation(){
        List<LeaderCard> list = new ArrayList<>();
        list.add(new LeaderCard(3, new ArrayList<>(), new Effect()));
        list.add(new LeaderCard(4, new ArrayList<>(), new Effect()));
        LeaderCards leaderCards = new LeaderCards(list);

        assertTrue(leaderCards.getActiveCards().isEmpty());
        List<LeaderCard> tmp = leaderCards.getNotPlayedCards();
        for(LeaderCard lD: list)
            assertTrue(tmp.contains(lD));
        for(LeaderCard lD: tmp)
            assertTrue(list.contains(lD));
        assertNotSame(tmp, list);
    }

    @Test
    //Tests that a LeaderCard is correctly activated
    public void ctrlActivation() throws NegativeQuantityException {
        CardRequirementColor requirement1;
        CardRequirementColor requirement2;
        PlayerBoard playerBoard;
        HashMap<ResourceType,Integer> hashMap;
        DevCard cardLevel1;
        DevCard cardLevel2;
        DevCard cardLevel3;
        DevSlots devSlots;
        requirement1 = new CardRequirementColor(DevCardColour.GREEN,2); //Met
        requirement2 = new CardRequirementColor(DevCardColour.BLUE,1); //Unmet
        playerBoard = new PlayerBoard();
        hashMap=new HashMap<>();
        cardLevel1=new DevCard(1, DevCardColour.GREEN,1,hashMap,hashMap,hashMap,"abc");
        cardLevel2=new DevCard(2,DevCardColour.GREEN,2,hashMap,hashMap,hashMap,"abc");
        cardLevel3=new DevCard(3,DevCardColour.GREEN,3,hashMap,hashMap,hashMap,"abc");
        devSlots=playerBoard.getDevSlots();
        devSlots.getDevSlot(0).addDevCard(cardLevel1);
        devSlots.getDevSlot(1).addDevCard(cardLevel1);
        devSlots.getDevSlot(1).addDevCard(cardLevel2);
        devSlots.getDevSlot(2).addDevCard(cardLevel1);
        devSlots.getDevSlot(2).addDevCard(cardLevel2);
        devSlots.getDevSlot(2).addDevCard(cardLevel3);

        CardRequirementColorAndLevel req1;
        CardRequirementColorAndLevel req2;
        CardRequirementColorAndLevel req3;
        req1 = new CardRequirementColorAndLevel(3, DevCardColour.GREEN, 2); //Unmet
        req2 = new CardRequirementColorAndLevel(1, DevCardColour.BLUE, 1); //Unmet
        req3 = new CardRequirementColorAndLevel(2, DevCardColour.GREEN, 2); //Met

        List<LeaderCard> list = new ArrayList<>();
        List<Requirement> metRequirements = new ArrayList<>();
        metRequirements.add(requirement1);
        metRequirements.add(req3);
        List<Requirement> unmetRequirements = new ArrayList<>();
        unmetRequirements.add(requirement1);
        unmetRequirements.add(req3);
        unmetRequirements.add(req1);
        LeaderCard met = new LeaderCard(3, metRequirements, new Effect());
        LeaderCard unmet = new LeaderCard(4, unmetRequirements, new Effect());
        list.add(met); //This card can be activated
        list.add(unmet); //This card can't be activated
        LeaderCards leaderCards = new LeaderCards(list);
        int size = leaderCards.getNotPlayedCards().size();

        //The player doesn't hold the card
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> leaderCards.activateLeaderCard(new LeaderCard(5, new ArrayList<>(), new Effect()), playerBoard));
        assertEquals(exception.getMessage(), "The player doesn't have this card!");

        //The player doesn't have the requirements to activate the card they hold
        UnmetRequirementException exception1 = assertThrows(UnmetRequirementException.class, () -> leaderCards.activateLeaderCard(unmet, playerBoard));
        assertEquals(exception1.getUnmetRequirement(), req1);
        assertTrue(leaderCards.getNotPlayedCards().contains(met));
        assertTrue(leaderCards.getNotPlayedCards().contains(unmet));
        assertTrue(leaderCards.getActiveCards().isEmpty());

        //The player has the requirements to activate the LeaderCard
        try {
            assertTrue(leaderCards.activateLeaderCard(met, playerBoard));
        } catch (UnmetRequirementException e) {
            e.printStackTrace();
        }
        assertEquals(leaderCards.getActiveCards().size(), 1);
        assertTrue(leaderCards.getActiveCards().contains(met));
        assertEquals(leaderCards.getNotPlayedCards().size(), size - 1);
        assertFalse(leaderCards.getNotPlayedCards().contains(met));
        assertTrue(leaderCards.getNotPlayedCards().contains(unmet));

        //The player tries to activate an already active card
        try {
            assertFalse(leaderCards.activateLeaderCard(met, playerBoard));
        } catch (UnmetRequirementException e) {
            e.printStackTrace();
        }
    }

    @Test
    //Tests that a LeaderCard is correctly discarded
    public void ctrlDiscard() throws NegativeQuantityException {
        CardRequirementColor requirement1;
        CardRequirementColor requirement2;
        PlayerBoard playerBoard;
        HashMap<ResourceType,Integer> hashMap;
        DevCard cardLevel1;
        DevCard cardLevel2;
        DevCard cardLevel3;
        DevSlots devSlots;
        requirement1 = new CardRequirementColor(DevCardColour.GREEN,2); //Met
        requirement2 = new CardRequirementColor(DevCardColour.BLUE,1); //Unmet
        playerBoard = new PlayerBoard();
        hashMap=new HashMap<>();
        cardLevel1=new DevCard(1, DevCardColour.GREEN,1,hashMap,hashMap,hashMap,"abc");
        cardLevel2=new DevCard(2,DevCardColour.GREEN,2,hashMap,hashMap,hashMap,"abc");
        cardLevel3=new DevCard(3,DevCardColour.GREEN,3,hashMap,hashMap,hashMap,"abc");
        devSlots=playerBoard.getDevSlots();
        devSlots.getDevSlot(0).addDevCard(cardLevel1);
        devSlots.getDevSlot(1).addDevCard(cardLevel1);
        devSlots.getDevSlot(1).addDevCard(cardLevel2);
        devSlots.getDevSlot(2).addDevCard(cardLevel1);
        devSlots.getDevSlot(2).addDevCard(cardLevel2);
        devSlots.getDevSlot(2).addDevCard(cardLevel3);

        CardRequirementColorAndLevel req1;
        CardRequirementColorAndLevel req2;
        CardRequirementColorAndLevel req3;
        req1 = new CardRequirementColorAndLevel(3, DevCardColour.GREEN, 2); //Unmet
        req2 = new CardRequirementColorAndLevel(1, DevCardColour.BLUE, 1); //Unmet
        req3 = new CardRequirementColorAndLevel(2, DevCardColour.GREEN, 2); //Met

        //This is the default output given by LeaderCards when they are discarded
        HashMap<ResourceType, Integer> defaultOutput = new HashMap<ResourceType, Integer>(){};
        defaultOutput.put(ResourceType.FAITHPOINT, 1);

        List<LeaderCard> list = new ArrayList<>();
        List<Requirement> metRequirements = new ArrayList<>();
        metRequirements.add(requirement1);
        metRequirements.add(req3);
        List<Requirement> unmetRequirements = new ArrayList<>();
        unmetRequirements.add(requirement1);
        unmetRequirements.add(req3);
        unmetRequirements.add(req1);
        LeaderCard met = new LeaderCard(3, metRequirements, new Effect());
        LeaderCard unmet = new LeaderCard(4, unmetRequirements, new Effect());
        list.add(met); //This card can be activated
        list.add(unmet); //This card can't be activated
        LeaderCards leaderCards = new LeaderCards(list);
        int size = leaderCards.getNotPlayedCards().size();

        //The player can't discard a card they don't hold
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> leaderCards.discardLeaderCard(new LeaderCard(23, new ArrayList<>(), new Effect())));
        assertEquals(exception.getMessage(), "The player can't discards cards they don't hold!");

        //The player discards a card they do hold
        HashMap<ResourceType, Integer> output = leaderCards.discardLeaderCard(unmet);
        assertEquals(output.size(), 1);
        assertEquals(output.get(ResourceType.FAITHPOINT), defaultOutput.get(ResourceType.FAITHPOINT));
        assertFalse(leaderCards.getActiveCards().contains(unmet));
        assertFalse(leaderCards.getNotPlayedCards().contains(unmet));
        assertEquals(leaderCards.getNotPlayedCards().size(), size - 1);
        assertTrue(leaderCards.getNotPlayedCards().contains(met));
        assertTrue(leaderCards.getActiveCards().isEmpty());

        try {
            leaderCards.activateLeaderCard(met, playerBoard);
        } catch (UnmetRequirementException e) {
            e.printStackTrace();
        }
        assertTrue(leaderCards.getNotPlayedCards().isEmpty());
        exception = assertThrows(IllegalArgumentException.class, () -> leaderCards.discardLeaderCard(met));
        assertEquals(exception.getMessage(), "Played cards can't be discarded!");

    }

    @Test
    //Tests what happens when the player asks for the effect of a LeaderCard
    public void ctrlEffect() throws NegativeQuantityException {
        CardRequirementColor requirement1;
        CardRequirementColor requirement2;
        PlayerBoard playerBoard;
        HashMap<ResourceType,Integer> hashMap;
        DevCard cardLevel1;
        DevCard cardLevel2;
        DevCard cardLevel3;
        DevSlots devSlots;
        requirement1 = new CardRequirementColor(DevCardColour.GREEN,2); //Met
        requirement2 = new CardRequirementColor(DevCardColour.BLUE,1); //Unmet
        playerBoard = new PlayerBoard();
        hashMap=new HashMap<>();
        cardLevel1=new DevCard(1, DevCardColour.GREEN,1,hashMap,hashMap,hashMap,"abc");
        cardLevel2=new DevCard(2,DevCardColour.GREEN,2,hashMap,hashMap,hashMap,"abc");
        cardLevel3=new DevCard(3,DevCardColour.GREEN,3,hashMap,hashMap,hashMap,"abc");
        devSlots=playerBoard.getDevSlots();
        devSlots.getDevSlot(0).addDevCard(cardLevel1);
        devSlots.getDevSlot(1).addDevCard(cardLevel1);
        devSlots.getDevSlot(1).addDevCard(cardLevel2);
        devSlots.getDevSlot(2).addDevCard(cardLevel1);
        devSlots.getDevSlot(2).addDevCard(cardLevel2);
        devSlots.getDevSlot(2).addDevCard(cardLevel3);

        CardRequirementColorAndLevel req1;
        CardRequirementColorAndLevel req2;
        CardRequirementColorAndLevel req3;
        req1 = new CardRequirementColorAndLevel(3, DevCardColour.GREEN, 2); //Unmet
        req2 = new CardRequirementColorAndLevel(1, DevCardColour.BLUE, 1); //Unmet
        req3 = new CardRequirementColorAndLevel(2, DevCardColour.GREEN, 2); //Met

        List<LeaderCard> list = new ArrayList<>();
        List<Requirement> metRequirements = new ArrayList<>();
        metRequirements.add(requirement1);
        metRequirements.add(req3);
        List<Requirement> unmetRequirements = new ArrayList<>();
        unmetRequirements.add(requirement1);
        unmetRequirements.add(req3);
        unmetRequirements.add(req1);
        LeaderCard met = new LeaderCard(3, metRequirements, new WhiteMarbleLeaderEffect(ResourceType.SERVANT));
        LeaderCard unmet = new LeaderCard(4, unmetRequirements, new Effect());
        list.add(met); //This card can be activated
        list.add(unmet); //This card can't be activated
        LeaderCards leaderCards = new LeaderCards(list);
        int size = leaderCards.getNotPlayedCards().size();

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> leaderCards.getEffectFromCard(met));
        assertEquals(exception.getMessage(), "The player can't use the effect of a LeaderCard they haven't played yet!");

        try {
            leaderCards.activateLeaderCard(met, playerBoard);
        } catch (UnmetRequirementException e) {
            e.printStackTrace();
        }

        WhiteMarbleLeaderEffect tmp = (WhiteMarbleLeaderEffect) leaderCards.getEffectFromCard(met);
        assertTrue(tmp instanceof WhiteMarbleLeaderEffect);
        assertEquals(tmp.getExtraResourceAmount(), 1);
        assertEquals(tmp.getExtraResourceType(), ResourceType.SERVANT);
        //WhiteMarbleLeaderEffect tmp2 = new WhiteMarbleLeaderEffect(ResourceType.SERVANT);
        //assertEquals(tmp, tmp2);

        exception = assertThrows(IllegalArgumentException.class, () -> leaderCards.getEffectFromCard(new LeaderCard(23, new ArrayList<>(), new Effect())));
        assertEquals(exception.getMessage(), "The player can't use the effect of a LeaderCard they don't have!");

    }

    @Test
    //Tests that LeaderCards give the right points
    public void ctrlPoints() throws NegativeQuantityException {
        CardRequirementColor requirement1;
        CardRequirementColor requirement2;
        PlayerBoard playerBoard;
        HashMap<ResourceType, Integer> hashMap;
        DevCard cardLevel1;
        DevCard cardLevel2;
        DevCard cardLevel3;
        DevSlots devSlots;
        requirement1 = new CardRequirementColor(DevCardColour.GREEN, 2); //Met
        requirement2 = new CardRequirementColor(DevCardColour.BLUE, 1); //Unmet
        playerBoard = new PlayerBoard();
        hashMap = new HashMap<>();
        cardLevel1 = new DevCard(1, DevCardColour.GREEN, 1, hashMap, hashMap, hashMap, "abc");
        cardLevel2 = new DevCard(2, DevCardColour.GREEN, 2, hashMap, hashMap, hashMap, "abc");
        cardLevel3 = new DevCard(3, DevCardColour.GREEN, 3, hashMap, hashMap, hashMap, "abc");
        devSlots = playerBoard.getDevSlots();
        devSlots.getDevSlot(0).addDevCard(cardLevel1);
        devSlots.getDevSlot(1).addDevCard(cardLevel1);
        devSlots.getDevSlot(1).addDevCard(cardLevel2);
        devSlots.getDevSlot(2).addDevCard(cardLevel1);
        devSlots.getDevSlot(2).addDevCard(cardLevel2);
        devSlots.getDevSlot(2).addDevCard(cardLevel3);

        CardRequirementColorAndLevel req1;
        CardRequirementColorAndLevel req2;
        CardRequirementColorAndLevel req3;
        req1 = new CardRequirementColorAndLevel(3, DevCardColour.GREEN, 2); //Unmet
        req2 = new CardRequirementColorAndLevel(1, DevCardColour.BLUE, 1); //Unmet
        req3 = new CardRequirementColorAndLevel(2, DevCardColour.GREEN, 2); //Met

        List<LeaderCard> list = new ArrayList<>();
        List<Requirement> metRequirements = new ArrayList<>();
        metRequirements.add(requirement1);
        metRequirements.add(req3);
        List<Requirement> unmetRequirements = new ArrayList<>();
        unmetRequirements.add(requirement1);
        unmetRequirements.add(req3);
        unmetRequirements.add(req1);
        LeaderCard met = new LeaderCard(3, metRequirements, new WhiteMarbleLeaderEffect(ResourceType.SERVANT));
        LeaderCard unmet = new LeaderCard(2, unmetRequirements, new Effect());
        list.add(met); //This card can be activated
        //list.add(unmet); //This card can't be activated
        LeaderCard met2 = new LeaderCard(4, metRequirements, new Effect());
        LeaderCard unmet2 = new LeaderCard(1, unmetRequirements, new Effect());
        list.add(met2);
        LeaderCards leaderCards = new LeaderCards(list);
        int size = leaderCards.getNotPlayedCards().size();

        //Both not-played
        assertEquals(leaderCards.getLeaderCardsPoints(), 0);

        //Both active
        try {
            leaderCards.activateLeaderCard(met, playerBoard);
            leaderCards.activateLeaderCard(met2, playerBoard);
        } catch (UnmetRequirementException e) {
            e.printStackTrace();
        }
        assertEquals(leaderCards.getLeaderCardsPoints(), 7);

        //Both discarded
        list = new ArrayList<>();
        list.add(unmet);
        list.add(unmet2);
        leaderCards = new LeaderCards(list);
        leaderCards.discardLeaderCard(unmet);
        leaderCards.discardLeaderCard(unmet2);
        assertEquals(leaderCards.getLeaderCardsPoints(), 0);

        //One Active, One discarded, One not-played
        list = new ArrayList<>();
        list.add(met);
        list.add(unmet);
        list.add(met2);
        leaderCards = new LeaderCards(list);
        leaderCards.discardLeaderCard(met2);
        try {
            leaderCards.activateLeaderCard(met, playerBoard);
        } catch (UnmetRequirementException e) {
            e.printStackTrace();
        }
        assertEquals(leaderCards.getLeaderCardsPoints(), 3);
    }

    @Test
    //Tests that LeaderCards get the list of the requirements of the card it does hold
    public void ctrlRequirementsReturn() throws NegativeQuantityException{
        CardRequirementColor requirement1;
        CardRequirementColor requirement2;
        PlayerBoard playerBoard;
        HashMap<ResourceType,Integer> hashMap;
        DevCard cardLevel1;
        DevCard cardLevel2;
        DevCard cardLevel3;
        DevSlots devSlots;
        requirement1 = new CardRequirementColor(DevCardColour.GREEN,2); //Met
        requirement2 = new CardRequirementColor(DevCardColour.BLUE,1); //Unmet
        playerBoard = new PlayerBoard();
        hashMap=new HashMap<>();
        cardLevel1=new DevCard(1, DevCardColour.GREEN,1,hashMap,hashMap,hashMap,"abc");
        cardLevel2=new DevCard(2,DevCardColour.GREEN,2,hashMap,hashMap,hashMap,"abc");
        cardLevel3=new DevCard(3,DevCardColour.GREEN,3,hashMap,hashMap,hashMap,"abc");
        devSlots=playerBoard.getDevSlots();
        devSlots.getDevSlot(0).addDevCard(cardLevel1);
        devSlots.getDevSlot(1).addDevCard(cardLevel1);
        devSlots.getDevSlot(1).addDevCard(cardLevel2);
        devSlots.getDevSlot(2).addDevCard(cardLevel1);
        devSlots.getDevSlot(2).addDevCard(cardLevel2);
        devSlots.getDevSlot(2).addDevCard(cardLevel3);

        CardRequirementColorAndLevel req1;
        CardRequirementColorAndLevel req2;
        CardRequirementColorAndLevel req3;
        req1 = new CardRequirementColorAndLevel(3, DevCardColour.GREEN, 2); //Unmet
        req2 = new CardRequirementColorAndLevel(1, DevCardColour.BLUE, 1); //Unmet
        req3 = new CardRequirementColorAndLevel(2, DevCardColour.GREEN, 2); //Met

        List<LeaderCard> list = new ArrayList<>();
        List<Requirement> metRequirements = new ArrayList<>();
        metRequirements.add(requirement1);
        metRequirements.add(req3);
        List<Requirement> unmetRequirements = new ArrayList<>();
        unmetRequirements.add(requirement1);
        unmetRequirements.add(req3);
        unmetRequirements.add(req1);
        LeaderCard met = new LeaderCard(3, metRequirements, new WhiteMarbleLeaderEffect(ResourceType.SERVANT));
        LeaderCard unmet = new LeaderCard(4, unmetRequirements, new Effect());
        list.add(met); //This card can be activated
        list.add(unmet); //This card can't be activated
        LeaderCards leaderCards = new LeaderCards(list);
        int size = leaderCards.getNotPlayedCards().size();

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> leaderCards.getLeaderCardRequirements(new LeaderCard(23, new ArrayList<>(), new Effect())));
        assertEquals(exception.getMessage(), "The player doesn't hold this card!");

        //It asks the requirements of a not-played card
        List<Requirement> tmp = leaderCards.getLeaderCardRequirements(unmet);
        for(Requirement req: tmp)
            assertTrue(unmetRequirements.contains(req));
        for(Requirement req: unmetRequirements)
            assertTrue(tmp.contains(req));

        //It asks the requirements of played card
        try {
            leaderCards.activateLeaderCard(met, playerBoard);
        } catch (UnmetRequirementException e) {
            e.printStackTrace();
        }
        tmp = leaderCards.getLeaderCardRequirements(met);
        for(Requirement req: tmp)
            assertTrue(metRequirements.contains(req));
        for(Requirement req: metRequirements)
            assertTrue(tmp.contains(req));

    }
}
