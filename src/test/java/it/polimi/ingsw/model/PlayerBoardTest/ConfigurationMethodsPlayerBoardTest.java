package it.polimi.ingsw.model.PlayerBoardTest;

import it.polimi.ingsw.exceptions.EndOfGameException;
import it.polimi.ingsw.exceptions.IllegalActionException;
import it.polimi.ingsw.exceptions.LastVaticanReportException;
import it.polimi.ingsw.exceptions.NegativeQuantityException;
import it.polimi.ingsw.model.BaseProduction;
import it.polimi.ingsw.model.devCards.DevCard;
import it.polimi.ingsw.model.devCards.DevCardColour;
import it.polimi.ingsw.model.faithTrack.FaithTrack;
import it.polimi.ingsw.model.board.PlayerBoard;
import it.polimi.ingsw.model.resources.ResourceType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

public class ConfigurationMethodsPlayerBoardTest {
    PlayerBoard playerBoard;
    List<ResourceType> resources;

    @BeforeEach
    public void setup() throws ParserConfigurationException, IOException, SAXException {
        playerBoard = new PlayerBoard();
        playerBoard.setPlayerFaithLevelFaithTrack(FaithTrack.instance(new File(this.getClass().getResource("/XMLs/FaithTrackConfig.xml").getFile())));
        resources = new ArrayList<>();
        resources.add(ResourceType.COIN);
        resources.add(ResourceType.STONE);
        resources.add(ResourceType.SERVANT);
        resources.add(ResourceType.SHIELD);
    }

    @Test
    //Tests whether the player is correctly given the resources and Faith steps they deserve at the beginning of the game: two resources
    public void ctrlExtraResourcesAtBeginningMultipleResources() throws IllegalActionException, LastVaticanReportException {
        Map<ResourceType, ArrayList<Integer>> map = new HashMap<>();
        ArrayList<Integer> tmp = new ArrayList<>();
        tmp.add(2);
        tmp.add(2);
        assertEquals(tmp.get(0), 2);
        assertEquals(tmp.get(1), 2);
        map.put(ResourceType.STONE, tmp);
        ArrayList<Integer> tmp2 = new ArrayList<>();
        tmp2.add(1);
        tmp2.add(1);
        assertEquals(tmp2.get(0), 1);
        assertEquals(tmp2.get(1), 1);
        map.put(ResourceType.COIN, tmp2);

        playerBoard.setBeginningExtraResources(map, 2);

        assertEquals(playerBoard.getResourceFromDepot(ResourceType.STONE), 2);
        assertEquals(playerBoard.getResourceFromDepot(ResourceType.COIN), 1);
        assertEquals(playerBoard.getResourceTypeFromShelf(1), ResourceType.COIN);
        assertEquals(playerBoard.getResourceTypeFromShelf(2), ResourceType.STONE);

        assertEquals(playerBoard.getPositionOnFaithTrack(), 2);
    }

    @Test
    //Tests whether the player is correctly given the resources and Faith steps they deserve at the beginning of the game: two resources
    public void ctrlExtraResourcesAtBeginningNoResources() throws IllegalActionException, LastVaticanReportException {
        Map<ResourceType, ArrayList<Integer>> map = new HashMap<>();

        playerBoard.setBeginningExtraResources(map, 0);

        for (ResourceType rT : resources)
            assertEquals(playerBoard.getResourceFromDepot(rT), 0);

        assertEquals(playerBoard.getPositionOnFaithTrack(), 0);
    }

    @Test
    //Tests that at the creation the player board is empty
    public void ctrlCreation() {
        PlayerBoard playerBoard1 = new PlayerBoard();

        assertNull(playerBoard1.getFaithTrack());
        assertEquals(playerBoard1.getPositionOnFaithTrack(), 0);
        for (ResourceType rT : resources) {
            assertEquals(playerBoard.getResourceFromDepot(rT), 0);
            assertEquals(playerBoard.getResourceFromStrongbox(rT), 0);
        }
        assertTrue(playerBoard1.getAllDevCards().isEmpty());
        assertTrue(playerBoard1.getNotPlayedLeaderCards().isEmpty());
        assertTrue(playerBoard1.getActiveLeaderCards().isEmpty());
        BaseProduction bP = playerBoard.getBaseProduction();
        assertNotNull(bP);
        assertTrue(bP.getInputForcedResources().isEmpty());
        assertTrue(bP.getOutputForcedResources().isEmpty());
        assertTrue(bP.getInputHashMap().isEmpty());
        assertTrue(bP.getOutputHashMap().isEmpty());
    }

    @Test
    public void ctrlPlayerBoardCloning() throws IllegalActionException, NegativeQuantityException, EndOfGameException, ParserConfigurationException, IOException, SAXException {
        playerBoard.addResourceToDepot(ResourceType.COIN, 1, 1);
        playerBoard.setPlayerFaithLevelFaithTrack(FaithTrack.instance(new File(this.getClass().getResource("/XMLs/FaithTrackConfig.xml").getFile())));
        playerBoard.addCardToDevSlot(1, new DevCard(1, DevCardColour.PURPLE, 1, new HashMap<>(), new HashMap<>(), new HashMap<>(), "url"));

        PlayerBoard copy = new PlayerBoard(playerBoard);

        assertNotSame(copy, playerBoard);

        assertEquals(copy.getPositionOnFaithTrack(), playerBoard.getPositionOnFaithTrack());
        assertEquals(copy.getResourceFromDepot(ResourceType.COIN), playerBoard.getResourceFromDepot(ResourceType.COIN));
        assertEquals(copy.getResourceTypeFromShelf(1), playerBoard.getResourceTypeFromShelf(1));
        assertEquals(playerBoard.getResourceFromStrongbox(ResourceType.COIN), copy.getResourceFromStrongbox(ResourceType.COIN));
        assertEquals(playerBoard.getAllDevCards().size(), copy.getAllDevCards().size(), 1);
        assertEquals(((DevCard) playerBoard.getAllDevCards().toArray()[0]).getUrl(), ((DevCard) copy.getAllDevCards().toArray()[0]).getUrl());
        assertEquals(copy.getNotPlayedLeaderCards().size(), playerBoard.getNotPlayedLeaderCards().size());
        assertEquals(copy.getBaseProduction().getInputHashMap(), playerBoard.getBaseProduction().getInputHashMap());
    }

}
