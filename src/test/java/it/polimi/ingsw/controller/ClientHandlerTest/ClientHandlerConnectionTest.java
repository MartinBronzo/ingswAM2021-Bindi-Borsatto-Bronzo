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
import org.junit.jupiter.api.BeforeAll;
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

    /*public static void cleanFiles(){
        try {
            final PrintWriter pw1 = new PrintWriter(new FileWriter("ClientHandler1File.json"));
            pw1.write("{}");
            pw1.flush();
            pw1.close();
            final PrintWriter pw2 = new PrintWriter(new FileWriter("ClientHandler2File.json"));
            pw2.write("{}");
            pw2.flush();
            pw2.close();
            final PrintWriter pw3 = new PrintWriter(new FileWriter("ClientHandler3File.json"));
            pw3.write("{}");
            pw3.flush();
            pw3.close();
            final PrintWriter pw4 = new PrintWriter(new FileWriter("ClientHandler4File.json"));
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



/*
    @Test
    public void waitYourTurn() throws IOException, InterruptedException {
        //Writes the json that the client handler will read
        fileWriter1.write("{\"cmd\":\"pingResponse\",\"parameters\":\"\"}");
        fileWriter1.write("\n{\"cmd\":\"quit\",\"parameters\":\"\"}");
        fileWriter1.close();

        assertEquals(c1.getPlayerState(), PlayerState.WAITING4NAME);
        new Thread(c1).start();
        Thread.sleep(2000);
        System.out.println(fileReader1.readLine());
        System.out.println(fileReader1.readLine());
    }

*/

    /*@Test
    public void ctrlReadingAndWriting() throws IOException {
        fileWriter1.write("this is a test");
        fileWriter1.close();

        System.out.println(c1.getInput());
    }

    @Test
    public void ctrlRunMethod(){
        fileWriter1.write(gson.toJson(new Command("login", null)));
        fileWriter1.close();
        new Thread(c1).start(); //Se decommenti la System.out.println() che c'è nel run prima del while allora vedi che legge i comandi
    }*/


}
