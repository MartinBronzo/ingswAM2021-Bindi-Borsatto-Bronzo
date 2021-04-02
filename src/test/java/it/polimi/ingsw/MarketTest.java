package it.polimi.ingsw;

import it.polimi.ingsw.exceptions.IllegalParameterException;
import it.polimi.ingsw.exceptions.NegativeQuantityException;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class MarketTest {
    Effect effect = new Effect();
    Market market = new Market();

    MarketTest() throws IllegalParameterException {
    }

    @Test
    void IllegalBuilderTest() {
        assertThrows(IllegalParameterException.class, () -> new Market(2,2,2,2,2,2));
        assertThrows(IllegalParameterException.class, () -> new Market(2,2,-3,6,3,3));
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
        assertThrows(IllegalParameterException.class, () -> market.moveRow(-1,effect));
        assertThrows(IllegalParameterException.class, () -> market.moveRow(3,effect));
        assertThrows(NullPointerException.class, () -> market.moveRow(1,null));
    }

    @Test
    void moveRowTest() {
    }

    /*Testing the known illegal calls to the moveColumn Method*/
    @Test
    void moveRowColumnsTest() {
        assertThrows(IllegalParameterException.class, () -> market.moveColumn(-1,effect));
        assertThrows(IllegalParameterException.class, () -> market.moveColumn(4,effect));
        assertThrows(NullPointerException.class, () -> market.moveColumn(1,null));
    }

    @Test
    void moveColumnTest() {
    }


    /*to string test is used to study the correct behaviour of the market due the initial configuration of the market grid is randomized*/
    @Test
    void toStringTest() throws IllegalParameterException, NegativeQuantityException {
        System.out.println(market.toString());
        System.out.println(market.moveColumn(1, effect).toString());
        System.out.println(market.toString());
        System.out.println(market.moveRow(2, effect).toString());
        System.out.println(market.toString());
    }
}