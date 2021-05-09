package it.polimi.ingsw.controller;

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

    @BeforeEach
    public void setup(){
        gameController = new GameController();
        Reader      inputStreamReader = new InputStreamReader(System.in);
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

}
