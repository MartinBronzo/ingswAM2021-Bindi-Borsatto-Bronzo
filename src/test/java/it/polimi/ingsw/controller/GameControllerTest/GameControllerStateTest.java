package it.polimi.ingsw.controller.GameControllerTest;

import com.google.gson.Gson;
import it.polimi.ingsw.controller.ClientHandler;
import it.polimi.ingsw.controller.GameController;
import it.polimi.ingsw.controller.enums.GameState;
import it.polimi.ingsw.exceptions.IllegalActionException;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.*;
import java.net.Socket;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class GameControllerStateTest {
    GameController gameController;
    ClientHandler c1;
    ClientHandler c2;
    ClientHandler c3;
    ClientHandler c4;
    Reader inputStreamReader;
    BufferedReader reader;
    File file;
    BufferedReader fileReader1;
    BufferedReader fileReader2;
    BufferedReader fileReader3;
    BufferedReader fileReader4;
    Gson gson;


    @BeforeEach
    public void setup() throws FileNotFoundException {
        gameController = new GameController();
        inputStreamReader = new InputStreamReader(System.in);
        c1 = new ClientHandler(new Socket(), new BufferedReader(inputStreamReader), new PrintWriter("ClientHandler1File.json"));
        c2 = new ClientHandler(new Socket(), new BufferedReader(inputStreamReader), new PrintWriter("ClientHandler2File.json"));
        c3 = new ClientHandler(new Socket(), new BufferedReader(inputStreamReader), new PrintWriter("ClientHandler3File.json"));
        c4 = new ClientHandler(new Socket(), new BufferedReader(inputStreamReader), new PrintWriter("ClientHandler4File.json"));
        reader = new BufferedReader(new InputStreamReader(System.in));
        c1.setNickname("Client 1");
        c2.setNickname("Client 2");
        c3.setNickname("Client 3");
        c4.setNickname("Client 4");
        gameController.startMainBoard(4);

        fileReader1 = new BufferedReader(new FileReader("ClientHandler1File.json"));
        fileReader2 = new BufferedReader(new FileReader("ClientHandler2File.json"));
        fileReader3 = new BufferedReader(new FileReader("ClientHandler3File.json"));
        fileReader4 = new BufferedReader(new FileReader("ClientHandler4File.json"));
    }

    @Test
    //Tests how the GameController changes over time: from CONFIGURING to STARTED
    public void ctrlStateChangeToStared() throws IllegalActionException {
        gameController.setState(GameState.CONFIGURING);

        gameController.setPlayerOld(c1);
        gameController.setPlayerOld(c2);
        gameController.setPlayerOld(c3);
        gameController.setPlayerOld(c4);

        assertEquals(gameController.getState(), GameState.STARTED);
    }

    /*@Test
    public void ctrlStateChangeToInSession() throws IllegalActionException {
        gameController.setState(GameState.CONFIGURING);

        gameController.setPlayerOld(c1);
        gameController.setPlayerOld(c2);
        gameController.setPlayerOld(c3);
        gameController.setPlayerOld(c4);

        //TODO: far mandare a tutte le risorse che devono e poi controllare che il game sia in INSESSION
    }*/

    //TODO: controllare cosa succede quando qualcuno si disconnette


}
