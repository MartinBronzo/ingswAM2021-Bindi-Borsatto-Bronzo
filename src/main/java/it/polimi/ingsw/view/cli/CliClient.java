package it.polimi.ingsw.view.cli;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.typeadapters.RuntimeTypeAdapterFactory;
import it.polimi.ingsw.controller.Command;
import it.polimi.ingsw.controller.enums.PlayerState;
import it.polimi.ingsw.model.leaderCard.LeaderCardRequirements.CardRequirementColor;
import it.polimi.ingsw.model.leaderCard.LeaderCardRequirements.CardRequirementColorAndLevel;
import it.polimi.ingsw.model.leaderCard.LeaderCardRequirements.CardRequirementResource;
import it.polimi.ingsw.model.leaderCard.LeaderCardRequirements.Requirement;
import it.polimi.ingsw.model.leaderCard.leaderEffects.*;
import it.polimi.ingsw.model.resources.ResourceType;
import it.polimi.ingsw.model.soloGame.DiscardToken;
import it.polimi.ingsw.model.soloGame.FaithPointToken;
import it.polimi.ingsw.model.soloGame.ShuffleToken;
import it.polimi.ingsw.model.soloGame.SoloActionToken;
import it.polimi.ingsw.network.messages.fromClient.CheatMessage;
import it.polimi.ingsw.network.messages.fromClient.LoginMessage;
import it.polimi.ingsw.network.messages.sendToClient.*;
import it.polimi.ingsw.view.Client;
import it.polimi.ingsw.view.lightModel.Game;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * This class constructs the Command Line Interface for the player. It manages the connection with the Server and shows to
 * the player useful information.
 */
public class CliClient implements Runnable, Client {
    private final int portNumber;
    private final String hostName;
    private Game gamemodel;
    private Socket socket;
    private PrintWriter out;
    private BufferedReader in;
    private static BufferedReader stdIn;
    private final Gson gson;
    private static Thread thread;
    private Map<ResourceType, Integer> resourcesMap;
    private String mapDescription;
    private int nLeadersToDiscard;
    private int resourcesToTake;
    private String nickname;
    private final AtomicBoolean forceLogout;
    private String logoutMessage = "Thanks for Playing, See you next time :D";
    private String cliCommandContent;
    private ExecutorService writers;


    /**
     * Constructs a CliClient object which is going to communicate via the specified connection
     * @param portNumber the port used for the communication
     * @param hostName the IP address of the server
     */
    public CliClient(int portNumber, String hostName) {
        this.gamemodel = null;
        this.portNumber = portNumber;
        this.hostName = hostName;
        this.forceLogout = new AtomicBoolean();
        this.forceLogout.set(false);
        stdIn = new BufferedReader(new InputStreamReader(System.in));
        writers = Executors.newCachedThreadPool();

        RuntimeTypeAdapterFactory<Requirement> requirementTypeFactory
                = RuntimeTypeAdapterFactory.of(Requirement.class, "type");
        requirementTypeFactory.registerSubtype(Requirement.class, "requirement"); // this is only for testing purpose, in the real game we won't have requirements of type Requirement but a subtype of it
        requirementTypeFactory.registerSubtype(CardRequirementColor.class, "cardRequirementColor");
        requirementTypeFactory.registerSubtype(CardRequirementResource.class, "cardRequirementResource");
        requirementTypeFactory.registerSubtype(CardRequirementColorAndLevel.class, "cardRequirementColorAndLevel");

        RuntimeTypeAdapterFactory<Effect> effectTypeFactory
                = RuntimeTypeAdapterFactory.of(Effect.class, "type");
        effectTypeFactory.registerSubtype(Effect.class, "effect"); // this is only for testing purpose, in the real game we won't have effect of type Effect but a subtype of it
        effectTypeFactory.registerSubtype(DiscountLeaderEffect.class, "discountLeaderEffect");
        effectTypeFactory.registerSubtype(ExtraProductionLeaderEffect.class, "extraProductionLeaderEffect");
        effectTypeFactory.registerSubtype(ExtraSlotLeaderEffect.class, "extraSlotLeaderEffect");
        effectTypeFactory.registerSubtype(WhiteMarbleLeaderEffect.class, "whiteMarbleLeaderEffect");

        RuntimeTypeAdapterFactory<SoloActionToken> tokenTypeFactory
                = RuntimeTypeAdapterFactory.of(SoloActionToken.class, "type");
        tokenTypeFactory.registerSubtype(SoloActionToken.class, "soloActionToken"); // this is only for testing purpose, in the real game we won't have token of type SoloActionToken but a subtype of it
        tokenTypeFactory.registerSubtype(DiscardToken.class, "discardToken");
        tokenTypeFactory.registerSubtype(FaithPointToken.class, "faithPointToken");
        tokenTypeFactory.registerSubtype(ShuffleToken.class, "shuffleToken");


        this.gson = new GsonBuilder().registerTypeAdapterFactory((requirementTypeFactory))
                .registerTypeAdapterFactory(effectTypeFactory).registerTypeAdapterFactory(tokenTypeFactory).create();
    }

    /*
    public CliClient(int portNumber, String hostName, BufferedReader bufferedReader) {
        this.gamemodel = null;
        this.portNumber = portNumber;
        this.hostName = hostName;
        this.forceLogout = new AtomicBoolean();
        this.forceLogout.set(false);
        this.stdIn = bufferedReader;

        RuntimeTypeAdapterFactory<Requirement> requirementTypeFactory
                = RuntimeTypeAdapterFactory.of(Requirement.class, "type");
        requirementTypeFactory.registerSubtype(Requirement.class, "requirement"); // this is only for testing purpose, in the real game we won't have requirements of type Requirement but a subtype of it
        requirementTypeFactory.registerSubtype(CardRequirementColor.class, "cardRequirementColor");
        requirementTypeFactory.registerSubtype(CardRequirementResource.class, "cardRequirementResource");
        requirementTypeFactory.registerSubtype(CardRequirementColorAndLevel.class, "cardRequirementColorAndLevel");

        RuntimeTypeAdapterFactory<Effect> effectTypeFactory
                = RuntimeTypeAdapterFactory.of(Effect.class, "type");
        effectTypeFactory.registerSubtype(Effect.class, "effect"); // this is only for testing purpose, in the real game we won't have effect of type Effect but a subtype of it
        effectTypeFactory.registerSubtype(DiscountLeaderEffect.class, "discountLeaderEffect");
        effectTypeFactory.registerSubtype(ExtraProductionLeaderEffect.class, "extraProductionLeaderEffect");
        effectTypeFactory.registerSubtype(ExtraSlotLeaderEffect.class, "extraSlotLeaderEffect");
        effectTypeFactory.registerSubtype(WhiteMarbleLeaderEffect.class, "whiteMarbleLeaderEffect");

        RuntimeTypeAdapterFactory<SoloActionToken> tokenTypeFactory
                = RuntimeTypeAdapterFactory.of(SoloActionToken.class, "type");
        tokenTypeFactory.registerSubtype(SoloActionToken.class, "soloActionToken"); // this is only for testing purpose, in the real game we won't have token of type SoloActionToken but a subtype of it
        tokenTypeFactory.registerSubtype(DiscardToken.class, "discardToken");
        tokenTypeFactory.registerSubtype(FaithPointToken.class, "faithPointToken");
        tokenTypeFactory.registerSubtype(ShuffleToken.class, "shuffleToken");


        this.gson = new GsonBuilder().registerTypeAdapterFactory((requirementTypeFactory))
                .registerTypeAdapterFactory(effectTypeFactory).registerTypeAdapterFactory(tokenTypeFactory).create();
    }
    */

    @Override
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
        CliView.printWelcome();
        thread = new Thread(this);
        thread.start();
    }

    @Override
    public void doConnection() {
        CliCommandType cliCommandType = CliCommandType.SETNICKNAME;
        String line = "";
        do {
            try {
                line = stdIn.readLine().toUpperCase();
            } catch (IOException e) {
                if (forceLogout.get())
                    break;
            }
            try {
                if (forceLogout.get())
                    break;
                String[] strings = line.split(":");
                cliCommandType = CliCommandType.valueOf(strings[0].strip());
                if (strings.length>1)
                    cliCommandContent = strings[1].strip();
                switch (cliCommandType) {
                    case QUIT:
                        synchronized (this) {
                            thread.interrupt();
                            sendMessage(new Command("quit"));
                        }
                        break;
                    case SETNICKNAME:
                        this.manageLogin();
                        break;
                    case SETNUMOFPLAYERS:
                        this.setNumPlayer();
                        break;
                    case CONFIGURESTART:
                        this.manageGameStarting();
                        break;
                    case GETRESOURCESFROMMARKET:
                        this.getResourcesFromMarket();
                        break;
                    case BUYFROMMARKET:
                        this.buyFromMarket();
                        break;
                    case GETDEVCARDCOST:
                        this.getDevCardCost();
                        break;
                    case BUYDEVCARD:
                        this.buyDevCard();
                        break;
                    case GETPRODUCTIONCOST:
                        this.getProductionCost();
                        break;
                    case ACTIVATEPRODUCTION:
                        this.activateProduction();
                        break;
                    case DISCARDLEADER:
                        this.discardLeader();
                        break;
                    case MOVEBETWEENSHELF:
                        this.moveBetweenShelves();
                        break;
                    case MOVELEADERTOSHELF:
                        this.moveLeaderToShelf();
                        break;
                    case MOVESHELFTOLEADER:
                        this.moveShelfToLeader();
                        break;
                    case ACTIVATELEADER:
                        this.activateLeader();
                        break;
                    case ENDTURN:
                        synchronized (this) {
                            sendMessage(new Command("endTurn"));
                        }
                        break;
                    case SEEPLAYERBOARD:
                        synchronized (this) {
                            if(gamemodel != null) {
                                System.out.print("\n");
                                CliView.printOtherGameBoard(gamemodel, cliCommandContent);
                            }else
                                CliView.printInfo("It can't be printed yet");
                        }
                        break;
                    case PRINTMYBOARD:
                        synchronized (this) {
                            if (this.gamemodel != null)
                                CliView.printGameState(gamemodel, nickname);
                            else
                                CliView.printInfo("It can't be printed yet");
                        }
                        break;
                    case SEEOTHERSPLAYERSNAMES:
                        synchronized (this) {
                            if (this.gamemodel != null)
                                CliView.printOthersPlayersName(gamemodel, nickname);
                            else
                                CliView.printInfo("It can't be printed yet");
                        }
                        break;
                    case CHEAT:
                        sendMessage(new Command("activateCheat", new CheatMessage(99)));
                        break;
                    case HELP:
                    case HOLP:
                        synchronized (this) {
                            CliView.printHolpMessage();
                        }
                        break;
                    default:
                        System.err.println("Command not Valid\n");
                }
            } catch (IllegalArgumentException e) {
                System.err.println("your Command doesn't exists");
            }
        } while (!cliCommandType.equals(CliCommandType.QUIT) && !forceLogout.get());
        this.endConnection();
    }

    @Override
    public synchronized void endConnection() {
        CliView.printInfo(logoutMessage);
        try {
            socket.close();
        } catch (IOException e) {
            System.err.println("Couldn't get I/O");
            System.exit(1);
        }
        thread.interrupt();
        System.exit(0);
    }


    @Override
    public void sendMessage(Command command) throws NullPointerException {
        if (out == null) throw new NullPointerException("PrintWriter is null");
        if (command == null) throw new NullPointerException("Command is null");
        writers.submit(() -> {
            out.println(gson.toJson(command));
            //System.out.println("Sending:\t" + gson.toJson(command));
        });
        return;
    }

    /**
     * Asks the Server for a connection
     */
    public synchronized void manageLogin() {
        Command loginCommand = new Command("login", new LoginMessage(cliCommandContent));
        sendMessage(loginCommand);
    }

    /**
     * Sends the Server the player's beginning of the game decisions
     */
    public synchronized void manageGameStarting() {
        try {
            Command configureStartCommand = new Command("discardLeaderAndExtraResBeginning", StringToMessage.toDiscardLeaderAndExtraResBeginningMessage(cliCommandContent));
            sendMessage(configureStartCommand);
        } catch (IllegalArgumentException e) {
            System.err.println(e.getMessage());
        }
    }


    /**
     * Sends the Server the game creator's decisions for the number of players they want in the game
     */
    public synchronized void setNumPlayer() {
        try {
            Command setNumPlayerCommand = new Command("setNumPlayer", StringToMessage.toSetNumPlayerMessage(cliCommandContent));
            sendMessage(setNumPlayerCommand);
        } catch (IllegalArgumentException e) {
            System.err.println(e.getMessage());
        }
    }

    /**
     * Sends the Server the player's interest in buying from the Market
     */
    public synchronized void getResourcesFromMarket() {
        try {
            Command getResourcesFromMarket = new Command("getResourcesFromMarket", StringToMessage.toMatrixMessageLine(cliCommandContent));
            sendMessage(getResourcesFromMarket);
        } catch (IllegalArgumentException e) {
            System.err.println(e.getMessage());
        }
    }

    /**
     * Sends the Server the player's decisions taken in order to buy from the Market
     */
    public synchronized void buyFromMarket() {
        try {
            Command buyFromMarketCommand = new Command("buyFromMarket", StringToMessage.toBuyFromMarketMessage(cliCommandContent));
            sendMessage(buyFromMarketCommand);
        } catch (IllegalArgumentException e) {
            System.err.println(e.getMessage());
        }
    }

    /**
     * Sends the Server the player's interest in buying a DevCard from the DevGrid
     */
    public synchronized void getDevCardCost() {
        try {
            Command getDevCardCost = new Command("getCardCost", StringToMessage.toMatrixMessageCell(cliCommandContent));
            sendMessage(getDevCardCost);
        } catch (IllegalArgumentException e) {
            System.err.println(e.getMessage());
        }
    }

    /**
     * Sends the Server the player's decisions taken in order to buy a DevCard the DevGrid
     */
    public synchronized void buyDevCard() {
        try {
            Command buyDevCard = new Command("buyDevCard", StringToMessage.toBuyDevCardMessage(cliCommandContent));
            sendMessage(buyDevCard);
        } catch (IllegalArgumentException e) {
            System.err.println(e.getMessage());
        }
    }

    /**
     * Sends the Server the player's interest in activating their production powers
     */
    public synchronized void getProductionCost() {
        try {
            Command getProductionCost = new Command("getProductionCost", StringToMessage.toGetProductionCostMessage(cliCommandContent));
            sendMessage(getProductionCost);
        } catch (IllegalArgumentException e) {
            System.err.println(e.getMessage());
        }
    }

    /**
     * Sends the Server the player's decisions taken in order to activate their production methods
     */
    public synchronized void activateProduction() {
        try {
            Command activateProduction = new Command("activateProductionMessage", StringToMessage.toActivateProductionMessage(cliCommandContent));
            sendMessage(activateProduction);
        } catch (IllegalArgumentException e) {
            System.err.println(e.getMessage());
        }
    }

    /**
     * Sends the Server the player's decisions taken in order to move resources between Depot shelves
     */
    public synchronized void moveBetweenShelves() {
        try {
            Command moveBetweenShelves = new Command("moveBetweenShelves", StringToMessage.toMoveBetweenShelvesMessage(cliCommandContent));
            sendMessage(moveBetweenShelves);
        } catch (IllegalArgumentException e) {
            System.err.println(e.getMessage());
        }
    }

    /**
     * Sends the Server the player's decisions taken in order to move resources from ExtraSlot LeaderCards to a Depot shelf
     */
    public synchronized void moveLeaderToShelf() {
        try {
            Command moveLeaderToShelf = new Command("moveLeaderToShelf", StringToMessage.toMoveLeaderToShelfMessage(cliCommandContent));
            sendMessage(moveLeaderToShelf);
        } catch (IllegalArgumentException e) {
            System.err.println(e.getMessage());
        }
    }

    /**
     * Sends the Server the player's decisions taken in order to move resources from a Depot shelf to ExtraSlot LeaderCards
     */
    public synchronized void moveShelfToLeader() {
        try {
            Command moveShelfToLeader = new Command("moveShelfToLeader", StringToMessage.toMoveShelfToLeaderMessage(cliCommandContent));
            sendMessage(moveShelfToLeader);
        } catch (IllegalArgumentException e) {
            System.err.println(e.getMessage());
        }
    }

    /**
     * Sends the Server the player's decision to discard a LeaderCard during the game
     */
    public synchronized void discardLeader() {
        try {
            Command discardLeader = new Command("discardLeader", StringToMessage.toLeaderMessage(cliCommandContent));
            sendMessage(discardLeader);
        } catch (IllegalArgumentException e) {
            System.err.println(e.getMessage());
        }
    }

    /**
     * Sends the Server the player's decision to activate a LeaderCard during the game
     */
    public synchronized void activateLeader() {
        try {
            Command activateLeader = new Command("ActivateLeader", StringToMessage.toLeaderMessage(cliCommandContent));
            sendMessage(activateLeader);
        } catch (IllegalArgumentException e) {
            System.err.println(e.getMessage());
        }
    }


    @Override
    public void run() {
        String response = null;
        ResponseMessage responseMessage = null;
        String responseContent;
        while (!Thread.currentThread().isInterrupted()) {
            try {
                response = in.readLine();
                if (response == null)
                    throw new IOException();
                responseMessage = gson.fromJson(response, ResponseMessage.class);
            } catch (IOException e) {
                logoutMessage = "the server is offline. Please try restart the game.";
                synchronized (this) {
                    CliView.printInfo(logoutMessage);
                    System.exit(1);
                }
                /*
                forceLogout.set(true);
                // this has no effect on stdin.readline();
                try {
                    stdIn.close();
                } catch (IOException ioException) {
                    // this catch should do nothing
                }
                thread.interrupt();

                 */
            }catch (com.google.gson.JsonSyntaxException gsone){
                logoutMessage = "message received from server is not well format";
                CliView.printInfo(logoutMessage);
                System.exit(1);
                /*
                forceLogout.set(true);
                try {
                    stdIn.close();
                } catch (IOException ioException) {
                    // this catch should do nothing
                }
                thread.interrupt();

                 */
            }
            try {
                /*if (responseMessage.getResponseType() != ResponseType.PING)
                    System.out.println("Received:\t" + response);*/
                responseContent = responseMessage.getResponseContent();
                switch (responseMessage.getResponseType()) {
                    case PING:
                        sendMessage(new Command("pingResponse"));
                        break;
                    case UPDATE:
                        synchronized (this) {
                            ModelUpdate modelUpdate = gson.fromJson(responseContent, ModelUpdate.class);
                            Game update = modelUpdate.getGame();
                            if (this.gamemodel == null)
                                gamemodel = update;
                            else
                                gamemodel.merge(update);
                            CliView.printGameState(gamemodel, nickname);
                        }
                        break;
                    case ERROR:
                        synchronized (this) {
                            ErrorMessage errorMessage = gson.fromJson(responseContent, ErrorMessage.class);
                            String error = errorMessage.getErrorMessage();
                            CliView.printError(error);
                        }
                        break;
                    case EXTRARESOURCEANDLEADERCARDBEGINNING:
                        synchronized (this) {
                            ExtraResAndLeadToDiscardBeginningMessage message = gson.fromJson(responseContent, ExtraResAndLeadToDiscardBeginningMessage.class);
                            nLeadersToDiscard = message.getNumLeader();
                            resourcesToTake = message.getNumRes();
                            CliView.printSetUpView(nLeadersToDiscard, resourcesToTake);
                        }
                        break;
                    case HASHMAPRESOURCESFROMDEVGRID:
                        synchronized (this) {
                            HashMapResFromDevGridMessage message = gson.fromJson(responseContent, HashMapResFromDevGridMessage.class);
                            this.resourcesMap = message.getResources();
                            this.mapDescription = "Master " + nickname + ", this improvement shall cost thou:";
                            CliView.printResourcesMap(resourcesMap, mapDescription);
                        }
                        break;
                    case HASHMAPRESOURCESFROMMARKET:
                        synchronized (this) {
                            HashMapResFromMarketMessage message = gson.fromJson(responseContent, HashMapResFromMarketMessage.class);
                            this.resourcesMap = message.getResources();
                            this.mapDescription = "Master " + nickname + ", this morrow, local shopkeepers tenders thou:";
                            CliView.printResourcesMap(resourcesMap, mapDescription);
                        }
                        break;
                    case HASHMAPRESOURCESFROMPRODCOST:
                        synchronized (this) {
                            HashMapResFromProdCostMessage message = gson.fromJson(responseContent, HashMapResFromProdCostMessage.class);
                            this.resourcesMap = message.getResources();
                            this.mapDescription = "Master " + nickname + ", the craftsman needs these resources to produce what you ask for:";
                            CliView.printResourcesMap(resourcesMap, mapDescription);
                        }
                        break;
                    case INFOSTRING:
                        synchronized (this) {
                            GeneralInfoStringMessage infoMessage = gson.fromJson(responseContent, GeneralInfoStringMessage.class);
                            String info = infoMessage.getMessage();
                            CliView.printInfo(info);
                        }
                        break;
                    case ASKFORNUMPLAYERS:
                        synchronized (this) {
                            String info = "You are the game Creator, you must set the number of players (1-4)";
                            CliView.printInfo(info);
                        }
                        break;
                    case KICKEDOUT:
                        logoutMessage = "You Have been kicked out, please restart the game to connect to a new Game";
                        CliView.printInfo(logoutMessage);
                        System.exit(0);
                        /*
                        forceLogout.set(true);
                        thread.interrupt();

                         */
                        break;
                    case TURNINFO:
                        break;
                    case SETNICK:
                        synchronized (this) {
                            LoginConfirmationMessage setNickMessage = gson.fromJson(responseContent, LoginConfirmationMessage.class);
                            nickname = setNickMessage.getConfirmedNickname();
                            CliView.printInfo("From now on thou shall known as master " + nickname + ", thou shall serve under thy liege demands and any committed crime shall cause our Holy Lord disappointment.");
                            CliView.printGameState(gamemodel, nickname);
                        }
                        break;
                    case SETBEGINNINGDECISIONS:
                        synchronized (this) {
                            nLeadersToDiscard = 0;
                            resourcesToTake = 0;
                            CliView.printInfo("Master " + nickname + ", I thank thee for showing thy great example of service.");
                        }
                        break;

                    case LORENZOSACTION:
                        synchronized (this){
                            LorenzosActionMessage lorenzosActionMessage = gson.fromJson(responseContent, LorenzosActionMessage.class);
                            SoloActionToken token = lorenzosActionMessage.getSoloActionToken();
                            CliView.printLorenzosAction(token);
                        }
                        break;
                    case FINALSCORES:
                        synchronized (this){
                            FinalScoresMessage message = gson.fromJson(responseContent, FinalScoresMessage.class);
                            //Orders the list of players by their scores
                            List<Map.Entry<String, Integer>> results = new LinkedList<>(message.getResults().entrySet());
                            results.sort((x, y) -> y.getValue().compareTo(x.getValue()));
                            CliView.printFinalScores(results);
                            CliView.printInfo(logoutMessage);
                            System.exit(0);
                        }
                        break;
                   /* case SETBEGINNINGDECISIONS:
                        synchronized (this){
                            nLeadersToDiscard = 0;
                            resourcesToTake = 0;
                            CliView.printInfo("Master " + nickname + ", I thank thee for showing thy great example of service.");
                        }
                        break;
                    case FINALSCORES:
                        synchronized (this){
                            FinalScoresMessage message = gson.fromJson(responseContent, FinalScoresMessage.class);
                            CliView.printFinalScores(message.getResults());
                        }
                        break;*/
                    case SETNUMPLAYERCONF:
                        synchronized (this) {
                            SetNumPlayersConfirmationMessage setNickMessage = gson.fromJson(responseContent, SetNumPlayersConfirmationMessage.class);
                            int numPlayers = setNickMessage.getConfirmedNumPlayers();
                            if(numPlayers == 1)
                                CliView.printInfo("Thou archenemy is the Almighty Lorenzo The Magnificent! But Glory is for you to be taken!");
                            else
                                CliView.printInfo("My Master, what a quest for Glory lies in front of you: " + (numPlayers - 1) + " dreadful competitors will be ready to challenge you every step of the way!");
                        }
                        break;
                    case PLAYERCONNECTIONUPDATE:
                        synchronized (this) {
                            PlayerConnectionsUpdate modelUpdate = gson.fromJson(responseContent, PlayerConnectionsUpdate.class);
                            Game update = modelUpdate.getUpdate();
                            if (this.gamemodel == null)
                                gamemodel = update;
                            else
                                gamemodel.merge(update);
                            CliView.printDisconnectionUpdate(modelUpdate.getChangedPlayer(), isPlayerDisconnected(gamemodel, modelUpdate.getChangedPlayer()));
                        }
                        break;
                    case SOLOGAMERESULT:
                        synchronized (this){
                            SoloGameResultMessage message = gson.fromJson(responseContent, SoloGameResultMessage.class);
                            CliView.printSoloGameScores(message.isVictory(), message.getMessage());
                            CliView.printInfo(logoutMessage);
                            System.exit(0);
                        }

                        break;
                }
            } catch (NullPointerException e) {
                CliView.printError(e.getMessage());
            }
        }
    }

    private boolean isPlayerDisconnected(Game game, String player){
        return game.getPlayers().stream().filter(x -> x.getNickName().equals(player)).findAny().get().getPlayerState() == PlayerState.DISCONNECTED;
    }

    /**
     * Starts a CliClient
     * @param args arguments that can be used to activate the CliClient
     * @throws IOException if an IO operations fails
     */
    public static void main(String[] args) throws IOException {
        String hostName = "127.0.0.1";
        int portNumber = 9047;
        Client client = new CliClient(portNumber, hostName);
        client.startConnection();
        client.doConnection();
    }

    //used only for testing
    public String getNickname() {
        return nickname;
    }

    //used only for testing
    public Game getGameModel() {
        return gamemodel;
    }
}







    /*
    @Override
    protected synchronized void buyFromMarket() throws InterruptedException, IOException {
        Boolean isRow;
        int rowColumnNumber = 0;
        int nWhiteMarble = 0;
        List<Integer> leaderCardsId = new ArrayList<>();
        String userResponse = null;

        while (!userResponse.equals("Row") && !userResponse.equals("Column")){
            System.out.println("Row or Column?\n");
            userResponse = stdIn.readLine();
            if (!userResponse.equals("Row") && !userResponse.equals("Column"))
                System.out.println(userResponse + "is not a valid response\n");
        }
        isRow = "Row".equals(userResponse);

        userResponse=null;
        Boolean isResponseValid = false;
        while (!isResponseValid){
            System.out.println("Row/Column number?\n");
            try {
                userResponse = stdIn.readLine();
                rowColumnNumber = Integer.parseInt(userResponse);
                rowColumnNumber--;
                if (isRow)
                    nWhiteMarble = gamemodel.getMainBoard().getNumberOfWhiteMarbleInTheRow(rowColumnNumber);
                else
                    nWhiteMarble = gamemodel.getMainBoard().getNumberOfWhiteMarbleInTheColumn(rowColumnNumber);
                isResponseValid = true;
            } catch (NumberFormatException e){
                System.out.println(userResponse + "is not a number\n");
                isResponseValid = false;
            }
        }
        rowColumnNumber++;

        userResponse = null;
        int c = 1;
        while (nWhiteMarble>0){
            System.out.println("Insert the id of the leader card to be activated for the white marble number " + c
                               + "\n0 if you don't have any whiteMarble LeaderCard Activated\n");
            try {
                userResponse = stdIn.readLine();
                leaderCardsId.add(Integer.parseInt(userResponse));
                nWhiteMarble--;
            } catch (NumberFormatException e) {
                System.out.println(userResponse + "is not a number\n");
            }
        }

        resourcesMap = null;
        errorReceived = false;
        Command getResourcesCommand;
        if (isRow)
            getResourcesCommand = new Command("getResourcesFromMarket", new GetFromMatrixMessage(rowColumnNumber, 0, leaderCardsId));
        else
            getResourcesCommand = new Command("getResourcesFromMarket", new GetFromMatrixMessage(0, rowColumnNumber, leaderCardsId));
        sendMessage(getResourcesCommand);
        while (resourcesMap == null || !errorReceived)
            wait();
        if (errorReceived) {
            return;
        }
        System.out.println("your bought resources:\n" + resourcesMap.toString());
        System.out.println("put the resources in the desired position:\n" + "Commands: MOVETODEPOT, MOVETOLEADER, DISCARD, END");
        userResponse=null;
        List<DepotParams> depotParamsList = new ArrayList<>();
        HashMap<ResourceType, Integer> leaderMap = new HashMap<>();
        HashMap<ResourceType, Integer> discardsMap = new HashMap<>();
        ResourceType resourceType;
        int qt;
        int shelf;
        while (!userResponse.equals("END")){
            switch (userResponse){
                case "MOVETODEPOT":
                    try {
                        System.out.println("Insert ResourceType to be added to shelf\n");
                        userResponse = stdIn.readLine();
                        resourceType = ResourceType.valueOf(userResponse);

                        System.out.println("Insert quantity to be added to shelf\n");
                        userResponse = stdIn.readLine();
                        qt = Integer.parseInt(userResponse);

                        System.out.println("Insert shelfnumber\n");
                        userResponse = stdIn.readLine();
                        shelf = Integer.parseInt(userResponse);

                        depotParamsList.add(new DepotParams(resourceType, qt, shelf));
                    } catch (IllegalArgumentException e){
                        System.out.println("param not valid\n");
                    }
                    break;
                case "MOVETOLEADER":
                    try {
                        System.out.println("Insert ResourceType to be added to leaderSlot\n");
                        userResponse = stdIn.readLine();
                        resourceType = ResourceType.valueOf(userResponse);

                        System.out.println("Insert quantity to be added to leaderSlot (delete previous quantity on same leadercard\n");
                        userResponse = stdIn.readLine();
                        qt = Integer.parseInt(userResponse);
                        leaderMap.put(resourceType, qt);
                    } catch (IllegalArgumentException e){
                        System.out.println("param not valid\n");
                    }
                    break;
                case "DISCARD":
                    try {
                        System.out.println("Insert ResourceType to be discarded\n");
                        userResponse = stdIn.readLine();
                        resourceType = ResourceType.valueOf(userResponse);

                        System.out.println("Insert quantity to be discarded (delete previous quantity on same resorcetype discarded\n");
                        userResponse = stdIn.readLine();
                        qt = Integer.parseInt(userResponse);
                        discardsMap.put(resourceType, qt);
                    } catch (IllegalArgumentException e){
                        System.out.println("param not valid\n");
                    }
                    break;
                case "END":
                    break;
                default:
                    System.out.println("Not Valid line");
            }
        }
        Command buyFromMarket;
        if (isRow)
            buyFromMarket = new Command("buyFromMarket", new BuyFromMarketMessage(rowColumnNumber, 0, leaderCardsId, depotParamsList, leaderMap, discardsMap));
        else
            buyFromMarket = new Command("buyFromMarket", new BuyFromMarketMessage(0, rowColumnNumber, leaderCardsId, depotParamsList, leaderMap, discardsMap));
        sendMessage(buyFromMarket);
    }
     */