package it.polimi.ingsw.LeaderCardTest;

import it.polimi.ingsw.DevCards.DevCard;
import it.polimi.ingsw.DevCards.DevCardColour;
import it.polimi.ingsw.DevCards.DevSlots;
import it.polimi.ingsw.LeaderCard.leaderEffects.ExtraSlotLeaderEffect;
import it.polimi.ingsw.LeaderCardRequirementsTests.*;
import it.polimi.ingsw.LeaderCard.LeaderCard;
import it.polimi.ingsw.LeaderCard.leaderEffects.Effect;
import it.polimi.ingsw.LeaderCard.leaderEffects.WhiteMarbleLeaderEffect;
import it.polimi.ingsw.PlayerBoard;
import it.polimi.ingsw.PlayerResourcesAndCards;
import it.polimi.ingsw.ResourceType;
import it.polimi.ingsw.exceptions.NegativeQuantityException;
import it.polimi.ingsw.exceptions.UnmetRequirementException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

public class LeaderCardTest {
    private static final ResourceType[] resources = new ResourceType[]{ResourceType.COIN, ResourceType.STONE, ResourceType.SERVANT, ResourceType.FAITHPOINT,  ResourceType.SHIELD};

    @Test
    //Tests correct creation of the LeaderCard (the default creation)
    public void ctrlLeaderCardCreation(){
        List<Requirement> requirements = new ArrayList<>();
        try {
            requirements.add(new CardRequirementColor(DevCardColour.BLUE, 3));
            requirements.add(new CardRequirementColorAndLevel(2, DevCardColour.GREEN, 2));
        } catch (NegativeQuantityException e) {
            e.printStackTrace();
        }
        Effect effect = new WhiteMarbleLeaderEffect(ResourceType.SERVANT);

        NullPointerException exception = assertThrows(NullPointerException.class, () -> new LeaderCard(2, null, effect));
        assertEquals(exception.getMessage(), "The requirement list can't be a null pointer!");
        exception = assertThrows(NullPointerException.class, () -> new LeaderCard(2, null, null));
        assertTrue(exception.getMessage().equals("The requirement list can't be a null pointer!") || exception.getMessage().equals("The effect can't be a null pointer!"));
        exception = assertThrows(NullPointerException.class, () -> new LeaderCard(2, requirements, null));
        assertEquals(exception.getMessage(), "The effect can't be a null pointer!");

        LeaderCard leaderCard = new LeaderCard(3, requirements, effect);

        List<Requirement> tmp = leaderCard.getRequirementsList();

        assertNotSame(requirements, tmp);
        assertEquals(((CardRequirementColor) tmp.get(0)).getQuantity(), 3);
        assertEquals(((CardRequirementColor) tmp.get(0)).getCardColour(), DevCardColour.BLUE);
        assertEquals(((CardRequirementColorAndLevel) tmp.get(1)).getLevel(), 2);
        assertEquals(((CardRequirementColorAndLevel) tmp.get(1)).getCardColour(), DevCardColour.GREEN);
        assertEquals(((CardRequirementColorAndLevel) tmp.get(1)).getQuantity(),2);

        assertEquals(leaderCard.getVictoryPoints(), 3);
        assertEquals(((WhiteMarbleLeaderEffect)leaderCard.getEffect()).getExtraResourceType(), ((WhiteMarbleLeaderEffect)effect).getExtraResourceType());

        HashMap<ResourceType, Integer> output = leaderCard.getOutputWhenDiscarded();
        assertFalse(output.isEmpty());
        assertEquals(output.size(), 1);
        for(ResourceType rT: resources)
            if(rT.equals(ResourceType.FAITHPOINT))
                assertEquals(output.get(rT), 1);
            else
                assertEquals(output.get(rT), null);
    }

    @Test
    //Tests if the LeaderCard correctly checks for its requirements
    public void ctrlCardRequirements() throws NegativeQuantityException {
        CardRequirementColor requirement1;
        CardRequirementColor requirement2;
        PlayerResourcesAndCards playerResourcesAndCards;
        HashMap<ResourceType,Integer> hashMap;
        DevCard cardLevel1;
        DevCard cardLevel2;
        DevCard cardLevel3;
        List<DevCard> devCards;
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
        if(tmp instanceof CardRequirementColorAndLevel ){
            assertEquals(((CardRequirementColorAndLevel) tmp).getLevel(),req1.getLevel());
            assertEquals( ((CardRequirementColorAndLevel) tmp).getCardColour(), req1.getCardColour());
            assertEquals(((CardRequirementColorAndLevel) tmp).getQuantity(), req1.getQuantity());
        } else{
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
        exception = assertThrows(UnmetRequirementException.class, () -> finalLeaderCard1.checkRequirements(playerResourcesAndCards));
        //assertTrue(exception.getUnmetRequirement().equals(req1) || exception.getUnmetRequirement().equals(requirement2) || exception.getUnmetRequirement().equals(req2));
        if(tmp instanceof CardRequirementColorAndLevel ){
            if(((CardRequirementColorAndLevel) tmp).getLevel() == 3) {
                assertEquals(((CardRequirementColorAndLevel) tmp).getLevel(), req1.getLevel());
                assertEquals(((CardRequirementColorAndLevel) tmp).getCardColour(), req1.getCardColour());
                assertEquals(((CardRequirementColorAndLevel) tmp).getQuantity(), req1.getQuantity());
            }else{
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

        for(ResourceType rT: resources)
            assertEquals(outputClone.get(rT), outputOriginal.get(rT));
        assertNotSame(outputClone, outputOriginal);

        List<Requirement> reqOriginal = original.getRequirementsList();
        List<Requirement> reqClone = original.getRequirementsList();
        assertNotSame(reqClone, reqOriginal);

        for(Requirement req: reqOriginal)
            assertEquals(req, reqClone.get(reqClone.indexOf(req)));

        assertEquals(reqClone, reqOriginal);

        WhiteMarbleLeaderEffect effectOriginal = (WhiteMarbleLeaderEffect) original.getEffect();
        WhiteMarbleLeaderEffect effectClone = (WhiteMarbleLeaderEffect) clone.getEffect();

        assertEquals(effectClone.getExtraResourceAmount(), effectOriginal.getExtraResourceAmount());
        assertEquals(effectClone.getExtraResourceType(), effectOriginal.getExtraResourceType());
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
        Requirement requirement1 = new CardRequirementColor(DevCardColour.GREEN,2); //Met
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
        for(int i = 0; i < list.size(); i++){
            assertEquals(copy.get(i), list.get(i));
            assertNotSame(copy.get(i), list.get(i));
        }
    }
}
