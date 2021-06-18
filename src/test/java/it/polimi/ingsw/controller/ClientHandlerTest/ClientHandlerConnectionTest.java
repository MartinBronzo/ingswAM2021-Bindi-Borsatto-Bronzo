package it.polimi.ingsw.controller.ClientHandlerTest;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.typeadapters.RuntimeTypeAdapterFactory;
import it.polimi.ingsw.controller.ClientHandler;
import it.polimi.ingsw.controller.Command;
import it.polimi.ingsw.controller.GameController;
import it.polimi.ingsw.controller.GamesManagerSingleton;
import it.polimi.ingsw.controller.enums.PlayerState;
import it.polimi.ingsw.exceptions.IllegalActionException;
import it.polimi.ingsw.exceptions.NotAvailableNicknameException;
import it.polimi.ingsw.model.LeaderCard.LeaderCardRequirements.CardRequirementColor;
import it.polimi.ingsw.model.LeaderCard.LeaderCardRequirements.CardRequirementColorAndLevel;
import it.polimi.ingsw.model.LeaderCard.LeaderCardRequirements.CardRequirementResource;
import it.polimi.ingsw.model.LeaderCard.LeaderCardRequirements.Requirement;
import it.polimi.ingsw.model.LeaderCard.leaderEffects.*;
import it.polimi.ingsw.model.ResourceType;
import it.polimi.ingsw.model.soloGame.DiscardToken;
import it.polimi.ingsw.model.soloGame.FaithPointToken;
import it.polimi.ingsw.model.soloGame.ShuffleToken;
import it.polimi.ingsw.model.soloGame.SoloActionToken;
import it.polimi.ingsw.network.messages.fromClient.*;
import it.polimi.ingsw.network.messages.sendToClient.HashMapResFromDevGridMessage;
import it.polimi.ingsw.network.messages.sendToClient.HashMapResFromMarketMessage;
import it.polimi.ingsw.network.messages.sendToClient.LorenzosActionMessage;
import it.polimi.ingsw.network.messages.sendToClient.ResponseInterface;
import it.polimi.ingsw.view.readOnlyModel.Player;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.*;
import java.lang.reflect.Array;
import java.net.Socket;
import java.rmi.UnexpectedException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;

import static org.junit.jupiter.api.Assertions.*;

public class ClientHandlerConnectionTest {

    private static GamesManagerSingleton gamesManagerSingleton;
    GameController gameController;
    ClientHandler c1;
    ClientHandler c2;
    ClientHandler c3;
    ClientHandler c4;
    Reader inputStreamReader;
    //BufferedReader reader;
    File file;
    Gson gson;
    Scanner clientFileReader;
    Scanner clientFileReader2;
    Scanner clientFileReader3;
    Scanner clientFileReader4;

    PipedWriter writer;
    PipedReader pipedReader;
    PrintWriter printWriter;
    BufferedReader bufferedReader;
    PipedWriter writer2;
    PipedReader pipedReader2;
    PrintWriter printWriter2;
    PipedWriter writer3;
    PipedReader pipedReader3;
    PrintWriter printWriter3;
    PipedWriter writer4;
    PipedReader pipedReader4;
    PrintWriter printWriter4;

    /*public static void cleanFiles(){
        try {
            final PrintWriter pw1 = new PrintWriter(new FileWriter("ClientHandler1File.txt"));
            pw1.write("{}");
            pw1.flush();
            pw1.close();
            final PrintWriter pw2 = new PrintWriter(new FileWriter("ClientHandler2File.txt"));
            pw2.write("{}");
            pw2.flush();
            pw2.close();
            final PrintWriter pw3 = new PrintWriter(new FileWriter("ClientHandler3File.txt"));
            pw3.write("{}");
            pw3.flush();
            pw3.close();
            final PrintWriter pw4 = new PrintWriter(new FileWriter("ClientHandler4File.txt"));
            pw4.write("{}");
            pw4.flush();
            pw4.close();
            final PrintWriter pw5 = new PrintWriter(new FileWriter("Client1File.json"));
            pw5.write("{}");
            pw5.flush();
            pw5.close();
            final PrintWriter pw6 = new PrintWriter(new FileWriter("Client2File.json"));
            pw6.write("{}");
            pw6.flush();
            pw6.close();
            final PrintWriter pw7 = new PrintWriter(new FileWriter("Client3File.json"));
            pw7.write("{}");
            pw7.flush();
            pw7.close();
            final PrintWriter pw8 = new PrintWriter(new FileWriter("Client4File.json"));
            pw8.write("{}");
            pw8.flush();
            pw8.close();
        }catch (IOException e){
            e.printStackTrace();
        }
    }*/

    @BeforeAll
    static void setUpAll() {
        gamesManagerSingleton = GamesManagerSingleton.getInstance();
    }

    @BeforeEach
    public void setup() throws IOException, IllegalActionException {
        gamesManagerSingleton.resetSingleton();
        gameController = new GameController();

        writer = new PipedWriter();
        pipedReader = new PipedReader(writer);
        printWriter = new PrintWriter(writer);

        writer2 = new PipedWriter();
        pipedReader2 = new PipedReader(writer2);
        printWriter2 = new PrintWriter(writer2);

        writer3 = new PipedWriter();
        pipedReader3 = new PipedReader(writer3);
        printWriter3 = new PrintWriter(writer3);

        writer4 = new PipedWriter();
        pipedReader4 = new PipedReader(writer4);
        printWriter4 = new PrintWriter(writer4);

        clientFileReader = new Scanner(new File("ClientHandler1File.txt"));
        clientFileReader2 = new Scanner(new File("ClientHandler2File.txt"));
        clientFileReader3 = new Scanner(new File("ClientHandler3File.txt"));
        clientFileReader4 = new Scanner(new File("ClientHandler4File.txt"));

        //The ClientHandlers read from the Client files and write to the ClientHandler files
        c1 = new ClientHandler(new Socket(), new BufferedReader(pipedReader), new PrintWriter("ClientHandler1File.txt"), false);
        c2 = new ClientHandler(new Socket(), new BufferedReader(pipedReader2), new PrintWriter("ClientHandler2File.txt"), false);
        c3 = new ClientHandler(new Socket(), new BufferedReader(pipedReader3), new PrintWriter("ClientHandler3File.txt"), false);
        c4 = new ClientHandler(new Socket(), new BufferedReader(pipedReader4), new PrintWriter("ClientHandler4File.txt"), false);
        //reader = new BufferedReader(new InputStreamReader(System.in));

        RuntimeTypeAdapterFactory<Requirement> requirementTypeFactory
                = RuntimeTypeAdapterFactory.of(Requirement.class, "type");
        requirementTypeFactory.registerSubtype(Requirement.class, "requirement"); //TODO: this is only for testing purpose, in the real game we won't have requirements of type Requirement but a subtype of it
        requirementTypeFactory.registerSubtype(CardRequirementColor.class, "cardRequirementColor");
        requirementTypeFactory.registerSubtype(CardRequirementResource.class, "cardRequirementResource");
        requirementTypeFactory.registerSubtype(CardRequirementColorAndLevel.class, "cardRequirementColorAndLevel");

        RuntimeTypeAdapterFactory<Effect> effectTypeFactory
                = RuntimeTypeAdapterFactory.of(Effect.class, "type");
        effectTypeFactory.registerSubtype(Effect.class, "effect"); //TODO: this is only for testing purpose, in the real game we won't have effect of type Effect but a subtype of it
        effectTypeFactory.registerSubtype(DiscountLeaderEffect.class, "discountLeaderEffect");
        effectTypeFactory.registerSubtype(ExtraSlotLeaderEffect.class, "extraSlotLeaderEffect");
        effectTypeFactory.registerSubtype(WhiteMarbleLeaderEffect.class, "whiteMarbleLeaderEffect");
        effectTypeFactory.registerSubtype(ExtraProductionLeaderEffect.class, "extraProductionLeaderEffect");

        RuntimeTypeAdapterFactory<SoloActionToken> tokenTypeFactory
                = RuntimeTypeAdapterFactory.of(SoloActionToken.class, "type");
        tokenTypeFactory.registerSubtype(SoloActionToken.class, "soloActionToken"); //TODO: this is only for testing purpose, in the real game we won't have token of type SoloActionToken but a subtype of it
        tokenTypeFactory.registerSubtype(DiscardToken.class, "discardToken");
        tokenTypeFactory.registerSubtype(FaithPointToken.class, "faithPointToken");

        this.gson = new GsonBuilder().registerTypeAdapterFactory((requirementTypeFactory))
                .registerTypeAdapterFactory(effectTypeFactory).registerTypeAdapterFactory(tokenTypeFactory).create();
    }

    @Test
    public void setnickname() throws InterruptedException, UnexpectedException, IllegalActionException, NotAvailableNicknameException {
        c1.executeCommand(new Command("login", new LoginMessage("1")));
        assertEquals(clientFileReader.nextLine(), "{\"responseType\":\"SETNICK\",\"responseContent\":\"{\\\"confirmedNickname\\\":\\\"1\\\"}\"}");
        assertEquals(clientFileReader.nextLine(), "{\"responseType\":\"ASKFORNUMPLAYERS\",\"responseContent\":\"{\\\"message\\\":\\\"You are creating a game! Tell me how many players you want in this game!\\\"}\"}");

        c1.executeCommand(new Command("setNumPlayer", new SetNumPlayerMessage(4)));
        assertEquals(clientFileReader.nextLine(), "{\"responseType\":\"SETNUMPLAYERCONF\",\"responseContent\":\"{\\\"confirmedNumPlayers\\\":4}\"}");
        assertEquals(clientFileReader.nextLine(), "{\"responseType\":\"INFOSTRING\",\"responseContent\":\"{\\\"message\\\":\\\"You are in Game! You\\\\u0027ll soon start play with others!\\\"}\"}");

        c2.executeCommand(new Command("login", new LoginMessage("2")));
        assertEquals(clientFileReader2.nextLine(), "{\"responseType\":\"SETNICK\",\"responseContent\":\"{\\\"confirmedNickname\\\":\\\"2\\\"}\"}");
        assertEquals(clientFileReader2.nextLine(), "{\"responseType\":\"INFOSTRING\",\"responseContent\":\"{\\\"message\\\":\\\"You are in Game! You\\\\u0027ll soon start play with others!\\\"}\"}");

        c3.executeCommand(new Command("login", new LoginMessage("3")));
        assertEquals(clientFileReader3.nextLine(), "{\"responseType\":\"SETNICK\",\"responseContent\":\"{\\\"confirmedNickname\\\":\\\"3\\\"}\"}");
        assertEquals(clientFileReader3.nextLine(), "{\"responseType\":\"INFOSTRING\",\"responseContent\":\"{\\\"message\\\":\\\"You are in Game! You\\\\u0027ll soon start play with others!\\\"}\"}");

        c4.executeCommand(new Command("login", new LoginMessage("4")));
        assertEquals(clientFileReader4.nextLine(), "{\"responseType\":\"SETNICK\",\"responseContent\":\"{\\\"confirmedNickname\\\":\\\"4\\\"}\"}");
        assertEquals(clientFileReader4.nextLine(), "{\"responseType\":\"INFOSTRING\",\"responseContent\":\"{\\\"message\\\":\\\"You are in Game! You\\\\u0027ll soon start play with others!\\\"}\"}");

        assertEquals(clientFileReader.nextLine(), "{\"responseType\":\"INFOSTRING\",\"responseContent\":\"{\\\"message\\\":\\\"GAME STARTED!\\\"}\"}");
    }

    @Test
    public void setnicknameError() throws InterruptedException, UnexpectedException, IllegalActionException, NotAvailableNicknameException {
        c1.executeCommand(new Command("login", new LoginMessage("1")));
        assertEquals(clientFileReader.nextLine(), "{\"responseType\":\"SETNICK\",\"responseContent\":\"{\\\"confirmedNickname\\\":\\\"1\\\"}\"}");
        assertEquals(clientFileReader.nextLine(), "{\"responseType\":\"ASKFORNUMPLAYERS\",\"responseContent\":\"{\\\"message\\\":\\\"You are creating a game! Tell me how many players you want in this game!\\\"}\"}");

        Exception exception = assertThrows(NotAvailableNicknameException.class, () -> c2.executeCommand(new Command("login", new LoginMessage("1"))));
        assertEquals("Nick is the same of the actual configurator", exception.getMessage());
    }

    @Test
    public void setnicknameError2() throws InterruptedException, UnexpectedException, IllegalActionException, NotAvailableNicknameException {
        c1.executeCommand(new Command("login", new LoginMessage("1")));
        assertEquals(clientFileReader.nextLine(), "{\"responseType\":\"SETNICK\",\"responseContent\":\"{\\\"confirmedNickname\\\":\\\"1\\\"}\"}");
        assertEquals(clientFileReader.nextLine(), "{\"responseType\":\"ASKFORNUMPLAYERS\",\"responseContent\":\"{\\\"message\\\":\\\"You are creating a game! Tell me how many players you want in this game!\\\"}\"}");

        c1.executeCommand(new Command("setNumPlayer", new SetNumPlayerMessage(4)));
        assertEquals(clientFileReader.nextLine(), "{\"responseType\":\"SETNUMPLAYERCONF\",\"responseContent\":\"{\\\"confirmedNumPlayers\\\":4}\"}");
        assertEquals(clientFileReader.nextLine(), "{\"responseType\":\"INFOSTRING\",\"responseContent\":\"{\\\"message\\\":\\\"You are in Game! You\\\\u0027ll soon start play with others!\\\"}\"}");

        Exception exception = assertThrows(NotAvailableNicknameException.class, () -> c2.executeCommand(new Command("login", new LoginMessage("1"))));
        assertEquals("Nick is taken", exception.getMessage());

        c3.executeCommand(new Command("login", new LoginMessage("3")));
        assertEquals(clientFileReader3.nextLine(), "{\"responseType\":\"SETNICK\",\"responseContent\":\"{\\\"confirmedNickname\\\":\\\"3\\\"}\"}");
        assertEquals(clientFileReader3.nextLine(), "{\"responseType\":\"INFOSTRING\",\"responseContent\":\"{\\\"message\\\":\\\"You are in Game! You\\\\u0027ll soon start play with others!\\\"}\"}");

        c4.executeCommand(new Command("login", new LoginMessage("4")));
        assertEquals(clientFileReader4.nextLine(), "{\"responseType\":\"SETNICK\",\"responseContent\":\"{\\\"confirmedNickname\\\":\\\"4\\\"}\"}");
        assertEquals(clientFileReader4.nextLine(), "{\"responseType\":\"INFOSTRING\",\"responseContent\":\"{\\\"message\\\":\\\"You are in Game! You\\\\u0027ll soon start play with others!\\\"}\"}");
    }

    @Test
    public void setnicknameErrorState() throws InterruptedException, UnexpectedException, IllegalActionException, NotAvailableNicknameException {
        //c1.setPlayerState(PlayerState.PLAYING);

        c1.executeCommand(new Command("login", new LoginMessage("1")));
        assertEquals(clientFileReader.nextLine(), "{\"responseType\":\"SETNICK\",\"responseContent\":\"{\\\"confirmedNickname\\\":\\\"1\\\"}\"}");
        assertEquals(clientFileReader.nextLine(), "{\"responseType\":\"ASKFORNUMPLAYERS\",\"responseContent\":\"{\\\"message\\\":\\\"You are creating a game! Tell me how many players you want in this game!\\\"}\"}");

        c1.executeCommand(new Command("setNumPlayer", new SetNumPlayerMessage(4)));
        assertEquals(clientFileReader.nextLine(), "{\"responseType\":\"SETNUMPLAYERCONF\",\"responseContent\":\"{\\\"confirmedNumPlayers\\\":4}\"}");
        assertEquals(clientFileReader.nextLine(), "{\"responseType\":\"INFOSTRING\",\"responseContent\":\"{\\\"message\\\":\\\"You are in Game! You\\\\u0027ll soon start play with others!\\\"}\"}");

        c1.executeCommand(new Command("login", new LoginMessage("5")));
        assertEquals(clientFileReader.nextLine(), "{\"responseType\":\"ERROR\",\"responseContent\":\"{\\\"errorMessage\\\":\\\"You can\\\\u0027t do this action now\\\"}\"}");
    }

    @Test
    public void commandTest(){
        Command command = new Command("Cmd");
        assertEquals("cmd", command.getCmd());
        assertEquals("", command.getParameters());
    }

    @Test
    public void setnicknameErrorState2() throws InterruptedException, UnexpectedException, IllegalActionException, NotAvailableNicknameException {
        //c1.setPlayerState(PlayerState.PLAYING);

        c1.executeCommand(new Command("login", new LoginMessage("1")));
        assertEquals(clientFileReader.nextLine(), "{\"responseType\":\"SETNICK\",\"responseContent\":\"{\\\"confirmedNickname\\\":\\\"1\\\"}\"}");
        assertEquals(clientFileReader.nextLine(), "{\"responseType\":\"ASKFORNUMPLAYERS\",\"responseContent\":\"{\\\"message\\\":\\\"You are creating a game! Tell me how many players you want in this game!\\\"}\"}");

        c1.executeCommand(new Command("setNumPlayer", new SetNumPlayerMessage(4)));
        assertEquals(clientFileReader.nextLine(), "{\"responseType\":\"SETNUMPLAYERCONF\",\"responseContent\":\"{\\\"confirmedNumPlayers\\\":4}\"}");
        assertEquals(clientFileReader.nextLine(), "{\"responseType\":\"INFOSTRING\",\"responseContent\":\"{\\\"message\\\":\\\"You are in Game! You\\\\u0027ll soon start play with others!\\\"}\"}");

        c1.executeCommand(new Command("setNumPlayer", new SetNumPlayerMessage(2)));
        assertEquals(clientFileReader.nextLine(), "{\"responseType\":\"ERROR\",\"responseContent\":\"{\\\"errorMessage\\\":\\\"You can\\\\u0027t do this action now\\\"}\"}");
    }

    @Test
    public void configureStart() throws InterruptedException, UnexpectedException, IllegalActionException, NotAvailableNicknameException {
        c1.executeCommand(new Command("login", new LoginMessage("1")));
        assertEquals(clientFileReader.nextLine(), "{\"responseType\":\"SETNICK\",\"responseContent\":\"{\\\"confirmedNickname\\\":\\\"1\\\"}\"}");
        assertEquals(clientFileReader.nextLine(), "{\"responseType\":\"ASKFORNUMPLAYERS\",\"responseContent\":\"{\\\"message\\\":\\\"You are creating a game! Tell me how many players you want in this game!\\\"}\"}");

        c1.executeCommand(new Command("setNumPlayer", new SetNumPlayerMessage(2)));
        assertEquals(clientFileReader.nextLine(), "{\"responseType\":\"SETNUMPLAYERCONF\",\"responseContent\":\"{\\\"confirmedNumPlayers\\\":2}\"}");
        assertEquals(clientFileReader.nextLine(), "{\"responseType\":\"INFOSTRING\",\"responseContent\":\"{\\\"message\\\":\\\"You are in Game! You\\\\u0027ll soon start play with others!\\\"}\"}");

        c2.executeCommand(new Command("login", new LoginMessage("2")));
        assertEquals(clientFileReader2.nextLine(), "{\"responseType\":\"SETNICK\",\"responseContent\":\"{\\\"confirmedNickname\\\":\\\"2\\\"}\"}");
        assertEquals(clientFileReader2.nextLine(), "{\"responseType\":\"INFOSTRING\",\"responseContent\":\"{\\\"message\\\":\\\"You are in Game! You\\\\u0027ll soon start play with others!\\\"}\"}");

        assertTrue((PlayerState.PLAYINGBEGINNINGDECISIONS == c1.getPlayerState() && PlayerState.WAITING4BEGINNINGDECISIONS == c2.getPlayerState())
                || (PlayerState.PLAYINGBEGINNINGDECISIONS == c2.getPlayerState() && PlayerState.WAITING4BEGINNINGDECISIONS == c1.getPlayerState()));


        List<Integer> indexList = new ArrayList<>();
        indexList.add(0);
        indexList.add(1);
        if (c1.getPlayerState() == PlayerState.PLAYINGBEGINNINGDECISIONS)
            c1.executeCommand(new Command("discardLeaderAndExtraResBeginning", new DiscardLeaderAndExtraResBeginningMessage(indexList, new ArrayList<>())));
        else
            c2.executeCommand(new Command("discardLeaderAndExtraResBeginning", new DiscardLeaderAndExtraResBeginningMessage(indexList, new ArrayList<>())));

        assertTrue((PlayerState.WAITING4TURN == c1.getPlayerState() && PlayerState.PLAYINGBEGINNINGDECISIONS == c2.getPlayerState())
                || (PlayerState.WAITING4TURN == c2.getPlayerState() && PlayerState.PLAYINGBEGINNINGDECISIONS == c1.getPlayerState()));
    }

    @Test
    public void soloGame() throws InterruptedException, UnexpectedException, IllegalActionException, NotAvailableNicknameException {
        c1.executeCommand(new Command("login", new LoginMessage("1")));
        assertEquals(clientFileReader.nextLine(), "{\"responseType\":\"SETNICK\",\"responseContent\":\"{\\\"confirmedNickname\\\":\\\"1\\\"}\"}");
        assertEquals(clientFileReader.nextLine(), "{\"responseType\":\"ASKFORNUMPLAYERS\",\"responseContent\":\"{\\\"message\\\":\\\"You are creating a game! Tell me how many players you want in this game!\\\"}\"}");

        c1.executeCommand(new Command("setNumPlayer", new SetNumPlayerMessage(1)));
        assertEquals(clientFileReader.nextLine(), "{\"responseType\":\"SETNUMPLAYERCONF\",\"responseContent\":\"{\\\"confirmedNumPlayers\\\":1}\"}");
//        assertEquals(clientFileReader.nextLine(), "{\"responseType\":\"INFOSTRING\",\"responseContent\":\"{\\\"message\\\":\\\"You are in Game! You\\\\u0027ll soon start play with others!\\\"}\"}");

        List<Integer> indexList = new ArrayList<>();
        indexList.add(0);
        indexList.add(1);
        c1.executeCommand(new Command("discardLeaderAndExtraResBeginning", new DiscardLeaderAndExtraResBeginningMessage(indexList, new ArrayList<>())));
        assertEquals(PlayerState.PLAYING, c1.getPlayerState());

        c1.executeCommand(new Command("endTurn", new Message()));
        assertEquals(PlayerState.PLAYING, c1.getPlayerState());
    }

    @Test
    public void getResourcesFromMarket() throws InterruptedException, UnexpectedException, IllegalActionException, NotAvailableNicknameException {
        c1.executeCommand(new Command("login", new LoginMessage("1")));
        assertEquals(clientFileReader.nextLine(), "{\"responseType\":\"SETNICK\",\"responseContent\":\"{\\\"confirmedNickname\\\":\\\"1\\\"}\"}");
        assertEquals(clientFileReader.nextLine(), "{\"responseType\":\"ASKFORNUMPLAYERS\",\"responseContent\":\"{\\\"message\\\":\\\"You are creating a game! Tell me how many players you want in this game!\\\"}\"}");

        c1.executeCommand(new Command("setNumPlayer", new SetNumPlayerMessage(1)));
        assertEquals(clientFileReader.nextLine(), "{\"responseType\":\"SETNUMPLAYERCONF\",\"responseContent\":\"{\\\"confirmedNumPlayers\\\":1}\"}");
//        assertEquals(clientFileReader.nextLine(), "{\"responseType\":\"INFOSTRING\",\"responseContent\":\"{\\\"message\\\":\\\"You are in Game! You\\\\u0027ll soon start play with others!\\\"}\"}");
        assertEquals(clientFileReader.nextLine(), "{\"responseType\":\"INFOSTRING\",\"responseContent\":\"{\\\"message\\\":\\\"GAME STARTED!\\\"}\"}");

        //reads update messages
        clientFileReader.nextLine();

        assertEquals(clientFileReader.nextLine(), "{\"responseType\":\"EXTRARESOURCEANDLEADERCARDBEGINNING\",\"responseContent\":\"{\\\"numRes\\\":0,\\\"numLeader\\\":2,\\\"order\\\":0}\"}");
        assertEquals(clientFileReader.nextLine(), "{\"responseType\":\"UPDATE\",\"responseContent\":\"{\\\"game\\\":{\\\"players\\\":[{\\\"nickName\\\":\\\"1\\\",\\\"playerState\\\":\\\"PLAYINGBEGINNINGDECISIONS\\\"}],\\\"mainBoard\\\":{},\\\"lorenzosPosition\\\":0}}\"}");

        List<Integer> indexList = new ArrayList<>();
        indexList.add(0);
        indexList.add(1);
        c1.executeCommand(new Command("discardLeaderAndExtraResBeginning", new DiscardLeaderAndExtraResBeginningMessage(indexList, new ArrayList<>())));
        assertEquals(PlayerState.PLAYING, c1.getPlayerState());

        clientFileReader.nextLine();
        assertEquals(clientFileReader.nextLine(), "{\"responseType\":\"SETBEGINNINGDECISIONS\",\"responseContent\":\"{}\"}");
        assertEquals(clientFileReader.nextLine(), "{\"responseType\":\"UPDATE\",\"responseContent\":\"{\\\"game\\\":{\\\"players\\\":[{\\\"nickName\\\":\\\"1\\\",\\\"playerState\\\":\\\"PLAYING\\\"}],\\\"mainBoard\\\":{},\\\"lorenzosPosition\\\":0}}\"}");

        c1.executeCommand(new Command("getResourcesFromMarket", new GetFromMatrixMessage(1, 0, new ArrayList<>())));
        //controllare se le risorse sono corrette
        System.out.println("Resources on the first row (upper row): " + clientFileReader.nextLine() + "\n");
        System.out.println(c1.getGame().getMainBoard().getMarket().toString());
    }

    @Test
    public void getDevCardCost() throws InterruptedException, UnexpectedException, IllegalActionException, NotAvailableNicknameException {
        c1.executeCommand(new Command("login", new LoginMessage("1")));
        assertEquals(clientFileReader.nextLine(), "{\"responseType\":\"SETNICK\",\"responseContent\":\"{\\\"confirmedNickname\\\":\\\"1\\\"}\"}");
        assertEquals(clientFileReader.nextLine(), "{\"responseType\":\"ASKFORNUMPLAYERS\",\"responseContent\":\"{\\\"message\\\":\\\"You are creating a game! Tell me how many players you want in this game!\\\"}\"}");

        c1.executeCommand(new Command("setNumPlayer", new SetNumPlayerMessage(1)));
        assertEquals(clientFileReader.nextLine(), "{\"responseType\":\"SETNUMPLAYERCONF\",\"responseContent\":\"{\\\"confirmedNumPlayers\\\":1}\"}");
//        assertEquals(clientFileReader.nextLine(), "{\"responseType\":\"INFOSTRING\",\"responseContent\":\"{\\\"message\\\":\\\"You are in Game! You\\\\u0027ll soon start play with others!\\\"}\"}");
        assertEquals(clientFileReader.nextLine(), "{\"responseType\":\"INFOSTRING\",\"responseContent\":\"{\\\"message\\\":\\\"GAME STARTED!\\\"}\"}");

        //reads update messages
        clientFileReader.nextLine();

        assertEquals(clientFileReader.nextLine(), "{\"responseType\":\"EXTRARESOURCEANDLEADERCARDBEGINNING\",\"responseContent\":\"{\\\"numRes\\\":0,\\\"numLeader\\\":2,\\\"order\\\":0}\"}");
        assertEquals(clientFileReader.nextLine(), "{\"responseType\":\"UPDATE\",\"responseContent\":\"{\\\"game\\\":{\\\"players\\\":[{\\\"nickName\\\":\\\"1\\\",\\\"playerState\\\":\\\"PLAYINGBEGINNINGDECISIONS\\\"}],\\\"mainBoard\\\":{},\\\"lorenzosPosition\\\":0}}\"}");

        List<Integer> indexList = new ArrayList<>();
        indexList.add(0);
        indexList.add(1);
        c1.executeCommand(new Command("discardLeaderAndExtraResBeginning", new DiscardLeaderAndExtraResBeginningMessage(indexList, new ArrayList<>())));
        assertEquals(PlayerState.PLAYING, c1.getPlayerState());

        clientFileReader.nextLine();
        assertEquals(clientFileReader.nextLine(), "{\"responseType\":\"SETBEGINNINGDECISIONS\",\"responseContent\":\"{}\"}");
        assertEquals(clientFileReader.nextLine(), "{\"responseType\":\"UPDATE\",\"responseContent\":\"{\\\"game\\\":{\\\"players\\\":[{\\\"nickName\\\":\\\"1\\\",\\\"playerState\\\":\\\"PLAYING\\\"}],\\\"mainBoard\\\":{},\\\"lorenzosPosition\\\":0}}\"}");

        c1.executeCommand(new Command("getCardCost", new GetFromMatrixMessage(1, 1, new ArrayList<>())));

        System.out.println(clientFileReader.nextLine());
        System.out.println(c1.getGame().getMainBoard().getDevCardFromDeckInDevGrid(0, 0).getCost());
    }

    @Test
    public void buyFromMarket() throws InterruptedException, UnexpectedException, IllegalActionException, NotAvailableNicknameException {
        c1.executeCommand(new Command("login", new LoginMessage("1")));
        assertEquals(clientFileReader.nextLine(), "{\"responseType\":\"SETNICK\",\"responseContent\":\"{\\\"confirmedNickname\\\":\\\"1\\\"}\"}");
        assertEquals(clientFileReader.nextLine(), "{\"responseType\":\"ASKFORNUMPLAYERS\",\"responseContent\":\"{\\\"message\\\":\\\"You are creating a game! Tell me how many players you want in this game!\\\"}\"}");

        c1.executeCommand(new Command("setNumPlayer", new SetNumPlayerMessage(1)));
        assertEquals(clientFileReader.nextLine(), "{\"responseType\":\"SETNUMPLAYERCONF\",\"responseContent\":\"{\\\"confirmedNumPlayers\\\":1}\"}");
//        assertEquals(clientFileReader.nextLine(), "{\"responseType\":\"INFOSTRING\",\"responseContent\":\"{\\\"message\\\":\\\"You are in Game! You\\\\u0027ll soon start play with others!\\\"}\"}");
        assertEquals(clientFileReader.nextLine(), "{\"responseType\":\"INFOSTRING\",\"responseContent\":\"{\\\"message\\\":\\\"GAME STARTED!\\\"}\"}");

        //reads update messages
        clientFileReader.nextLine();

        assertEquals(clientFileReader.nextLine(), "{\"responseType\":\"EXTRARESOURCEANDLEADERCARDBEGINNING\",\"responseContent\":\"{\\\"numRes\\\":0,\\\"numLeader\\\":2,\\\"order\\\":0}\"}");
        assertEquals(clientFileReader.nextLine(), "{\"responseType\":\"UPDATE\",\"responseContent\":\"{\\\"game\\\":{\\\"players\\\":[{\\\"nickName\\\":\\\"1\\\",\\\"playerState\\\":\\\"PLAYINGBEGINNINGDECISIONS\\\"}],\\\"mainBoard\\\":{},\\\"lorenzosPosition\\\":0}}\"}");

        List<Integer> indexList = new ArrayList<>();
        indexList.add(0);
        indexList.add(1);
        c1.executeCommand(new Command("discardLeaderAndExtraResBeginning", new DiscardLeaderAndExtraResBeginningMessage(indexList, new ArrayList<>())));
        assertEquals(PlayerState.PLAYING, c1.getPlayerState());

        clientFileReader.nextLine();
        assertEquals(clientFileReader.nextLine(), "{\"responseType\":\"SETBEGINNINGDECISIONS\",\"responseContent\":\"{}\"}");
        assertEquals(clientFileReader.nextLine(), "{\"responseType\":\"UPDATE\",\"responseContent\":\"{\\\"game\\\":{\\\"players\\\":[{\\\"nickName\\\":\\\"1\\\",\\\"playerState\\\":\\\"PLAYING\\\"}],\\\"mainBoard\\\":{},\\\"lorenzosPosition\\\":0}}\"}");

        c1.executeCommand(new Command("getResourcesFromMarket", new GetFromMatrixMessage(1, 0, new ArrayList<>())));
        //controllare se le risorse sono corrette
        System.out.println("Resources on the first row (upper row): " + clientFileReader.nextLine() + "\n");
        System.out.println(c1.getGame().getMainBoard().getMarket().toString());
    }

    @Test
    public void endTurn() throws InterruptedException, UnexpectedException, NotAvailableNicknameException, IllegalActionException {
        c1.executeCommand(new Command("login", new LoginMessage("1")));
        assertEquals(clientFileReader.nextLine(), "{\"responseType\":\"SETNICK\",\"responseContent\":\"{\\\"confirmedNickname\\\":\\\"1\\\"}\"}");
        assertEquals(clientFileReader.nextLine(), "{\"responseType\":\"ASKFORNUMPLAYERS\",\"responseContent\":\"{\\\"message\\\":\\\"You are creating a game! Tell me how many players you want in this game!\\\"}\"}");

        c1.executeCommand(new Command("setNumPlayer", new SetNumPlayerMessage(1)));
        assertEquals(clientFileReader.nextLine(), "{\"responseType\":\"SETNUMPLAYERCONF\",\"responseContent\":\"{\\\"confirmedNumPlayers\\\":1}\"}");
        //assertEquals(clientFileReader.nextLine(), "{\"responseType\":\"INFOSTRING\",\"responseContent\":\"{\\\"message\\\":\\\"You are in Game! You\\\\u0027ll soon start play with others!\\\"}\"}");
        assertEquals(clientFileReader.nextLine(), "{\"responseType\":\"INFOSTRING\",\"responseContent\":\"{\\\"message\\\":\\\"GAME STARTED!\\\"}\"}");

        //reads update messages
        clientFileReader.nextLine();

        assertEquals(clientFileReader.nextLine(), "{\"responseType\":\"EXTRARESOURCEANDLEADERCARDBEGINNING\",\"responseContent\":\"{\\\"numRes\\\":0,\\\"numLeader\\\":2,\\\"order\\\":0}\"}");
        assertEquals(clientFileReader.nextLine(), "{\"responseType\":\"UPDATE\",\"responseContent\":\"{\\\"game\\\":{\\\"players\\\":[{\\\"nickName\\\":\\\"1\\\",\\\"playerState\\\":\\\"PLAYINGBEGINNINGDECISIONS\\\"}],\\\"mainBoard\\\":{},\\\"lorenzosPosition\\\":0}}\"}");

        List<Integer> indexList = new ArrayList<>();
        indexList.add(0);
        indexList.add(1);
        c1.executeCommand(new Command("discardLeaderAndExtraResBeginning", new DiscardLeaderAndExtraResBeginningMessage(indexList, new ArrayList<>())));
        assertEquals(PlayerState.PLAYING, c1.getPlayerState());

        clientFileReader.nextLine();
        assertEquals(clientFileReader.nextLine(), "{\"responseType\":\"SETBEGINNINGDECISIONS\",\"responseContent\":\"{}\"}");
        assertEquals(clientFileReader.nextLine(), "{\"responseType\":\"UPDATE\",\"responseContent\":\"{\\\"game\\\":{\\\"players\\\":[{\\\"nickName\\\":\\\"1\\\",\\\"playerState\\\":\\\"PLAYING\\\"}],\\\"mainBoard\\\":{},\\\"lorenzosPosition\\\":0}}\"}");


        try {
            c1.executeCommand(new Command("endTurn", new Message()));
            c1.executeCommand(new Command("endTurn", new Message()));
            c1.executeCommand(new Command("endTurn", new Message()));
            c1.executeCommand(new Command("endTurn", new Message()));
            c1.executeCommand(new Command("endTurn", new Message()));
            c1.executeCommand(new Command("endTurn", new Message()));
        }catch (IllegalActionException e){
            e.printStackTrace();
        }
    }


}
