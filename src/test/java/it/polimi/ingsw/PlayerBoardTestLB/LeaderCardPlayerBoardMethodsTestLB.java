package it.polimi.ingsw.PlayerBoardTestLB;

import it.polimi.ingsw.DevCards.DevCard;
import it.polimi.ingsw.DevCards.DevCardColour;
import it.polimi.ingsw.FaithTrack.FaithTrack;
import it.polimi.ingsw.FaithTrack.ReportNumOrder;
import it.polimi.ingsw.LeaderCard.LeaderCard;
import it.polimi.ingsw.LeaderCard.leaderEffects.Effect;
import it.polimi.ingsw.LeaderCard.leaderEffects.ExtraSlotLeaderEffect;
import it.polimi.ingsw.LeaderCard.leaderEffects.WhiteMarbleLeaderEffect;
import it.polimi.ingsw.LeaderCard.LeaderCardRequirements.CardRequirementColor;
import it.polimi.ingsw.LeaderCard.LeaderCardRequirements.CardRequirementColorAndLevel;
import it.polimi.ingsw.LeaderCard.LeaderCardRequirements.Requirement;
import it.polimi.ingsw.PlayerBoard;
import it.polimi.ingsw.PlayerResourcesAndCards;
import it.polimi.ingsw.ResourceType;
import it.polimi.ingsw.exceptions.IllegalActionException;
import it.polimi.ingsw.exceptions.LastVaticanReportException;
import it.polimi.ingsw.exceptions.NegativeQuantityException;
import it.polimi.ingsw.exceptions.UnmetRequirementException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class LeaderCardPlayerBoardMethodsTestLB {
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
    PlayerBoard playerBoard;


    @BeforeEach
    public void setup() throws NegativeQuantityException {
        requirement1 = new CardRequirementColor(DevCardColour.GREEN,2); //Met
        requirement2 = new CardRequirementColor(DevCardColour.BLUE,1); //Unmet
        hashMap=new HashMap<>();
        cardLevel1=new DevCard(1, DevCardColour.GREEN,1,hashMap,hashMap,hashMap,"abc");
        cardLevel2=new DevCard(1,DevCardColour.GREEN,2,hashMap,hashMap,hashMap,"abc");
        cardLevel3=new DevCard(1,DevCardColour.GREEN,3,hashMap,hashMap,hashMap,"abc");
        req1 = new CardRequirementColorAndLevel(3, DevCardColour.GREEN, 2); //Unmet
        req2 = new CardRequirementColorAndLevel(1, DevCardColour.BLUE, 1); //Unmet
        req3 = new CardRequirementColorAndLevel(2, DevCardColour.GREEN, 2); //Met
        playerBoard = new PlayerBoard();
        playerBoard.addCardToDevSlot(0, cardLevel1);
        playerBoard.addCardToDevSlot(1, cardLevel2);
        playerBoard.addCardToDevSlot(2, cardLevel3);
    }

    @Test
    //Tests how LeaderCards are activated
    public void ctrlLeaderCardActivation(){
        List<LeaderCard> list = new ArrayList<>();
        List<Requirement> metRequirements = new ArrayList<>();
        metRequirements.add(requirement1);
        //metRequirements.add(req3);
        LeaderCard met = new LeaderCard(3, metRequirements, new Effect());
        list.add(met); //This card can be activated
        playerBoard.setNotPlayedLeaderCards(list);

        assertTrue(playerBoard.getActiveLeaderCards().isEmpty());

        try {
            playerBoard.activateLeaderCard(met);
        } catch (UnmetRequirementException e) {
            e.printStackTrace();
        }

        List<LeaderCard> activeCards = playerBoard.getActiveLeaderCards();
        assertEquals(activeCards.size(), 1);
        assertEquals(activeCards.get(0).getVictoryPoints(), 3);

    }

    @Test
    //Tests how LeaderCards are discarded
    public void ctrlLeaderCardDiscard(){
        List<LeaderCard> list = new ArrayList<>();
        List<Requirement> metRequirements = new ArrayList<>();
        metRequirements.add(requirement1);
        //metRequirements.add(req3);
        LeaderCard met = new LeaderCard(3, metRequirements, new Effect());
        list.add(met); //This card can be activated
        playerBoard.setNotPlayedLeaderCards(list);
        playerBoard.setPlayerFaithLevelFaithTrack(FaithTrack.instance(ReportNumOrder.instance()));
        assertEquals(playerBoard.getPositionOnFaithTrack(), 0);

        assertNotSame(playerBoard.getNotPlayedLeaderCards(), list);

        try {
            playerBoard.discardLeaderCard(met);
        } catch (LastVaticanReportException e) {
            e.printStackTrace();
        }

        assertTrue(playerBoard.getActiveLeaderCards().isEmpty());
        assertTrue(playerBoard.getNotPlayedLeaderCards().isEmpty());
        assertEquals(playerBoard.getPositionOnFaithTrack(), 1); //The player was on position number 0
    }

    @Test
    //Tests that the correct Cards are returned when it is ask for not-played leader card
    public void ctrlNotPlayedLeaderCards(){
        List<LeaderCard> list = new ArrayList<>();
        List<Requirement> metRequirements = new ArrayList<>();
        metRequirements.add(requirement1);
        //metRequirements.add(req3);
        LeaderCard met = new LeaderCard(3, metRequirements, new Effect());
        list.add(met); //This card can be activated
        met = new LeaderCard(4, metRequirements, new Effect());
        list.add(met);
        playerBoard.setNotPlayedLeaderCards(list);
        playerBoard.setPlayerFaithLevelFaithTrack(FaithTrack.instance(ReportNumOrder.instance()));

        /*
        assertEquals(playerBoard.getNotPlayedLeaderCards().get(0).getVictoryPoints(), 3);
        assertEquals(playerBoard.getNotPlayedLeaderCards().get(1).getVictoryPoints(), 4);*/
        assertNotSame(playerBoard.getNotPlayedLeaderCards(), list);
        assertEquals(playerBoard.getNotPlayedLeaderCards().size(), list.size());
        assertTrue(playerBoard.getNotPlayedLeaderCards().containsAll(list));
        assertTrue(list.containsAll(playerBoard.getNotPlayedLeaderCards()));
        assertNotSame(playerBoard.getNotPlayedLeaderCards().get(0), list.get(0));
        assertNotSame(playerBoard.getNotPlayedLeaderCards().get(1), list.get(1));
    }

    @Test
    //Tests that the correct Cards are returned when it is ask for played leader card
    public void ctrlPlayedLeaderCards(){
        List<LeaderCard> list = new ArrayList<>();
        List<Requirement> metRequirements = new ArrayList<>();
        metRequirements.add(requirement1);
        //metRequirements.add(req3);
        LeaderCard met = new LeaderCard(3, metRequirements, new Effect());
        list.add(met); //This card can be activated
        met = new LeaderCard(4, metRequirements, new Effect());
        list.add(met);
        assertEquals(list.size(), 2);
        playerBoard.setNotPlayedLeaderCards(list);
        playerBoard.setPlayerFaithLevelFaithTrack(FaithTrack.instance(ReportNumOrder.instance()));

        try {
            playerBoard.activateLeaderCard(met);
        } catch (UnmetRequirementException e) {
            e.printStackTrace();
        }

        assertEquals(list.size(), 2);

        assertEquals(playerBoard.getNotPlayedLeaderCards().size(), 1);
        assertEquals(playerBoard.getNotPlayedLeaderCards().get(0), list.get(0)); //I activated the last card I put in list
        assertEquals(playerBoard.getActiveLeaderCards().size(), 1);
        assertEquals(playerBoard.getActiveLeaderCards().get(0), list.get(1));
    }

    @Test
    //Tests that the PlayerBoard deals correctly with one-shot cards:
    public void ctrlOneShotCards(){
        List<LeaderCard> list = new ArrayList<>();
        List<Requirement> metRequirements = new ArrayList<>();
        metRequirements.add(requirement1);
        //metRequirements.add(req3);
        Effect effect = new ExtraSlotLeaderEffect(ResourceType.COIN, 2);
        LeaderCard met = new LeaderCard(3, metRequirements, effect);
        list.add(met); //This card can be activated
        playerBoard.setNotPlayedLeaderCards(list);
        playerBoard.setPlayerFaithLevelFaithTrack(FaithTrack.instance(ReportNumOrder.instance()));

        assertTrue(playerBoard.getAlreadyUsedOneShotCard().isEmpty());
        assertTrue(playerBoard.getActiveLeaderCards().isEmpty());
        assertEquals(playerBoard.getNotPlayedLeaderCards().size(), 1);
        assertTrue(playerBoard.getNotPlayedLeaderCards().containsAll(list) && list.containsAll(playerBoard.getNotPlayedLeaderCards()));
        assertNotSame(playerBoard.getNotPlayedLeaderCards(), list);
        assertTrue(playerBoard.getNotPlayedLeaderCards().contains(met));

        try {
            assertTrue(playerBoard.activateLeaderCard(met));
        } catch (UnmetRequirementException e) {
            e.printStackTrace();
        }
        assertTrue(playerBoard.getAlreadyUsedOneShotCard().isEmpty());
        assertEquals(playerBoard.getActiveLeaderCards().size(), 1);
        assertTrue(playerBoard.getActiveLeaderCards().contains(met));
        assertTrue(playerBoard.getNotPlayedLeaderCards().isEmpty());

        Effect effect1 = new Effect();

        try {
            effect1 = playerBoard.getEffectFromCard(met);
        } catch (IllegalActionException e) {
            e.printStackTrace();
        }

        assertEquals(playerBoard.getAlreadyUsedOneShotCard().size(), 1);
        assertTrue(playerBoard.getAlreadyUsedOneShotCard().contains(met));
        assertEquals(playerBoard.getActiveLeaderCards().size(), 1);
        assertTrue(playerBoard.getActiveLeaderCards().contains(met));
        assertTrue(playerBoard.getNotPlayedLeaderCards().isEmpty());

        LeaderCard met2 = met;
        IllegalActionException exception = assertThrows(IllegalActionException.class, () -> playerBoard.getEffectFromCard(met2));
        assertEquals(exception.getMessage(), "This Card can only be used once: it has already been used!");

        assertEquals(playerBoard.getAlreadyUsedOneShotCard().size(), 1);
        assertTrue(playerBoard.getAlreadyUsedOneShotCard().contains(met));
        assertEquals(playerBoard.getActiveLeaderCards().size(), 1);
        assertTrue(playerBoard.getActiveLeaderCards().contains(met));
        assertTrue(playerBoard.getNotPlayedLeaderCards().isEmpty());
    }

    @Test
    public void ctrlNotOneShotCard(){
        List<LeaderCard> list = new ArrayList<>();
        List<Requirement> metRequirements = new ArrayList<>();
        metRequirements.add(requirement1);
        Effect effect = new WhiteMarbleLeaderEffect(ResourceType.SERVANT);
        LeaderCard met = new LeaderCard(3, metRequirements, effect);
        list.add(met); //This card can be activated
        playerBoard.setNotPlayedLeaderCards(list);
        playerBoard.setPlayerFaithLevelFaithTrack(FaithTrack.instance(ReportNumOrder.instance()));

        assertTrue(playerBoard.getAlreadyUsedOneShotCard().isEmpty());

        try {
            playerBoard.activateLeaderCard(met);
        } catch (UnmetRequirementException e) {
            e.printStackTrace();
        }

        Effect effect2 = new Effect();
        try {
            effect2 = playerBoard.getEffectFromCard(met);
        } catch (IllegalActionException e) {
            e.printStackTrace();
        }

        assertTrue(playerBoard.getAlreadyUsedOneShotCard().isEmpty());

        Effect effect3 = new Effect();
        try {
            effect3 = playerBoard.getEffectFromCard(met);
        } catch (IllegalActionException e) {
            e.printStackTrace();
        }
        assertTrue(playerBoard.getAlreadyUsedOneShotCard().isEmpty());
    }

    @Test
    //Tests that a player gets the right points from their active LeaderCards
    public void ctrlLeaderCardPoints(){
        List<LeaderCard> list = new ArrayList<>();
        List<Requirement> metRequirements = new ArrayList<>();
        metRequirements.add(requirement1);
        Effect effect = new WhiteMarbleLeaderEffect(ResourceType.SERVANT);
        LeaderCard met = new LeaderCard(3, metRequirements, effect);
        list.add(met); //This card can be activated
        LeaderCard dumbCard = new LeaderCard(10, new ArrayList<>(), new Effect());
        list.add(dumbCard);
        playerBoard.setNotPlayedLeaderCards(list);
        playerBoard.setPlayerFaithLevelFaithTrack(FaithTrack.instance(ReportNumOrder.instance()));

        try {
            playerBoard.activateLeaderCard(met);
        } catch (UnmetRequirementException e) {
            e.printStackTrace();
        }

        assertEquals(playerBoard.getLeaderCardsPoints(), 3);
    }

    @Test
    //Tests that the requirements of a player's active LeaderCard are returned
    public void ctrlActiveLeaderCardReturnedRequirements(){
        List<LeaderCard> list = new ArrayList<>();
        List<Requirement> metRequirements = new ArrayList<>();
        metRequirements.add(requirement1);
        Effect effect = new WhiteMarbleLeaderEffect(ResourceType.SERVANT);
        LeaderCard met = new LeaderCard(3, metRequirements, effect);
        list.add(met); //This card can be activated
        playerBoard.setNotPlayedLeaderCards(list);
        playerBoard.setPlayerFaithLevelFaithTrack(FaithTrack.instance(ReportNumOrder.instance()));

        try {
            playerBoard.activateLeaderCard(met);
        } catch (UnmetRequirementException e) {
            e.printStackTrace();
        }

        List<Requirement> copy = playerBoard.getLeaderCardRequirements(met);

        assertNotSame(copy, metRequirements);
        assertEquals(copy.size(), metRequirements.size());
        for(int i = 0; i < list.size(); i++){
            assertEquals(copy.get(i), metRequirements.get(i));
            assertNotSame(copy.get(i), metRequirements.get(i));
        }
    }

    @Test
    //Tests that the requirements of a player's not-played LeaderCard are returned
    public void ctrlNotPlayedCardReturnedRequirements(){
        List<LeaderCard> list = new ArrayList<>();
        List<Requirement> unmetRequirements = new ArrayList<>();
        unmetRequirements.add(req1);
        Effect effect = new WhiteMarbleLeaderEffect(ResourceType.SERVANT);
        LeaderCard unmet = new LeaderCard(3, unmetRequirements, effect);
        list.add(unmet); //This card can be activated
        playerBoard.setNotPlayedLeaderCards(list);
        playerBoard.setPlayerFaithLevelFaithTrack(FaithTrack.instance(ReportNumOrder.instance()));


        List<Requirement> copy = playerBoard.getLeaderCardRequirements(unmet);

        assertNotSame(copy, unmetRequirements);
        assertEquals(copy.size(), unmetRequirements.size());
        for(int i = 0; i < list.size(); i++){
            assertEquals(copy.get(i), unmetRequirements.get(i));
            assertNotSame(copy.get(i), unmetRequirements.get(i));
        }
    }

    @Test
    //Tests that the requirements of a LeaderCard the player doesn't hold are not returned
    public void ctrlNotHoldCardReturnedRequirements(){
        List<LeaderCard> list = new ArrayList<>();
        List<Requirement> unmetRequirements = new ArrayList<>();
        unmetRequirements.add(req1);
        Effect effect = new WhiteMarbleLeaderEffect(ResourceType.SERVANT);
        LeaderCard unmet = new LeaderCard(3, unmetRequirements, effect);
        list.add(unmet); //This card can be activated
        playerBoard.setNotPlayedLeaderCards(list);
        playerBoard.setPlayerFaithLevelFaithTrack(FaithTrack.instance(ReportNumOrder.instance()));

        LeaderCard fakeLD = new LeaderCard(2, unmetRequirements, effect);

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> playerBoard.getLeaderCardRequirements(fakeLD));
        assertEquals(exception.getMessage(), "The player doesn't hold this card!");
    }

    @Test
    //Tests that the PlayerBoard answers correctly when it is asked whether a LeaderCard is active: the player doesn't hold the card
    public void ctrlNotHoldActivationQuestion(){
        List<LeaderCard> list = new ArrayList<>();
        List<Requirement> metRequirements = new ArrayList<>();
        metRequirements.add(requirement1);
        Effect effect = new WhiteMarbleLeaderEffect(ResourceType.SERVANT);
        LeaderCard met = new LeaderCard(3, metRequirements, effect);
        list.add(met); //This card can be activated
        playerBoard.setNotPlayedLeaderCards(list);
        LeaderCard dumbLD = new LeaderCard(4, metRequirements, effect);

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> playerBoard.isLeaderCardActive(dumbLD));
        assertEquals(exception.getMessage(), "The player doesn't hold this card!");
    }

    @Test
    //Tests that the PlayerBoard answers correctly when it is asked whether a LeaderCard is active: the player does hold the card ant it is active
    public void ctrlActiveActivationQuestion(){
        List<LeaderCard> list = new ArrayList<>();
        List<Requirement> metRequirements = new ArrayList<>();
        metRequirements.add(requirement1);
        Effect effect = new WhiteMarbleLeaderEffect(ResourceType.SERVANT);
        LeaderCard met = new LeaderCard(3, metRequirements, effect);
        list.add(met); //This card can be activated
        playerBoard.setNotPlayedLeaderCards(list);

        try {
            playerBoard.activateLeaderCard(met);
        } catch (UnmetRequirementException e) {
            e.printStackTrace();
        }

        assertTrue(playerBoard.isLeaderCardActive(met));
    }

    @Test
    //Tests that the PlayerBoard answers correctly when it is asked whether a LeaderCard is active: the player does hold the card but it is not active
    public void ctrlNotPlayedActivationQuestion(){
        List<LeaderCard> list = new ArrayList<>();
        List<Requirement> metRequirements = new ArrayList<>();
        metRequirements.add(requirement1);
        Effect effect = new WhiteMarbleLeaderEffect(ResourceType.SERVANT);
        LeaderCard met = new LeaderCard(3, metRequirements, effect);
        list.add(met); //This card can be activated
        playerBoard.setNotPlayedLeaderCards(list);

        assertFalse(playerBoard.isLeaderCardActive(met));
    }

    @Test
    //Tests the correct discard of LeaderCards as if we were at the beginning of the game
    public void ctrlLeaderCardDiscardAtBeginning(){
        List<LeaderCard> list = new ArrayList<>();
        List<Requirement> metRequirements = new ArrayList<>();
        metRequirements.add(requirement1);
        LeaderCard met = new LeaderCard(3, metRequirements, new Effect());
        list.add(met); //This card can be activated
        playerBoard.setNotPlayedLeaderCards(list);
        playerBoard.setPlayerFaithLevelFaithTrack(FaithTrack.instance(ReportNumOrder.instance()));

        assertTrue(playerBoard.getActiveLeaderCards().isEmpty());
        assertTrue(playerBoard.getNotPlayedLeaderCards().contains(met));
        assertEquals(playerBoard.getNotPlayedLeaderCards().size(), 1);

        assertEquals(playerBoard.getPositionOnFaithTrack(), 0);

        playerBoard.discardLeaderCardAtTheBeginning(met);

        assertTrue(playerBoard.getActiveLeaderCards().isEmpty());
        assertTrue(playerBoard.getNotPlayedLeaderCards().isEmpty());
        assertEquals(playerBoard.getPositionOnFaithTrack(), 0); //The player was on position number 0

    }


}
