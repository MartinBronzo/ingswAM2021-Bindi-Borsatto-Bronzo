package it.polimi.ingsw.controller;

import it.polimi.ingsw.exceptions.IllegalActionException;
import it.polimi.ingsw.exceptions.NotAvailableNicknameException;
import it.polimi.ingsw.view.Client;
import it.polimi.ingsw.view.cli.CliClient;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;


import java.io.*;
import java.rmi.UnexpectedException;

import static java.lang.Thread.sleep;
import static org.junit.jupiter.api.Assertions.*;

class ServerTest {
    private Server server;
    private final static int portNumber =9047;
    private final static String hostName = "127.0.0.1";
    Thread serverThread;

    @BeforeEach
    void setUp() throws FileNotFoundException, InterruptedException {
        Server server = new Server(portNumber);
        serverThread = new Thread(() -> {
            server.startServer();
        });
        serverThread.start();
        sleep(2000);
    }


    @AfterEach
    void tearDown() {
        serverThread.interrupt();
    }

    @Test
    void SetNicknameTest() throws InterruptedException, IllegalArgumentException, IOException {

        PipedWriter writer = new PipedWriter();
        PipedReader reader = new PipedReader(writer);
        PrintWriter printWriter = new PrintWriter(writer);
        BufferedReader bufferedReader = new BufferedReader(reader);

        Client client = new CliClient(portNumber, hostName, bufferedReader);
        Thread thread =  new Thread(() -> {
            try {
                client.startConnection();
            } catch (IOException e) {
                e.printStackTrace();
            }
            client.doConnection();
        });
        thread.start();
        sleep(1000);
        printWriter.println("setnickname");
        printWriter.println("AndrePuzza");
        sleep(1000);
        printWriter.println("quit");
        sleep(1000);

    }
}