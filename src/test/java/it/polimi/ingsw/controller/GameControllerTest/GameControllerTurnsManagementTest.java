package it.polimi.ingsw.controller.GameControllerTest;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.typeadapters.RuntimeTypeAdapterFactory;
import it.polimi.ingsw.controller.ClientHandler;
import it.polimi.ingsw.controller.GameController;
import it.polimi.ingsw.controller.enums.GameState;
import it.polimi.ingsw.controller.enums.PlayerState;
import it.polimi.ingsw.exceptions.IllegalActionException;
import it.polimi.ingsw.model.LeaderCard.LeaderCardRequirements.CardRequirementColor;
import it.polimi.ingsw.model.LeaderCard.LeaderCardRequirements.CardRequirementColorAndLevel;
import it.polimi.ingsw.model.LeaderCard.LeaderCardRequirements.CardRequirementResource;
import it.polimi.ingsw.model.LeaderCard.LeaderCardRequirements.Requirement;
import it.polimi.ingsw.model.LeaderCard.leaderEffects.*;
import it.polimi.ingsw.network.messages.sendToClient.ModelUpdate;
import it.polimi.ingsw.network.messages.sendToClient.ResponseMessage;
import it.polimi.ingsw.network.messages.sendToClient.ResponseType;
import it.polimi.ingsw.view.readOnlyModel.Game;
import it.polimi.ingsw.view.readOnlyModel.Player;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.*;
import java.net.Socket;

import static org.junit.jupiter.api.Assertions.assertEquals;

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

        gameController.setState(GameState.INSESSION);
    }

    @Test
    //All the players are still connected to the game: the player who's finishing their turn is the third in the list
    public void ctrlAllPlayersPresent() {
        c1.setPlayerState(PlayerState.WAITING4TURN);
        c2.setPlayerState(PlayerState.WAITING4TURN);
        c3.setPlayerState(PlayerState.PLAYING);
        c4.setPlayerState(PlayerState.WAITING4TURN);

        gameController.specifyNextPlayer(c3);

        assertEquals(c3.getPlayerState(), PlayerState.WAITING4BEGINNINGDECISIONS);
        assertEquals(c4.getPlayerState(), PlayerState.PLAYINGBEGINNINGDECISIONS);
        assertEquals(c1.getPlayerState(), PlayerState.WAITING4BEGINNINGDECISIONS);
        assertEquals(c2.getPlayerState(), PlayerState.WAITING4BEGINNINGDECISIONS);
    }

    @Test
    //All the players are still connected to the game: the player who's finishing their turn is the last in the list
    public void ctrlAllPlayersPresentTakingARoundTrip() {
        c1.setPlayerState(PlayerState.WAITING4TURN);
        c2.setPlayerState(PlayerState.WAITING4TURN);
        c3.setPlayerState(PlayerState.WAITING4TURN);
        c4.setPlayerState(PlayerState.PLAYING);

        gameController.specifyNextPlayer(c4);

        assertEquals(c4.getPlayerState(), PlayerState.WAITING4BEGINNINGDECISIONS);
        assertEquals(c1.getPlayerState(), PlayerState.PLAYINGBEGINNINGDECISIONS);
        assertEquals(c2.getPlayerState(), PlayerState.WAITING4BEGINNINGDECISIONS);
        assertEquals(c3.getPlayerState(), PlayerState.WAITING4BEGINNINGDECISIONS);
    }

    @Test
    public void ctrlOnePlayerDisconnected() {
        c1.setPlayerState(PlayerState.WAITING4TURN);
        c2.setPlayerState(PlayerState.PLAYING);
        c3.setPlayerState(PlayerState.DISCONNECTED);
        c4.setPlayerState(PlayerState.WAITING4TURN);

        gameController.specifyNextPlayer(c2);

        assertEquals(c4.getPlayerState(), PlayerState.PLAYINGBEGINNINGDECISIONS);
        assertEquals(c1.getPlayerState(), PlayerState.WAITING4BEGINNINGDECISIONS);
        assertEquals(c2.getPlayerState(), PlayerState.WAITING4BEGINNINGDECISIONS);
        assertEquals(c3.getPlayerState(), PlayerState.DISCONNECTED);
    }

    @Test
    public void ctrlTwoPlayerDisconnected() {
        c1.setPlayerState(PlayerState.WAITING4TURN);
        c2.setPlayerState(PlayerState.PLAYING);
        c3.setPlayerState(PlayerState.DISCONNECTED);
        c4.setPlayerState(PlayerState.DISCONNECTED);

        gameController.specifyNextPlayer(c2);

        assertEquals(PlayerState.PLAYINGBEGINNINGDECISIONS, c1.getPlayerState());
        assertEquals(PlayerState.WAITING4BEGINNINGDECISIONS, c2.getPlayerState());
        assertEquals(PlayerState.DISCONNECTED, c3.getPlayerState());
        assertEquals(PlayerState.DISCONNECTED, c4.getPlayerState());
    }

    @Test
    public void ctrlThreePlayerDisconnected() {
        c1.setPlayerState(PlayerState.DISCONNECTED);
        c2.setPlayerState(PlayerState.PLAYING);
        c3.setPlayerState(PlayerState.DISCONNECTED);
        c4.setPlayerState(PlayerState.DISCONNECTED);

        gameController.specifyNextPlayer(c2);

        assertEquals(c2.getPlayerState(), PlayerState.PLAYINGBEGINNINGDECISIONS);
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

    @Test
    public void ctrlUpdateMessageNoDisconnection() throws IOException {
        c1.setPlayerState(PlayerState.WAITING4TURN);
        c1.setBeginningActionDone(true);
        c2.setPlayerState(PlayerState.WAITING4TURN);
        c2.setBeginningActionDone(true);
        c3.setPlayerState(PlayerState.PLAYING);
        c3.setBeginningActionDone(true);
        c4.setPlayerState(PlayerState.WAITING4TURN);
        c4.setBeginningActionDone(true);

        gameController.specifyNextPlayer(c3);

        String res1 = fileReader1.readLine();
        String res2 = fileReader2.readLine();
        String res3 = fileReader3.readLine();
        String res4 = fileReader4.readLine();

        /*assertEquals(res1, res2);
        assertEquals(res2, res3);
        assertEquals(res3, res4);*/

        ResponseMessage responseMessage = gson.fromJson(res1, ResponseMessage.class);
        assertEquals(responseMessage.getResponseType(), ResponseType.UPDATE);
        ModelUpdate update = gson.fromJson(responseMessage.getResponseContent(), ModelUpdate.class);
        Game game = update.getGame();

        Player player;

        assertEquals(game.getPlayers().size(), 4);

        player = game.getPlayers().stream().filter(x -> x.getNickName().equals("Client 1")).findFirst().get();
        assertEquals(player.getPlayerState(), PlayerState.WAITING4TURN);

        player = game.getPlayers().stream().filter(x -> x.getNickName().equals("Client 2")).findFirst().get();
        assertEquals(player.getPlayerState(), PlayerState.WAITING4TURN);

        player = game.getPlayers().stream().filter(x -> x.getNickName().equals("Client 3")).findFirst().get();
        assertEquals(player.getPlayerState(), PlayerState.WAITING4TURN);

        player = game.getPlayers().stream().filter(x -> x.getNickName().equals("Client 4")).findFirst().get();
        assertEquals(player.getPlayerState(), PlayerState.PLAYING);
    }

    @Test
    public void ctrlUpdateMessageOneDisconnection() throws IOException {
        c1.setPlayerState(PlayerState.WAITING4TURN);
        c1.setBeginningActionDone(true);
        c2.setPlayerState(PlayerState.PLAYING);
        c2.setBeginningActionDone(true);
        c3.setPlayerState(PlayerState.DISCONNECTED);
        c3.setBeginningActionDone(true);
        c4.setPlayerState(PlayerState.WAITING4TURN);
        c4.setBeginningActionDone(true);

        gameController.specifyNextPlayer(c2);

        String res1 = fileReader1.readLine();
        String res2 = fileReader2.readLine();
        String res4 = fileReader4.readLine();

        //We don't read what the third client receives because they are disconnected!
        /*assertEquals(res1, res2);
        assertEquals(res2, res4);*/

        ResponseMessage responseMessage = gson.fromJson(res1, ResponseMessage.class);
        assertEquals(responseMessage.getResponseType(), ResponseType.UPDATE);
        ModelUpdate update = gson.fromJson(responseMessage.getResponseContent(), ModelUpdate.class);
        Game game = update.getGame();

        Player player;

        assertEquals(game.getPlayers().size(), 4);

        player = game.getPlayers().stream().filter(x -> x.getNickName().equals("Client 1")).findFirst().get();
        assertEquals(player.getPlayerState(), PlayerState.WAITING4TURN);

        player = game.getPlayers().stream().filter(x -> x.getNickName().equals("Client 2")).findFirst().get();
        assertEquals(player.getPlayerState(), PlayerState.WAITING4TURN);

        player = game.getPlayers().stream().filter(x -> x.getNickName().equals("Client 3")).findFirst().get();
        assertEquals(player.getPlayerState(), PlayerState.DISCONNECTED);

        player = game.getPlayers().stream().filter(x -> x.getNickName().equals("Client 4")).findFirst().get();
        assertEquals(player.getPlayerState(), PlayerState.PLAYING);
    }
}
