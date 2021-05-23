package it.polimi.ingsw.model.MainBoardTest;

import it.polimi.ingsw.exceptions.NegativeQuantityException;
import it.polimi.ingsw.model.MainBoard;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertThrows;

public class MarketMainBoardMethodsTest {
    MainBoard mainBoard;

    @BeforeEach
    public void setup() throws NegativeQuantityException, ParserConfigurationException, IOException, SAXException {
        mainBoard = new MainBoard(4); //The number of players here is irrelevant
    }

    @Test
    //Tests that the inside methods of market are called: moveRow
    public void ctrlMoveRow() {
        assertThrows(IllegalArgumentException.class, () -> mainBoard.moveRowInMarket(155, new ArrayList<>()));
    }

    @Test
    //Tests that the inside methods of market are called: moveColumn
    public void ctrlMoveColumn() {
        assertThrows(IllegalArgumentException.class, () -> mainBoard.moveColumnInMarket(5, new ArrayList<>()));
    }

    @Test
    //Tests that the inside methods of market are called: getResourcesInRow
    public void ctrlGetResInRow() {
        assertThrows(IllegalArgumentException.class, () -> mainBoard.getResourcesFromRowInMarket(5, new ArrayList<>()));
    }

    @Test
    //Tests that the inside methods of market are called: getResourcesInColumn
    public void ctrlGetResInColumn() {
        assertThrows(IllegalArgumentException.class, () -> mainBoard.getResourcesFromColumnInMarket(5, new ArrayList<>()));
    }

    @Test
    //Tests that the inside methods of market are called: getMarble
    public void ctrlGetMarble() {
        assertThrows(IllegalArgumentException.class, () -> mainBoard.getMarbleInMarket(155, 155));
    }


}
