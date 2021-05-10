package it.polimi.ingsw.controller;

import it.polimi.ingsw.controller.enums.PlayerState;
import it.polimi.ingsw.exceptions.IllegalActionException;
import it.polimi.ingsw.exceptions.LastVaticanReportException;
import it.polimi.ingsw.model.FaithTrack.ReportNum;
import it.polimi.ingsw.model.MainBoard;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.*;
import java.net.Socket;

import static org.junit.jupiter.api.Assertions.*;

public class GameControllerTest {
    GameController gameController;
    ClientHandler clientHandler1;
    ClientHandler clientHandler2;
    ClientHandler clientHandler3;
    ClientHandler clientHandler4;
    ClientHandler clientHandler5;
    Reader inputStreamReader;

    @BeforeEach
    public void setup(){
        gameController = new GameController();
        inputStreamReader = new InputStreamReader(System.in);
        clientHandler1 = new ClientHandler(new Socket(), new BufferedReader(inputStreamReader), new PrintWriter(System.out));
        clientHandler2 = new ClientHandler(new Socket(), new BufferedReader(inputStreamReader), new PrintWriter(System.out));
        clientHandler3 = new ClientHandler(new Socket(), new BufferedReader(inputStreamReader), new PrintWriter(System.out));
        clientHandler4 = new ClientHandler(new Socket(), new BufferedReader(inputStreamReader), new PrintWriter(System.out));
        clientHandler5 = new ClientHandler(new Socket(), new BufferedReader(inputStreamReader), new PrintWriter(System.out));
    }

    @Test
    public void ctrlCreationFirstSetUp(){
        gameController = new GameController();

        assertTrue(gameController.getNumberOfPlayers() < 0);
        assertEquals(gameController.getPlayersList().size(), 0);
    }

    @Test
    public void ctrlCreationSettingMainBoard(){
        gameController.startMainBoard(4);

        assertEquals(gameController.getNumberOfPlayers(), 4);
    }

    @Test
    public void ctrlCreationWrongNumberOfPlayersTooLow(){
        Exception e = assertThrows(IllegalArgumentException.class, () -> gameController.startMainBoard(0));
        assertEquals(e.getMessage(), "The number of players must be a number between 1 and 4 included!");
    }

    @Test
    public void ctrlCreationWrongNumberOfPlayersTooHigh(){
        Exception e = assertThrows(IllegalArgumentException.class, () -> gameController.startMainBoard(5));
        assertEquals(e.getMessage(), "The number of players must be a number between 1 and 4 included!");
    }

    @Test
    public void ctrlCreationGoodNumberOfPlayers(){
        gameController.startMainBoard(4);

        assertEquals(gameController.getNumberOfPlayers(), 4);
    }

    @Test
    public void ctrlCreationAddingOnePlayer(){
        gameController.startMainBoard(4);
        gameController.setPlayer(clientHandler1);

        assertEquals(gameController.getPlayersList().size(), 1);
        assertSame(gameController.getPlayersList().get(0), clientHandler1);
    }

    @Test
    //Adding an already added player won't change the internal state of the GameController
    public void ctrlCreationAddingTheSamePlayerAgain(){
        gameController.startMainBoard(4);
        gameController.setPlayer(clientHandler1);

        gameController.setPlayer(clientHandler1);
        assertEquals(gameController.getPlayersList().size(), 1);
        assertSame(gameController.getPlayersList().get(0), clientHandler1);
    }

    @Test
    public void ctrlCreationAddingAllPlayers(){
        gameController.startMainBoard(4);
        gameController.setPlayer(clientHandler1);
        gameController.setPlayer(clientHandler2);
        gameController.setPlayer(clientHandler3);
        gameController.setPlayer(clientHandler4);

        assertEquals(gameController.getPlayersList().size(), 4);
        assertSame(gameController.getPlayersList().get(0), clientHandler1);
        assertSame(gameController.getPlayersList().get(1), clientHandler2);
        assertSame(gameController.getPlayersList().get(2), clientHandler3);
        assertSame(gameController.getPlayersList().get(3), clientHandler4);
    }

    @Test
    public void ctrlAddingMorePlayersThanNeeded(){
        gameController.startMainBoard(1);
        gameController.setPlayer(clientHandler1);

        gameController.setPlayer(clientHandler2);
        assertEquals(gameController.getPlayersList().size(), 1);
        assertSame(gameController.getPlayersList().get(0), clientHandler1);

    }

    @Test
    //Tests that the GameController saves the correct copy of the model of the game
    public void ctrlSavingState() throws LastVaticanReportException, IllegalActionException {
        gameController.startMainBoard(4);
        MainBoard original = gameController.getMainBoard();

        //We change the inner state of the MainBoard
        original.getPlayerBoard(0).moveForwardOnFaithTrack(6);
        original.dealWithVaticanReportAllPlayers(ReportNum.REPORT1);
        assertTrue(original.getPlayerBoard(0).getPopeTile().get(0).isActivated());
        assertTrue(original.getPlayerBoard(1).getPopeTile().get(0).isDiscarded());
        assertTrue(original.getPlayerBoard(2).getPopeTile().get(0).isDiscarded());
        assertTrue(original.getPlayerBoard(3).getPopeTile().get(0).isDiscarded());

        gameController.doSaveState();
        MainBoard copy = gameController.getModelCopy();

        //We check that the inner copy of the MainBoard is equal to the original one but they are two different instances
        assertNotSame(copy, original);
        assertTrue(copy.getPlayerBoard(0).getPopeTile().get(0).isActivated());
        assertTrue(copy.getPlayerBoard(1).getPopeTile().get(0).isDiscarded());
        assertTrue(copy.getPlayerBoard(2).getPopeTile().get(0).isDiscarded());
        assertTrue(copy.getPlayerBoard(3).getPopeTile().get(0).isDiscarded());
    }

    @Test
    //Tests that the GameController does the rollback correctly
    public void ctrlRollbackState() throws LastVaticanReportException, IllegalActionException {
        gameController.startMainBoard(4);

        //First state: the first Vatican Report is activated and the only PopeTile activate is the one belonging to the first player
        gameController.getMainBoard().getPlayerBoard(0).moveForwardOnFaithTrack(6);
        gameController.getMainBoard().dealWithVaticanReportAllPlayers(ReportNum.REPORT1);
        assertEquals(gameController.getMainBoard().getPlayerBoard(0).getPositionOnFaithTrack(), 6);
        assertEquals(gameController.getMainBoard().getPlayerBoard(1).getPositionOnFaithTrack(), 0);
        assertEquals(gameController.getMainBoard().getPlayerBoard(2).getPositionOnFaithTrack(), 0);
        assertEquals(gameController.getMainBoard().getPlayerBoard(3).getPositionOnFaithTrack(), 0);
        assertTrue(gameController.getMainBoard().getPlayerBoard(0).getPopeTile().get(0).isActivated());
        assertTrue(gameController.getMainBoard().getPlayerBoard(1).getPopeTile().get(0).isDiscarded());
        assertTrue(gameController.getMainBoard().getPlayerBoard(2).getPopeTile().get(0).isDiscarded());
        assertTrue(gameController.getMainBoard().getPlayerBoard(3).getPopeTile().get(0).isDiscarded());

        //We save the first state
        gameController.doSaveState();

        //We change the state a second time: the second Vatican Report is activated and the second player's PopeTile is the only one active
        gameController.getMainBoard().getPlayerBoard(1).moveForwardOnFaithTrack(14);
        gameController.getMainBoard().dealWithVaticanReportAllPlayers(ReportNum.REPORT2);
        assertTrue(gameController.getMainBoard().getPlayerBoard(0).getPopeTile().get(1).isDiscarded());
        assertTrue(gameController.getMainBoard().getPlayerBoard(1).getPopeTile().get(1).isActivated());
        assertTrue(gameController.getMainBoard().getPlayerBoard(2).getPopeTile().get(1).isDiscarded());
        assertTrue(gameController.getMainBoard().getPlayerBoard(3).getPopeTile().get(1).isDiscarded());
        assertEquals(gameController.getMainBoard().getPlayerBoard(0).getPositionOnFaithTrack(), 6);
        assertEquals(gameController.getMainBoard().getPlayerBoard(1).getPositionOnFaithTrack(), 14);
        assertEquals(gameController.getMainBoard().getPlayerBoard(2).getPositionOnFaithTrack(), 0);
        assertEquals(gameController.getMainBoard().getPlayerBoard(3).getPositionOnFaithTrack(), 0);

        //We rollback: the state must be the same as the first state
        gameController.doRollbackState();
        assertTrue(gameController.getMainBoard().getPlayerBoard(0).getPopeTile().get(0).isActivated());
        assertTrue(gameController.getMainBoard().getPlayerBoard(1).getPopeTile().get(0).isDiscarded());
        assertTrue(gameController.getMainBoard().getPlayerBoard(2).getPopeTile().get(0).isDiscarded());
        assertTrue(gameController.getMainBoard().getPlayerBoard(3).getPopeTile().get(0).isDiscarded());
        assertFalse(gameController.getMainBoard().getPlayerBoard(0).getPopeTile().get(1).isChanged());
        assertFalse(gameController.getMainBoard().getPlayerBoard(1).getPopeTile().get(1).isChanged());
        assertFalse(gameController.getMainBoard().getPlayerBoard(2).getPopeTile().get(1).isChanged());
        assertFalse(gameController.getMainBoard().getPlayerBoard(3).getPopeTile().get(1).isChanged());
        assertEquals(gameController.getMainBoard().getPlayerBoard(0).getPositionOnFaithTrack(), 6);
        assertEquals(gameController.getMainBoard().getPlayerBoard(1).getPositionOnFaithTrack(), 0);
        assertEquals(gameController.getMainBoard().getPlayerBoard(2).getPositionOnFaithTrack(), 0);
        assertEquals(gameController.getMainBoard().getPlayerBoard(3).getPositionOnFaithTrack(), 0);
    }

    @Test
    //Tests whether the substitution of a ClientHandler happens correctly: the player was already in the game
    public void ctrlClientHandlerSubstitutionPlayerInTheGame(){
        gameController.startMainBoard(4);
        clientHandler1.setNickname("Client 1");
        clientHandler2.setNickname("Client 2");
        clientHandler3.setNickname("Client 3");
        clientHandler4.setNickname("Client 4");
        gameController.setPlayer(clientHandler1);
        gameController.setPlayer(clientHandler2);
        gameController.setPlayer(clientHandler3);
        gameController.setPlayer(clientHandler4);

        assertSame(gameController.getPlayersList().get(0), clientHandler1);
        assertSame(gameController.getPlayersList().get(1), clientHandler2);
        assertSame(gameController.getPlayersList().get(2), clientHandler3);
        assertSame(gameController.getPlayersList().get(3), clientHandler4);

        assertEquals(gameController.getPlayersList().indexOf(gameController.getClientHandlerFromNickname("Client 3")), 2);

        ClientHandler newCH3 = new ClientHandler(new Socket(), new BufferedReader(inputStreamReader), new PrintWriter(System.out));
        newCH3.setNickname("Client 3");

        assertTrue(gameController.substitutesClient(newCH3));

        assertEquals(gameController.getPlayersList().size(), 4);
        assertSame(gameController.getPlayersList().get(0), clientHandler1);
        assertSame(gameController.getPlayersList().get(1), clientHandler2);
        assertSame(gameController.getPlayersList().get(3), clientHandler4);

        assertEquals(gameController.getPlayersList().indexOf(gameController.getClientHandlerFromNickname("Client 3")), 2);
        assertSame(gameController.getPlayersList().get(2), newCH3);
    }

    @Test
    //Tests whether the substitution of a ClientHandler happens correctly: the player wasn't even in the game
    public void ctrlClientHandlerSubstitutionPlayerNotInTheGame(){
        gameController.startMainBoard(4);
        clientHandler1.setNickname("Client 1");
        clientHandler2.setNickname("Client 2");
        clientHandler3.setNickname("Client 3");
        clientHandler4.setNickname("Client 4");
        gameController.setPlayer(clientHandler1);
        gameController.setPlayer(clientHandler2);
        gameController.setPlayer(clientHandler3);
        gameController.setPlayer(clientHandler4);

        assertSame(gameController.getPlayersList().get(0), clientHandler1);
        assertSame(gameController.getPlayersList().get(1), clientHandler2);
        assertSame(gameController.getPlayersList().get(2), clientHandler3);
        assertSame(gameController.getPlayersList().get(3), clientHandler4);

        assertEquals(gameController.getPlayersList().indexOf(gameController.getClientHandlerFromNickname("AAA")), -1);

        ClientHandler newCH = new ClientHandler(new Socket(), new BufferedReader(inputStreamReader), new PrintWriter(System.out));
        newCH.setNickname("AAA");

        assertFalse(gameController.substitutesClient(newCH));

        assertEquals(gameController.getPlayersList().size(), 4);
        assertSame(gameController.getPlayersList().get(0), clientHandler1);
        assertSame(gameController.getPlayersList().get(1), clientHandler2);
        assertSame(gameController.getPlayersList().get(2), clientHandler3);
        assertSame(gameController.getPlayersList().get(3), clientHandler4);

        assertEquals(gameController.getPlayersList().indexOf(gameController.getClientHandlerFromNickname("AAA")), -1);
    }

    @Test
    //Tests whether the substitution of a ClientHandler happens correctly: the player's state is changed correctly
    public void ctrlClientHandlerSubstitutionPlayerStateCorrectChange(){
        gameController.startMainBoard(1);
        clientHandler1.setNickname("Client 1");
        gameController.setPlayer(clientHandler1);
        clientHandler1.setPlayerSate(PlayerState.DISCONNECTED);

        assertEquals(gameController.getPlayersList().get(0).getPlayerSate(), PlayerState.DISCONNECTED);
        assertSame(gameController.getPlayersList().get(0), clientHandler1);

        ClientHandler newCH = new ClientHandler(new Socket(), new BufferedReader(inputStreamReader), new PrintWriter(System.out));
        newCH.setNickname("Client 1");

        assertTrue(gameController.substitutesClient(newCH));

        assertEquals(gameController.getPlayersList().get(0).getPlayerSate(), PlayerState.WAITING4TURN);
        assertSame(gameController.getPlayersList().get(0), newCH);
    }


}
