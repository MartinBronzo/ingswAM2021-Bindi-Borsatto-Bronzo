package it.polimi.ingsw.LeaderCardTest;

import it.polimi.ingsw.DevCards.DevCard;
import it.polimi.ingsw.DevCards.DevCardColour;
import it.polimi.ingsw.DevCards.DevSlots;
import it.polimi.ingsw.LeaderCardRequirementsTests.*;
import it.polimi.ingsw.LeaderCard.LeaderCard;
import it.polimi.ingsw.LeaderCard.leaderEffects.Effect;
import it.polimi.ingsw.LeaderCard.leaderEffects.WhiteMarbleLeaderEffect;
import it.polimi.ingsw.PlayerBoard;
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
        for(Requirement req: requirements)
            assertEquals(req, tmp.get(tmp.indexOf(req)));

        assertEquals(leaderCard.getVictoryPoints(), 3);
        assertEquals(leaderCard.getEffect(), effect);

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

        Effect effect = new WhiteMarbleLeaderEffect(ResourceType.SERVANT);
        LeaderCard leaderCard;
        List<Requirement> requirements = new ArrayList<>();

        //All the requirements are met
        requirements.add(requirement1);
        requirements.add(req3);
        leaderCard = new LeaderCard(3, requirements, effect);
        try {
            assertTrue(leaderCard.checkRequirements(playerBoard));
        } catch (UnmetRequirementException e) {
            e.printStackTrace();
        }

        //One requirement is unmet
        requirements.add(req1);
        Collections.shuffle(requirements);
        leaderCard = new LeaderCard(3, requirements, effect);
        LeaderCard finalLeaderCard = leaderCard;
        UnmetRequirementException exception = assertThrows(UnmetRequirementException.class, () -> finalLeaderCard.checkRequirements(playerBoard));
        assertEquals(exception.getUnmetRequirement(), req1);

        //Two requirements are unmet
        requirements.add(requirement2);
        Collections.shuffle(requirements);
        leaderCard = new LeaderCard(3, requirements, effect);
        LeaderCard finalLeaderCard1 = leaderCard;
        exception = assertThrows(UnmetRequirementException.class, () -> finalLeaderCard1.checkRequirements(playerBoard));
        assertTrue(exception.getUnmetRequirement().equals(req1) || exception.getUnmetRequirement().equals(requirement2));

        //All the requirements are unmet
        requirements = new ArrayList<>();
        requirements.add(requirement2);
        requirements.add(req1);
        requirements.add(req2);
        Collections.shuffle(requirements);
        leaderCard = new LeaderCard(3, requirements, effect);
        exception = assertThrows(UnmetRequirementException.class, () -> finalLeaderCard1.checkRequirements(playerBoard));
        assertTrue(exception.getUnmetRequirement().equals(req1) || exception.getUnmetRequirement().equals(requirement2) || exception.getUnmetRequirement().equals(req2));

    }
}
