package it.polimi.ingsw.controller.GameControllerTest;

import com.google.gson.Gson;
import it.polimi.ingsw.controller.ClientHandler;
import it.polimi.ingsw.controller.GameController;
import it.polimi.ingsw.controller.enums.PlayerState;
import it.polimi.ingsw.exceptions.IllegalActionException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.*;
import java.net.Socket;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class GameControllerTurnsManagementTest {
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
    public void setup() throws FileNotFoundException, IllegalActionException {
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
        gameController.setPlayerOld(c1);
        gameController.setPlayerOld(c2);
        gameController.setPlayerOld(c3);
        gameController.setPlayerOld(c4);
        fileReader1 = new BufferedReader(new FileReader("ClientHandler1File.json"));
        fileReader2 = new BufferedReader(new FileReader("ClientHandler2File.json"));
        fileReader3 = new BufferedReader(new FileReader("ClientHandler3File.json"));
        fileReader4 = new BufferedReader(new FileReader("ClientHandler4File.json"));
    }

    @Test
    //All the players are still connected to the game: the player who's finishing their turn is the third in the list
    public void ctrlAllPlayersPresent(){
        c1.setPlayerState(PlayerState.WAITING4TURN);
        c2.setPlayerState(PlayerState.WAITING4TURN);
        c3.setPlayerState(PlayerState.PLAYING);
        c4.setPlayerState(PlayerState.WAITING4TURN);

        gameController.specifyNextPlayer(c3);

        assertEquals(c3.getPlayerState(), PlayerState.WAITING4TURN);
        assertEquals(c4.getPlayerState(), PlayerState.PLAYING);
        assertEquals(c1.getPlayerState(), PlayerState.WAITING4TURN);
        assertEquals(c2.getPlayerState(), PlayerState.WAITING4TURN);
    }

    @Test
    //All the players are still connected to the game: the player who's finishing their turn is the last in the list
    public void ctrlAllPlayersPresentTakingARoundTrip(){
        c1.setPlayerState(PlayerState.WAITING4TURN);
        c2.setPlayerState(PlayerState.WAITING4TURN);
        c3.setPlayerState(PlayerState.WAITING4TURN);
        c4.setPlayerState(PlayerState.PLAYING);

        gameController.specifyNextPlayer(c4);

        assertEquals(c4.getPlayerState(), PlayerState.WAITING4TURN);
        assertEquals(c1.getPlayerState(), PlayerState.PLAYING);
        assertEquals(c2.getPlayerState(), PlayerState.WAITING4TURN);
        assertEquals(c3.getPlayerState(), PlayerState.WAITING4TURN);
    }

    @Test
    public void ctrlOnePlayerDisconnected(){
        c1.setPlayerState(PlayerState.WAITING4TURN);
        c2.setPlayerState(PlayerState.PLAYING);
        c3.setPlayerState(PlayerState.DISCONNECTED);
        c4.setPlayerState(PlayerState.WAITING4TURN);

        gameController.specifyNextPlayer(c2);

        assertEquals(c4.getPlayerState(), PlayerState.PLAYING);
        assertEquals(c1.getPlayerState(), PlayerState.WAITING4TURN);
        assertEquals(c2.getPlayerState(), PlayerState.WAITING4TURN);
        assertEquals(c3.getPlayerState(), PlayerState.DISCONNECTED);
    }

    @Test
    public void ctrlTwoPlayerDisconnected(){
        c1.setPlayerState(PlayerState.WAITING4TURN);
        c2.setPlayerState(PlayerState.PLAYING);
        c3.setPlayerState(PlayerState.DISCONNECTED);
        c4.setPlayerState(PlayerState.DISCONNECTED);

        gameController.specifyNextPlayer(c2);

        assertEquals(c1.getPlayerState(), PlayerState.PLAYING);
        assertEquals(c2.getPlayerState(), PlayerState.WAITING4TURN);
        assertEquals(c3.getPlayerState(), PlayerState.DISCONNECTED);
        assertEquals(c4.getPlayerState(), PlayerState.DISCONNECTED);
    }

    @Test
    public void ctrlThreePlayerDisconnected(){
        c1.setPlayerState(PlayerState.DISCONNECTED);
        c2.setPlayerState(PlayerState.PLAYING);
        c3.setPlayerState(PlayerState.DISCONNECTED);
        c4.setPlayerState(PlayerState.DISCONNECTED);

        gameController.specifyNextPlayer(c2);

        assertEquals(c2.getPlayerState(), PlayerState.PLAYING);
        assertEquals(c3.getPlayerState(), PlayerState.DISCONNECTED);
        assertEquals(c4.getPlayerState(), PlayerState.DISCONNECTED);
        assertEquals(c1.getPlayerState(), PlayerState.DISCONNECTED);
    }

    //This test can properly function if in the specifyNextPlayer method in the GameController class there is no check about the
    //the state of the specified player (there is no:  if (currentPlayer.getPlayerState() != PlayerState.PLAYING) return;). We could
    //test the same case if we were able to make the specified client handler in the specifyNextPlayer method - in this case, c2 -
    //to become disconnected while the specifyNextPlayer is running
    /*@Test
    public void ctrlFourPlayerDisconnected(){
        c1.setPlayerState(PlayerState.DISCONNECTED);
        c2.setPlayerState(PlayerState.DISCONNECTED);
        c3.setPlayerState(PlayerState.DISCONNECTED);
        c4.setPlayerState(PlayerState.DISCONNECTED);

        Exception e = assertThrows(IllegalStateException.class, ()->gameController.specifyNextPlayer(c2));
        assertEquals(e.getMessage(), "All the players are disconnected!");
    }*/
}
