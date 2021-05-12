package it.polimi.ingsw.controller.GameControllerTest;


import com.google.gson.Gson;
import it.polimi.ingsw.client.readOnlyModel.Game;
import it.polimi.ingsw.client.readOnlyModel.Player;
import it.polimi.ingsw.client.readOnlyModel.player.DepotShelf;
import it.polimi.ingsw.controller.ClientHandler;
import it.polimi.ingsw.controller.GameController;
import it.polimi.ingsw.exceptions.EndOfGameException;
import it.polimi.ingsw.exceptions.IllegalActionException;
import it.polimi.ingsw.exceptions.NegativeQuantityException;
import it.polimi.ingsw.model.DevCards.DevCard;
import it.polimi.ingsw.model.DevCards.DevCardColour;
import it.polimi.ingsw.model.LeaderCard.LeaderCard;
import it.polimi.ingsw.model.LeaderCard.LeaderCardRequirements.CardRequirementResource;
import it.polimi.ingsw.model.LeaderCard.LeaderCardRequirements.Requirement;
import it.polimi.ingsw.model.LeaderCard.leaderEffects.*;
import it.polimi.ingsw.model.MainBoard;
import it.polimi.ingsw.model.PlayerBoard;
import it.polimi.ingsw.model.ResourceType;
import it.polimi.ingsw.model.marble.MarbleType;
import it.polimi.ingsw.network.messages.fromClient.*;
import it.polimi.ingsw.network.messages.sendToClient.HashMapResources;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.*;
import java.net.Socket;
import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.*;

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
        c1 = new ClientHandler(new Socket(), new BufferedReader(inputStreamReader), new PrintWriter("ClientHandler1File.json"));
        c2 = new ClientHandler(new Socket(), new BufferedReader(inputStreamReader), new PrintWriter("ClientHandler2File.json"));
        reader = new BufferedReader(new InputStreamReader(System.in));
        c1.setNickname("Client 1");
        c2.setNickname("Client 2");
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
        LeaderCard l1 = new LeaderCard(4, new ArrayList<>(), new Effect());
        LeaderCard l4 = new LeaderCard(7, new ArrayList<>(), new Effect());
        list.add(l1);
        list.add(new LeaderCard(5, new ArrayList<>(), new Effect()));
        list.add(new LeaderCard(6, new ArrayList<>(), new Effect()));
        list.add(l4);
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

        //Checks the inside status of the PlayerBoard
        assertEquals(p1.getResourceTypeFromShelf(1), ResourceType.COIN);
        assertEquals(p1.getNumberOfResInShelf(1), 1);
        assertNull(p1.getResourceTypeFromShelf(2));
        assertEquals(p1.getNumberOfResInShelf(2), 0);
        assertNull(p1.getResourceTypeFromShelf(3));
        assertEquals(p1.getNumberOfResInShelf(3), 0);
        assertEquals(p1.getNotPlayedLeaderCards().size(), 2);
        assertTrue(p1.getNotPlayedLeaderCards().contains(l1));
        assertTrue(p1.getNotPlayedLeaderCards().contains(l4));
        assertEquals(p1.getPositionOnFaithTrack(), 0);
        assertFalse(p1.getPopeTile().get(0).isChanged());
        assertFalse(p1.getPopeTile().get(1).isChanged());
        assertFalse(p1.getPopeTile().get(2).isChanged());

        //Checks that the message retrieved from the JSON received by the clients is correctly formed
        Game game = gson.fromJson(result1, Game.class);
        Collection<Player> playersCollection = game.getPlayers();
        assertEquals(playersCollection.size(), 1);
        Player playerModel = playersCollection.stream().filter(x -> x.getNickName().equals(c1.getNickname())).findFirst().get();
        assertEquals(playerModel.getUnUsedLeaders().size(), 2);
        assertTrue(playerModel.getUnUsedLeaders().contains(l1));
        assertTrue(playerModel.getUnUsedLeaders().contains(l4));
        assertEquals(playerModel.getFaithPosition(), 0);
        assertFalse(playerModel.getPopeTiles().get(0).isChanged());
        assertFalse(playerModel.getPopeTiles().get(1).isChanged());
        assertFalse(playerModel.getPopeTiles().get(2).isChanged());
        assertEquals(playerModel.getDepotShelves().get(0).getResourceType(), ResourceType.COIN);
        assertEquals(playerModel.getDepotShelves().get(0).getQuantity(), 1);
        assertNull(playerModel.getDepotShelves().get(1).getResourceType());
        assertEquals(playerModel.getDepotShelves().get(1).getQuantity(), 0);
        assertNull(playerModel.getDepotShelves().get(2).getResourceType());
        assertEquals(playerModel.getDepotShelves().get(2).getQuantity(), 0);
    }

    @Test
    public void getDevCardCost() throws IOException {
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
    public void getDevCardCost2() throws IllegalActionException, IOException {
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

        List<Integer> devListIndex = new ArrayList<>();
        devListIndex.add(0);
        devListIndex.add(1);

        List<DevCard> devCardList = new ArrayList<>();
        devCardList.add(devCard1);
        devCardList.add(devCard2);

        GetProductionCostMessage message = new GetProductionCostMessage(devListIndex, leaderCards, new BaseProductionParams(false, new ArrayList<>(), new ArrayList<>()));
        assertTrue(gameController.getProductionCost(message, c1));

        String result = fileReader1.readLine();
        HashMapResources resultObject = gson.fromJson(result, HashMapResources.class);
        HashMap<ResourceType, Integer> resultMap = resultObject.getResources();

        HashMap<ResourceType, Integer> supposedResult = p1.getProductionCost(devCardList, new ArrayList<>(), false);

        assertEquals(resultMap, supposedResult);
        for (Map.Entry<ResourceType, Integer> e : resultMap.entrySet())
            assertEquals(e.getValue(), supposedResult.get(e.getKey()));
        for (Map.Entry<ResourceType, Integer> e : supposedResult.entrySet())
            assertEquals(e.getValue(), resultMap.get(e.getKey()));

        System.out.println(supposedResult + " ... " + result);
    }

    @Test
    public void getProdCostWithBaseProd() throws IllegalActionException, IOException, NegativeQuantityException, EndOfGameException {
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

        p1.addResourcesToStrongbox(input1);
        p1.addResourcesToStrongbox(input2);

        List<Integer> devListIndex = new ArrayList<>();
        devListIndex.add(0);
        devListIndex.add(1);

        List<DevCard> devCardList = new ArrayList<>();
        devCardList.add(devCard1);
        devCardList.add(devCard2);

        ArrayList<ResourceType> inputList = new ArrayList<>();
        inputList.add(ResourceType.COIN);
        inputList.add(ResourceType.SHIELD);

        ArrayList<ResourceType> outputList = new ArrayList<>();
        outputList.add(ResourceType.SERVANT);

        GetProductionCostMessage message = new GetProductionCostMessage(devListIndex, leaderCards, new BaseProductionParams(true, inputList, outputList));
        assertTrue(gameController.getProductionCost(message, c1));

        String result = fileReader1.readLine();
        HashMapResources resultObject = gson.fromJson(result, HashMapResources.class);
        HashMap<ResourceType, Integer> resultMap = resultObject.getResources();

        HashMap<ResourceType, Integer> supposedResult = p1.getProductionCost(devCardList, new ArrayList<>(), true);

        assertEquals(resultMap, supposedResult);
        for (Map.Entry<ResourceType, Integer> e : resultMap.entrySet())
            assertEquals(e.getValue(), supposedResult.get(e.getKey()));
        for (Map.Entry<ResourceType, Integer> e : supposedResult.entrySet())
            assertEquals(e.getValue(), resultMap.get(e.getKey()));

        System.out.println(supposedResult + " ... " + result);
    }

    @Test
    public void getProdCostWithLeaders() throws IllegalActionException, IOException, NegativeQuantityException, EndOfGameException {
        //Initiates the game
        gameController.startMainBoard(1);
        gameController.setPlayer(c1);

        PlayerBoard p1 = gameController.getMainBoard().getPlayerBoard(0);

        //Creates a list of fake LeaderCards the player will have and activate them
        List<LeaderCard> list = new ArrayList<>();
        LeaderCard leaderCard1 = new LeaderCard(4, new ArrayList<>(), new ExtraProductionLeaderEffect(ResourceType.COIN, 2));

        list.add(leaderCard1);
        list.add(new LeaderCard(5, new ArrayList<>(), new Effect()));
        p1.setNotPlayedLeaderCardsAtGameBeginning(list);
        p1.activateLeaderCard(p1.getNotPlayedLeaderCards().get(0));

        List<Integer> leaderCards = new ArrayList<>();
        leaderCards.add(0);

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

        p1.addResourcesToStrongbox(input1);
        p1.addResourcesToStrongbox(input2);

        List<Integer> devListIndex = new ArrayList<>();
        devListIndex.add(0);
        devListIndex.add(1);

        List<DevCard> devCardList = new ArrayList<>();
        devCardList.add(devCard1);
        devCardList.add(devCard2);

        ArrayList<ResourceType> inputList = new ArrayList<>();
        inputList.add(ResourceType.COIN);
        inputList.add(ResourceType.SHIELD);

        ArrayList<ResourceType> outputList = new ArrayList<>();
        outputList.add(ResourceType.SERVANT);

        GetProductionCostMessage message = new GetProductionCostMessage(devListIndex, leaderCards, new BaseProductionParams(true, inputList, outputList));
        assertTrue(gameController.getProductionCost(message, c1));

        String result = fileReader1.readLine();
        HashMapResources resultObject = gson.fromJson(result, HashMapResources.class);
        HashMap<ResourceType, Integer> resultMap = resultObject.getResources();

        List<LeaderCard> extraProdLeaderList = new ArrayList<>();
        extraProdLeaderList.add(leaderCard1);

        HashMap<ResourceType, Integer> supposedResult = p1.getProductionCost(devCardList, extraProdLeaderList, true);

        assertEquals(resultMap, supposedResult);
        for (Map.Entry<ResourceType, Integer> e : resultMap.entrySet())
            assertEquals(e.getValue(), supposedResult.get(e.getKey()));
        for (Map.Entry<ResourceType, Integer> e : supposedResult.entrySet())
            assertEquals(e.getValue(), resultMap.get(e.getKey()));

        System.out.println(supposedResult + " ... " + result);
    }

    @Test
    public void ctrlDiscardALeaderCard() throws IOException {
        //Initiates the game
        gameController.startMainBoard(2);
        gameController.setPlayer(c1);
        gameController.setPlayer(c2);

        //Let's get PlayerBoard and MainBoard so we can change their values
        MainBoard mainBoard = gameController.getMainBoard();
        PlayerBoard p1 = mainBoard.getPlayerBoard(0);
        PlayerBoard p2 = mainBoard.getPlayerBoard(1);


        //Creates a list of fake LeaderCards the player will have (we skipp the passage in which the player should discard the some LeaderCards they
        //received at the beginning of the game: these two cards are already the ones they'll have for the rest of the game)
        List<LeaderCard> list = new ArrayList<>();
        LeaderCard l1 = new LeaderCard(4, new ArrayList<>(), new Effect());
        LeaderCard l2 = new LeaderCard(5, new ArrayList<>(), new Effect());
        list.add(l1);
        list.add(l2);
        p1.setNotPlayedLeaderCardsAtGameBeginning(list);

        //Creates a mockup message and calls the GameController method which is going to write on a file
        LeaderMessage message = new LeaderMessage(1);
        gameController.discardLeader(message, c1);

        //We check that the status of the Client 1 has correctly changed
        assertEquals(p1.getNotPlayedLeaderCards().size(), 1);
        assertTrue(p1.getNotPlayedLeaderCards().contains(l1));
        assertEquals(p1.getPositionOnFaithTrack(), 1);
        assertFalse(p1.getPopeTile().get(0).isChanged());
        assertFalse(p1.getPopeTile().get(1).isChanged());
        assertFalse(p1.getPopeTile().get(2).isChanged());

        //We check that the status of the other client has also correctly changed (actually, in this case since no Vatican Report took place then
        //Client 2 has not changed)
        assertEquals(p2.getPositionOnFaithTrack(), 0);
        assertFalse(p2.getPopeTile().get(0).isChanged());
        assertFalse(p2.getPopeTile().get(1).isChanged());
        assertFalse(p2.getPopeTile().get(2).isChanged());

        //Retrieves the JSON results from the files of the two ClientHandlers and checks that they are equal
        String result1 = fileReader1.readLine();
        String result2 = fileReader2.readLine();
        assertEquals(result1, result2);


        //Checks that the message retrieved from the JSON received by the clients is correctly formed
        Game game = gson.fromJson(result1, Game.class);
        Collection<Player> playersCollection = game.getPlayers();
        assertEquals(playersCollection.size(), 2);
        Player playerModel1 = playersCollection.stream().filter(x -> x.getNickName().equals(c1.getNickname())).findFirst().get();
        Player playerModel2 = playersCollection.stream().filter(x -> x.getNickName().equals(c2.getNickname())).findFirst().get();
        assertEquals(playerModel1.getUnUsedLeaders().size(), 1);
        assertTrue(playerModel1.getUnUsedLeaders().contains(l1));
        assertEquals(playerModel1.getFaithPosition(), 1);
        assertFalse(playerModel1.getPopeTiles().get(0).isChanged());
        assertFalse(playerModel1.getPopeTiles().get(1).isChanged());
        assertFalse(playerModel1.getPopeTiles().get(2).isChanged());
        assertEquals(playerModel2.getFaithPosition(), 0);
        assertFalse(playerModel2.getPopeTiles().get(0).isChanged());
        assertFalse(playerModel2.getPopeTiles().get(1).isChanged());
        assertFalse(playerModel2.getPopeTiles().get(2).isChanged());
    }

    @Test
    public void ctrlActivateLeaderCard() throws IOException, IllegalActionException {
        //Initiates the game
        gameController.startMainBoard(2);
        gameController.setPlayer(c1);
        gameController.setPlayer(c2);

        //Let's get PlayerBoard and MainBoard so we can change their values
        MainBoard mainBoard = gameController.getMainBoard();
        PlayerBoard p1 = mainBoard.getPlayerBoard(0);
        PlayerBoard p2 = mainBoard.getPlayerBoard(1);


        //Creates a list of fake LeaderCards the player will have (we skipp the passage in which the player should discard the some LeaderCards they
        //received at the beginning of the game: these two cards are already the ones they'll have for the rest of the game)
        List<LeaderCard> list = new ArrayList<>();
        CardRequirementResource req = new CardRequirementResource(ResourceType.COIN, 1);
        List<Requirement> reqList = new ArrayList<>();
        reqList.add(req);
        LeaderCard l1 = new LeaderCard(4, new ArrayList<>() /*reqList*/, new Effect() /*new ExtraSlotLeaderEffect(ResourceType.COIN, 2)*/);
        LeaderCard l2 = new LeaderCard(5, new ArrayList<>(), new Effect());
        list.add(l1);
        list.add(l2);
        p1.setNotPlayedLeaderCardsAtGameBeginning(list);

        //We make sure that the player meets all the requirements for the LeaderCard l1
        //p1.addResourceToDepot(ResourceType.COIN, 1, 1);

        //Creates a mockup message and calls the GameController method which is going to write on a file
        LeaderMessage message = new LeaderMessage(0);
        gameController.activateLeader(message, c1);

        //We check that the status of the Client 1 has correctly changed
        assertEquals(p1.getNotPlayedLeaderCards().size(), 1);
        assertTrue(p1.getNotPlayedLeaderCards().contains(l2));
        assertEquals(p1.getActiveLeaderCards().size(), 1);
        assertTrue(p1.getActiveLeaderCards().contains(l1));

        //Retrieves the JSON results from the files of the two ClientHandlers and checks that they are equal
        String result1 = fileReader1.readLine();
        String result2 = fileReader2.readLine();
        assertEquals(result1, result2);

        //Checks that the message retrieved from the JSON received by the clients is correctly formed
        Game game = gson.fromJson(result1, Game.class);
        Collection<Player> playersCollection = game.getPlayers();
        assertEquals(playersCollection.size(), 1);
        Player playerModel1 = playersCollection.stream().filter(x -> x.getNickName().equals(c1.getNickname())).findFirst().get();
        assertEquals(playerModel1.getUnUsedLeaders().size(), 1);
        assertTrue(playerModel1.getUnUsedLeaders().contains(l2));
        assertEquals(playerModel1.getUsedLeaders().size(), 1);
        assertTrue(playerModel1.getUsedLeaders().contains(l1));
    }

    @Test
    //Tests that nothing happens if the player gives bad parameters: they don't specify what to do with the resources
    public void ctrlBuyFromMarketBadParameters() throws IllegalActionException {
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

        //We save the status of the Market before trying to make any change
        String marketBefore = mainBoard.getMarket().toString();
        MarbleType marbleOnSlideBefore = mainBoard.getMarbleOnSlideWithMarbleType();

        //Creates a mockup message and calls the GameController method which is going to write on a file
        BuyFromMarketMessage buyFromMarketMessage = new BuyFromMarketMessage(2, 0, leaderCards, new ArrayList<>(), new HashMap<>(), new HashMap<>());
        IllegalArgumentException e = assertThrows(IllegalArgumentException.class, () -> gameController.buyFromMarket(buyFromMarketMessage, c1));
        assertEquals(e.getMessage(), "The given input parameters don't match the result: you are missing out some resources!");
        //We need to get the new reference to the MainBoard since it changed due to the rollback!
        MainBoard mainBoardAfter = gameController.getMainBoard();
        assertNotSame(mainBoard, mainBoardAfter);
        String marketAfter = mainBoardAfter.getMarket().toString();
        MarbleType marbleOnSlideAfter = mainBoardAfter.getMarbleOnSlideWithMarbleType();
        assertEquals(marketAfter, marketBefore);
        assertEquals(marbleOnSlideAfter, marbleOnSlideBefore);
    }

    @Test
    public void moveResBtwShelves() throws IllegalActionException, IOException {
        //Initiates the game
        gameController.startMainBoard(2);
        gameController.setPlayer(c1);
        gameController.setPlayer(c2);

        PlayerBoard p1 = gameController.getMainBoard().getPlayerBoard(0);

        p1.addResourceToDepot(ResourceType.SHIELD, 2, 2);
        p1.addResourceToDepot(ResourceType.COIN, 1,3);


        MoveBetweenShelvesMessage moveBetweenShelvesMessage = new MoveBetweenShelvesMessage(2,3);
        assertTrue(gameController.moveResourcesBetweenShelves(moveBetweenShelvesMessage, c1));

        assertEquals(p1.getResourceTypeFromShelf(2),ResourceType.COIN);
        assertEquals(p1.getResourceTypeFromShelf(3),ResourceType.SHIELD);
        assertEquals(p1.getNumberOfResInShelf(2),1);
        assertEquals(p1.getNumberOfResInShelf(3),2);

        String result1 = fileReader1.readLine();
        String result2 = fileReader2.readLine();
        assertEquals(result1, result2);

        Game game = gson.fromJson(result1, Game.class);
        Collection<Player> playersCollection = game.getPlayers();
        Player playerModel1 = playersCollection.stream().filter(x -> x.getNickName().equals(c1.getNickname())).findFirst().get();
        List<DepotShelf> shelves =  playerModel1.getDepotShelves();
        assertNull(shelves.get(0).getResourceType());
        assertEquals(shelves.get(1).getResourceType(), ResourceType.COIN);
        assertEquals(shelves.get(2).getResourceType(), ResourceType.SHIELD);
        assertEquals(shelves.get(1).getQuantity(), 1);
        assertEquals(shelves.get(2).getQuantity(), 2);
    }

    @Test
    public void moveToLeader() throws IllegalActionException, IOException {
        //Initiates the game
        gameController.startMainBoard(2);
        gameController.setPlayer(c1);
        gameController.setPlayer(c2);

        PlayerBoard p1 = gameController.getMainBoard().getPlayerBoard(0);

        List<LeaderCard> list = new ArrayList<>();
        LeaderCard leaderCard1 = new LeaderCard(4, new ArrayList<>(), new ExtraSlotLeaderEffect(ResourceType.COIN, 2));

        list.add(leaderCard1);
        list.add(new LeaderCard(5, new ArrayList<>(), new Effect()));
        p1.setNotPlayedLeaderCardsAtGameBeginning(list);
        p1.activateLeaderCard(p1.getNotPlayedLeaderCards().get(0));

        p1.addResourceToDepot(ResourceType.SHIELD, 2, 2);
        p1.addResourceToDepot(ResourceType.COIN, 3,3);


        MoveShelfToLeaderMessage moveLeaderToShelfMessage = new MoveShelfToLeaderMessage(3, 2);
        assertTrue(gameController.moveResourcesToLeader(moveLeaderToShelfMessage, c1));

        assertEquals(p1.getResourceTypeFromShelf(2),ResourceType.SHIELD);
        assertEquals(p1.getResourceTypeFromShelf(3),ResourceType.COIN);
        assertEquals(p1.getNumberOfResInShelf(2),2);
        assertEquals(p1.getNumberOfResInShelf(3),1);


        String result1 = fileReader1.readLine();
        String result2 = fileReader2.readLine();
        assertEquals(result1, result2);

        Game game = gson.fromJson(result1, Game.class);
        Collection<Player> playersCollection = game.getPlayers();
        Player playerModel1 = playersCollection.stream().filter(x -> x.getNickName().equals(c1.getNickname())).findFirst().get();
        List<DepotShelf> shelves =  playerModel1.getDepotShelves();
        assertNull(shelves.get(0).getResourceType());
        assertEquals(shelves.get(1).getResourceType(), ResourceType.SHIELD);
        assertEquals(shelves.get(2).getResourceType(), ResourceType.COIN);
        assertEquals(shelves.get(1).getQuantity(), 2);
        assertEquals(shelves.get(2).getQuantity(), 1);
    }

    @Test
    public void moveToShelf() throws IllegalActionException, IOException {
        //Initiates the game
        gameController.startMainBoard(2);
        gameController.setPlayer(c1);
        gameController.setPlayer(c2);

        PlayerBoard p1 = gameController.getMainBoard().getPlayerBoard(0);

        List<LeaderCard> list = new ArrayList<>();
        LeaderCard leaderCard1 = new LeaderCard(4, new ArrayList<>(), new ExtraSlotLeaderEffect(ResourceType.COIN, 2));

        list.add(leaderCard1);
        list.add(new LeaderCard(5, new ArrayList<>(), new Effect()));
        p1.setNotPlayedLeaderCardsAtGameBeginning(list);
        p1.activateLeaderCard(p1.getNotPlayedLeaderCards().get(0));

        p1.addResourceToDepot(ResourceType.SHIELD, 2, 2);
        p1.addResourceToDepot(ResourceType.COIN, 1,3);
        p1.addResourceToLeader(ResourceType.COIN,1);

        MoveLeaderToShelfMessage moveLeaderToShelfMessage = new MoveLeaderToShelfMessage(ResourceType.COIN, 1, 3);
        assertTrue(gameController.moveResourcesToShelf(moveLeaderToShelfMessage, c1));

        assertEquals(p1.getResourceTypeFromShelf(2),ResourceType.SHIELD);
        assertEquals(p1.getResourceTypeFromShelf(3),ResourceType.COIN);
        assertEquals(p1.getNumberOfResInShelf(2),2);
        assertEquals(p1.getNumberOfResInShelf(3),2);


        String result1 = fileReader1.readLine();
        String result2 = fileReader2.readLine();
        assertEquals(result1, result2);

        Game game = gson.fromJson(result1, Game.class);
        Collection<Player> playersCollection = game.getPlayers();
        Player playerModel1 = playersCollection.stream().filter(x -> x.getNickName().equals(c1.getNickname())).findFirst().get();
        List<DepotShelf> shelves =  playerModel1.getDepotShelves();
        assertNull(shelves.get(0).getResourceType());
        assertEquals(shelves.get(1).getResourceType(), ResourceType.SHIELD);
        assertEquals(shelves.get(2).getResourceType(), ResourceType.COIN);
        assertEquals(shelves.get(1).getQuantity(), 2);
        assertEquals(shelves.get(2).getQuantity(), 2);
    }

    @Test
    public void activateProduction() throws IllegalActionException, NegativeQuantityException, EndOfGameException, IOException {
        //Initiates the game
        gameController.startMainBoard(2);
        gameController.setPlayer(c1);
        gameController.setPlayer(c2);

        PlayerBoard p1 = gameController.getMainBoard().getPlayerBoard(0);

        //Add fake devCards to devSlots
        HashMap<ResourceType, Integer> input1 = new HashMap<>();
        input1.put(ResourceType.STONE, 3);
        input1.put(ResourceType.COIN, 1);
        HashMap<ResourceType, Integer> output1 = new HashMap<>();
        output1.put(ResourceType.SHIELD,2);

        HashMap<ResourceType, Integer> input2 = new HashMap<>();
        input2.put(ResourceType.COIN, 1);
        HashMap<ResourceType, Integer> output2 = new HashMap<>();
        output2.put(ResourceType.STONE,1);

        DevCard devCard1 = new DevCard(1, DevCardColour.GREEN, 1, input1, output1, new HashMap<>(), "");
        DevCard devCard2 = new DevCard(1, DevCardColour.GREEN, 1, input2, output2, new HashMap<>(), "");

        p1.addCardToDevSlot(0, devCard1);
        p1.addCardToDevSlot(1, devCard2);

        p1.addResourceToDepot(ResourceType.COIN, 2, 2);
        p1.addResourceToDepot(ResourceType.STONE, 3,3);

        List<Integer> devCardIndexes = new ArrayList<>();
        devCardIndexes.add(0);
        devCardIndexes.add(1);

        List<DepotParams> depoRes = new ArrayList<>();
        depoRes.add(new DepotParams(ResourceType.STONE, 3,3));
        depoRes.add(new DepotParams(ResourceType.COIN, 2,2));
        ActivateProductionMessage activateProductionMessage = new ActivateProductionMessage(devCardIndexes, new HashMap<>(), new BaseProductionParams(false, new ArrayList<>(), new ArrayList<>()), depoRes, new HashMap<>(), new HashMap<>());
        assertTrue(gameController.activateProduction(activateProductionMessage, c1));

        assertNull(p1.getResourceTypeFromShelf(2));
        assertNull(p1.getResourceTypeFromShelf(3));
        assertEquals(p1.getNumberOfResInShelf(2),0);
        assertEquals(p1.getNumberOfResInShelf(3),0);

        String result1 = fileReader1.readLine();
        String result2 = fileReader2.readLine();
        assertEquals(result1, result2);

        Game game = gson.fromJson(result1, Game.class);
        Collection<Player> playersCollection = game.getPlayers();
        Player playerModel1 = playersCollection.stream().filter(x -> x.getNickName().equals(c1.getNickname())).findFirst().get();
        List<DepotShelf> shelves =  playerModel1.getDepotShelves();
        assertNull(shelves.get(0).getResourceType());
        assertNull(shelves.get(1).getResourceType());
        assertNull(shelves.get(2).getResourceType());
        HashMap<ResourceType, Integer> strongboxMap = playerModel1.getStrongBox();
        assertEquals(strongboxMap.get(ResourceType.COIN),0);
        assertEquals(strongboxMap.get(ResourceType.STONE),1);
        assertEquals(strongboxMap.get(ResourceType.SHIELD),2);
        assertEquals(strongboxMap.get(ResourceType.SERVANT),0);
    }
}
