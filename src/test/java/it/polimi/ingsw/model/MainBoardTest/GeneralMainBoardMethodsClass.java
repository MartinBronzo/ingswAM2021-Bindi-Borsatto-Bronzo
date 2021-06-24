package it.polimi.ingsw.model.MainBoardTest;

import it.polimi.ingsw.exceptions.IllegalActionException;
import it.polimi.ingsw.exceptions.LastVaticanReportException;
import it.polimi.ingsw.exceptions.NegativeQuantityException;
import it.polimi.ingsw.model.devCards.DevGrid;
import it.polimi.ingsw.model.faithTrack.*;
import it.polimi.ingsw.model.Interfaces.Observer;
import it.polimi.ingsw.model.leaderCard.LeaderCardDeck;
import it.polimi.ingsw.model.board.MainBoard;
import it.polimi.ingsw.model.market.Market;
import it.polimi.ingsw.model.board.PlayerBoard;
import it.polimi.ingsw.model.resources.ResourceType;
import it.polimi.ingsw.model.soloGame.SoloBoard;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class GeneralMainBoardMethodsClass {
    private MainBoard mainBoard;

    @BeforeEach
    public void setup() throws ParserConfigurationException, IOException, SAXException {
        mainBoard = new MainBoard(4);
    }

    @Test
    public void ctrlWrongPlayersNumber() {
        IllegalArgumentException e = assertThrows(IllegalArgumentException.class, () -> new MainBoard(-1));
        assertEquals(e.getMessage(), "The number of players is illegal!");
    }

    @Test
    //Tests that MainBoard is created correctly: the MainBoard was indeed created
    public void ctrlCreationHasHappen() {
        assertNotNull(mainBoard);
    }

    @Test
    //Tests that MainBoard is created correctly: the FaithTrack is the right one
    public void ctrlCreationFaithTrack() throws ParserConfigurationException, IOException, SAXException {
        FaithTrack inMainBoard = mainBoard.getFaithTrack();
        FaithTrack usualFaithTrack = FaithTrack.instance(new File("FaithTrackConfig.xml"));

        assertTrue(inMainBoard.lighterEquals(usualFaithTrack));
    }

    @Test
    //Tests that MainBoard is created correctly: the LeaderCards are the right one
    public void ctrlCreationLeaderCards() throws NegativeQuantityException, ParserConfigurationException, IOException, SAXException {
        LeaderCardDeck inMB = (LeaderCardDeck) mainBoard.getLeaderCardsDeck();
        LeaderCardDeck usual = new LeaderCardDeck(LeaderCardDeck.initLeaderCards(new File("LeaderCardConfig.xml")));

        assertEquals(inMB, usual);
    }

    @Test
    public void discardResources() throws LastVaticanReportException {
        HashMap<ResourceType, Integer> discardMap = new HashMap<>();
        discardMap.put(ResourceType.COIN, 1);
        mainBoard.discardResources(discardMap, mainBoard.getPlayerBoard(0));

        assertEquals(0, mainBoard.getPlayerBoard(0).getPositionOnFaithTrack());
        assertEquals(1, mainBoard.getPlayerBoard(1).getPositionOnFaithTrack());
        assertEquals(1, mainBoard.getPlayerBoard(2).getPositionOnFaithTrack());
        assertEquals(1, mainBoard.getPlayerBoard(3).getPositionOnFaithTrack());
    }

    @Test
    public void discardResourcesSolo() throws LastVaticanReportException, IOException, SAXException, ParserConfigurationException {
        mainBoard = new SoloBoard();
        HashMap<ResourceType, Integer> discardMap = new HashMap<>();
        discardMap.put(ResourceType.COIN, 1);
        mainBoard.discardResources(discardMap, mainBoard.getPlayerBoard(0));

        assertEquals(0, mainBoard.getPlayerBoard(0).getPositionOnFaithTrack());
        assertEquals(1, mainBoard.getLorenzoFaithTrackPosition());

    }

    @Test
    //Tests that MainBoard is created correctly: the DevGrid exists
    public void ctrlCreationDevDeck() {
        DevGrid inMB = mainBoard.getDevGrid();

        assertNotNull(inMB);
    }

    @Test
    //Tests that MainBoard is created correctly: the Market exists
    public void ctrlCreationMarket() {
        Market inMB = mainBoard.getMarket();

        assertNotNull(inMB);
    }

    @Test
    //Tests that MainBoard is created correctly: the number of player is right
    public void ctrlCreationPlayerNum() {
        int inMB = mainBoard.getNumberOfPlayers();

        assertEquals(inMB, 4);
        assertEquals(mainBoard.getPlayerBoardsNumber(), 4);
    }

    @Test
    //Tests that MainBoard is created correctly: the number of LeaderCards to give is right
    public void ctrlCreationLeaderCardsToGiveNum() {
        int inMB = mainBoard.getNumberOfLeaderCardsToGive();

        assertEquals(inMB, 4);
    }

    @Test
    //Tests that MainBoard is created correctly: all the player get the right FaithTrack
    public void ctrlPlayerBoardsFaithTrack() throws ParserConfigurationException, IOException, SAXException {
        FaithTrack usual = FaithTrack.instance(new File("FaithTrackConfig.xml"));
        for (int i = 0; i < 4; i++)
            assertTrue(mainBoard.getPlayerBoard(i).getFaithTrack().lighterEquals(usual));
    }

    @Test
    //Tests that MainBoard is created correctly: all the player are given the right PopeTiles as the default configuration for PopeTiles dictates
    public void ctrlPlayerPopeTiles() {
        List<PopeTile> usual = new ArrayList<>();
        usual.add(new PopeTile(2, ReportNum.REPORT1));
        usual.add(new PopeTile(3, ReportNum.REPORT2));
        usual.add(new PopeTile(4, ReportNum.REPORT3));

        PlayerBoard tmp;
        for (int i = 0; i < 4; i++) {
            tmp = mainBoard.getPlayerBoard(i);
            List<PopeTile> list = tmp.getPopeTile();
            //Default configuration
            assertEquals(list.size(), usual.size());
            for (int j = 0; j < 3; j++)
                assertEquals(list.get(j), usual.get(j));
        }

    }

    @Test
    //Tests that MainBoard is created correctly: the extra Faith points given to players at the beginning of the game is correct
    public void ctrlExtraFaithPointsAtGameBeginning() {
        int[] inMB = mainBoard.getExtraFaithPointsAtBeginning();
        int[] usual = new int[]{0, 0, 1, 1};

        assertEquals(inMB.length, usual.length);
        for (int i = 0; i < usual.length; i++)
            assertEquals(inMB[i], usual[i]);
    }

    @Test
    //Tests that MainBoard is created correctly: the extra Faith points given to players at the beginning of the game is correct
    public void ctrlExtraResourcesAtGameBeginning() {
        int[] inMB = mainBoard.getExtraResourcesAtBeginning();
        int[] usual = new int[]{0, 1, 1, 2};

        assertEquals(inMB.length, usual.length);
        for (int i = 0; i < usual.length; i++)
            assertEquals(inMB[i], usual[i]);
    }

    @Test
    //Tests that at the beginning of the game the players are given four LeaderCards by default
    public void ctrlGivingLeaderCardsToPlayerAtGameBeginning() throws ParserConfigurationException, IOException, SAXException {
        MainBoard m1 = new MainBoard(1);

        m1.giveLeaderCardsToPlayerAtGameBeginning();

        assertEquals(m1.getPlayerBoard(0).getNotPlayedLeaderCards().size(), 4);
    }

    @Test
    public void ctrlExtraFaithPointsAtBeginningFourPlayers() throws LastVaticanReportException {
        mainBoard.giveExtraFaithPointAtBeginning(2); //The third player is the first to play

        assertEquals(mainBoard.getPlayerBoard(2).getPositionOnFaithTrack(), 0);
        assertEquals(mainBoard.getPlayerBoard(3).getPositionOnFaithTrack(), 0);
        assertEquals(mainBoard.getPlayerBoard(0).getPositionOnFaithTrack(), 1);
        assertEquals(mainBoard.getPlayerBoard(1).getPositionOnFaithTrack(), 1);
    }

    @Test
    public void ctrlExtraFaithPointsAtBeginningFourPlayersNaturalOrder() throws LastVaticanReportException {
        mainBoard.giveExtraFaithPointAtBeginning(0); //The third player is the first to play

        assertEquals(mainBoard.getPlayerBoard(0).getPositionOnFaithTrack(), 0);
        assertEquals(mainBoard.getPlayerBoard(1).getPositionOnFaithTrack(), 0);
        assertEquals(mainBoard.getPlayerBoard(2).getPositionOnFaithTrack(), 1);
        assertEquals(mainBoard.getPlayerBoard(3).getPositionOnFaithTrack(), 1);
    }

    @Test
    public void ctrlExtraFaithPointsAtBeginningFourPlayersBorderCase() throws LastVaticanReportException {
        mainBoard.giveExtraFaithPointAtBeginning(3); //The last player is the first to play

        assertEquals(mainBoard.getPlayerBoard(3).getPositionOnFaithTrack(), 0);
        assertEquals(mainBoard.getPlayerBoard(0).getPositionOnFaithTrack(), 0);
        assertEquals(mainBoard.getPlayerBoard(1).getPositionOnFaithTrack(), 1);
        assertEquals(mainBoard.getPlayerBoard(2).getPositionOnFaithTrack(), 1);
    }


    @Test
    public void ctrlExtraFaithPointsAtBeginningThreePlayers() throws LastVaticanReportException, ParserConfigurationException, IOException, SAXException {
        MainBoard m1 = new MainBoard(3);
        m1.giveExtraFaithPointAtBeginning(2); //The third player is the first to play

        assertEquals(m1.getPlayerBoard(2).getPositionOnFaithTrack(), 0);
        assertEquals(m1.getPlayerBoard(0).getPositionOnFaithTrack(), 0);
        assertEquals(m1.getPlayerBoard(1).getPositionOnFaithTrack(), 1);
    }

    @Test
    public void ctrlExtraFaithPointsAtBeginningTwoPlayers() throws LastVaticanReportException, ParserConfigurationException, IOException, SAXException {
        MainBoard m1 = new MainBoard(2);
        m1.giveExtraFaithPointAtBeginning(1); //The second player is the first to play

        assertEquals(m1.getPlayerBoard(1).getPositionOnFaithTrack(), 0);
        assertEquals(m1.getPlayerBoard(0).getPositionOnFaithTrack(), 0);
    }

    @Test
    public void ctrlExtraFaithPointsAtBeginningOnePlayers() throws LastVaticanReportException, ParserConfigurationException, IOException, SAXException {
        MainBoard m1 = new MainBoard(1);
        m1.giveExtraFaithPointAtBeginning(0); //There is only one player in the game

        assertEquals(m1.getPlayerBoard(0).getPositionOnFaithTrack(), 0);
    }

    @Test
    //Tests that the MainBoard returns the correct number of extra resources to the players in order: there are four player, the third player is first
    public void ctrlExtraResourcesAtBeginningFourPlayer3First() {
        int[] usual = new int[]{0, 1, 1, 2};
        assertEquals(mainBoard.getExtraResourcesAtBeginningForPlayer(3, 3), usual[0]);
        assertEquals(mainBoard.getExtraResourcesAtBeginningForPlayer(3, 0), usual[1]);
        assertEquals(mainBoard.getExtraResourcesAtBeginningForPlayer(3, 1), usual[2]);
        assertEquals(mainBoard.getExtraResourcesAtBeginningForPlayer(3, 2), usual[3]);
    }

    @Test
    //Tests that the MainBoard returns the correct number of extra resources to the players in order: there are four player, the second player is first
    public void ctrlExtraResourcesAtBeginningFourPlayer2First() {
        int[] usual = new int[]{0, 1, 1, 2};
        assertEquals(mainBoard.getExtraResourcesAtBeginningForPlayer(2, 2), usual[0]);
        assertEquals(mainBoard.getExtraResourcesAtBeginningForPlayer(2, 3), usual[1]);
        assertEquals(mainBoard.getExtraResourcesAtBeginningForPlayer(2, 0), usual[2]);
        assertEquals(mainBoard.getExtraResourcesAtBeginningForPlayer(2, 1), usual[3]);
    }

    @Test
    //Tests that the MainBoard returns the correct number of extra resources to the players in order: there are four player, the first player is first
    public void ctrlExtraResourcesAtBeginningFourPlayer1First() {
        int[] usual = new int[]{0, 1, 1, 2};
        assertEquals(mainBoard.getExtraResourcesAtBeginningForPlayer(1, 1), usual[0]);
        assertEquals(mainBoard.getExtraResourcesAtBeginningForPlayer(1, 2), usual[1]);
        assertEquals(mainBoard.getExtraResourcesAtBeginningForPlayer(1, 3), usual[2]);
        assertEquals(mainBoard.getExtraResourcesAtBeginningForPlayer(1, 0), usual[3]);
    }

    @Test
    //Tests that the MainBoard returns the correct number of extra resources to the players in order: there are four player, the 0th player is first
    public void ctrlExtraResourcesAtBeginningFourPlayer0First() {
        int[] usual = new int[]{0, 1, 1, 2};
        assertEquals(mainBoard.getExtraResourcesAtBeginningForPlayer(0, 0), usual[0]);
        assertEquals(mainBoard.getExtraResourcesAtBeginningForPlayer(0, 1), usual[1]);
        assertEquals(mainBoard.getExtraResourcesAtBeginningForPlayer(0, 2), usual[2]);
        assertEquals(mainBoard.getExtraResourcesAtBeginningForPlayer(0, 3), usual[3]);
    }

    @Test
    //Tests that the MainBoard returns the correct number of extra resources to the players in order: there are only three players
    public void ctrlExtraResourcesAtBeginningThreePlayers() throws ParserConfigurationException, IOException, SAXException {
        MainBoard m1 = new MainBoard(3);

        int[] usual = new int[]{0, 1, 1, 2};
        assertEquals(m1.getExtraResourcesAtBeginningForPlayer(1, 1), usual[0]);
        assertEquals(m1.getExtraResourcesAtBeginningForPlayer(1, 2), usual[1]);
        assertEquals(m1.getExtraResourcesAtBeginningForPlayer(1, 0), usual[2]);
    }

    @Test
    //Tests that the MainBoard returns the correct number of extra resources to the players in order: there are only two players
    public void ctrlExtraResourcesAtBeginningTwoPlayers() throws ParserConfigurationException, IOException, SAXException {
        MainBoard m1 = new MainBoard(3);

        int[] usual = new int[]{0, 1, 1, 2};
        assertEquals(m1.getExtraResourcesAtBeginningForPlayer(1, 1), usual[0]);
        assertEquals(m1.getExtraResourcesAtBeginningForPlayer(1, 0), usual[1]);
    }

    @Test
    //Tests that the MainBoard returns the correct number of extra resources to the players in order: there is only one players
    public void ctrlExtraResourcesAtBeginningOnePlayer() throws ParserConfigurationException, IOException, SAXException {
        MainBoard m1 = new MainBoard(3);

        int[] usual = new int[]{0, 1, 1, 2};
        assertEquals(m1.getExtraResourcesAtBeginningForPlayer(0, 0), usual[0]);
    }

    @Test
    public void ctrlPlayerOrder() {
        //If there are four players and the first to go is the number two in the array then their are going to be the last to play
        assertEquals(mainBoard.getPlayerOder(2, 1), 3);
    }

    @Test
    //Tests that a random player is chosen to be the firs
    public void getFirstPlayerRandomly() {
        int result;

        for (int j = 0; j < 10; j++) {
            result = mainBoard.getFirstPlayerRandomly();

            assertTrue(result >= 0);
            assertTrue(result < 4);
        }
    }

    @Test

    public void ctrlMainBoardCloning() throws IllegalActionException, ParserConfigurationException, IOException, SAXException {
        MainBoard original = new MainBoard(1);
        original.drawDevCardFromDeckInDevGrid(0, 1);
        original.giveLeaderCardsToPlayerAtGameBeginning();

        MainBoard clone = new MainBoard(original);

        assertNotSame(clone, original);
        assertTrue(clone.getFaithTrack().lighterEquals(original.getFaithTrack()));
        assertEquals(clone.getPlayerBoard(0).getPopeTile().size(), original.getPlayerBoard(0).getPopeTile().size());
        assertEquals(clone.getPlayerBoard(0).getNotPlayedLeaderCards().size(), original.getPlayerBoard(0).getNotPlayedLeaderCards().size());
        assertEquals(clone.getDevGrid(), original.getDevGrid());
        assertEquals(clone.getMarket(), original.getMarket());
        assertEquals(clone.getNumberOfPlayers(), original.getNumberOfPlayers());
        assertEquals(clone.getPlayerBoardsNumber(), original.getPlayerBoardsNumber());
        assertEquals(clone.getNumberOfLeaderCardsToGive(), original.getNumberOfLeaderCardsToGive());
        assertArrayEquals(clone.getExtraFaithPointsAtBeginning(), original.getExtraFaithPointsAtBeginning());
        assertArrayEquals(clone.getExtraResourcesAtBeginning(), original.getExtraResourcesAtBeginning());
        assertEquals(clone.getStepForEachDiscardedRes(), original.getStepForEachDiscardedRes());

    }

    @Test
    //Tests that the PlayerBoard in the MainBoard reference to the same FaithTrack as the one stored in the MainBoard before and
    //after cloning and that the FaithTrack in the original MainBoard and the one in the new MainBoard are equals but are not the same instance
    public void ctrlFaithTrackReferences() {
        assertSame(mainBoard.getPlayerBoard(0).getFaithTrack(), mainBoard.getPlayerBoard(1).getFaithTrack());
        assertSame(mainBoard.getFaithTrackReference(), mainBoard.getPlayerBoard(0).getFaithTrack());

        MainBoard clone = new MainBoard(mainBoard);
        assertNotSame(mainBoard.getFaithTrackReference(), clone.getFaithTrackReference());
        assertTrue(mainBoard.getFaithTrack().lighterEquals(clone.getFaithTrack()));

        assertSame(clone.getPlayerBoard(0).getFaithTrack(), clone.getPlayerBoard(1).getFaithTrack());
        assertSame(clone.getPlayerBoard(0).getFaithTrack(), clone.getFaithTrackReference());

        assertNotSame(clone.getPlayerBoard(0).getFaithTrack(), mainBoard.getPlayerBoard(0).getFaithTrack());
    }

    @Test
    public void ctrlCellObserverCreationInMainBoardConstructor() {
        assertEquals(((PopeCell) mainBoard.getFaithTrack().getCell(8)).getObserversList().size(), 1);
        assertEquals(((PopeCell) mainBoard.getFaithTrack().getCell(16)).getObserversList().size(), 1);
        assertEquals(((PopeCell) mainBoard.getFaithTrack().getCell(24)).getObserversList().size(), 1);

        Observer p1 = ((PopeCell) mainBoard.getFaithTrack().getCell(8)).getObserversList().get(0);
        Observer p2 = ((PopeCell) mainBoard.getFaithTrack().getCell(16)).getObserversList().get(0);
        Observer p3 = ((PopeCell) mainBoard.getFaithTrack().getCell(24)).getObserversList().get(0);

        assertSame(((PopeCellObserver) p1).getMainBoard(), mainBoard);

        assertSame(p1, p2);
        assertSame(p2, p3);
    }

    @Test
    public void ctrlGetClone() {
        MainBoard copy = mainBoard.getClone();

        assertNotSame(copy, mainBoard);
    }

    @Test
    public void ctrlCellObserverCloningInMainBoardCopyConstructor() {
        MainBoard copy = mainBoard.getClone();

        Observer p0 = ((PopeCell) mainBoard.getFaithTrack().getCell(8)).getObserversList().get(0);

        assertEquals(((PopeCell) copy.getFaithTrack().getCell(8)).getObserversList().size(), 1);
        assertEquals(((PopeCell) copy.getFaithTrack().getCell(16)).getObserversList().size(), 1);
        assertEquals(((PopeCell) copy.getFaithTrack().getCell(24)).getObserversList().size(), 1);

        Observer p1 = ((PopeCell) copy.getFaithTrack().getCell(8)).getObserversList().get(0);
        Observer p2 = ((PopeCell) copy.getFaithTrack().getCell(16)).getObserversList().get(0);
        Observer p3 = ((PopeCell) copy.getFaithTrack().getCell(24)).getObserversList().get(0);

        assertSame(p1, p2);
        assertSame(p2, p3);

        assertNotSame(p0, p1);

        assertSame(((PopeCellObserver) p0).getMainBoard(), mainBoard);
        assertSame(((PopeCellObserver) p1).getMainBoard(), copy);
        assertNotSame(((PopeCellObserver) p1).getMainBoard(), ((PopeCellObserver) p0).getMainBoard());
    }

}