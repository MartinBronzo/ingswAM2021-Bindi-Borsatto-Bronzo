package it.polimi.ingsw.model.MainBoardTestLB;

import it.polimi.ingsw.exceptions.IllegalActionException;
import it.polimi.ingsw.exceptions.NegativeQuantityException;
import it.polimi.ingsw.model.DevCards.DevCardColour;
import it.polimi.ingsw.model.MainBoard;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

public class DevGridMainBoardMethodsTest {
    MainBoard mainBoard;

    @BeforeEach
    public void setup() throws NegativeQuantityException, ParserConfigurationException, IOException, SAXException {
        mainBoard = new MainBoard();
    }

    @Test
    //Tests that the inside methods of devGrid are called: getDevCard with position
    public void ctrlGetDevCardPosition(){
        assertThrows(IllegalArgumentException.class, () -> mainBoard.getDevCardFromDeckInDevGrid(155, 155));
    }

    @Test
    //Tests that the inside methods of devGrid are called: getDevCard with level and color
    public void ctrlGetDevCardLevCol(){
        assertThrows(IllegalArgumentException.class, () -> mainBoard.getDevCardFromDeckInDevGrid(155, DevCardColour.PURPLE));
    }

    @Test
    //Tests that the inside methods of devGrid are called: drawDevCard with position (Exception thrown in the method of DevGrid)
    public void ctrlDrawDevCardPosInsideException(){
        assertThrows(IllegalArgumentException.class, () -> mainBoard.drawDevCardFromDeckInDevGrid(155, 155));
    }

    @Test
    //Tests that the inside methods of devGrid are called: drawDevCard with position (Exception thrown in the method of main board)
    public void ctrlDrawDevCardPosOutsideException(){
        boolean tmp = false;
        while(!tmp){
            try {
                this.mainBoard.drawDevCardFromDeckInDevGrid(1,1);
            }catch(IllegalActionException e){
                tmp = true;
            }
        }
        //Now we are sure that the DevDeck at [1][1] is empty
        Exception e = assertThrows(IllegalActionException.class, () -> mainBoard.drawDevCardFromDeckInDevGrid(1, 1));
        assertEquals(e.getMessage(), "drawDevCardFromDeck: deck is empty");
    }

    @Test
    //Tests that the inside methods of devGrid are called: drawDevCard with position (Exception thrown in the method of DevGrid)
    public void ctrlDrawDevCardLevColInsideException(){
        assertThrows(IllegalArgumentException.class, () -> mainBoard.drawDevCardFromDeckInDevGrid(155, DevCardColour.PURPLE));
    }

    @Test
    //Tests that the inside methods of devGrid are called: getDrawableCards
    public void ctrlGetDrawableCards(){
        //When the main board is not changed, yet, (like at the beginning of the game) there are still drawable cards
        assertFalse(mainBoard.getDrawableCardsInDevGrid().isEmpty());
    }

    @Test
    //Tests that the inside methods of devGrid are called: getDevDeckSize
    public void ctrlGetDevDeckSize(){
        //When the main board is not changed, yet, (like at the beginning of the game) there are still cards with the specified color
        assertTrue(mainBoard.getDevDeckSizeInDevGrid(DevCardColour.PURPLE) > 0);
    }



}
