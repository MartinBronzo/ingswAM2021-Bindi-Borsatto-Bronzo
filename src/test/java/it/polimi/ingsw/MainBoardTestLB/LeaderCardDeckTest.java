package it.polimi.ingsw.MainBoardTestLB;

import it.polimi.ingsw.DevCards.DevCardColour;
import it.polimi.ingsw.LeaderCard.LeaderCard;
import it.polimi.ingsw.LeaderCard.LeaderCardRequirements.CardRequirementColor;
import it.polimi.ingsw.LeaderCard.LeaderCardRequirements.CardRequirementColorAndLevel;
import it.polimi.ingsw.LeaderCard.LeaderCardRequirements.CardRequirementResource;
import it.polimi.ingsw.LeaderCard.LeaderCardRequirements.Requirement;
import it.polimi.ingsw.LeaderCard.leaderEffects.*;
import it.polimi.ingsw.LeaderCardDeck;
import it.polimi.ingsw.ResourceType;
import it.polimi.ingsw.exceptions.NegativeQuantityException;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class LeaderCardDeckTest {
    LeaderCardDeck leaderCardDeck;
    static LeaderCard lD1;
    static LeaderCard lD2;
    static LeaderCard lD3;
    List<LeaderCard> tmp;

    @BeforeAll
    public static void setup(){
        lD1 = new LeaderCard(4, new ArrayList<>(), new Effect());
        lD2 = new LeaderCard(5, new ArrayList<>(), new Effect());
        lD3 = new LeaderCard(6, new ArrayList<>(), new Effect());
    }

    @Test
    public void ctrlCreation(){
        leaderCardDeck = new LeaderCardDeck();

        assertTrue(leaderCardDeck.isEmpty());
    }

    @Test
    public void ctrlOneInsertion(){
        leaderCardDeck = new LeaderCardDeck();
        leaderCardDeck.addLeaderCard(lD1);

        assertEquals(leaderCardDeck.drawFromDeck(), lD1);
        assertTrue(leaderCardDeck.isEmpty());
    }

    @Test
    public void ctrlMultipleInsertionDraw(){
        leaderCardDeck = new LeaderCardDeck();
        leaderCardDeck.addLeaderCard(lD1);
        leaderCardDeck.addLeaderCard(lD2);
        leaderCardDeck.addLeaderCard(lD3);

        List<LeaderCard> tmp = new ArrayList<>();

        assertEquals(leaderCardDeck.size(), 3);
        tmp.add((LeaderCard) leaderCardDeck.drawFromDeck());
        assertEquals(leaderCardDeck.size(), 2);
        tmp.add((LeaderCard) leaderCardDeck.drawFromDeck());
        assertEquals(leaderCardDeck.size(), 1);
        tmp.add((LeaderCard) leaderCardDeck.drawFromDeck());

        assertTrue(tmp.contains(lD1));
        assertTrue(tmp.contains(lD2));
        assertTrue(tmp.contains(lD3));

        assertTrue(leaderCardDeck.isEmpty());
    }

    @Test
    public void ctrlMultipleInsertionGetFirst(){
        leaderCardDeck = new LeaderCardDeck();
        leaderCardDeck.addLeaderCard(lD1);
        leaderCardDeck.addLeaderCard(lD2);
        leaderCardDeck.addLeaderCard(lD3);

        tmp = new ArrayList<>();

        assertEquals(leaderCardDeck.size(), 3);
        tmp.add((LeaderCard) leaderCardDeck.getFirst());
        assertEquals(leaderCardDeck.size(), 2);
        tmp.add((LeaderCard) leaderCardDeck.getFirst());
        assertEquals(leaderCardDeck.size(), 1);
        tmp.add((LeaderCard) leaderCardDeck.getFirst());

        assertTrue(tmp.contains(lD1));
        assertTrue(tmp.contains(lD2));
        assertTrue(tmp.contains(lD3));

        assertTrue(leaderCardDeck.isEmpty());
    }

    @Test
    public void voidCtrlCreationWithList(){
        tmp = new ArrayList<>();
        tmp.add(lD1);
        tmp.add(lD2);
        tmp.add(lD3);

        leaderCardDeck = new LeaderCardDeck(tmp);

        assertEquals(leaderCardDeck.size(), 3);

        List<LeaderCard> results = new ArrayList<>();

        results.add((LeaderCard) leaderCardDeck.drawFromDeck());
        assertEquals(leaderCardDeck.size(), 2);
        results.add((LeaderCard) leaderCardDeck.drawFromDeck());
        assertEquals(leaderCardDeck.size(), 1);
        results.add((LeaderCard) leaderCardDeck.drawFromDeck());

        assertTrue(results.contains(lD1));
        assertTrue(results.contains(lD2));
        assertTrue(results.contains(lD3));

        assertTrue(leaderCardDeck.isEmpty());

    }

   @Test
    public void ctrlCopyLeaderCards(){
        tmp = new ArrayList<>();
        tmp.add(lD1);
        tmp.add(lD2);
        tmp.add(lD3);
        leaderCardDeck = new LeaderCardDeck(tmp);
        List<LeaderCard> list = leaderCardDeck.getCopyLeaderCards();

        assertNotSame(list, tmp);
        assertTrue(list.containsAll(tmp));
       assertTrue(tmp.containsAll(list));
   }

   @Test
    public void ctrlShuffle(){
       tmp = new ArrayList<>();
       tmp.add(lD1);
       tmp.add(lD2);
       tmp.add(lD3);
       leaderCardDeck = new LeaderCardDeck(tmp);

       List<LeaderCard> list;

        for(int i = 0; i < Math.random()* 10; i++)
            for(int j = 0; j < Math.random() * 10; j++){
                leaderCardDeck.shuffle();
                list = leaderCardDeck.getCopyLeaderCards();
                assertNotSame(list, tmp);
                assertTrue(list.containsAll(tmp));
                assertTrue(tmp.containsAll(list));
            }
   }

   @Test
   //The file read contains an example of four LeaderCards
    public void ctrlCreationWithConfigFile() throws ParserConfigurationException, NegativeQuantityException, SAXException, IOException {
        leaderCardDeck = new LeaderCardDeck(new File("LeaderCardConfig.xml"));
        tmp = new ArrayList<>();

        List<Requirement> reqList = new ArrayList<>();
        reqList.add(new CardRequirementColor(DevCardColour.BLUE, 1));
        reqList.add(new CardRequirementColor(DevCardColour.PURPLE, 1));
        tmp.add(new LeaderCard(2, reqList, new DiscountLeaderEffect(ResourceType.SHIELD, 1)));

        reqList = new ArrayList<>();
        reqList.add(new CardRequirementResource(ResourceType.COIN, 5));
        tmp.add(new LeaderCard(3, reqList, new ExtraSlotLeaderEffect(ResourceType.STONE, 2)));

       reqList = new ArrayList<>();
       reqList.add(new CardRequirementColor(DevCardColour.YELLOW, 2));
       reqList.add(new CardRequirementColor(DevCardColour.BLUE, 1));
       tmp.add(new LeaderCard(5, reqList, new WhiteMarbleLeaderEffect(ResourceType.SERVANT)));

       reqList = new ArrayList<>();
       reqList.add(new CardRequirementColorAndLevel(2, DevCardColour.YELLOW, 1));
       tmp.add(new LeaderCard(4, reqList, new ExtraProductionLeaderEffect(ResourceType.SHIELD, 1)));

       List<LeaderCard> result = leaderCardDeck.getCopyLeaderCards();

       assertNotSame(result, tmp);
       assertTrue(result.containsAll(tmp));
       assertTrue(tmp.containsAll(result));
   }


}
