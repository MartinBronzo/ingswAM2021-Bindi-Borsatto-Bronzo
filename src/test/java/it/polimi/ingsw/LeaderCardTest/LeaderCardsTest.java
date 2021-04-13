package it.polimi.ingsw.LeaderCardTest;

import it.polimi.ingsw.DevCards.DevCard;
import it.polimi.ingsw.DevCards.DevCardColour;
import it.polimi.ingsw.DevCards.DevSlots;
import it.polimi.ingsw.LeaderCard.*;
import it.polimi.ingsw.LeaderCard.leaderEffects.Effect;
import it.polimi.ingsw.LeaderCard.leaderEffects.ExtraSlotLeaderEffect;
import it.polimi.ingsw.LeaderCard.leaderEffects.WhiteMarbleLeaderEffect;
import it.polimi.ingsw.PlayerBoard;
import it.polimi.ingsw.PlayerResourcesAndCards;
import it.polimi.ingsw.ResourceType;
import it.polimi.ingsw.exceptions.IllegalActionException;
import it.polimi.ingsw.exceptions.NegativeQuantityException;
import it.polimi.ingsw.exceptions.UnmetRequirementException;
import it.polimi.ingsw.LeaderCardRequirementsTests.*;
import it.polimi.ingsw.marble.WhiteMarble;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class LeaderCardsTest {
    CardRequirementColor requirement1;
    CardRequirementColor requirement2;
    PlayerResourcesAndCards playerResourcesAndCards;
    HashMap<ResourceType,Integer> hashMap;
    DevCard cardLevel1;
    DevCard cardLevel2;
    DevCard cardLevel3;
    List<DevCard> devCards;



    CardRequirementColorAndLevel req1;
    CardRequirementColorAndLevel req2;
    CardRequirementColorAndLevel req3;

    @BeforeEach
    public void setup() throws NegativeQuantityException {
        requirement1 = new CardRequirementColor(DevCardColour.GREEN,2); //Met
        requirement2 = new CardRequirementColor(DevCardColour.BLUE,1); //Unmet
        hashMap=new HashMap<>();
        cardLevel1=new DevCard(1, DevCardColour.GREEN,1,hashMap,hashMap,hashMap,"abc");
        cardLevel2=new DevCard(2,DevCardColour.GREEN,2,hashMap,hashMap,hashMap,"abc");
        cardLevel3=new DevCard(3,DevCardColour.GREEN,3,hashMap,hashMap,hashMap,"abc");
        devCards=new ArrayList<>();
        devCards.add(cardLevel1);
        devCards.add(cardLevel1);
        devCards.add(cardLevel2);
        devCards.add(cardLevel1);
        devCards.add(cardLevel2);
        devCards.add(cardLevel3);
        playerResourcesAndCards = new PlayerResourcesAndCards(new HashMap<>(),devCards);
        req1 = new CardRequirementColorAndLevel(3, DevCardColour.GREEN, 2); //Unmet
        req2 = new CardRequirementColorAndLevel(1, DevCardColour.BLUE, 1); //Unmet
        req3 = new CardRequirementColorAndLevel(2, DevCardColour.GREEN, 2); //Met
    }


    @Test
    //Tests the creation of LeaderCards
    public void ctrlCreation(){
        List<LeaderCard> list = new ArrayList<>();
        list.add(new LeaderCard(3, new ArrayList<>(), new ExtraSlotLeaderEffect(ResourceType.COIN, 2)));
        list.add(new LeaderCard(4, new ArrayList<>(), new ExtraSlotLeaderEffect(ResourceType.SERVANT, 1)));
        LeaderCards leaderCards = new LeaderCards(list);

        assertTrue(leaderCards.getActiveCards().isEmpty());
        List<LeaderCard> tmp = leaderCards.getNotPlayedCards();
        for(int i = 0; i < tmp.size(); i++){
            assertEquals(tmp.get(i).getVictoryPoints(), list.get(i).getVictoryPoints());
            assertTrue(tmp.get(i).getRequirementsList().isEmpty());
            assertTrue(tmp.get(i).getEffect() instanceof ExtraSlotLeaderEffect);
            assertEquals(((ExtraSlotLeaderEffect) tmp.get(i).getEffect()).extraSlotGetResourceNumber(), ((ExtraSlotLeaderEffect) list.get(i).getEffect()).extraSlotGetResourceNumber());
            assertEquals(((ExtraSlotLeaderEffect) tmp.get(i).getEffect()).extraSlotGetType(), ((ExtraSlotLeaderEffect) list.get(i).getEffect()).extraSlotGetType());
            assertNotSame(tmp.get(i), list.get(i));
        }


        assertNotSame(tmp, list);
    }

    @Test
    //Tests that a LeaderCard is correctly activated
    public void ctrlActivation() throws NegativeQuantityException {
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
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> leaderCards.activateLeaderCard(new LeaderCard(5, new ArrayList<>(), new Effect()), playerResourcesAndCards));
        assertEquals(exception.getMessage(), "The player doesn't have this card!");

        //The player doesn't have the requirements to activate the card they hold
        UnmetRequirementException exception1 = assertThrows(UnmetRequirementException.class, () -> leaderCards.activateLeaderCard(unmet, playerResourcesAndCards));
        assertEquals(((CardRequirementColorAndLevel) exception1.getUnmetRequirement()).getLevel(),req1.getLevel());
        assertEquals( ((CardRequirementColorAndLevel) exception1.getUnmetRequirement()).getCardColour(), req1.getCardColour());
        assertEquals(((CardRequirementColorAndLevel) exception1.getUnmetRequirement()).getQuantity(), req1.getQuantity());
        assertEquals(leaderCards.getNotPlayedCards().size(), 2);
        assertEquals(leaderCards.getNotPlayedCards().stream().filter((x -> x.getVictoryPoints() == 3)).count(), 1);
        assertEquals(leaderCards.getNotPlayedCards().stream().filter(((x) -> x.getVictoryPoints() == 4)).count(), 1);
        //assertTrue(leaderCards.getNotPlayedCards().contains(unmet));
        assertTrue(leaderCards.getActiveCards().isEmpty());

        //The player has the requirements to activate the LeaderCard
        try {
            assertTrue(leaderCards.activateLeaderCard(met, playerResourcesAndCards));
        } catch (UnmetRequirementException e) {
            e.printStackTrace();
        }
        assertEquals(leaderCards.getActiveCards().size(), 1);
        assertEquals(leaderCards.getActiveCards().get(0).getVictoryPoints(), 3);
        //assertTrue(leaderCards.getActiveCards().contains(met));
        assertEquals(leaderCards.getNotPlayedCards().size(), 1);
        //assertFalse(leaderCards.getNotPlayedCards().contains(met));
        assertEquals(leaderCards.getNotPlayedCards().get(0).getVictoryPoints(), 4);
        //assertTrue(leaderCards.getNotPlayedCards().contains(unmet));

        //The player tries to activate an already active card
        try {
            assertFalse(leaderCards.activateLeaderCard(met, playerResourcesAndCards));
        } catch (UnmetRequirementException e) {
            e.printStackTrace();
        }
    }

    @Test
    //Tests that a LeaderCard is correctly discarded
    public void ctrlDiscard() throws NegativeQuantityException {
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
        assertEquals(leaderCards.getNotPlayedCards().size(), 1);
        //assertTrue(leaderCards.getNotPlayedCards().contains(met));
        assertEquals(leaderCards.getNotPlayedCards().get(0).getVictoryPoints(), 3);
        assertTrue(leaderCards.getActiveCards().isEmpty());

        try {
            leaderCards.activateLeaderCard(met, playerResourcesAndCards);
        } catch (UnmetRequirementException e) {
            e.printStackTrace();
        }
        assertTrue(leaderCards.getNotPlayedCards().isEmpty());
        exception = assertThrows(IllegalArgumentException.class, () -> leaderCards.discardLeaderCard(met));
        assertEquals(exception.getMessage(), "Played cards can't be discarded!");

    }

    @Test
    //Tests what happens when the player asks for the effect of a LeaderCard
    public void ctrlEffect() throws NegativeQuantityException, IllegalActionException {
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
            leaderCards.activateLeaderCard(met, playerResourcesAndCards);
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
            leaderCards.activateLeaderCard(met, playerResourcesAndCards);
            leaderCards.activateLeaderCard(met2, playerResourcesAndCards);
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
            leaderCards.activateLeaderCard(met, playerResourcesAndCards);
        } catch (UnmetRequirementException e) {
            e.printStackTrace();
        }
        assertEquals(leaderCards.getLeaderCardsPoints(), 3);
    }

    @Test
    //Tests that LeaderCards get the list of the requirements of the card it does hold
    public void ctrlRequirementsReturn() throws NegativeQuantityException{
        List<LeaderCard> list = new ArrayList<>();
        List<Requirement> metRequirements = new ArrayList<>();
        metRequirements.add(requirement1);
        metRequirements.add(req3);
        List<Requirement> unmetRequirements = new ArrayList<>();
        unmetRequirements.add(requirement1);
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
        assertTrue(tmp.size() == unmetRequirements.size());
        assertEquals(((CardRequirementColor) tmp.get(0)).getQuantity(), requirement1.getQuantity());
        assertEquals(((CardRequirementColor) tmp.get(0)).getCardColour(), requirement1.getCardColour());
        assertEquals(((CardRequirementColorAndLevel) tmp.get(1)).getLevel(), req1.getLevel());
        assertEquals(((CardRequirementColorAndLevel) tmp.get(1)).getCardColour(), req1.getCardColour());
        assertEquals(((CardRequirementColorAndLevel) tmp.get(1)).getQuantity(), req1.getQuantity());

        //It asks the requirements of played card
        try {
            leaderCards.activateLeaderCard(met, playerResourcesAndCards);
        } catch (UnmetRequirementException e) {
            e.printStackTrace();
        }
        tmp = leaderCards.getLeaderCardRequirements(met);
        assertEquals(((CardRequirementColor) tmp.get(0)).getQuantity(), requirement1.getQuantity());
        assertEquals(((CardRequirementColor) tmp.get(0)).getCardColour(), requirement1.getCardColour());
        assertEquals(((CardRequirementColorAndLevel) tmp.get(1)).getLevel(), req3.getLevel());
        assertEquals(((CardRequirementColorAndLevel) tmp.get(1)).getCardColour(), req3.getCardColour());
        assertEquals(((CardRequirementColorAndLevel) tmp.get(1)).getQuantity(), req3.getQuantity());


    }

    @Test
    //Tests that LeaderCard with a one-shot effect such as ExtraSlotEffect can't be activated twice
    public void ctrlOneShotCard(){
        List<LeaderCard> list = new ArrayList<>();
        List<Requirement> metRequirements = new ArrayList<>();
        metRequirements.add(requirement1);
        metRequirements.add(req3);
        Effect effect = new ExtraSlotLeaderEffect(ResourceType.COIN, 2);
        LeaderCard met = new LeaderCard(3, metRequirements, effect);
        list.add(met); //This card can be activated
        LeaderCards leaderCards = new LeaderCards(list);

        assertTrue(leaderCards.getAlreadyUsedOneShotCard().isEmpty());

        try {
            leaderCards.activateLeaderCard(met, playerResourcesAndCards);
        } catch (UnmetRequirementException e) {
            e.printStackTrace();
        }

        Effect effect2 = new Effect();
        try {
            effect2 = leaderCards.getEffectFromCard(met);
        } catch (IllegalActionException e) {
            e.printStackTrace();
        }

        assertNotSame(effect2, effect);
        assertEquals(effect2, effect);
        assertEquals(leaderCards.getAlreadyUsedOneShotCard().size(), 1);
        assertTrue(leaderCards.getAlreadyUsedOneShotCard().contains(met));

        IllegalActionException exception = assertThrows(IllegalActionException.class, () -> leaderCards.getEffectFromCard(met));
        assertEquals(exception.getMessage(), "This Card can only be used once: it has already been used!");
        assertEquals(leaderCards.getAlreadyUsedOneShotCard().size(), 1);
        assertTrue(leaderCards.getAlreadyUsedOneShotCard().contains(met));

    }

    @Test
    //Tests that not-one-shot card are not labeled as such
    public void ctrlNotOneShotCard(){
        List<LeaderCard> list = new ArrayList<>();
        List<Requirement> metRequirements = new ArrayList<>();
        metRequirements.add(requirement1);
        metRequirements.add(req3);
        Effect effect = new WhiteMarbleLeaderEffect(ResourceType.SERVANT);
        LeaderCard met = new LeaderCard(3, metRequirements, effect);
        list.add(met); //This card can be activated
        LeaderCards leaderCards = new LeaderCards(list);

        assertTrue(leaderCards.getAlreadyUsedOneShotCard().isEmpty());

        try {
            leaderCards.activateLeaderCard(met, playerResourcesAndCards);
        } catch (UnmetRequirementException e) {
            e.printStackTrace();
        }

        Effect effect2 = new Effect();
        try {
            effect2 = leaderCards.getEffectFromCard(met);
        } catch (IllegalActionException e) {
            e.printStackTrace();
        }

        assertNotSame(effect2, effect);
        assertEquals(effect2, effect);
        assertTrue(leaderCards.getAlreadyUsedOneShotCard().isEmpty());

        Effect effect3 = new Effect();
        try {
            effect3 = leaderCards.getEffectFromCard(met);
        } catch (IllegalActionException e) {
            e.printStackTrace();
        }
        assertNotSame(effect3, effect);
        assertEquals(effect3, effect);
        assertTrue(leaderCards.getAlreadyUsedOneShotCard().isEmpty());
    }
}
