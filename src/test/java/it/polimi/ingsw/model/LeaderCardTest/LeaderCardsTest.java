package it.polimi.ingsw.model.LeaderCardTest;

import it.polimi.ingsw.exceptions.IllegalActionException;
import it.polimi.ingsw.exceptions.NegativeQuantityException;
import it.polimi.ingsw.exceptions.UnmetRequirementException;
import it.polimi.ingsw.model.DevCards.DevCard;
import it.polimi.ingsw.model.DevCards.DevCardColour;
import it.polimi.ingsw.model.LeaderCard.LeaderCard;
import it.polimi.ingsw.model.LeaderCard.LeaderCardRequirements.CardRequirementColor;
import it.polimi.ingsw.model.LeaderCard.LeaderCardRequirements.CardRequirementColorAndLevel;
import it.polimi.ingsw.model.LeaderCard.LeaderCardRequirements.Requirement;
import it.polimi.ingsw.model.LeaderCard.LeaderCards;
import it.polimi.ingsw.model.LeaderCard.leaderEffects.Effect;
import it.polimi.ingsw.model.LeaderCard.leaderEffects.ExtraSlotLeaderEffect;
import it.polimi.ingsw.model.LeaderCard.leaderEffects.WhiteMarbleLeaderEffect;
import it.polimi.ingsw.model.PlayerResourcesAndCards;
import it.polimi.ingsw.model.ResourceType;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class LeaderCardsTest {
    CardRequirementColor requirement1;
    CardRequirementColor requirement2;
    PlayerResourcesAndCards playerResourcesAndCards;
    HashMap<ResourceType, Integer> hashMap;
    DevCard cardLevel1;
    DevCard cardLevel2;
    DevCard cardLevel3;
    List<DevCard> devCards;


    CardRequirementColorAndLevel req1;
    CardRequirementColorAndLevel req2;
    CardRequirementColorAndLevel req3;

    @BeforeEach
    public void setup() throws NegativeQuantityException {
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
        req1 = new CardRequirementColorAndLevel(3, DevCardColour.GREEN, 2); //Unmet
        req2 = new CardRequirementColorAndLevel(1, DevCardColour.BLUE, 1); //Unmet
        req3 = new CardRequirementColorAndLevel(2, DevCardColour.GREEN, 2); //Met
    }


    @Test
    //Tests the creation of LeaderCards
    public void ctrlCreation() {
        List<LeaderCard> list = new ArrayList<>();
        list.add(new LeaderCard(3, new ArrayList<>(), new ExtraSlotLeaderEffect(ResourceType.COIN, 2)));
        list.add(new LeaderCard(4, new ArrayList<>(), new ExtraSlotLeaderEffect(ResourceType.SERVANT, 1)));
        LeaderCards leaderCards = new LeaderCards(list);

        assertTrue(leaderCards.getActiveCards().isEmpty());
        List<LeaderCard> tmp = leaderCards.getNotPlayedCards();
        for (int i = 0; i < tmp.size(); i++) {
            assertEquals(tmp.get(i).getVictoryPoints(), list.get(i).getVictoryPoints());
            assertTrue(tmp.get(i).getRequirementsListSafe().isEmpty());
            assertTrue(tmp.get(i).getEffect() instanceof ExtraSlotLeaderEffect);
            assertEquals(tmp.get(i).getEffect().extraSlotGetResourceNumber(), list.get(i).getEffect().extraSlotGetResourceNumber());
            assertEquals(tmp.get(i).getEffect().extraSlotGetType(), list.get(i).getEffect().extraSlotGetType());
            assertNotSame(tmp.get(i), list.get(i));
        }


        assertNotSame(tmp, list);
    }

    @Test
    //Tests that a not-hold LeaderCard can't be activated
    public void ctrlActivationCardNotHold() {
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

        //The player doesn't hold the card
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> leaderCards.activateLeaderCard(new LeaderCard(5, new ArrayList<>(), new Effect()), playerResourcesAndCards));
        assertEquals(exception.getMessage(), "The player doesn't have this card!");
    }

    @Test
    //Tests that a LeaderCard is correctly activated
    public void ctrlActivation() {
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

        //The player has the requirements to activate the LeaderCard
        try {
            assertTrue(leaderCards.activateLeaderCard(met, playerResourcesAndCards));
        } catch (UnmetRequirementException e) {
            e.printStackTrace();
        }
        assertEquals(leaderCards.getActiveCards().size(), 1);
        assertTrue(leaderCards.getActiveCards().contains(met));
        assertEquals(leaderCards.getNotPlayedCards().size(), 1);
        assertFalse(leaderCards.getNotPlayedCards().contains(met));
        assertTrue(leaderCards.getNotPlayedCards().contains(unmet));

        //The player tries to activate an already active card
        try {
            assertFalse(leaderCards.activateLeaderCard(met, playerResourcesAndCards));
        } catch (UnmetRequirementException e) {
            e.printStackTrace();
        }
    }

    @Test
    //Tests that a LeaderCard the player doesn't have the requirements to activate is not activated
    public void ctrlActivationRequirementNotMet() {
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

        //The player doesn't have the requirements to activate the card they hold
        UnmetRequirementException exception1 = assertThrows(UnmetRequirementException.class, () -> leaderCards.activateLeaderCard(unmet, playerResourcesAndCards));
        assertEquals(((CardRequirementColorAndLevel) exception1.getUnmetRequirement()).getLevel(), req1.getLevel());
        assertEquals(((CardRequirementColorAndLevel) exception1.getUnmetRequirement()).getCardColour(), req1.getCardColour());
        assertEquals(((CardRequirementColorAndLevel) exception1.getUnmetRequirement()).getQuantity(), req1.getQuantity());
        assertEquals(exception1.getUnmetRequirement(), req1);
        assertEquals(leaderCards.getNotPlayedCards().size(), 2);
        assertEquals(leaderCards.getNotPlayedCards().stream().filter((x -> x.getVictoryPoints() == 3)).count(), 1);
        assertEquals(leaderCards.getNotPlayedCards().stream().filter(((x) -> x.getVictoryPoints() == 4)).count(), 1);
        assertTrue(leaderCards.getNotPlayedCards().contains(unmet));
        assertTrue(leaderCards.getNotPlayedCards().contains(met));
        assertTrue(leaderCards.getActiveCards().isEmpty());
    }

    @Test
    //Tests that a player can't discard a card they don't have
    public void ctrlDiscardNotHoldCard() {
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
    }

    @Test
    //Tests that a LeaderCard is correctly discarded
    public void ctrlDiscard() {
        //This is the default output given by LeaderCards when they are discarded
        HashMap<ResourceType, Integer> defaultOutput = new HashMap<ResourceType, Integer>() {
        };
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

        //The player discards a card they do hold
        HashMap<ResourceType, Integer> output = leaderCards.discardLeaderCard(unmet);
        assertEquals(output.size(), 1);
        assertEquals(output.get(ResourceType.FAITHPOINT), defaultOutput.get(ResourceType.FAITHPOINT));
        assertFalse(leaderCards.getActiveCards().contains(unmet));
        assertFalse(leaderCards.getNotPlayedCards().contains(unmet));
        assertEquals(leaderCards.getNotPlayedCards().size(), 1);
        assertTrue(leaderCards.getNotPlayedCards().contains(met));
        assertEquals(leaderCards.getNotPlayedCards().get(0).getVictoryPoints(), 3);
        assertTrue(leaderCards.getActiveCards().isEmpty());

        try {
            leaderCards.activateLeaderCard(met, playerResourcesAndCards);
        } catch (UnmetRequirementException e) {
            e.printStackTrace();
        }
        assertTrue(leaderCards.getNotPlayedCards().isEmpty());
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> leaderCards.discardLeaderCard(met));
        assertEquals(exception.getMessage(), "Played cards can't be discarded!");

    }

    @Test
    //Tests that a player can't ask for the effect of a card they don't hold
    public void ctrlEffectNotHoldCard() {
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


        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> leaderCards.getEffectFromCard(new LeaderCard(23, new ArrayList<>(), new Effect())));
        assertEquals(exception.getMessage(), "The player can't use the effect of a LeaderCard they don't have!");

    }

    @Test
    //Tests that a player can't ask for the effect of a LeaderCard they haven't activated yet
    public void ctrlEffectCardNotActive() {
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

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> leaderCards.getEffectFromCard(met));
        assertEquals(exception.getMessage(), "The player can't use the effect of a LeaderCard they haven't played yet!");

    }


    @Test
    //Tests that the player gets the effect of an active card correctly
    public void ctrlEffect() throws IllegalActionException {
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

        try {
            leaderCards.activateLeaderCard(met, playerResourcesAndCards);
        } catch (UnmetRequirementException e) {
            e.printStackTrace();
        }

        WhiteMarbleLeaderEffect tmp = (WhiteMarbleLeaderEffect) leaderCards.getEffectFromCard(met);
        //assertTrue(tmp instanceof WhiteMarbleLeaderEffect);
        assertNotNull(tmp);
        assertEquals(tmp.getExtraResourceAmount(), 1);
        Assertions.assertEquals(tmp.getExtraResourceType(), ResourceType.SERVANT);
        //WhiteMarbleLeaderEffect tmp2 = new WhiteMarbleLeaderEffect(ResourceType.SERVANT);
        //assertEquals(tmp, tmp2);
    }

    @Test
    //Tests that LeaderCard with a one-shot effect such as ExtraSlotEffect can't be activated twice
    public void ctrlOneShotCard() {
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
    //Tests that if the player holds only not-played cards, then they get no points out of them
    public void ctrlPointsNotPlayedCards() {
        List<LeaderCard> list = new ArrayList<>();
        List<Requirement> metRequirements = new ArrayList<>();
        metRequirements.add(requirement1);
        metRequirements.add(req3);
        List<Requirement> unmetRequirements = new ArrayList<>();
        unmetRequirements.add(requirement1);
        unmetRequirements.add(req3);
        unmetRequirements.add(req1);
        LeaderCard met = new LeaderCard(3, metRequirements, new WhiteMarbleLeaderEffect(ResourceType.SERVANT));
        list.add(met); //This card can be activated
        LeaderCard met2 = new LeaderCard(4, metRequirements, new Effect());
        list.add(met2);
        LeaderCards leaderCards = new LeaderCards(list);

        //Both not-played
        assertEquals(leaderCards.getLeaderCardsPoints(), 0);
    }

    @Test
    //Tests that both discarded LeaderCards don't give any point
    public void ctrlPointsBothDiscarded() {
        List<Requirement> unmetRequirements = new ArrayList<>();
        unmetRequirements.add(requirement1);
        unmetRequirements.add(req3);
        unmetRequirements.add(req1);
        LeaderCard unmet = new LeaderCard(2, unmetRequirements, new Effect());
        LeaderCard unmet2 = new LeaderCard(1, unmetRequirements, new Effect());

        //Both discarded
        List<LeaderCard> list = new ArrayList<>();
        list.add(unmet);
        list.add(unmet2);
        LeaderCards leaderCards = new LeaderCards(list);
        leaderCards.discardLeaderCard(unmet);
        leaderCards.discardLeaderCard(unmet2);
        assertEquals(leaderCards.getLeaderCardsPoints(), 0);

    }

    @Test
    //Tests that a mix of LeaderCards (one played, one discarded, one not-played) gives the right points
    public void ctrlPointsMix() {
        List<Requirement> metRequirements = new ArrayList<>();
        metRequirements.add(requirement1);
        metRequirements.add(req3);
        List<Requirement> unmetRequirements = new ArrayList<>();
        unmetRequirements.add(requirement1);
        unmetRequirements.add(req3);
        unmetRequirements.add(req1);
        LeaderCard met = new LeaderCard(3, metRequirements, new WhiteMarbleLeaderEffect(ResourceType.SERVANT));
        LeaderCard unmet = new LeaderCard(2, unmetRequirements, new Effect());
        LeaderCard met2 = new LeaderCard(4, metRequirements, new Effect());


        //One Active, One discarded, One not-played
        List<LeaderCard> list = new ArrayList<>();
        list.add(met);
        list.add(unmet);
        list.add(met2);
        LeaderCards leaderCards = new LeaderCards(list);
        leaderCards.discardLeaderCard(met2);
        try {
            leaderCards.activateLeaderCard(met, playerResourcesAndCards);
        } catch (UnmetRequirementException e) {
            e.printStackTrace();
        }
        assertEquals(leaderCards.getLeaderCardsPoints(), 3);
    }

    @Test
    //Tests that LeaderCards give the right points
    public void ctrlPointsBothActive() {
        List<LeaderCard> list = new ArrayList<>();
        List<Requirement> metRequirements = new ArrayList<>();
        metRequirements.add(requirement1);
        metRequirements.add(req3);
        List<Requirement> unmetRequirements = new ArrayList<>();
        unmetRequirements.add(requirement1);
        unmetRequirements.add(req3);
        unmetRequirements.add(req1);
        LeaderCard met = new LeaderCard(3, metRequirements, new WhiteMarbleLeaderEffect(ResourceType.SERVANT));
        list.add(met); //This card can be activated
        LeaderCard met2 = new LeaderCard(4, metRequirements, new Effect());
        list.add(met2);
        LeaderCards leaderCards = new LeaderCards(list);

        //Both active
        try {
            leaderCards.activateLeaderCard(met, playerResourcesAndCards);
            leaderCards.activateLeaderCard(met2, playerResourcesAndCards);
        } catch (UnmetRequirementException e) {
            e.printStackTrace();
        }
        assertEquals(leaderCards.getLeaderCardsPoints(), 7);
    }

    @Test
    public void ctrlRequirementsReturnCardNotHold() {
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

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> leaderCards.getLeaderCardRequirements(new LeaderCard(23, new ArrayList<>(), new Effect())));
        assertEquals(exception.getMessage(), "The player doesn't hold this card!");
    }

    @Test
    //Tests that LeaderCards get the list of the requirements of the card it does hold
    public void ctrlRequirementsReturn() {
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

        //It asks the requirements of a not-played card
        List<Requirement> tmp = leaderCards.getLeaderCardRequirements(unmet);
        assertEquals(unmetRequirements.size(), tmp.size());
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
    //Tests that not-one-shot card are not labeled as such
    public void ctrlNotOneShotCard() {
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

    @Test
    //Tests that a one-shot card is labeled as such
    public void ctrlOneShotCardTrue() {
        List<LeaderCard> list = new ArrayList<>();
        List<Requirement> metRequirements = new ArrayList<>();
        metRequirements.add(requirement1);
        metRequirements.add(req3);
        Effect effect = new ExtraSlotLeaderEffect(ResourceType.SERVANT, 2);
        LeaderCard met = new LeaderCard(3, metRequirements, effect);
        list.add(met); //This card can be activated
        LeaderCards leaderCards = new LeaderCards(list);

        assertTrue(leaderCards.getAlreadyUsedOneShotCard().isEmpty());

        try {
            leaderCards.activateLeaderCard(met, playerResourcesAndCards);
        } catch (UnmetRequirementException e) {
            e.printStackTrace();
        }

        assertTrue(leaderCards.getAlreadyUsedOneShotCard().isEmpty());

        Effect effect2 = new Effect();
        try {
            effect2 = leaderCards.getEffectFromCard(met);
        } catch (IllegalActionException e) {
            e.printStackTrace();
        }

        assertNotSame(effect2, effect);
        assertEquals(effect2, effect);
        assertFalse(leaderCards.getAlreadyUsedOneShotCard().isEmpty());
        assertEquals(leaderCards.getAlreadyUsedOneShotCard().size(), 1);
        assertTrue(leaderCards.getAlreadyUsedOneShotCard().contains(met));

        IllegalActionException exception = assertThrows(IllegalActionException.class, () -> leaderCards.getEffectFromCard(met));
        assertEquals(exception.getMessage(), "This Card can only be used once: it has already been used!");
        assertFalse(leaderCards.getAlreadyUsedOneShotCard().isEmpty());
        assertEquals(leaderCards.getAlreadyUsedOneShotCard().size(), 1);
        assertTrue(leaderCards.getAlreadyUsedOneShotCard().contains(met));
    }


    @Test
    public void ctrlCloningList() {
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

        List<LeaderCard> l2 = LeaderCards.cloneList(list);

        assertNotSame(l2, list);
        assertTrue(l2.containsAll(list));
        assertTrue(list.containsAll(l2));
        for (int i = 0; i < list.size(); i++) {
            assertEquals(list.get(i), l2.get(i));
            assertNotSame(list.get(i), l2.get(i));
        }

        l2.remove(0);
        assertFalse(l2.containsAll(list));
    }

    @Test
    public void ctrlEqualsTrue() {
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
        LeaderCards original = new LeaderCards(list);
        LeaderCards clone = new LeaderCards(list);

        assertEquals(clone, original);
        assertTrue(clone.getNotPlayedCards().containsAll(original.getNotPlayedCards()));
        assertTrue(original.getNotPlayedCards().containsAll(clone.getNotPlayedCards()));
        assertEquals(original.getActiveCards().isEmpty(), clone.getActiveCards().isEmpty());
        assertTrue(original.getActiveCards().isEmpty());
        assertEquals(original.isAreNotPlayedCardsSet(), clone.isAreNotPlayedCardsSet());
        assertTrue(original.isAreNotPlayedCardsSet());
        assertEquals(original.getAlreadyUsedOneShotCard().isEmpty(), clone.getAlreadyUsedOneShotCard().isEmpty());
        assertTrue(original.getAlreadyUsedOneShotCard().isEmpty());
    }

    @Test
    public void ctrlEqualsTrueOnePlayed() throws UnmetRequirementException {
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
        LeaderCards original = new LeaderCards(list);
        original.activateLeaderCard(met, playerResourcesAndCards);

        LeaderCards clone = new LeaderCards(list);
        clone.activateLeaderCard(met, playerResourcesAndCards);


        assertEquals(clone, original);
        assertTrue(clone.getNotPlayedCards().containsAll(original.getNotPlayedCards()));
        assertTrue(original.getNotPlayedCards().containsAll(clone.getNotPlayedCards()));
        assertTrue(clone.getActiveCards().containsAll(original.getActiveCards()));
        assertTrue(original.getActiveCards().containsAll(clone.getActiveCards()));
        assertEquals(original.isAreNotPlayedCardsSet(), clone.isAreNotPlayedCardsSet());
        assertTrue(original.isAreNotPlayedCardsSet());
        assertEquals(original.getAlreadyUsedOneShotCard().isEmpty(), clone.getAlreadyUsedOneShotCard().isEmpty());
        assertTrue(original.getAlreadyUsedOneShotCard().isEmpty());
    }

    @Test
    void ctrlEqualsTrueOneShotPlayed() throws UnmetRequirementException, IllegalActionException {
        List<LeaderCard> list = new ArrayList<>();
        List<Requirement> metRequirements = new ArrayList<>();
        metRequirements.add(requirement1);
        metRequirements.add(req3);
        List<Requirement> unmetRequirements = new ArrayList<>();
        unmetRequirements.add(requirement1);
        unmetRequirements.add(req3);
        unmetRequirements.add(req1);
        LeaderCard met = new LeaderCard(3, metRequirements, new ExtraSlotLeaderEffect(ResourceType.STONE, 2));
        LeaderCard unmet = new LeaderCard(4, unmetRequirements, new Effect());
        list.add(met); //This card can be activated
        list.add(unmet); //This card can't be activated
        LeaderCards original = new LeaderCards(list);
        original.activateLeaderCard(met, playerResourcesAndCards);
        LeaderCards clone = new LeaderCards(list);
        clone.activateLeaderCard(met, playerResourcesAndCards);

        original.getEffectFromCard(met);
        clone.getEffectFromCard(met);


        assertEquals(clone, original);
        assertTrue(clone.getNotPlayedCards().containsAll(original.getNotPlayedCards()));
        assertTrue(original.getNotPlayedCards().containsAll(clone.getNotPlayedCards()));
        assertTrue(clone.getActiveCards().containsAll(original.getActiveCards()));
        assertTrue(original.getActiveCards().containsAll(clone.getActiveCards()));
        assertEquals(original.isAreNotPlayedCardsSet(), clone.isAreNotPlayedCardsSet());
        assertTrue(original.isAreNotPlayedCardsSet());
        assertTrue(clone.getAlreadyUsedOneShotCard().containsAll(original.getAlreadyUsedOneShotCard()));
        assertTrue(original.getAlreadyUsedOneShotCard().containsAll(clone.getAlreadyUsedOneShotCard()));
        assertTrue(original.getAlreadyUsedOneShotCard().contains(met));
    }

    @Test
    public void ctrlEqualsTrueBase() {
        LeaderCards original = new LeaderCards();
        LeaderCards clone = new LeaderCards();

        assertEquals(clone, original);
        assertEquals(clone.getNotPlayedCards().isEmpty(), original.getNotPlayedCards().isEmpty());
        assertTrue(clone.getNotPlayedCards().isEmpty());
        assertEquals(original.getActiveCards().isEmpty(), clone.getActiveCards().isEmpty());
        assertTrue(original.getActiveCards().isEmpty());
        assertEquals(original.isAreNotPlayedCardsSet(), clone.isAreNotPlayedCardsSet());
        assertFalse(original.isAreNotPlayedCardsSet());
        assertEquals(original.getAlreadyUsedOneShotCard().isEmpty(), clone.getAlreadyUsedOneShotCard().isEmpty());
        assertTrue(original.getAlreadyUsedOneShotCard().isEmpty());
    }

    @Test
    public void ctrlEqualsFalse() {
        List<LeaderCard> list = new ArrayList<>();
        List<Requirement> metRequirements = new ArrayList<>();
        metRequirements.add(requirement1);
        metRequirements.add(req3);
        List<Requirement> unmetRequirements = new ArrayList<>();
        unmetRequirements.add(requirement1);
        unmetRequirements.add(req3);
        unmetRequirements.add(req1);
        LeaderCard met = new LeaderCard(3, metRequirements, new ExtraSlotLeaderEffect(ResourceType.STONE, 2));
        LeaderCard unmet = new LeaderCard(4, unmetRequirements, new Effect());
        list.add(met); //This card can be activated
        LeaderCards original = new LeaderCards(list);
        list.add(unmet); //This card can't be activated
        LeaderCards clone = new LeaderCards(list);

        assertNotEquals(original, clone);
        assertFalse(original.getNotPlayedCards().containsAll(clone.getNotPlayedCards()));
    }

    @Test
    public void ctrlEqualsFalseOnePlayed() throws UnmetRequirementException {
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
        LeaderCards original = new LeaderCards(list);
        original.activateLeaderCard(met, playerResourcesAndCards);

        LeaderCards clone = new LeaderCards(list);

        assertNotEquals(clone, original);
        assertTrue(clone.getActiveCards().isEmpty());
        assertTrue(original.getActiveCards().contains(met));
    }

    @Test
    public void ctrlEqualsFalseOneShotPlayed() throws UnmetRequirementException, IllegalActionException {
        List<LeaderCard> list = new ArrayList<>();
        List<Requirement> metRequirements = new ArrayList<>();
        metRequirements.add(requirement1);
        metRequirements.add(req3);
        List<Requirement> unmetRequirements = new ArrayList<>();
        unmetRequirements.add(requirement1);
        unmetRequirements.add(req3);
        unmetRequirements.add(req1);
        LeaderCard met = new LeaderCard(3, metRequirements, new ExtraSlotLeaderEffect(ResourceType.STONE, 2));
        LeaderCard unmet = new LeaderCard(4, unmetRequirements, new Effect());
        list.add(met); //This card can be activated
        list.add(unmet); //This card can't be activated
        LeaderCards original = new LeaderCards(list);
        original.activateLeaderCard(met, playerResourcesAndCards);
        LeaderCards clone = new LeaderCards(list);

        original.getEffectFromCard(met);

        assertNotEquals(clone, original);
        assertTrue(clone.getActiveCards().isEmpty());
        assertTrue(original.getActiveCards().contains(met));
        assertTrue(clone.getAlreadyUsedOneShotCard().isEmpty());
        assertTrue(original.getAlreadyUsedOneShotCard().contains(met));
    }

    @Test
    public void ctrlCloning() {
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
        LeaderCards original = new LeaderCards(list);
        LeaderCards clone = new LeaderCards(original);

        assertEquals(original, clone);
    }

    @Test
    public void ctrlCloningOnePlayed() throws UnmetRequirementException {
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
        LeaderCards original = new LeaderCards(list);
        original.activateLeaderCard(met, playerResourcesAndCards);

        LeaderCards clone = new LeaderCards(original);

        assertEquals(clone, original);
    }

    @Test
    public void ctrlCloningOneShotPlayed() throws IllegalActionException, UnmetRequirementException {
        List<LeaderCard> list = new ArrayList<>();
        List<Requirement> metRequirements = new ArrayList<>();
        metRequirements.add(requirement1);
        metRequirements.add(req3);
        List<Requirement> unmetRequirements = new ArrayList<>();
        unmetRequirements.add(requirement1);
        unmetRequirements.add(req3);
        unmetRequirements.add(req1);
        LeaderCard met = new LeaderCard(3, metRequirements, new ExtraSlotLeaderEffect(ResourceType.STONE, 2));
        LeaderCard unmet = new LeaderCard(4, unmetRequirements, new Effect());
        list.add(met); //This card can be activated
        list.add(unmet); //This card can't be activated
        LeaderCards original = new LeaderCards(list);
        original.activateLeaderCard(met, playerResourcesAndCards);
        original.getEffectFromCard(met);
        LeaderCards clone = new LeaderCards(original);

        assertEquals(original, clone);

    }

    @Test
    public void ctrlCloningBase() {
        LeaderCards original = new LeaderCards();
        LeaderCards clone = new LeaderCards(original);

        assertEquals(original, clone);
    }

    @Test
    public void ctrlGetEffectWithIndexCardIsActive() throws UnmetRequirementException {
        List<LeaderCard> list = new ArrayList<>();
        List<Requirement> metRequirements = new ArrayList<>();
        metRequirements.add(requirement1);
        metRequirements.add(req3);
        LeaderCard met = new LeaderCard(3, metRequirements, new WhiteMarbleLeaderEffect(ResourceType.COIN));
        LeaderCard met2 = new LeaderCard(4, metRequirements, new ExtraSlotLeaderEffect(ResourceType.COIN, 2));
        list.add(met);
        list.add(met2);
        LeaderCards leaderCards = new LeaderCards(list);
        leaderCards.activateLeaderCard(met, playerResourcesAndCards);
        leaderCards.activateLeaderCard(met2, playerResourcesAndCards);

        assertEquals(leaderCards.getEffectFromCard(1), new ExtraSlotLeaderEffect(ResourceType.COIN, 2));
    }

    @Test
    public void ctrlGetEffectWithIndexOutOfBound() {
        List<LeaderCard> list = new ArrayList<>();
        List<Requirement> metRequirements = new ArrayList<>();
        metRequirements.add(requirement1);
        metRequirements.add(req3);
        LeaderCard met = new LeaderCard(3, metRequirements, new WhiteMarbleLeaderEffect(ResourceType.COIN));
        LeaderCard met2 = new LeaderCard(4, metRequirements, new ExtraSlotLeaderEffect(ResourceType.COIN, 2));
        list.add(met);
        list.add(met2);
        LeaderCards leaderCards = new LeaderCards(list);

        Exception e = assertThrows(IllegalArgumentException.class, () -> leaderCards.getEffectFromCard(1));
        assertEquals(e.getMessage(), "The player doesn't hold this card (the given index is out of bound)!");
    }
}
