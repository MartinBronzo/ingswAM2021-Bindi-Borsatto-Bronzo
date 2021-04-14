package it.polimi.ingsw.MainBoardTestLB;

import it.polimi.ingsw.LeaderCard.LeaderCard;
import it.polimi.ingsw.LeaderCard.leaderEffects.Effect;
import it.polimi.ingsw.LeaderCardDeck;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

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

   /*@Test
    public void ctrlCreationWithConfigFile(){
        leaderCardDeck = new LeaderCardDeck("LeaderCardConfig.xml");
   }*/


}
