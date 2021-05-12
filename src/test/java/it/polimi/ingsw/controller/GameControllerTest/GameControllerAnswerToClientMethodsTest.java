package it.polimi.ingsw.controller.GameControllerTest;


import com.google.gson.Gson;
import it.polimi.ingsw.client.readOnlyModel.Game;
import it.polimi.ingsw.client.readOnlyModel.Player;
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
import it.polimi.ingsw.model.LeaderCard.leaderEffects.DiscountLeaderEffect;
import it.polimi.ingsw.model.LeaderCard.leaderEffects.Effect;
import it.polimi.ingsw.model.LeaderCard.leaderEffects.ExtraSlotLeaderEffect;
import it.polimi.ingsw.model.LeaderCard.leaderEffects.WhiteMarbleLeaderEffect;
import it.polimi.ingsw.model.MainBoard;
import it.polimi.ingsw.model.PlayerBoard;
import it.polimi.ingsw.model.ResourceType;
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
        LeaderCard l1 = new LeaderCard(4, new ArrayList<>() /*reqList*/ , new Effect() /*new ExtraSlotLeaderEffect(ResourceType.COIN, 2)*/);
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


    //TODO: other tests for base production and leader cards
}
