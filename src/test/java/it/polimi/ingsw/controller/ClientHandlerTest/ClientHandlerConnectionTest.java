package it.polimi.ingsw.controller.ClientHandlerTest;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.typeadapters.RuntimeTypeAdapterFactory;
import it.polimi.ingsw.controller.ClientHandler;
import it.polimi.ingsw.controller.Command;
import it.polimi.ingsw.controller.GameController;
import it.polimi.ingsw.controller.enums.PlayerState;
import it.polimi.ingsw.exceptions.IllegalActionException;
import it.polimi.ingsw.model.LeaderCard.LeaderCardRequirements.CardRequirementColor;
import it.polimi.ingsw.model.LeaderCard.LeaderCardRequirements.CardRequirementColorAndLevel;
import it.polimi.ingsw.model.LeaderCard.LeaderCardRequirements.CardRequirementResource;
import it.polimi.ingsw.model.LeaderCard.LeaderCardRequirements.Requirement;
import it.polimi.ingsw.model.LeaderCard.leaderEffects.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.*;
import java.net.Socket;
import java.net.SocketException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class ClientHandlerConnectionTest {

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
    PrintWriter fileWriter1;
    PrintWriter fileWriter2;
    PrintWriter fileWriter3;
    PrintWriter fileWriter4;

    @BeforeEach
    public void setup() throws IOException, IllegalActionException {
        gameController = new GameController();

        //The ClientHandlers read from the Client files and write to the ClientHandler files
        c1 = new ClientHandler(new Socket(), new BufferedReader(new FileReader("Client1File.json")), new PrintWriter("ClientHandler1File.json"));
        c2 = new ClientHandler(new Socket(), new BufferedReader(new FileReader("Client2File.json")), new PrintWriter("ClientHandler2File.json"));
        c3 = new ClientHandler(new Socket(), new BufferedReader(new FileReader("Client3File.json")), new PrintWriter("ClientHandler3File.json"));
        c4 = new ClientHandler(new Socket(), new BufferedReader(new FileReader("Client4File.json")), new PrintWriter("ClientHandler4File.json"));
        reader = new BufferedReader(new InputStreamReader(System.in));
        c1.setNickname("Client 1");
        c2.setNickname("Client 2");
        c3.setNickname("Client 3");
        c4.setNickname("Client 4");
        fileReader1 = new BufferedReader(new FileReader("ClientHandler1File.json"));
        fileReader2 = new BufferedReader(new FileReader("ClientHandler2File.json"));
        fileReader3 = new BufferedReader(new FileReader("ClientHandler3File.json"));
        fileReader4 = new BufferedReader(new FileReader("ClientHandler4File.json"));
        fileWriter1 = new PrintWriter("Client1File.json");
        fileWriter2 = new PrintWriter("Client2File.json");
        fileWriter3 = new PrintWriter("Client3File.json");
        fileWriter4 = new PrintWriter("Client4File.json");

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

        this.gson = new GsonBuilder().registerTypeAdapterFactory((requirementTypeFactory))
                .registerTypeAdapterFactory(effectTypeFactory).create();
    }

    /*@Test
    public void waitYourTurn() throws IOException {

        //Writes the json that the client handler will read
        fileWriter1.write("{\"cmd\": \"getResourcesFromMarket\",\"parameter\":\"\"}");
        fileWriter1.write("\n{\"cmd\": \"quit\",\"parameter\":\"\"}");
        fileWriter1.close();

        assertEquals(c1.getPlayerState(), PlayerState.WAITING4NAME);
        new Thread(c1).start();
        System.out.println(fileReader1.readLine());
    }*/

    /*@Test
    public void ctrlReadingAndWriting() throws IOException {
        fileWriter1.write("this is a test");
        fileWriter1.close();

        System.out.println(c1.getInput());
    }*/

    /*@Test
    public void ctrlRunMethod(){
        fileWriter1.write(gson.toJson(new Command("login", null)));
        fileWriter1.close();
        new Thread(c1).start(); //Se decommenti la System.out.println() che c'è nel run prima del while allora vedi che legge i comandi
    }*/


}
