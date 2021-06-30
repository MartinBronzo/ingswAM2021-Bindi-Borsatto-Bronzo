package it.polimi.ingsw.view.gui;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.typeadapters.RuntimeTypeAdapterFactory;
import it.polimi.ingsw.controller.Command;
import it.polimi.ingsw.model.leaderCard.LeaderCardRequirements.CardRequirementColor;
import it.polimi.ingsw.model.leaderCard.LeaderCardRequirements.CardRequirementColorAndLevel;
import it.polimi.ingsw.model.leaderCard.LeaderCardRequirements.CardRequirementResource;
import it.polimi.ingsw.model.leaderCard.LeaderCardRequirements.Requirement;
import it.polimi.ingsw.model.leaderCard.leaderEffects.*;
import it.polimi.ingsw.model.soloGame.DiscardToken;
import it.polimi.ingsw.model.soloGame.FaithPointToken;
import it.polimi.ingsw.model.soloGame.ShuffleToken;
import it.polimi.ingsw.model.soloGame.SoloActionToken;
import it.polimi.ingsw.network.messages.sendToClient.*;
import it.polimi.ingsw.view.Client;
import it.polimi.ingsw.view.gui.mainViews.PanelManager;

import javax.swing.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicBoolean;


/**
 * This class is responsible for the connection with the server, reads and sends messages to the server.
 * it manages the response for non gui related messages
 */
public class GuiClient implements Runnable, Client {
    private final int portNumber;
    private final String hostName;
    private Socket socket;
    private PrintWriter out;
    private BufferedReader in;
    private final Gson gson;
    private final AtomicBoolean forceLogout;
    private Thread threadReader;
    private ExecutorService writers;
    private ExecutorService readers;

    /**
     * Builds a new Gui Client knowing port and ip server number
     * @param portNumber the socket port of the server
     * @param hostName the server ip
     */
    public GuiClient(int portNumber, String hostName) {
        this.portNumber = portNumber;
        this.hostName = hostName;
        this.forceLogout = new AtomicBoolean();
        this.forceLogout.set(false);
        this.writers = Executors.newCachedThreadPool();
        this.readers = Executors.newSingleThreadExecutor();

        PanelManager.createInstance(this);

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
        effectTypeFactory.registerSubtype(ExtraProductionLeaderEffect.class, "extraProductionLeaderEffect");
        effectTypeFactory.registerSubtype(ExtraSlotLeaderEffect.class, "extraSlotLeaderEffect");
        effectTypeFactory.registerSubtype(WhiteMarbleLeaderEffect.class, "whiteMarbleLeaderEffect");

        RuntimeTypeAdapterFactory<SoloActionToken> tokenTypeFactory
                = RuntimeTypeAdapterFactory.of(SoloActionToken.class, "type");
        tokenTypeFactory.registerSubtype(SoloActionToken.class, "soloActionToken"); //TODO: this is only for testing purpose, in the real game we won't have token of type SoloActionToken but a subtype of it
        tokenTypeFactory.registerSubtype(DiscardToken.class, "discardToken");
        tokenTypeFactory.registerSubtype(FaithPointToken.class, "faithPointToken");
        tokenTypeFactory.registerSubtype(ShuffleToken.class, "shuffleToken");


        this.gson = new GsonBuilder().registerTypeAdapterFactory((requirementTypeFactory))
                .registerTypeAdapterFactory(effectTypeFactory).registerTypeAdapterFactory(tokenTypeFactory).create();
    }

    /**
     * USED ONLY FOR TEST PURPOSE
     */
    @Deprecated
    public GuiClient() {
        portNumber = 9047;
        hostName = "127.0.0.1";
        gson = new Gson();
        forceLogout = new AtomicBoolean();
    }


    /**
     * Connects with the server in listening creating a new socket
     * if the server is not available, it makes the program ends
     */
    public void startConnection() {
        try {
            socket = new Socket(hostName, portNumber);
            out = new PrintWriter(socket.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        } catch (UnknownHostException e) {
            System.err.println("Don't know about host " + hostName);
            System.exit(1);
        } catch (IOException e) {
            System.err.println("Couldn't get I/O for the connection to " + hostName);
            System.exit(1);
        }
    }


    /**
     * creates a new thread to read from server, instantiate the panel Manager to manage the gui
     */
    public void doConnection() {
        try {
            threadReader = new Thread(this);
            SwingUtilities.invokeLater(() -> {
                try {
                    PanelManager.getInstance().init();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
            threadReader.start();
            threadReader.join();
        } catch (InterruptedException e) {
            System.out.println("Connection closed!");
        } finally {
            endConnection();
        }

    }


    /**
     * Ends the connection with the server
     * Stops all the threads
     */
    public void endConnection() {
            writers.shutdown();
            readers.shutdown();
        try {
            socket.close();
        } catch (IOException e) {
            System.err.println("Couldn't get I/O");
            System.exit(1);
        }
    }


    /**
     * Sends a new command to the server. Each message is sent sequentially so it's impossible that concurrent calls to this method sends messages contemporary
     * @param command the command to be send to the client
     * @throws NullPointerException if command is null or the buffered reader responsible to send messages to the server is not yet specified
     */
    public void sendMessage(Command command) throws NullPointerException {
        if (out == null) throw new NullPointerException("PrintWriter is null");
        if (command == null) throw new NullPointerException("Command is null");
        writers.submit(() -> {
            out.println(gson.toJson(command));
            System.out.println("Sending:\t" + gson.toJson(command));
        });
        return;
    }

    /**
     * Prints the logout message in the gui
     * Set up everything to end the connection and the program
     */
    public void quitCommand(){
        threadReader.interrupt();
        PanelManager.getInstance().printLogout("Thanks for playing");
        forceLogout.set(true);
    }



    /**
     * When an object implementing interface {@code Runnable} is used
     * to create a thread, starting the thread causes the object's
     * {@code run} method to be called in that separately executing
     * thread.
     * <p>
     * It's responsible for reading from the server, managing the response for kickedOut and ping messages
     * for each of the others messages generates a new thread which it's responsible of managing the gui
     */

    public void run() {
        String response = null;
        ResponseMessage responseMessage = null;
        while (!Thread.currentThread().isInterrupted()){
            try {
                response = in.readLine();
                if (response == null)
                    throw new IOException();
                responseMessage = gson.fromJson(response, ResponseMessage.class);
            } catch (IOException e) {
                PanelManager.getInstance().printLogout("the server is offline. Please try restart the game.");
                forceLogout.set(true);
                threadReader.interrupt();
            } catch (com.google.gson.JsonSyntaxException gsone){
                PanelManager.getInstance().printLogout("serverError formatting communication");
                forceLogout.set(true);
                threadReader.interrupt();
            } finally {
                /*//TODO: DA TOGLIERE QUESTO IF
                if (responseMessage.getResponseType() != ResponseType.PING)*/
                    System.out.println("Received:\t" + response);
                switch (responseMessage.getResponseType()){
                    case PING:
                        sendMessage(new Command("pingResponse"));
                        break;
                    case KICKEDOUT:
                        PanelManager.getInstance().printLogout("kicked out");
                        forceLogout.set(true);
                        threadReader.interrupt();
                        break;
                    default:
                        ResponseMessage finalResponseMessage = responseMessage;
                        this.readers.submit(() -> {
                            PanelManager.getInstance().readMessage(finalResponseMessage);
                        });
                        break;
                }
            }
        }
    }


    public static void main(String[] args) throws IOException {
        String hostName = "127.0.0.1";
        int portNumber = 9047;
        GuiClient client = new GuiClient(portNumber, hostName);
        client.startConnection();
        client.doConnection();
    }
}
