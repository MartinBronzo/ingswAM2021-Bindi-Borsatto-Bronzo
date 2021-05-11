package it.polimi.ingsw.controller.GameControllerTest;


import com.google.gson.Gson;
import it.polimi.ingsw.controller.ClientHandler;
import it.polimi.ingsw.controller.GameController;
import it.polimi.ingsw.exceptions.IllegalActionException;
import it.polimi.ingsw.model.LeaderCard.LeaderCard;
import it.polimi.ingsw.model.LeaderCard.leaderEffects.Effect;
import it.polimi.ingsw.model.MainBoard;
import it.polimi.ingsw.model.PlayerBoard;
import it.polimi.ingsw.model.ResourceType;
import it.polimi.ingsw.network.messages.fromClient.GetFromMatrixMessage;
import it.polimi.ingsw.network.messages.sendToClient.ResGottenFromMarket;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class GameControllerAnswerToClientMethodsTest {
    GameController gameController;
    ClientHandler c1;
    Reader inputStreamReader;
    BufferedReader reader;
    File file;
    BufferedReader fileReader;
    Gson gson;



    @BeforeEach
    public void setup() throws FileNotFoundException, IllegalActionException {
        gameController = new GameController();
        inputStreamReader = new InputStreamReader(System.in);
        file = new File("ClientHandler1File.txt");
        c1 = new ClientHandler(new Socket(), new BufferedReader(inputStreamReader), new PrintWriter(file));
        reader = new BufferedReader(new InputStreamReader(System.in));
        fileReader = new BufferedReader(new FileReader("ClientHandler1File.txt"));

        gameController.startMainBoard(1);
        gameController.setPlayer(c1);
        gson = new Gson();
    }

    @Test
    public void ctrlGetResFromMarket() throws IllegalActionException, IOException {
        //Let's get PlayerBoard and MainBoard so we can change their values
        PlayerBoard p1 = gameController.getMainBoard().getPlayerBoard(0);
        MainBoard mainBoard = gameController.getMainBoard();

        //Creates a list of fake LeaderCards the player will have and activate them
        List<LeaderCard> list = new ArrayList<>();
        list.add(new LeaderCard(4, new ArrayList<>(), new Effect()));
        list.add(new LeaderCard(5, new ArrayList<>(), new Effect()));
        p1.setNotPlayedLeaderCardsAtGameBeginning(list);
        p1.activateLeaderCard(p1.getNotPlayedLeaderCards().get(0));
        p1.activateLeaderCard(p1.getNotPlayedLeaderCards().get(0));

        //Gets the number of white marbles in the game so we are sure how many LeaderCards to pass to the GameController method, getResFromMkt
        int tmp = mainBoard.getNumberOfWhiteMarbleInMarketRowOrColumn(1,0);
        List<Integer> leaderCards = new ArrayList<>(tmp);
        for(int i = 0; i < tmp; i++)
            leaderCards.add(0);

        //Creates a mockup message and calls the GameController method which is going to write on a file
        GetFromMatrixMessage message = new GetFromMatrixMessage(2, 0, leaderCards);
        gameController.getResFromMkt(message, c1);

        //Retrieves the JSON result from the file, then parses it to the corresponding object and the get the resources the gameController computed when
        //we called the getResFromMkt
        String result = fileReader.readLine();
        ResGottenFromMarket resultObject = gson.fromJson(result, ResGottenFromMarket.class);
        HashMap<ResourceType, Integer> resultMap = resultObject.getResources();

        //Bypasses the GameController by directly computing the resources we would get from the market using the reference of the MainBoard of the game
        List<Effect> effects = p1.getEffectsFromCards(leaderCards);
        HashMap<ResourceType, Integer> supposedResult = mainBoard.getResourcesFromRowInMarket(1, effects);

        //Checks that the result gotten by using the GameController and the one using directly the MainBoard are equal
        assertEquals(resultMap, supposedResult);
        for(Map.Entry<ResourceType, Integer> e: resultMap.entrySet())
            assertEquals(e.getValue(), supposedResult.get(e.getKey()));
        for(Map.Entry<ResourceType, Integer> e: supposedResult.entrySet())
            assertEquals(e.getValue(), resultMap.get(e.getKey()));
    }

}
