package it.polimi.ingsw.controller.GameControllerTest;


import com.google.gson.Gson;
import it.polimi.ingsw.controller.ClientHandler;
import it.polimi.ingsw.controller.GameController;
import it.polimi.ingsw.exceptions.EndOfGameException;
import it.polimi.ingsw.exceptions.IllegalActionException;
import it.polimi.ingsw.exceptions.NegativeQuantityException;
import it.polimi.ingsw.model.DevCards.DevCard;
import it.polimi.ingsw.model.DevCards.DevCardColour;
import it.polimi.ingsw.model.LeaderCard.LeaderCard;
import it.polimi.ingsw.model.LeaderCard.leaderEffects.DiscountLeaderEffect;
import it.polimi.ingsw.model.LeaderCard.leaderEffects.Effect;
import it.polimi.ingsw.model.MainBoard;
import it.polimi.ingsw.model.PlayerBoard;
import it.polimi.ingsw.model.ResourceType;
import it.polimi.ingsw.network.messages.fromClient.*;
import it.polimi.ingsw.network.messages.sendToClient.HashMapResources;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class GameControllerAnswerToClientMethodsTest {
    GameController gameController;
    ClientHandler c1;
    ClientHandler c2;
    Reader inputStreamReader;
    BufferedReader reader;
    File file;
    BufferedReader fileReader1;
    BufferedReader fileReader2;
    Gson gson;


    @BeforeEach
    public void setup() throws FileNotFoundException, IllegalActionException {
        gameController = new GameController();
        inputStreamReader = new InputStreamReader(System.in);
        c1 = new ClientHandler(new Socket(), new BufferedReader(inputStreamReader), new PrintWriter(new File("ClientHandler1File.json")));
        c2 = new ClientHandler(new Socket(), new BufferedReader(inputStreamReader), new PrintWriter(new File("ClientHandler2File.json")));
        reader = new BufferedReader(new InputStreamReader(System.in));
        fileReader1 = new BufferedReader(new FileReader("ClientHandler1File.json"));
        fileReader2 = new BufferedReader(new FileReader("ClientHandler2File.json"));


        gson = new Gson();
    }

    @Test
    public void ctrlGetResFromMarket() throws IllegalActionException, IOException {
        //Initiates the game
        gameController.startMainBoard(1);
        gameController.setPlayer(c1);

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
        int tmp = mainBoard.getNumberOfWhiteMarbleInMarketRowOrColumn(1, 0);
        List<Integer> leaderCards = new ArrayList<>(tmp);
        for (int i = 0; i < tmp; i++)
            leaderCards.add(0);

        //Creates a mockup message and calls the GameController method which is going to write on a file
        GetFromMatrixMessage message = new GetFromMatrixMessage(2, 0, leaderCards);
        gameController.getResFromMkt(message, c1);

        //Retrieves the JSON result from the file, then parses it to the corresponding object and the get the resources the gameController computed when
        //we called the getResFromMkt
        String result = fileReader1.readLine();
        HashMapResources resultObject = gson.fromJson(result, HashMapResources.class);
        HashMap<ResourceType, Integer> resultMap = resultObject.getResources();

        //Bypasses the GameController by directly computing the resources we would get from the market using the reference of the MainBoard of the game
        List<Effect> effects = p1.getEffectsFromCards(leaderCards);
        HashMap<ResourceType, Integer> supposedResult = mainBoard.getResourcesFromRowInMarket(1, effects);

        //Checks that the result gotten by using the GameController and the one using directly the MainBoard are equal
        assertEquals(resultMap, supposedResult);
        for (Map.Entry<ResourceType, Integer> e : resultMap.entrySet())
            assertEquals(e.getValue(), supposedResult.get(e.getKey()));
        for (Map.Entry<ResourceType, Integer> e : supposedResult.entrySet())
            assertEquals(e.getValue(), resultMap.get(e.getKey()));
    }

    @Test
    public void ctrlDiscardLeaderAtBeginning() throws IllegalActionException, IOException {
        //Initiates the game
        gameController.startMainBoard(2);
        gameController.setPlayer(c1);
        gameController.setPlayer(c2);

        //Let's get PlayerBoard and MainBoard so we can change their values
        PlayerBoard p1 = gameController.getMainBoard().getPlayerBoard(0);
        MainBoard mainBoard = gameController.getMainBoard();

        //Creates a list of fake LeaderCards the player will have
        List<LeaderCard> list = new ArrayList<>();
        list.add(new LeaderCard(4, new ArrayList<>(), new Effect()));
        list.add(new LeaderCard(5, new ArrayList<>(), new Effect()));
        list.add(new LeaderCard(6, new ArrayList<>(), new Effect()));
        list.add(new LeaderCard(7, new ArrayList<>(), new Effect()));
        p1.setNotPlayedLeaderCardsAtGameBeginning(list);

        //Creates a list of indexes of the cards the player wants to discard
        List<Integer> indexes = new ArrayList<>();
        indexes.add(1);
        indexes.add(2);

        //Sets the first player: c1 will be second so they'll get 1 extra resource to pick
        gameController.setFirstPlayer(1);

        //Creates a mockup message and calls the GameController method which is going to write on a file
        List<DepotParams> depotList = new ArrayList<>();
        depotList.add(new DepotParams(ResourceType.COIN, 1, 1));
        DiscardLeaderAndExtraResBeginningMessage message = new DiscardLeaderAndExtraResBeginningMessage(indexes, depotList);
        gameController.discardLeaderAndExtraResBeginning(message, c1);

        //Retrieves the JSON results from the files of the two ClientHandlers and checks that they are equal
        String result1 = fileReader1.readLine();
        String result2 = fileReader2.readLine();
        assertEquals(result1, result2);

        //TO BE continued...
    }

    @Test
    public void getDevCardCost() throws IllegalActionException, IOException {
        //Initiates the game
        gameController.startMainBoard(1);
        gameController.setPlayer(c1);

        PlayerBoard p1 = gameController.getMainBoard().getPlayerBoard(0);
        MainBoard mainBoard = gameController.getMainBoard();

        //Creates a list of fake LeaderCards the player will have and activate them
        List<LeaderCard> list = new ArrayList<>();
        list.add(new LeaderCard(4, new ArrayList<>(), new Effect()));
        list.add(new LeaderCard(5, new ArrayList<>(), new Effect()));
        p1.setNotPlayedLeaderCardsAtGameBeginning(list);

        List<Integer> leaderCards = new ArrayList<>();

        GetFromMatrixMessage message = new GetFromMatrixMessage(3, 1, leaderCards);
        assertTrue(gameController.getCardCost(message, c1));

        String result = fileReader1.readLine();
        HashMapResources resultObject = gson.fromJson(result, HashMapResources.class);
        HashMap<ResourceType, Integer> resultMap = resultObject.getResources();

        List<Effect> effects = p1.getEffectsFromCards(leaderCards);
        DevCard devCard = mainBoard.getDevCardFromDeckInDevGrid(2, 0);

        HashMap<ResourceType, Integer> supposedResult = mainBoard.applyDiscountToDevCard(devCard, effects);

        assertEquals(resultMap, supposedResult);
        for (Map.Entry<ResourceType, Integer> e : resultMap.entrySet())
            assertEquals(e.getValue(), supposedResult.get(e.getKey()));
        for (Map.Entry<ResourceType, Integer> e : supposedResult.entrySet())
            assertEquals(e.getValue(), resultMap.get(e.getKey()));

        System.out.println(supposedResult + " ... " + result);
    }

    @Test
    public void getDevCardCost2() throws IllegalActionException, IOException, NegativeQuantityException {
        //Initiates the game
        gameController.startMainBoard(1);
        gameController.setPlayer(c1);

        PlayerBoard p1 = gameController.getMainBoard().getPlayerBoard(0);
        MainBoard mainBoard = gameController.getMainBoard();

        //Creates a list of fake LeaderCards the player will have and activate them
        List<LeaderCard> list = new ArrayList<>();
        list.add(new LeaderCard(4, new ArrayList<>(), new DiscountLeaderEffect(ResourceType.SHIELD, 1)));
        list.add(new LeaderCard(5, new ArrayList<>(), new DiscountLeaderEffect(ResourceType.STONE, 1)));
        p1.setNotPlayedLeaderCardsAtGameBeginning(list);
        p1.activateLeaderCard(p1.getNotPlayedLeaderCards().get(0));
        p1.activateLeaderCard(p1.getNotPlayedLeaderCards().get(0));

        List<Integer> leaderCards = new ArrayList<>();
        leaderCards.add(0);
        leaderCards.add(1);

        GetFromMatrixMessage message = new GetFromMatrixMessage(3, 1, leaderCards);
        assertTrue(gameController.getCardCost(message, c1));

        String result = fileReader1.readLine();
        HashMapResources resultObject = gson.fromJson(result, HashMapResources.class);
        HashMap<ResourceType, Integer> resultMap = resultObject.getResources();

        List<Effect> effects = p1.getEffectsFromCards(leaderCards);
        DevCard devCard = mainBoard.getDevCardFromDeckInDevGrid(2, 0);

        HashMap<ResourceType, Integer> supposedResult = mainBoard.applyDiscountToDevCard(devCard, effects);

        assertEquals(resultMap, supposedResult);
        for (Map.Entry<ResourceType, Integer> e : resultMap.entrySet())
            assertEquals(e.getValue(), supposedResult.get(e.getKey()));
        for (Map.Entry<ResourceType, Integer> e : supposedResult.entrySet())
            assertEquals(e.getValue(), resultMap.get(e.getKey()));

        System.out.println(devCard.getCost());
        System.out.println(supposedResult + " ... " + result);
    }

    @Test
    public void getProdCost() throws IllegalActionException, IOException, NegativeQuantityException, EndOfGameException {
        //Initiates the game
        gameController.startMainBoard(1);
        gameController.setPlayer(c1);

        PlayerBoard p1 = gameController.getMainBoard().getPlayerBoard(0);
        MainBoard mainBoard = gameController.getMainBoard();

        //Creates a list of fake LeaderCards the player will have and activate them
        List<LeaderCard> list = new ArrayList<>();
        list.add(new LeaderCard(4, new ArrayList<>(), new Effect()));
        list.add(new LeaderCard(5, new ArrayList<>(), new Effect()));
        p1.setNotPlayedLeaderCardsAtGameBeginning(list);

        List<Integer> leaderCards = new ArrayList<>();

        //Add fake devCards to devSlots
        HashMap<ResourceType, Integer> input1 = new HashMap<>();
        input1.put(ResourceType.STONE, 3);
        input1.put(ResourceType.COIN, 5);

        HashMap<ResourceType, Integer> input2 = new HashMap<>();
        input2.put(ResourceType.SERVANT, 4);
        input2.put(ResourceType.SHIELD, 1);

        DevCard devCard1 = new DevCard(1, DevCardColour.GREEN, 1, input1, new HashMap<>(), new HashMap<>(), "");
        DevCard devCard2 = new DevCard(1, DevCardColour.GREEN, 1, input2, new HashMap<>(), new HashMap<>(), "");

        p1.addCardToDevSlot(0, devCard1);
        p1.addCardToDevSlot(1, devCard2);

        p1.addResourcesToStrongbox(input1);
        p1.addResourcesToStrongbox(input2);

        List < Integer > devListIndex = new ArrayList<>();
        devListIndex.add(0);
        devListIndex.add(1);

        List < DevCard > devCardList = new ArrayList<>();
        devCardList.add(devCard1);
        devCardList.add(devCard2);

        GetProductionCostMessage message = new GetProductionCostMessage(devListIndex, leaderCards, new BaseProductionParams(false, new ArrayList<>()));
        assertTrue(gameController.getProductionCost(message, c1));

        String result = fileReader1.readLine();
        HashMapResources resultObject = gson.fromJson(result, HashMapResources.class);
        HashMap<ResourceType, Integer> resultMap = resultObject.getResources();

        HashMap<ResourceType, Integer> supposedResult = p1.getProductionCost(devCardList, new ArrayList<>() , false);

        assertEquals(resultMap, supposedResult);
        for (Map.Entry<ResourceType, Integer> e : resultMap.entrySet())
            assertEquals(e.getValue(), supposedResult.get(e.getKey()));
        for (Map.Entry<ResourceType, Integer> e : supposedResult.entrySet())
            assertEquals(e.getValue(), resultMap.get(e.getKey()));

        System.out.println(supposedResult + " ... " + result);
    }

    //TODO: other tests for base production and leader cards
}
