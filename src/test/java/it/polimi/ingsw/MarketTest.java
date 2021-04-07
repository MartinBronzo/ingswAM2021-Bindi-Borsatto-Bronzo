package it.polimi.ingsw;

import it.polimi.ingsw.Market.Market;
import it.polimi.ingsw.exceptions.NegativeQuantityException;
import it.polimi.ingsw.leaderEffects.Effect;
import it.polimi.ingsw.marble.*;
import org.junit.jupiter.api.Test;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

/*Testing Market is difficult because Market configuration is generated randomically so I used instanceof to
test the correct behaviour of the HashMap and to string to see the correct transition of the grid*/
class MarketTest {
    Effect effect = new Effect();
    File MarketConfigFile = new File("MarketConfig.xsd.xml");
    Market market = new Market(MarketConfigFile);
    Random rnd=new Random();
    final ResourceType coin = ResourceType.COIN;
    final ResourceType shield = ResourceType.SHIELD;
    final ResourceType servant = ResourceType.SERVANT;
    final ResourceType stone = ResourceType.STONE;
    final ResourceType faith = ResourceType.FAITHPOINT;

    MarketTest() throws IllegalArgumentException, ParserConfigurationException, NegativeQuantityException, SAXException, IOException {
    }

    @Test
    void IllegalBuilderTest() {
        assertThrows(IllegalArgumentException.class, () -> new Market(2,2,2,2,2,2));
        assertThrows(IllegalArgumentException.class, () -> new Market(2,2,-3,6,3,3));
    }

    /*used to test not classic number of marbles in the game */
    @Test
    void LegalBuilderTest(){
        assertDoesNotThrow(() -> new Market(6,0,2,1,1,3));
        assertDoesNotThrow(() -> new Market(0,0,0,0,0,13));
    }

    /*Testing the known illegal calls to the moveRow Method*/
    @Test
    void moveRowExceptionsTest() {
        assertThrows(IllegalArgumentException.class, () -> market.moveRow(-1,effect));
        assertThrows(IllegalArgumentException.class, () -> market.moveRow(3,effect));
        assertThrows(NullPointerException.class, () -> market.moveRow(1,null));
    }


    @Test
    void moveRowTest() throws IllegalArgumentException {
        int i =rnd.nextInt(3);
        Marble[] marbles = {market.getMarbleInTheGrid(i ,0), market.getMarbleInTheGrid(i ,1), market.getMarbleInTheGrid(i ,2), market.getMarbleInTheGrid(i ,3)};
        int blueCounter=0;
        int greyCounter=0;
        int purpleCounter=0;
        int redCounter=0;
        int yellowCounter=0;
        for (Marble marble:marbles) {
            if (marble instanceof BlueMarble){
                blueCounter++;
            } else if (marble instanceof GreyMarble){
                greyCounter++;
            } else if (marble instanceof PurpleMarble){
                purpleCounter++;
            } else if (marble instanceof RedMarble){
                redCounter++;
            } else if (marble instanceof YellowMarble){
                yellowCounter++;
            }
        }
        HashMap<ResourceType, Integer> resourceMap = market.moveRow(i, effect);

        assertEquals(blueCounter,resourceMap.getOrDefault(shield,0));
        assertEquals(greyCounter,resourceMap.getOrDefault(stone,0));
        assertEquals(purpleCounter,resourceMap.getOrDefault(servant,0));
        assertEquals(redCounter,resourceMap.getOrDefault(faith,0));
        assertEquals(yellowCounter,resourceMap.getOrDefault(coin,0));

    }

    /*Testing the known illegal calls to the moveColumn Method*/
    @Test
    void moveColumnExceptionsTest() {
        assertThrows(IllegalArgumentException.class, () -> market.moveColumn(-1,effect));
        assertThrows(IllegalArgumentException.class, () -> market.moveColumn(4,effect));
        assertThrows(NullPointerException.class, () -> market.moveColumn(1,null));
    }

    @Test
    void moveColumnTest() throws IllegalArgumentException {
        int i =rnd.nextInt(4);
        Marble[] marbles = {market.getMarbleInTheGrid(0 ,i), market.getMarbleInTheGrid(1,i), market.getMarbleInTheGrid(2,i)};
        int blueCounter=0;
        int greyCounter=0;
        int purpleCounter=0;
        int redCounter=0;
        int yellowCounter=0;
        for (Marble marble:marbles) {
            if (marble instanceof BlueMarble){
                blueCounter++;
            } else if (marble instanceof GreyMarble){
                greyCounter++;
            } else if (marble instanceof PurpleMarble){
                purpleCounter++;
            } else if (marble instanceof RedMarble){
                redCounter++;
            } else if (marble instanceof YellowMarble){
                yellowCounter++;
            }
        }
        HashMap<ResourceType, Integer> resourceMap = market.moveColumn(i, effect);

        assertEquals(blueCounter,resourceMap.getOrDefault(shield,0));
        assertEquals(greyCounter,resourceMap.getOrDefault(stone,0));
        assertEquals(purpleCounter,resourceMap.getOrDefault(servant,0));
        assertEquals(redCounter,resourceMap.getOrDefault(faith,0));
        assertEquals(yellowCounter,resourceMap.getOrDefault(coin,0));
    }


    /*to string test is used to study the correct behaviour of the market due the initial configuration of the market grid is randomized*/
    @Test
    void toStringTest() throws IllegalArgumentException {
        System.out.println(market.toString());
        System.out.println(market.moveColumn(1, effect).toString());
        System.out.println(market.toString());
        System.out.println(market.moveRow(2, effect).toString());
        System.out.println(market.toString());
    }
}