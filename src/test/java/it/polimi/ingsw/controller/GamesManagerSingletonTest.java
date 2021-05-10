package it.polimi.ingsw.controller;

import it.polimi.ingsw.exceptions.NotAvailableNicknameException;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;


import java.io.IOException;
import java.net.Socket;
import java.rmi.UnexpectedException;

import static java.lang.Thread.sleep;
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
    void setUp() {
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
    void sequentialAddClients() throws  InterruptedException {
        assertNull(gamesManagerSingleton.getStartingGame());
        Thread thread1 = new Thread(() -> {
            try {
                assertNull(gamesManagerSingleton.joinOrCreateNewGame(client1));
            } catch (InterruptedException | NotAvailableNicknameException e) {
                e.printStackTrace();
            }
        });
        thread1.start();
        thread1.join();
        assertNotNull(gamesManagerSingleton.getStartingGame());

        Thread thread2 = new Thread(() -> {
            try {
                assertNotNull(gamesManagerSingleton.joinOrCreateNewGame(client2));
            } catch (InterruptedException | NotAvailableNicknameException e) {
                e.printStackTrace();
            }
        });
        thread2.start();
        Thread thread3 = new Thread(() -> {
            try {
                assertNotNull(gamesManagerSingleton.joinOrCreateNewGame(client3));
            } catch (InterruptedException | NotAvailableNicknameException e) {
                e.printStackTrace();
            }
        });
        thread3.start();
        Thread thread4 = new Thread(() -> {
            try {
                assertNotNull(gamesManagerSingleton.joinOrCreateNewGame(client4));
            } catch (InterruptedException | NotAvailableNicknameException e) {
                e.printStackTrace();
            }
        });
        thread4.start();

        assertTrue(thread2.isAlive());
        assertTrue(thread3.isAlive());
        assertTrue(thread4.isAlive());

        thread1 = new Thread(() -> {
            try {
                assertNotNull(gamesManagerSingleton.configureGame(client1, 4));
            } catch (UnexpectedException e) {
                e.printStackTrace();
            }
        });
        thread1.start();
        thread1.join();
        thread2.join();
        thread3.join();
        thread4.join();
        assertNull(gamesManagerSingleton.getStartingGame());
        assertEquals(1, gamesManagerSingleton.getGames().size());
    }

    @Test
    void clientConfigureBeforeOthersJoin() throws  InterruptedException {
        assertNull(gamesManagerSingleton.getStartingGame());
        Thread thread1 = new Thread(() -> {
            try {
                assertNull(gamesManagerSingleton.joinOrCreateNewGame(client1));
                assertNotNull(gamesManagerSingleton.configureGame(client1, 4));
            } catch (InterruptedException | NotAvailableNicknameException | UnexpectedException e) {
                e.printStackTrace();
            }
        });
        thread1.start();
        thread1.join();
        assertNotNull(gamesManagerSingleton.getStartingGame());

        Thread thread2 = new Thread(() -> {
            try {
                assertNotNull(gamesManagerSingleton.joinOrCreateNewGame(client2));
            } catch (InterruptedException | NotAvailableNicknameException e) {
                e.printStackTrace();
            }
        });
        thread2.start();
        Thread thread3 = new Thread(() -> {
            try {
                assertNotNull(gamesManagerSingleton.joinOrCreateNewGame(client3));
            } catch (InterruptedException | NotAvailableNicknameException e) {
                e.printStackTrace();
            }
        });
        thread3.start();
        Thread thread4 = new Thread(() -> {
            try {
                assertNotNull(gamesManagerSingleton.joinOrCreateNewGame(client4));
            } catch (InterruptedException | NotAvailableNicknameException e) {
                e.printStackTrace();
            }
        });
        thread4.start();

        thread1.join();
        thread2.join();
        thread3.join();
        thread4.join();
        assertNull(gamesManagerSingleton.getStartingGame());
        assertEquals(1, gamesManagerSingleton.getGames().size());

    }

    @Test
    void solitaryGameTest() throws  InterruptedException {
        assertNull(gamesManagerSingleton.getStartingGame());
        Thread thread1 = new Thread(() -> {
            try {
                assertNull(gamesManagerSingleton.joinOrCreateNewGame(client1));
                assertNotNull(gamesManagerSingleton.configureGame(client1, 1));
            } catch (InterruptedException | NotAvailableNicknameException | UnexpectedException e) {
                e.printStackTrace();
            }
        });
        thread1.start();
        thread1.join();
        assertNull(gamesManagerSingleton.getStartingGame());
        assertEquals(1, gamesManagerSingleton.getGames().size());

    }

    @Test
    void sameNicksTest() throws  InterruptedException {
        ClientHandler clientSameNick = new ClientHandler(null, null, null);
        clientSameNick.setNickname("1");

        assertNull(gamesManagerSingleton.getStartingGame());
        Thread thread1 = new Thread(() -> {
            try {
                assertNull(gamesManagerSingleton.joinOrCreateNewGame(client1));
                assertThrows(NotAvailableNicknameException.class, () -> gamesManagerSingleton.joinOrCreateNewGame(clientSameNick));
                assertNotNull(gamesManagerSingleton.configureGame(client1, 2));
            } catch (InterruptedException | NotAvailableNicknameException | UnexpectedException e) {
                e.printStackTrace();
            }
        });
        thread1.start();
        thread1.join();
        assertThrows(NotAvailableNicknameException.class, () -> gamesManagerSingleton.joinOrCreateNewGame(clientSameNick));

        Thread thread2 = new Thread(() -> {
            try {
                assertNotNull(gamesManagerSingleton.joinOrCreateNewGame(client2));
            } catch (InterruptedException | NotAvailableNicknameException e) {
                e.printStackTrace();
            }
        });
        thread2.start();
        thread2.join();
        assertNull(gamesManagerSingleton.getStartingGame());
        assertEquals(1, gamesManagerSingleton.getGames().size());
        assertThrows(NotAvailableNicknameException.class, () -> gamesManagerSingleton.joinOrCreateNewGame(clientSameNick));
    }

    @Test
    void timerTest() throws  InterruptedException {
        assertNull(gamesManagerSingleton.getStartingGame());
        Thread thread1 = new Thread(() -> {
            try {
                assertNull(gamesManagerSingleton.joinOrCreateNewGame(client1));
                Thread.sleep(31000);
                assertNull(gamesManagerSingleton.getStartingGame());
                assertThrows(IllegalStateException.class, () -> gamesManagerSingleton.configureGame(client1, 1));
            } catch (InterruptedException | NotAvailableNicknameException e) {
                e.printStackTrace();
            }
        });
        thread1.start();
        thread1.join();
        assertNull(gamesManagerSingleton.getStartingGame());
        assertEquals(0, gamesManagerSingleton.getGames().size());

    }

}