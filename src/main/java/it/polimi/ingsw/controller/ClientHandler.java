package it.polimi.ingsw.controller;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.typeadapters.RuntimeTypeAdapterFactory;
import it.polimi.ingsw.controller.enums.GameState;
import it.polimi.ingsw.controller.enums.PlayerState;
import it.polimi.ingsw.exceptions.IllegalActionException;
import it.polimi.ingsw.exceptions.NotAvailableNicknameException;
import it.polimi.ingsw.model.leaderCard.LeaderCardRequirements.CardRequirementColor;
import it.polimi.ingsw.model.leaderCard.LeaderCardRequirements.CardRequirementColorAndLevel;
import it.polimi.ingsw.model.leaderCard.LeaderCardRequirements.CardRequirementResource;
import it.polimi.ingsw.model.leaderCard.LeaderCardRequirements.Requirement;
import it.polimi.ingsw.model.leaderCard.leaderEffects.*;
import it.polimi.ingsw.model.soloGame.*;
import it.polimi.ingsw.network.messages.fromClient.*;
import it.polimi.ingsw.network.messages.sendToClient.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.SocketException;
import java.rmi.UnexpectedException;
import java.util.Timer;
import java.util.TimerTask;

/**
 * This class
 */

public class ClientHandler implements Runnable {
    private String nickname;
    private final Socket socket;
    private final BufferedReader in;
    private final PrintWriter out;
    private GameController game;
    private PlayerState playerState;
    private boolean mainActionDone;
    private int numLeaderActionDone;
    private boolean pingAnswered;
    private final Gson gson;
    private boolean keepRunning;
    private boolean beginningActionDone;
    private final int MAX_LEADER_ACTION = 2;
    private boolean gameEnded;
    //ONLY FOR TESTING PURPOSE
    private boolean startTimer;


    private ClientHandler(ClientHandler original) {
        this.nickname = original.nickname;
        this.socket = null;
        this.in = null;
        this.out = null;
        this.game = null;
        this.playerState = original.playerState;
        this.mainActionDone = original.mainActionDone;
        this.numLeaderActionDone = original.numLeaderActionDone;
        this.pingAnswered = true;
        this.gson = null;
        this.keepRunning = true;
        this.beginningActionDone = original.beginningActionDone;
        this.gameEnded = original.gameEnded;
    }

    /**
     * Creates a partial copy of the specified ClientHandler. It only copies the information related to the state of the Player.
     *
     * @param original the ClientHandler to be partially cloned
     */
    public static ClientHandler getPartialCopy(ClientHandler original) {
        return new ClientHandler(original);
    }


    /**
     * Copies from the specified ClientHandler partial copy its information and saves them in this ClientHandler. It pays attention to
     * players' disconnection: if in the copy the player was registered as disconnected but now is connected then the state doesn't change;
     * if in the copy the player was registered as not disconnected but now is disconnected then the state doesn't change
     *
     * @param partialCopy the partial copy where to get information
     */
    public void refreshState(ClientHandler partialCopy) {
        //If in the copy the player was registered as disconnected but now is connected then the state doesn't change
        if (this.playerState == PlayerState.DISCONNECTED && partialCopy.playerState != PlayerState.DISCONNECTED)
            ;
            //If in the copy the player was registered as not disconnected but now is disconnected then the state doesn't change
        else if (this.playerState != PlayerState.DISCONNECTED && partialCopy.playerState == PlayerState.DISCONNECTED)
            ;
            //In the other cases, we copy the original value
        else
            this.playerState = partialCopy.playerState;

        this.mainActionDone = partialCopy.mainActionDone;
        this.numLeaderActionDone = partialCopy.numLeaderActionDone;
    }

    /**
     * used only for test purpose
     */
    @Deprecated
    public ClientHandler(Socket socket, BufferedReader in, PrintWriter out) {
        this.socket = socket;
        this.in = in;
        this.out = new PrintWriter(out, true);
        this.playerState = PlayerState.WAITING4NAME;
        mainActionDone = false;
        numLeaderActionDone = 0;
        pingAnswered = true;
        keepRunning = true;
        beginningActionDone = false;
        startTimer = true;
        gameEnded = false;

        RuntimeTypeAdapterFactory<Requirement> requirementTypeFactory
                = RuntimeTypeAdapterFactory.of(Requirement.class, "type");
        requirementTypeFactory.registerSubtype(Requirement.class, "requirement");
        requirementTypeFactory.registerSubtype(CardRequirementColor.class, "cardRequirementColor");
        requirementTypeFactory.registerSubtype(CardRequirementResource.class, "cardRequirementResource");
        requirementTypeFactory.registerSubtype(CardRequirementColorAndLevel.class, "cardRequirementColorAndLevel");

        RuntimeTypeAdapterFactory<Effect> effectTypeFactory
                = RuntimeTypeAdapterFactory.of(Effect.class, "type");
        effectTypeFactory.registerSubtype(Effect.class, "effect");
        effectTypeFactory.registerSubtype(DiscountLeaderEffect.class, "discountLeaderEffect");
        effectTypeFactory.registerSubtype(ExtraProductionLeaderEffect.class, "extraProductionLeaderEffect");
        effectTypeFactory.registerSubtype(ExtraSlotLeaderEffect.class, "extraSlotLeaderEffect");
        effectTypeFactory.registerSubtype(WhiteMarbleLeaderEffect.class, "whiteMarbleLeaderEffect");

        RuntimeTypeAdapterFactory<SoloActionToken> tokenTypeFactory
                = RuntimeTypeAdapterFactory.of(SoloActionToken.class, "type");
        tokenTypeFactory.registerSubtype(SoloActionToken.class, "soloActionToken");
        tokenTypeFactory.registerSubtype(DiscardToken.class, "discardToken");
        tokenTypeFactory.registerSubtype(FaithPointToken.class, "faithPointToken");
        tokenTypeFactory.registerSubtype(ShuffleToken.class, "shuffleToken");

        this.gson = new GsonBuilder().registerTypeAdapterFactory((requirementTypeFactory))
                .registerTypeAdapterFactory(effectTypeFactory).registerTypeAdapterFactory(tokenTypeFactory).create();
    }


    /**
     * Creates a new clienHandler, it sends and reads messages from the client
     *
     * @param socket the communication socket, which allows the communication with the client
     * @throws IOException if there is a problem in the communication streams with the client
     */
    public ClientHandler(Socket socket) throws IOException {
        this.socket = socket;
        this.in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        this.out = new PrintWriter(socket.getOutputStream(), true);
        this.playerState = PlayerState.WAITING4NAME;
        pingAnswered = true;
        keepRunning = true;
        beginningActionDone = false;
        startTimer = true;
        gameEnded = false;

        RuntimeTypeAdapterFactory<Requirement> requirementTypeFactory
                = RuntimeTypeAdapterFactory.of(Requirement.class, "type");
        requirementTypeFactory.registerSubtype(Requirement.class, "requirement");
        requirementTypeFactory.registerSubtype(CardRequirementColor.class, "cardRequirementColor");
        requirementTypeFactory.registerSubtype(CardRequirementResource.class, "cardRequirementResource");
        requirementTypeFactory.registerSubtype(CardRequirementColorAndLevel.class, "cardRequirementColorAndLevel");

        RuntimeTypeAdapterFactory<Effect> effectTypeFactory
                = RuntimeTypeAdapterFactory.of(Effect.class, "type");
        effectTypeFactory.registerSubtype(Effect.class, "effect");
        effectTypeFactory.registerSubtype(DiscountLeaderEffect.class, "discountLeaderEffect");
        effectTypeFactory.registerSubtype(ExtraProductionLeaderEffect.class, "extraProductionLeaderEffect");
        effectTypeFactory.registerSubtype(ExtraSlotLeaderEffect.class, "extraSlotLeaderEffect");
        effectTypeFactory.registerSubtype(WhiteMarbleLeaderEffect.class, "whiteMarbleLeaderEffect");

        RuntimeTypeAdapterFactory<SoloActionToken> tokenTypeFactory
                = RuntimeTypeAdapterFactory.of(SoloActionToken.class, "type");
        tokenTypeFactory.registerSubtype(SoloActionToken.class, "soloActionToken");
        tokenTypeFactory.registerSubtype(DiscardToken.class, "discardToken");
        tokenTypeFactory.registerSubtype(FaithPointToken.class, "faithPointToken");
        tokenTypeFactory.registerSubtype(ShuffleToken.class, "shuffleToken");

        this.gson = new GsonBuilder().registerTypeAdapterFactory((requirementTypeFactory))
                .registerTypeAdapterFactory(effectTypeFactory).registerTypeAdapterFactory(tokenTypeFactory).create();
    }

    /**
     * used only for test purpose
     */
    @Deprecated
    public ClientHandler(Socket socket, BufferedReader in, PrintWriter out, boolean startTimer) {
        this.socket = socket;
        this.in = in;
        this.out = new PrintWriter(out, true);
        this.playerState = PlayerState.WAITING4NAME;
        mainActionDone = false;
        numLeaderActionDone = 0;
        pingAnswered = true;
        keepRunning = true;
        beginningActionDone = false;
        this.startTimer = startTimer;
        gameEnded = false;

        RuntimeTypeAdapterFactory<Requirement> requirementTypeFactory
                = RuntimeTypeAdapterFactory.of(Requirement.class, "type");
        requirementTypeFactory.registerSubtype(Requirement.class, "requirement");
        requirementTypeFactory.registerSubtype(CardRequirementColor.class, "cardRequirementColor");
        requirementTypeFactory.registerSubtype(CardRequirementResource.class, "cardRequirementResource");
        requirementTypeFactory.registerSubtype(CardRequirementColorAndLevel.class, "cardRequirementColorAndLevel");

        RuntimeTypeAdapterFactory<Effect> effectTypeFactory
                = RuntimeTypeAdapterFactory.of(Effect.class, "type");
        effectTypeFactory.registerSubtype(Effect.class, "effect");
        effectTypeFactory.registerSubtype(DiscountLeaderEffect.class, "discountLeaderEffect");
        effectTypeFactory.registerSubtype(ExtraProductionLeaderEffect.class, "extraProductionLeaderEffect");
        effectTypeFactory.registerSubtype(ExtraSlotLeaderEffect.class, "extraSlotLeaderEffect");
        effectTypeFactory.registerSubtype(WhiteMarbleLeaderEffect.class, "whiteMarbleLeaderEffect");

        RuntimeTypeAdapterFactory<SoloActionToken> tokenTypeFactory
                = RuntimeTypeAdapterFactory.of(SoloActionToken.class, "type");
        tokenTypeFactory.registerSubtype(SoloActionToken.class, "soloActionToken");
        tokenTypeFactory.registerSubtype(DiscardToken.class, "discardToken");
        tokenTypeFactory.registerSubtype(FaithPointToken.class, "faithPointToken");
        tokenTypeFactory.registerSubtype(ShuffleToken.class, "shuffleToken");

        this.gson = new GsonBuilder().registerTypeAdapterFactory((requirementTypeFactory))
                .registerTypeAdapterFactory(effectTypeFactory).registerTypeAdapterFactory(tokenTypeFactory).create();
    }


    /**
     * Creates a new thread for the communication with the client and starts a timer that send a ping message to the
     * client every 10 seconds and handles the response.
     * This thread receives the message from the client, parses the message in the Command class and reads the name of
     * the command; based on this name it parses the parameters of the command in the correct class and calls the
     * methods of game to do the action
     */
    @Override
    public void run() {
        Command command;

        Timer pingTimer = new Timer();
        //Creates a timer that pings the client every 10 sec
        if (startTimer) {
            pingTimer.schedule(new TimerTask() {
                @Override
                public void run() {
                    if (pingAnswered) {
                        send(new PingMessage("Ping"));
                        pingAnswered = false;
                    } else {
                        updatePlayerState();

                        try {
                            socket.close();
                            pingTimer.cancel();
                            keepRunning = false;
                        } catch (IOException ioException) {
                            ioException.printStackTrace();
                            keepRunning = false;
                            System.out.println("Error while trying to close the socket");
                        }
                    }
                }
            }, 0, 10000);
        }

        while (keepRunning) {
            try {
                String line = in.readLine();
                //check if the read string respects some basic syntax properties
                while (!(line.charAt(0) == '{' && line.contains("{\"cmd\":") && line.contains("\"parameters\":") && line.charAt(line.length() - 1) == '}')) {
                    this.send(new ErrorMessage("Command not correctly formatted"));
                    line = in.readLine();
                }
                command = gson.fromJson(line, Command.class);
                while (!socket.isClosed() && !command.getCmd().equals("quit")) {
                    if (gameEnded && !command.getCmd().equals("ping"))
                        this.send(new ErrorMessage("The game is ended, you can't do this action"));
                    else
                        executeCommand(command);

                    line = in.readLine();
                    while (!(line.charAt(0) == '{' && line.contains("{\"cmd\":") && line.contains("\"parameters\":") && line.charAt(line.length() - 1) == '}')) {
                        this.send(new ErrorMessage("Command not correctly formatted"));
                        line = in.readLine();
                    }
                    command = gson.fromJson(line, Command.class);
                }

                updatePlayerState();
                keepRunning = false;

            } catch (SocketException e) {
                //If the socket is closed by the timer and the thread was waiting waiting a message from InputStream, this exception is launched
                //we simply print what happened and we finish the thread. The socket is closed only if the client haven't logged in yet
                System.out.println("Closed socket while waiting message from client or trying to send a response");
                keepRunning = false;
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
                this.send(new ErrorMessage("An error occurred (IOException)"));
                keepRunning = false;
            } catch (IllegalActionException | IllegalArgumentException e) {
                e.printStackTrace();
                this.send(new ErrorMessage(e.getMessage()));
            } catch (InterruptedException e) {
                e.printStackTrace();
                this.send(new ErrorMessage("An error occurred (InterruptedException)"));
                keepRunning = false;
            } catch (NotAvailableNicknameException e) {
                e.printStackTrace();
                this.send(new ErrorMessage("This nickname isn't available!"));
            } catch (IllegalStateException e) {
                System.out.println("Error in selecting num player or in next player");
                e.printStackTrace();
                keepRunning = false;
            }
        }

        //Close stream and socket
        try {
            in.close();
        } catch (IOException e) {
            System.out.println("An error occurred while closing InputStream");
            e.printStackTrace();
        }
        out.close();
        try {
            socket.close();
        } catch (IOException e) {
            System.out.println("An error occurred while closing Socket");
            e.printStackTrace();
        }
    }

    /**
     * Sends a string message to the client
     *
     * @param message string to be sent
     */
    public synchronized void send(String message) {
        out.println(message);
        //out.flush();
    }

    /**
     * sets that the game is ended, so other actions can't be done
     */
    public void setGameEnded() {
        gameEnded = true;
    }

    /**
     * Sends a Json string message to the client
     *
     * @param response the response to a command to be sent to the client through Json
     */
    public synchronized void send(ResponseInterface response) {
        if (response != null) {
            ResponseMessage responseMessage = new ResponseMessage(response.getResponseType(), gson.toJson(response));
            out.println(gson.toJson(responseMessage));
        } else {
            ResponseMessage responseMessage = new ResponseMessage(ResponseType.INFOSTRING, gson.toJson(new GeneralInfoStringMessage("Empty message")));
            out.println(gson.toJson(responseMessage));
        }
        //out.flush();
    }

    /**
     * Updates the state of the player after a disconnection according to the current situation:
     * register if the player hasn't done the beginning action;
     * sets the player state to disconnected and, if the player is playing his turn, ends the turn;
     * if it's a solo game, it ends the game
     */
    public void updatePlayerState() {
        //check if the player has done the beginning action
        if (playerState == PlayerState.WAITING4BEGINNINGDECISIONS || playerState == PlayerState.WAITING4GAMESTART || (playerState == PlayerState.PLAYINGBEGINNINGDECISIONS && !beginningActionDone)) {
            game.registerPlayerDisconnectionBeforeStarting(ClientHandler.this);
        }

        PlayerState tmp = playerState;

        //Set the player's state to disconnected
        if (playerState != PlayerState.WAITING4NAME && playerState != PlayerState.WAITING4SETNUMPLAYER) {
            setPlayerState(PlayerState.DISCONNECTED);
            if (tmp != PlayerState.PLAYING && tmp != PlayerState.PLAYINGBEGINNINGDECISIONS)
                game.updatesAfterDisconnection(ClientHandler.this, false);
        } else
            setPlayerState(PlayerState.DISCONNECTED);

        //If the player was playing his turn, the turn is ended
        if (game.getNumberOfPlayers() > 1 && (tmp == PlayerState.PLAYING || tmp == PlayerState.PLAYINGBEGINNINGDECISIONS))
            game.specifyNextPlayer(ClientHandler.this);
        else if (game.getNumberOfPlayers() == 1)
            game.endGameSolo();
    }

    /**
     * sets the nickname of the player
     *
     * @param nickname: the nickname of the player
     */
    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    /**
     * sets the state of the player
     *
     * @param state: the state of the player
     */
    public void setPlayerState(PlayerState state) {
        this.playerState = state;
    }

    /**
     * sets the game
     *
     * @param game: the game
     */
    public void setGame(GameController game) {
        this.game = game;
    }

    /*
    public BufferedReader getIn() {
        return in;
    }

    public PrintWriter getOut() {
        return out;
    }*/

    /**
     * returns the nickname of the player
     *
     * @return the nickname of the player
     */
    public String getNickname() {
        return this.nickname;
    }

    /**
     * returns the state of the player
     *
     * @return the state of the player
     */
    public PlayerState getPlayerState() {
        return playerState;
    }

    /**
     * return if the player has already done the initial action of the game
     *
     * @return return if the player has already done the initial action of the game
     */
    public boolean isBeginningActionDone() {
        return beginningActionDone;
    }

    /**
     * check if the player has done the initial action of the game and if not sends an error
     *
     * @return false if the player hasn't done the initial action yet
     */
    private boolean checkBeginningActionDone() {
        if (playerState == PlayerState.PLAYINGBEGINNINGDECISIONS && !beginningActionDone) {
            this.send(new ErrorMessage("You must satisfy your liege's demand first"));
            return false;
        }
        return true;
    }

    /**
     * used for testing purpose
     */
    public String getInput() throws IOException {
        //Scanner in = new Scanner(socket.getInputStream());
        return this.in.readLine();
    }

    /**
     * used for testing purpose
     */
    public void setBeginningActionDone(boolean value) {
        this.beginningActionDone = value;
    }

    /**
     * used for testing purpose
     */
    public GameController getGame() {
        return game;
    }


    /**
     * execute the command received from the client, based on the name of the command
     *
     * @param command: the received command
     * @throws IllegalActionException        if the action can't be performed
     * @throws UnexpectedException           if the configuration of the game fails
     * @throws NotAvailableNicknameException if the nickname is already taken by another player
     * @throws InterruptedException          if the creation of a game fails
     */
    public void executeCommand(Command command) throws IllegalActionException, UnexpectedException, NotAvailableNicknameException, InterruptedException {
        switch (command.getCmd()) {
            case "pingResponse":
                pingAnswered = true;
                break;

            case "login":
                login(command);
                break;

            case "setNumPlayer":
                setNumOfPlayers(command);
                break;

            case "getResourcesFromMarket":
                getResourcesFromMarket(command);
                break;

            case "buyFromMarket":
                buyFromMarket(command);
                break;

            case "getCardCost":
                getCardCost(command);
                break;

            case "buyDevCard":
                buyDevCard(command);
                break;

            case "getProductionCost":
                getProductionCost(command);
                break;

            case "activateProductionMessage":
                activateProduction(command);
                break;

            case "discardLeaderAndExtraResBeginning":
                if (playerState != PlayerState.PLAYINGBEGINNINGDECISIONS) {
                    this.send(new ErrorMessage("You can't do this action now"));
                    break;
                }

                DiscardLeaderAndExtraResBeginningMessage discardLeaderCardBeginning = gson.fromJson(command.getParameters(), DiscardLeaderAndExtraResBeginningMessage.class);
                game.discardLeaderAndExtraResBeginning(discardLeaderCardBeginning, this);
                beginningActionDone = true;

                if (game.getState() != GameState.STARTED || game.getNumberOfPlayers() == 1) {
                    if (playerState != PlayerState.DISCONNECTED) {
                        playerState = PlayerState.PLAYING;
                        game.updatesPlayersStates();
                    }
                    if (game.getNumberOfPlayers() == 1) {
                        game.setState(GameState.INSESSION);
                        game.sendUpdateSolo(this);
                    }
                    break;
                }
            case "endTurn":
                if (playerState != PlayerState.PLAYING && playerState != PlayerState.PLAYINGBEGINNINGDECISIONS) {
                    this.send(new ErrorMessage("Wait your turn to do the action"));
                    break;
                }
                if (!checkBeginningActionDone()) {
                    break;
                }

                if (game.getNumberOfPlayers() == 1) {
                    send(new GeneralInfoStringMessage("Lorenzos' Turn"));
                    game.drawSoloToken(this);
                    send(new GeneralInfoStringMessage("Now it's Your turn, Master " + nickname));
                } else {
                    send(new GeneralInfoStringMessage("You ended the turn"));
                    game.specifyNextPlayer(this);

                }
                mainActionDone = false;
                break;

            case "moveBetweenShelves":
                moveBetweenShelves(command);

                break;

            case "moveLeaderToShelf":
                moveLeaderToShelf(command);

                break;

            case "moveShelfToLeader":
                moveShelfToLeader(command);
                break;

            case "discardLeader":
                discardLeader(command);
                break;

            case "ActivateLeader":
                activateLeader(command);
                break;

            default:
                this.send(new ErrorMessage("No command found"));
                break;
        }
    }

    /**
     * handle the login of the player: it makes the player create a new game or it adds the player to the game from which he left
     *
     * @param command the command received from the client
     * @throws InterruptedException          if the login fails
     * @throws NotAvailableNicknameException if the nickname is already taken
     * @throws IllegalActionException        if there are errors during the login
     */
    public void login(Command command) throws InterruptedException, NotAvailableNicknameException, IllegalActionException {
        if (playerState != PlayerState.WAITING4NAME) {
            this.send(new ErrorMessage("You can't do this action now"));
            return;
        }

        LoginMessage loginMessage = gson.fromJson(command.getParameters(), LoginMessage.class);
        this.nickname = loginMessage.getNickName();
        this.game = GamesManagerSingleton.getInstance().joinOrCreateNewGame(this);
        //If we arrive here, then the player has a valid login
        //this.send(new LoginConfirmationMessage(this.nickname));
        if (this.game == null) {
            //this.send(new AskForNumPlayersMessage("You are creating a game! Tell me how many players you want in this game!"));
            this.playerState = PlayerState.WAITING4SETNUMPLAYER;
        }
    }

    /**
     * sets the number of players for the game that is being created
     *
     * @param command the command received from the client
     * @throws IllegalActionException if the action can't be performed
     * @throws UnexpectedException    if the creation of the game fails
     */
    public void setNumOfPlayers(Command command) throws IllegalActionException, UnexpectedException {
        if (playerState != PlayerState.WAITING4SETNUMPLAYER) {
            this.send(new ErrorMessage("You can't do this action now"));
            return;
        }
        if (game != null)
            throw new IllegalActionException("You are not supposed to set the number of players for this game: it has already been set!");
        SetNumPlayerMessage setNumPlayerMessage = gson.fromJson(command.getParameters(), SetNumPlayerMessage.class);
        this.game = GamesManagerSingleton.getInstance().configureGame(this, setNumPlayerMessage.getNumPlayer());
        //If we arrive here, then the player has set a valid number of players
        //this.send(new SetNumPlayersConfirmationMessage(game.getNumberOfPlayers()));
        //game.setState(GameState.WAITING4PLAYERS);
        //game.setPlayer(this);
    }

    /**
     * parses the command received from the client and makes the game send the resources of the marked in the specified line
     *
     * @param command the command received from the client
     */
    public void getResourcesFromMarket(Command command) {
        if (!checkBeginningActionDone()) {
            return;
        }
        if (playerState != PlayerState.PLAYING) {
            this.send(new ErrorMessage("Wait your turn to do the action"));
            return;
        }
        GetFromMatrixMessage resFromMkt = gson.fromJson(command.getParameters(), GetFromMatrixMessage.class);
        game.getResFromMkt(resFromMkt, this);
    }

    /**
     * parses the command and makes the game buy the resources in the specified line of the market
     *
     * @param command the command received from the client
     * @throws IllegalActionException if the action can't be performed due to errors
     */
    public void buyFromMarket(Command command) throws IllegalActionException {
        if (!checkBeginningActionDone()) {
            return;
        }
        if (playerState != PlayerState.PLAYING) {
            this.send(new ErrorMessage("Wait your turn to do the action"));
            return;
        }
        if (!mainActionDone) {
            BuyFromMarketMessage buyFromMarket = gson.fromJson(command.getParameters(), BuyFromMarketMessage.class);
            game.buyFromMarket(buyFromMarket, this);
            mainActionDone = true;
        } else {
            this.send(new ErrorMessage("You've already done your main action in this turn"));
        }
    }

    /**
     * parses the command and makes the game get the cost of the specified card in the dev grid
     *
     * @param command the command received from the client
     */
    public void getCardCost(Command command) {
        if (!checkBeginningActionDone()) {
            return;
        }
        if (playerState != PlayerState.PLAYING) {
            this.send(new ErrorMessage("Wait your turn to do the action"));
            return;
        }
        GetFromMatrixMessage cardCost = gson.fromJson(command.getParameters(), GetFromMatrixMessage.class);
        game.getCardCost(cardCost, this);
    }

    /**
     * parses the command and makes the game buy the card in the specified position in the dev grid
     *
     * @param command the command received from the client
     * @throws IllegalActionException if the action can't be performed due to errors
     */
    public void buyDevCard(Command command) throws IllegalActionException {
        if (!checkBeginningActionDone()) {
            return;
        }
        if (playerState != PlayerState.PLAYING) {
            this.send(new ErrorMessage("Wait your turn to do the action"));
            return;
        }

        if (!mainActionDone) {
            BuyDevCardMessage buyDevCard = gson.fromJson(command.getParameters(), BuyDevCardMessage.class);
            game.buyDevCard(buyDevCard, this);
            mainActionDone = true;
        } else {
            this.send(new ErrorMessage("You've already done your main action in this turn"));
        }
    }

    /**
     * parses the command and makes the game get the cost of the production specified by the client
     *
     * @param command the command received from the client
     * @throws IllegalActionException if the data received from the client are incorrect
     */
    public void getProductionCost(Command command) throws IllegalActionException {
        if (!checkBeginningActionDone()) {
            return;
        }
        if (playerState != PlayerState.PLAYING) {
            this.send(new ErrorMessage("Wait your turn to do the action"));
            return;
        }
        GetProductionCostMessage productionCost = gson.fromJson(command.getParameters(), GetProductionCostMessage.class);
        game.getProductionCost(productionCost, this);
    }

    /**
     * parses the command and makes the game activate the production specified by the client
     *
     * @param command the command received from the client
     * @throws IllegalActionException if the data received from the client are incorrect
     */
    public void activateProduction(Command command) throws IllegalActionException {
        if (!checkBeginningActionDone()) {
            return;
        }
        if (playerState != PlayerState.PLAYING) {
            this.send(new ErrorMessage("Wait your turn to do the action"));
            return;
        }
        if (!mainActionDone) {
            ActivateProductionMessage activateProduction = gson.fromJson(command.getParameters(), ActivateProductionMessage.class);
            game.activateProduction(activateProduction, this);
            mainActionDone = true;
        } else {
            this.send(new ErrorMessage("You've already done your main action in this turn"));
        }
    }

    /**
     * parses the command and makes the game move the specified resources of the depot
     *
     * @param command the command received from the client
     * @throws IllegalActionException if the resources can't be moved correctly
     */
    public void moveBetweenShelves(Command command) throws IllegalActionException {
        if (!checkBeginningActionDone()) {
            return;
        }
        if (playerState != PlayerState.PLAYING) {
            this.send(new ErrorMessage("Wait your turn to do the action"));
            return;
        }
        MoveBetweenShelvesMessage moveBetweenShelves = gson.fromJson(command.getParameters(), MoveBetweenShelvesMessage.class);
        game.moveResourcesBetweenShelves(moveBetweenShelves, this);
    }

    /**
     * parses the command and makes the game move the specified resources from a leader card to the depot
     *
     * @param command the command received from the client
     * @throws IllegalActionException if the resources can't be moved correctly
     */
    public void moveLeaderToShelf(Command command) throws IllegalActionException {
        if (!checkBeginningActionDone()) {
            return;
        }
        if (playerState != PlayerState.PLAYING) {
            this.send(new ErrorMessage("Wait your turn to do the action"));
            return;
        }
        MoveLeaderToShelfMessage moveLeaderToShelf = gson.fromJson(command.getParameters(), MoveLeaderToShelfMessage.class);
        game.moveResourcesToShelf(moveLeaderToShelf, this);
    }

    /**
     * parses the command and makes the game move the specified resources from the depot to a leader card
     *
     * @param command the command received from the client
     * @throws IllegalActionException if the resources can't be moved correctly
     */
    public void moveShelfToLeader(Command command) throws IllegalActionException {
        if (!checkBeginningActionDone()) {
            return;
        }
        if (playerState != PlayerState.PLAYING) {
            this.send(new ErrorMessage("Wait your turn to do the action"));
            return;
        }
        MoveShelfToLeaderMessage moveShelfToLeader = gson.fromJson(command.getParameters(), MoveShelfToLeaderMessage.class);
        game.moveResourcesToLeader(moveShelfToLeader, this);
    }

    /**
     * parses the command and makes the game discard the specified leader card
     *
     * @param command the command received from the client
     */
    public void discardLeader(Command command) {
        if (!checkBeginningActionDone()) {
            return;
        }
        if (playerState != PlayerState.PLAYING) {
            this.send(new ErrorMessage("Wait your turn to do the action"));
            return;
        }
        if (numLeaderActionDone < MAX_LEADER_ACTION) {
            LeaderMessage discardLeader = gson.fromJson(command.getParameters(), LeaderMessage.class);
            game.discardLeader(discardLeader, this);
            numLeaderActionDone++;
        } else {
            this.send(new ErrorMessage("You've already done all the possible leader actions"));
        }
    }

    /**
     * parses the command and makes the game activate the specified leader card
     *
     * @param command the command received from the client
     * @throws IllegalArgumentException if the card can't be activated
     */
    public void activateLeader(Command command) throws IllegalActionException {
        if (!checkBeginningActionDone()) {
            return;
        }
        if (playerState != PlayerState.PLAYING) {
            this.send(new ErrorMessage("Wait your turn to do the action"));
            return;
        }
        if (numLeaderActionDone < MAX_LEADER_ACTION) {
            LeaderMessage activateLeader = gson.fromJson(command.getParameters(), LeaderMessage.class);
            game.activateLeader(activateLeader, this);
            numLeaderActionDone++;
        } else {
            this.send(new ErrorMessage("You've already done all the possible leader actions"));
        }
    }


}
