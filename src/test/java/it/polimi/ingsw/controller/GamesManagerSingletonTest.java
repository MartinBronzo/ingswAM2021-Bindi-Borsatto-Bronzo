package it.polimi.ingsw.controller;

import it.polimi.ingsw.exceptions.NotAvailableNicknameException;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;


import java.io.IOException;
import java.net.Socket;
import java.rmi.UnexpectedException;

import static org.junit.jupiter.api.Assertions.*;

class GamesManagerSingletonTest {
    private static GamesManagerSingleton gamesManagerSingleton;
    private ClientHandler client1;
    private ClientHandler client2;
    private ClientHandler client3;
    private ClientHandler client4;
    private Socket socket;

    @BeforeAll
    static void setUpAll() {
        gamesManagerSingleton = GamesManagerSingleton.getInstance();
    }

    @BeforeEach
    void setUp() throws IOException {
        gamesManagerSingleton.resetSingleton();
        client1 = new ClientHandler(null, null, null);
        client2 = new ClientHandler(null, null, null);
        client3 = new ClientHandler(null, null, null);
        client4 = new ClientHandler(null, null, null);
        client1.setNickname("1");
        client2.setNickname("2");
        client3.setNickname("3");
        client4.setNickname("4");
    }

    @Test
    void sequentialAddClients() throws UnexpectedException, InterruptedException, NotAvailableNicknameException {
        assertNull(gamesManagerSingleton.getStartingGame());
        gamesManagerSingleton.joinOrCreateNewGame(client1);
        assertNotNull(gamesManagerSingleton.getStartingGame());
        gamesManagerSingleton.joinOrCreateNewGame(client2);
        assertNotNull(gamesManagerSingleton.getStartingGame());
        gamesManagerSingleton.joinOrCreateNewGame(client3);
        assertNotNull(gamesManagerSingleton.getStartingGame());
        gamesManagerSingleton.joinOrCreateNewGame(client4);
        assertNotNull(gamesManagerSingleton.getStartingGame());
        gamesManagerSingleton.configureGame(client1, 4);
        assertNull(gamesManagerSingleton.getStartingGame());
    }
}