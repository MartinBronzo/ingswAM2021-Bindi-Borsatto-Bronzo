package it.polimi.ingsw.controller.GameControllerTest;

import it.polimi.ingsw.controller.ClientHandler;
import it.polimi.ingsw.controller.GameController;
import it.polimi.ingsw.controller.enums.GameState;
import it.polimi.ingsw.controller.enums.PlayerState;
import it.polimi.ingsw.exceptions.IllegalActionException;
import it.polimi.ingsw.exceptions.LastVaticanReportException;
import it.polimi.ingsw.model.FaithTrack.ReportNum;
import it.polimi.ingsw.model.MainBoard;
import it.polimi.ingsw.view.Client;
import it.polimi.ingsw.view.readOnlyModel.Player;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.*;
import java.net.Socket;
import java.util.List;

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
    public void ctrlCreationAddingOnePlayer() throws IllegalActionException {
        gameController.startMainBoard(4);
        gameController.setPlayerOld(clientHandler1);

        assertEquals(gameController.getPlayersList().size(), 1);
        assertSame(gameController.getPlayersList().get(0), clientHandler1);
    }

    @Test
    //Adding an already added player won't change the internal state of the GameController
    public void ctrlCreationAddingTheSamePlayerAgain() throws IllegalActionException {
        gameController.startMainBoard(4);
        gameController.setPlayerOld(clientHandler1);

        gameController.setPlayerOld(clientHandler1);
        assertEquals(gameController.getPlayersList().size(), 1);
        assertSame(gameController.getPlayersList().get(0), clientHandler1);
    }

    @Test
    public void ctrlCreationAddingAllPlayers() throws IllegalActionException {
        gameController.startMainBoard(4);
        gameController.setPlayerOld(clientHandler1);
        gameController.setPlayerOld(clientHandler2);
        gameController.setPlayerOld(clientHandler3);
        gameController.setPlayerOld(clientHandler4);

        assertEquals(gameController.getPlayersList().size(), 4);
        assertSame(gameController.getPlayersList().get(0), clientHandler1);
        assertSame(gameController.getPlayersList().get(1), clientHandler2);
        assertSame(gameController.getPlayersList().get(2), clientHandler3);
        assertSame(gameController.getPlayersList().get(3), clientHandler4);
    }

    @Test
    public void ctrlAddingMorePlayersThanNeeded() throws IllegalActionException {
        gameController.startMainBoard(1);
        gameController.setPlayerOld(clientHandler1);

        IllegalActionException e = assertThrows(IllegalActionException.class, ()->gameController.setPlayerOld(clientHandler2));
        assertEquals(e.getMessage(),"You can't be added to this game!");
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
    public void ctrlClientHandlerSubstitutionPlayerInTheGame() throws IllegalActionException {
        gameController.startMainBoard(4);
        clientHandler1.setNickname("Client 1");
        clientHandler2.setNickname("Client 2");
        clientHandler3.setNickname("Client 3");
        clientHandler4.setNickname("Client 4");
        gameController.setPlayerOld(clientHandler1);
        gameController.setPlayerOld(clientHandler2);
        gameController.setPlayerOld(clientHandler3);
        gameController.setPlayerOld(clientHandler4);
        gameController.setState(GameState.INSESSION);

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
    public void ctrlClientHandlerSubstitutionPlayerNotInTheGame() throws IllegalActionException {
        gameController.startMainBoard(4);
        clientHandler1.setNickname("Client 1");
        clientHandler2.setNickname("Client 2");
        clientHandler3.setNickname("Client 3");
        clientHandler4.setNickname("Client 4");
        gameController.setPlayerOld(clientHandler1);
        gameController.setPlayerOld(clientHandler2);
        gameController.setPlayerOld(clientHandler3);
        gameController.setPlayerOld(clientHandler4);

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
    public void ctrlClientHandlerSubstitutionPlayerStateCorrectChange() throws IllegalActionException {
        gameController.startMainBoard(1);
        clientHandler1.setNickname("Client 1");
        gameController.setPlayerOld(clientHandler1);
        gameController.setState(GameState.INSESSION);
        clientHandler1.setPlayerState(PlayerState.DISCONNECTED);

        assertEquals(gameController.getPlayersList().get(0).getPlayerState(), PlayerState.DISCONNECTED);
        assertSame(gameController.getPlayersList().get(0), clientHandler1);

        ClientHandler newCH = new ClientHandler(new Socket(), new BufferedReader(inputStreamReader), new PrintWriter(System.out));
        newCH.setNickname("Client 1");

        assertTrue(gameController.substitutesClient(newCH));

        assertEquals(gameController.getPlayersList().get(0).getPlayerState(), PlayerState.WAITING4TURN);
        assertSame(gameController.getPlayersList().get(0), newCH);
    }

    @Test
    public void substituteClientINSESSION() throws IllegalActionException {
        ClientHandler newClientHandler = new ClientHandler(new Socket(), new BufferedReader(inputStreamReader), new PrintWriter(System.out));
        newClientHandler.setNickname("Client 4");

        gameController.startMainBoard(4);
        clientHandler1.setNickname("Client 1");
        clientHandler2.setNickname("Client 2");
        clientHandler3.setNickname("Client 3");
        clientHandler4.setNickname("Client 4");
        gameController.setPlayerOld(clientHandler1);
        gameController.setPlayerOld(clientHandler2);
        gameController.setPlayerOld(clientHandler3);
        gameController.setPlayerOld(clientHandler4);
        gameController.setFirstPlayer(0);
        gameController.setActivePlayer(clientHandler2);
        gameController.setState(GameState.INSESSION);

        assertTrue(gameController.substitutesClient(newClientHandler));
        assertEquals(PlayerState.WAITING4TURN, gameController.getClientHandlerFromNickname("Client 4").getPlayerState());
    }

    @Test
    public void substituteClientLASTTURN1() throws IllegalActionException {
        ClientHandler newClientHandler = new ClientHandler(new Socket(), new BufferedReader(inputStreamReader), new PrintWriter(System.out));
        newClientHandler.setNickname("Client 4");

        gameController.startMainBoard(4);
        clientHandler1.setNickname("Client 1");
        clientHandler2.setNickname("Client 2");
        clientHandler3.setNickname("Client 3");
        clientHandler4.setNickname("Client 4");
        gameController.setPlayerOld(clientHandler1);
        gameController.setPlayerOld(clientHandler2);
        gameController.setPlayerOld(clientHandler3);
        gameController.setPlayerOld(clientHandler4);
        gameController.setFirstPlayer(0);
        gameController.setActivePlayer(clientHandler2);
        gameController.setState(GameState.LASTTURN);

        assertTrue(gameController.substitutesClient(newClientHandler));
        assertEquals(PlayerState.WAITING4LASTTURN, gameController.getClientHandlerFromNickname("Client 4").getPlayerState());
    }

    @Test
    public void substituteClientLASTTURN2() throws IllegalActionException {
        ClientHandler newClientHandler = new ClientHandler(new Socket(), new BufferedReader(inputStreamReader), new PrintWriter(System.out));
        newClientHandler.setNickname("Client 2");

        gameController.startMainBoard(4);
        clientHandler1.setNickname("Client 1");
        clientHandler2.setNickname("Client 2");
        clientHandler3.setNickname("Client 3");
        clientHandler4.setNickname("Client 4");
        gameController.setPlayerOld(clientHandler1);
        gameController.setPlayerOld(clientHandler2);
        gameController.setPlayerOld(clientHandler3);
        gameController.setPlayerOld(clientHandler4);
        gameController.setFirstPlayer(0);
        gameController.setActivePlayer(clientHandler4);
        gameController.setState(GameState.LASTTURN);

        assertTrue(gameController.substitutesClient(newClientHandler));
        assertEquals(PlayerState.WAITING4GAMEEND, gameController.getClientHandlerFromNickname("Client 2").getPlayerState());
    }

    @Test
    public void substituteClientLASTTURN3() throws IllegalActionException {
        ClientHandler newClientHandler = new ClientHandler(new Socket(), new BufferedReader(inputStreamReader), new PrintWriter(System.out));
        newClientHandler.setNickname("Client 2");

        gameController.startMainBoard(4);
        clientHandler1.setNickname("Client 1");
        clientHandler2.setNickname("Client 2");
        clientHandler3.setNickname("Client 3");
        clientHandler4.setNickname("Client 4");
        gameController.setPlayerOld(clientHandler1);
        gameController.setPlayerOld(clientHandler2);
        gameController.setPlayerOld(clientHandler3);
        gameController.setPlayerOld(clientHandler4);
        gameController.setFirstPlayer(2);
        gameController.setActivePlayer(clientHandler1);
        gameController.setState(GameState.LASTTURN);

        assertTrue(gameController.substitutesClient(newClientHandler));
        assertEquals(PlayerState.WAITING4LASTTURN, gameController.getClientHandlerFromNickname("Client 2").getPlayerState());
    }

    @Test
    public void substituteClientLASTTURN4() throws IllegalActionException {
        ClientHandler newClientHandler = new ClientHandler(new Socket(), new BufferedReader(inputStreamReader), new PrintWriter(System.out));
        newClientHandler.setNickname("Client 4");

        gameController.startMainBoard(4);
        clientHandler1.setNickname("Client 1");
        clientHandler2.setNickname("Client 2");
        clientHandler3.setNickname("Client 3");
        clientHandler4.setNickname("Client 4");
        gameController.setPlayerOld(clientHandler1);
        gameController.setPlayerOld(clientHandler2);
        gameController.setPlayerOld(clientHandler3);
        gameController.setPlayerOld(clientHandler4);
        gameController.setFirstPlayer(2);
        gameController.setActivePlayer(clientHandler1);
        gameController.setState(GameState.LASTTURN);

        assertTrue(gameController.substitutesClient(newClientHandler));
        assertEquals(PlayerState.WAITING4GAMEEND, gameController.getClientHandlerFromNickname("Client 4").getPlayerState());
    }

    @Test
    public void substituteClientDisconnectedBeforeStarting() throws IllegalActionException {
        ClientHandler newClientHandler = new ClientHandler(new Socket(), new BufferedReader(inputStreamReader), new PrintWriter(System.out));
        newClientHandler.setNickname("Client 1");

        gameController.startMainBoard(4);
        clientHandler1.setNickname("Client 1");
        clientHandler1.setPlayerState(PlayerState.DISCONNECTED);
        clientHandler2.setNickname("Client 2");
        clientHandler3.setNickname("Client 3");
        clientHandler4.setNickname("Client 4");
        gameController.setPlayerOld(clientHandler1);
        gameController.setPlayerOld(clientHandler2);
        gameController.setPlayerOld(clientHandler3);
        gameController.setPlayerOld(clientHandler4);
        gameController.setFirstPlayer(2);
        gameController.setActivePlayer(clientHandler3);
        gameController.setState(GameState.INSESSION);
        gameController.addDisconnectedBeforeStart(clientHandler1);

        assertTrue(gameController.substitutesClient(newClientHandler));
        assertEquals(PlayerState.WAITING4BEGINNINGDECISIONS, gameController.getClientHandlerFromNickname("Client 1").getPlayerState());
    }

    @Test
    public void playerPositionInTurn() throws IllegalActionException {
        gameController.startMainBoard(4);
        clientHandler1.setNickname("Client 1");
        clientHandler2.setNickname("Client 2");
        clientHandler3.setNickname("Client 3");
        clientHandler4.setNickname("Client 4");
        gameController.setPlayerOld(clientHandler1);
        gameController.setPlayerOld(clientHandler2);
        gameController.setPlayerOld(clientHandler3);
        gameController.setPlayerOld(clientHandler4);
        gameController.setFirstPlayer(0);

        assertEquals(4, gameController.getPlayerPositionInTurn(clientHandler4));

    }

    @Test
    public void playerPositionInTurn2() throws IllegalActionException {
        gameController.startMainBoard(4);
        clientHandler1.setNickname("Client 1");
        clientHandler2.setNickname("Client 2");
        clientHandler3.setNickname("Client 3");
        clientHandler4.setNickname("Client 4");
        gameController.setPlayerOld(clientHandler1);
        gameController.setPlayerOld(clientHandler2);
        gameController.setPlayerOld(clientHandler3);
        gameController.setPlayerOld(clientHandler4);
        gameController.setFirstPlayer(1);

        assertEquals(3, gameController.getPlayerPositionInTurn(clientHandler4));
    }

    @Test
    public void playerPositionInTurn3() throws IllegalActionException {
        gameController.startMainBoard(4);
        clientHandler1.setNickname("Client 1");
        clientHandler2.setNickname("Client 2");
        clientHandler3.setNickname("Client 3");
        clientHandler4.setNickname("Client 4");
        gameController.setPlayerOld(clientHandler1);
        gameController.setPlayerOld(clientHandler2);
        gameController.setPlayerOld(clientHandler3);
        gameController.setPlayerOld(clientHandler4);
        gameController.setFirstPlayer(2);

        assertEquals(4, gameController.getPlayerPositionInTurn(clientHandler2));
    }

    @Test
    public void willPlay() throws IllegalActionException {
        gameController.startMainBoard(4);
        clientHandler1.setNickname("Client 1");
        clientHandler2.setNickname("Client 2");
        clientHandler3.setNickname("Client 3");
        clientHandler4.setNickname("Client 4");
        gameController.setPlayerOld(clientHandler1);
        gameController.setPlayerOld(clientHandler2);
        gameController.setPlayerOld(clientHandler3);
        gameController.setPlayerOld(clientHandler4);
        gameController.setFirstPlayer(2);
        gameController.setActivePlayer(clientHandler3);
        clientHandler2.setPlayerState(PlayerState.PLAYING);
        gameController.setLastTurn();

        assertTrue(gameController.willPlayInThisTurn(clientHandler1));
        assertTrue(gameController.willPlayInThisTurn(clientHandler2));
        assertTrue(gameController.willPlayInThisTurn(clientHandler3));
        assertTrue(gameController.willPlayInThisTurn(clientHandler4));
    }

    @Test
    public void willPlay2() throws IllegalActionException {
        gameController.startMainBoard(4);
        clientHandler1.setNickname("Client 1");
        clientHandler2.setNickname("Client 2");
        clientHandler3.setNickname("Client 3");
        clientHandler4.setNickname("Client 4");
        gameController.setPlayerOld(clientHandler1);
        gameController.setPlayerOld(clientHandler2);
        gameController.setPlayerOld(clientHandler3);
        gameController.setPlayerOld(clientHandler4);
        gameController.setFirstPlayer(2);
        gameController.setActivePlayer(clientHandler2);
        clientHandler2.setPlayerState(PlayerState.PLAYING);
        gameController.setLastTurn();

        assertFalse(gameController.willPlayInThisTurn(clientHandler1));
        assertTrue(gameController.willPlayInThisTurn(clientHandler2));
        assertFalse(gameController.willPlayInThisTurn(clientHandler3));
        assertFalse(gameController.willPlayInThisTurn(clientHandler4));
    }


    @Test
    public void ctrlLastTurn() throws IllegalActionException {
        gameController.startMainBoard(4);
        clientHandler1.setNickname("Client 1");
        clientHandler2.setNickname("Client 2");
        clientHandler3.setNickname("Client 3");
        clientHandler4.setNickname("Client 4");
        gameController.setPlayerOld(clientHandler1);
        gameController.setPlayerOld(clientHandler2);
        gameController.setPlayerOld(clientHandler3);
        gameController.setPlayerOld(clientHandler4);
        gameController.setFirstPlayer(1);
        gameController.setActivePlayer(clientHandler3);
        clientHandler3.setPlayerState(PlayerState.PLAYING);
        gameController.setLastTurn();

        assertEquals(PlayerState.WAITING4LASTTURN, clientHandler1.getPlayerState());
        assertEquals(PlayerState.WAITING4GAMEEND, clientHandler2.getPlayerState());
        assertEquals(PlayerState.PLAYING, clientHandler3.getPlayerState());
        assertEquals(PlayerState.WAITING4LASTTURN, clientHandler4.getPlayerState());
    }

    @Test
    public void ctrlLastTurn2() throws IllegalActionException {
        gameController.startMainBoard(4);
        clientHandler1.setNickname("Client 1");
        clientHandler2.setNickname("Client 2");
        clientHandler3.setNickname("Client 3");
        clientHandler4.setNickname("Client 4");
        gameController.setPlayerOld(clientHandler1);
        gameController.setPlayerOld(clientHandler2);
        gameController.setPlayerOld(clientHandler3);
        gameController.setPlayerOld(clientHandler4);
        gameController.setFirstPlayer(0);
        gameController.setActivePlayer(clientHandler1);
        clientHandler1.setPlayerState(PlayerState.PLAYING);
        gameController.setLastTurn();

        assertEquals(PlayerState.PLAYING, clientHandler1.getPlayerState());
        assertEquals(PlayerState.WAITING4LASTTURN, clientHandler2.getPlayerState());
        assertEquals(PlayerState.WAITING4LASTTURN, clientHandler3.getPlayerState());
        assertEquals(PlayerState.WAITING4LASTTURN, clientHandler4.getPlayerState());
    }

    @Test
    public void ctrlLastTurn3() throws IllegalActionException {
        gameController.startMainBoard(4);
        clientHandler1.setNickname("Client 1");
        clientHandler2.setNickname("Client 2");
        clientHandler3.setNickname("Client 3");
        clientHandler4.setNickname("Client 4");
        gameController.setPlayerOld(clientHandler1);
        gameController.setPlayerOld(clientHandler2);
        gameController.setPlayerOld(clientHandler3);
        gameController.setPlayerOld(clientHandler4);
        gameController.setFirstPlayer(2);
        gameController.setActivePlayer(clientHandler2);
        clientHandler2.setPlayerState(PlayerState.PLAYING);
        gameController.setLastTurn();

        assertEquals(PlayerState.WAITING4GAMEEND, clientHandler1.getPlayerState());
        assertEquals(PlayerState.PLAYING, clientHandler2.getPlayerState());
        assertEquals(PlayerState.WAITING4GAMEEND, clientHandler3.getPlayerState());
        assertEquals(PlayerState.WAITING4GAMEEND, clientHandler4.getPlayerState());
    }

    @Test
    public void ctrlLastTurn4() throws IllegalActionException {
        gameController.startMainBoard(4);
        clientHandler1.setNickname("Client 1");
        clientHandler2.setNickname("Client 2");
        clientHandler3.setNickname("Client 3");
        clientHandler4.setNickname("Client 4");
        gameController.setPlayerOld(clientHandler1);
        gameController.setPlayerOld(clientHandler2);
        gameController.setPlayerOld(clientHandler3);
        gameController.setPlayerOld(clientHandler4);
        gameController.setFirstPlayer(2);
        gameController.setActivePlayer(clientHandler2);
        clientHandler1.setPlayerState(PlayerState.WAITING4BEGINNINGDECISIONS);
        clientHandler2.setPlayerState(PlayerState.PLAYING);
        clientHandler3.setPlayerState(PlayerState.DISCONNECTED);
        gameController.setLastTurn();

        assertEquals(PlayerState.WAITING4BEGINNINGDECISIONS, clientHandler1.getPlayerState());
        assertEquals(PlayerState.PLAYING, clientHandler2.getPlayerState());
        assertEquals(PlayerState.DISCONNECTED, clientHandler3.getPlayerState());
        assertEquals(PlayerState.WAITING4GAMEEND, clientHandler4.getPlayerState());
    }

    @Test
    public void ctrlSaveStateWithClientHandlers() throws IllegalActionException {
        gameController.startMainBoard(4);
        gameController.setPlayerOld(clientHandler1);
        gameController.setPlayerOld(clientHandler2);
        gameController.setPlayerOld(clientHandler3);
        gameController.setPlayerOld(clientHandler4);

        //We change the inner state of the Players
        clientHandler1.setPlayerState(PlayerState.WAITING4TURN);
        clientHandler2.setPlayerState(PlayerState.DISCONNECTED);
        clientHandler3.setPlayerState(PlayerState.WAITING4TURN);
        clientHandler4.setPlayerState(PlayerState.PLAYING);

        gameController.doSaveState();
        List<ClientHandler> copy = gameController.getClientHandlersCopy();

        //We check that the inner values of the ClientHandlers are the same to the ones stored in the safe copy
        assertEquals(copy.get(0).getPlayerState(), PlayerState.WAITING4TURN);
        assertEquals(copy.get(1).getPlayerState(), PlayerState.DISCONNECTED);
        assertEquals(copy.get(2).getPlayerState(), PlayerState.WAITING4TURN);
        assertEquals(copy.get(3).getPlayerState(), PlayerState.PLAYING);
    }

    @Test
    public void ctrlRollBackStateWithClientHandlers() throws IllegalActionException {
        gameController.startMainBoard(4);
        gameController.setPlayerOld(clientHandler1);
        gameController.setPlayerOld(clientHandler2);
        gameController.setPlayerOld(clientHandler3);
        gameController.setPlayerOld(clientHandler4);

        //We change the inner state of the Players
        clientHandler1.setPlayerState(PlayerState.WAITING4TURN);
        clientHandler2.setPlayerState(PlayerState.WAITING4TURN);
        clientHandler3.setPlayerState(PlayerState.WAITING4TURN);
        clientHandler4.setPlayerState(PlayerState.PLAYING);

        gameController.doSaveState();

        //We change again the Players states
        clientHandler1.setPlayerState(PlayerState.PLAYING);
        clientHandler2.setPlayerState(PlayerState.WAITING4TURN);
        clientHandler3.setPlayerState(PlayerState.WAITING4TURN);
        clientHandler4.setPlayerState(PlayerState.WAITING4TURN);

        gameController.doRollbackState();

        //We check that the inner values of the ClientHandlers are the same to the ones stored in the safe copy
        assertEquals(clientHandler1.getPlayerState(), PlayerState.WAITING4TURN);
        assertEquals(clientHandler2.getPlayerState(), PlayerState.WAITING4TURN);
        assertEquals(clientHandler3.getPlayerState(), PlayerState.WAITING4TURN);
        assertEquals(clientHandler4.getPlayerState(), PlayerState.PLAYING);
    }

    @Test
    //One player disconnects after the copy has been made
    public void ctrlRollBackStateWithClientHandlersWithDisconnectedPlayerNowDisconnected() throws IllegalActionException {
        gameController.startMainBoard(4);
        gameController.setPlayerOld(clientHandler1);
        gameController.setPlayerOld(clientHandler2);
        gameController.setPlayerOld(clientHandler3);
        gameController.setPlayerOld(clientHandler4);

        //We change the inner state of the Players
        clientHandler1.setPlayerState(PlayerState.WAITING4TURN);
        clientHandler2.setPlayerState(PlayerState.WAITING4TURN);
        clientHandler3.setPlayerState(PlayerState.WAITING4TURN);
        clientHandler4.setPlayerState(PlayerState.PLAYING);

        gameController.doSaveState();

        //We change again the Players states
        clientHandler1.setPlayerState(PlayerState.PLAYING);
        clientHandler2.setPlayerState(PlayerState.WAITING4TURN);
        clientHandler3.setPlayerState(PlayerState.DISCONNECTED);
        clientHandler4.setPlayerState(PlayerState.WAITING4TURN);

        gameController.doRollbackState();

        //We check that the inner values of the ClientHandlers are the same to the ones stored in the safe copy
        assertEquals(clientHandler1.getPlayerState(), PlayerState.WAITING4TURN);
        assertEquals(clientHandler2.getPlayerState(), PlayerState.WAITING4TURN);
        assertEquals(clientHandler3.getPlayerState(), PlayerState.DISCONNECTED);
        assertEquals(clientHandler4.getPlayerState(), PlayerState.PLAYING);
    }

    @Test
    //One player reconnects after the copy has been made
    public void ctrlRollBackStateWithClientHandlersWithDisconnectedPlayerNowActive() throws IllegalActionException {
        gameController.startMainBoard(4);
        gameController.setPlayerOld(clientHandler1);
        gameController.setPlayerOld(clientHandler2);
        gameController.setPlayerOld(clientHandler3);
        gameController.setPlayerOld(clientHandler4);

        //We change the inner state of the Players
        clientHandler1.setPlayerState(PlayerState.WAITING4TURN);
        clientHandler2.setPlayerState(PlayerState.WAITING4TURN);
        clientHandler3.setPlayerState(PlayerState.DISCONNECTED);
        clientHandler4.setPlayerState(PlayerState.PLAYING);

        gameController.doSaveState();

        //We change again the Players states
        clientHandler1.setPlayerState(PlayerState.PLAYING);
        clientHandler2.setPlayerState(PlayerState.WAITING4TURN);
        clientHandler3.setPlayerState(PlayerState.WAITING4TURN);
        clientHandler4.setPlayerState(PlayerState.WAITING4TURN);

        gameController.doRollbackState();

        //We check that the inner values of the ClientHandlers are the same to the ones stored in the safe copy
        assertEquals(clientHandler1.getPlayerState(), PlayerState.WAITING4TURN);
        assertEquals(clientHandler2.getPlayerState(), PlayerState.WAITING4TURN);
        assertEquals(clientHandler3.getPlayerState(), PlayerState.WAITING4TURN);
        assertEquals(clientHandler4.getPlayerState(), PlayerState.PLAYING);
    }

    @Test
    public void ctrlSaveStateWithGameState() throws IllegalActionException {
        gameController.startMainBoard(1);
        gameController.setPlayerOld(clientHandler1);

        gameController.setState(GameState.INSESSION);

        gameController.doSaveState();

        assertEquals(gameController.getGameStateCopy(), GameState.INSESSION);
    }

    @Test
    public void ctrlRollBackStateWithGameState() throws IllegalActionException {
        gameController.startMainBoard(1);
        gameController.setPlayerOld(clientHandler1);

        gameController.setState(GameState.INSESSION);

        gameController.doSaveState();

        //We change the state another time
        gameController.setState(GameState.LASTTURN);

        gameController.doRollbackState();

        assertEquals(gameController.getGameStateCopy(), GameState.INSESSION);

    }

}
