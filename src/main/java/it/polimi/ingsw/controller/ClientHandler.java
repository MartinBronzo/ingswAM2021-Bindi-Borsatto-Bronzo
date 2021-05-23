package it.polimi.ingsw.controller;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.typeadapters.RuntimeTypeAdapterFactory;
import it.polimi.ingsw.controller.enums.GameState;
import it.polimi.ingsw.controller.enums.PlayerState;
import it.polimi.ingsw.exceptions.IllegalActionException;
import it.polimi.ingsw.exceptions.NotAvailableNicknameException;
import it.polimi.ingsw.model.LeaderCard.LeaderCardRequirements.CardRequirementColor;
import it.polimi.ingsw.model.LeaderCard.LeaderCardRequirements.CardRequirementColorAndLevel;
import it.polimi.ingsw.model.LeaderCard.LeaderCardRequirements.CardRequirementResource;
import it.polimi.ingsw.model.LeaderCard.LeaderCardRequirements.Requirement;
import it.polimi.ingsw.model.LeaderCard.leaderEffects.*;
import it.polimi.ingsw.network.messages.fromClient.*;
import it.polimi.ingsw.network.messages.sendToClient.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.SocketException;
import java.util.Timer;
import java.util.TimerTask;

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

        this.gson = new GsonBuilder().registerTypeAdapterFactory((requirementTypeFactory))
                .registerTypeAdapterFactory(effectTypeFactory).create();
    }

    public ClientHandler(Socket socket) throws IOException {
        this.socket = socket;
        this.in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        this.out = new PrintWriter(socket.getOutputStream(), true);
        this.playerState = PlayerState.WAITING4NAME;
        pingAnswered = true;
        keepRunning = true;
        beginningActionDone = false;

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

        this.gson = new GsonBuilder().registerTypeAdapterFactory((requirementTypeFactory))
                .registerTypeAdapterFactory(effectTypeFactory).create();
    }

    /**
     * Creates a new thread for the communication with the client. This thread receives the message from the client,
     * parses the message in the Command class and reads the name of the command; based on this name it parses
     * the parameters of the command in the correct class and calls the methods of game to do the action
     */
    @Override
    public void run() {
        Command command;
        final int MAX_LEADER_ACTION = 2;

        Timer pingTimer = new Timer();
        //Creates a timer that pings the client every 5 sec
        pingTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                if (pingAnswered) {
                    send(new PingMessage("Ping"));
                    pingAnswered = false;
                } else {
                    /*if(game.getNumberOfPlayers() == 1){
                        setPlayerState(PlayerState.DISCONNECTED);
                        GamesManagerSingleton.getInstance().deleteGame(game);
                    }*/


                    if (playerState == PlayerState.WAITING4BEGINNINGDECISIONS || playerState == PlayerState.WAITING4GAMESTART) {
                        //if(gameState == WAIT4BEGINNINGDECISIONS && il tuo turno non è ancora passato) (non dovrebbe servire)
                        game.registerPlayerDisconnectionBeforeStarting(ClientHandler.this);
                    }

                    PlayerState tmp = playerState;

                    if (playerState != PlayerState.WAITING4NAME && playerState != PlayerState.WAITING4SETNUMPLAYER) {
                        setPlayerState(PlayerState.DISCONNECTED);
                        game.updatesAfterDisconnection(ClientHandler.this);
                    } else
                        setPlayerState(PlayerState.DISCONNECTED);

                    if (tmp == PlayerState.PLAYING)
                        game.specifyNextPlayer(ClientHandler.this);

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

               /* try {
                    pingClient(socket);
                } catch (IOException e) {
                    //e.printStackTrace();
                    //If the player disconnects before logging in, the socket is closed and the pingTimer is canceled
                    if (playerSate == PlayerState.WAITING4NAME) {
                        try {
                            socket.close();
                            pingTimer.cancel();
                        } catch (IOException ioException) {
                            ioException.printStackTrace();
                        }
                    } else {
                        setPlayerState(PlayerState.DISCONNECTED);
                        if (playerSate != PlayerState.DISCONNECTED)
                            game.updatesAfterDisconnection(ClientHandler.this);
                    }
                }*/
            }
        }, 0, 200000);

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

                    switch (command.getCmd()) {

                        case "pingResponse":
                            pingAnswered = true;
                            // send("ok");
                            break;

                        case "login":
                            if (playerState != PlayerState.WAITING4NAME) {
                                this.send(new ErrorMessage("You can't do this action now"));
                                break;
                            }

                            LoginMessage loginMessage = gson.fromJson(command.getParameters(), LoginMessage.class);
                            this.nickname = loginMessage.getNickName();
                            this.game = GamesManagerSingleton.getInstance().joinOrCreateNewGame(this);
                            //If we arrive here, then the player has a valid login
                            this.send(new LoginConfirmationMessage(this.nickname));
                            if (this.game == null) {
                                this.send(new AskForNumPlayersMessage("You are creating a game! Tell me how many players you want in this game!"));
                                this.playerState = PlayerState.WAITING4SETNUMPLAYER;
                            }
                            break;

                        case "setNumPlayer":
                            if (playerState != PlayerState.WAITING4SETNUMPLAYER) {
                                this.send(new ErrorMessage("You can't do this action now"));
                                break;
                            }
                            if (game != null)
                                throw new IllegalActionException("You are not supposed to set the number of players for this game: it has already been set!");
                            SetNumPlayerMessage setNumPlayerMessage = gson.fromJson(command.getParameters(), SetNumPlayerMessage.class);
                            this.game = GamesManagerSingleton.getInstance().configureGame(this, setNumPlayerMessage.getNumPlayer());
                            //game.setState(GameState.WAITING4PLAYERS);
                            //game.setPlayer(this);
                            break;

                        case "getResourcesFromMarket":
                            if (playerState != PlayerState.PLAYING) {
                                this.send(new ErrorMessage("Wait your turn to do the action"));
                                break;
                            }
                            GetFromMatrixMessage resFromMkt = gson.fromJson(command.getParameters(), GetFromMatrixMessage.class);
                            game.getResFromMkt(resFromMkt, this);
                            break;

                        case "buyFromMarket":
                            if (playerState != PlayerState.PLAYING) {
                                this.send(new ErrorMessage("Wait your turn to do the action"));
                                break;
                            }
                            if (!mainActionDone) {
                                BuyFromMarketMessage buyFromMarket = gson.fromJson(command.getParameters(), BuyFromMarketMessage.class);
                                game.buyFromMarket(buyFromMarket, this);
                                mainActionDone = true;
                            } else {
                                this.send(new ErrorMessage("You've already done your main action in this turn"));
                            }
                            break;

                        case "getCardCost":
                            if (playerState != PlayerState.PLAYING) {
                                this.send(new ErrorMessage("Wait your turn to do the action"));
                                break;
                            }
                            GetFromMatrixMessage cardCost = gson.fromJson(command.getParameters(), GetFromMatrixMessage.class);
                            game.getCardCost(cardCost, this);
                            break;

                        case "buyDevCard":
                            if (playerState != PlayerState.PLAYING) {
                                this.send(new ErrorMessage("Wait your turn to do the action"));
                                break;
                            }

                            if (!mainActionDone) {
                                BuyDevCardMessage buyDevCard = gson.fromJson(command.getParameters(), BuyDevCardMessage.class);
                                game.buyDevCard(buyDevCard, this);
                                mainActionDone = true;
                            } else {
                                this.send(new ErrorMessage("You've already done your main action in this turn"));
                            }
                            break;

                        case "getProductionCost":
                            if (playerState != PlayerState.PLAYING) {
                                this.send(new ErrorMessage("Wait your turn to do the action"));
                                break;
                            }
                            GetProductionCostMessage productionCost = gson.fromJson(command.getParameters(), GetProductionCostMessage.class);
                            game.getProductionCost(productionCost, this);
                            break;

                        case "activateProductionMessage":
                            if (playerState != PlayerState.PLAYING) {
                                this.send(new ErrorMessage("Wait your turn to do the action"));
                                break;
                            }
                            if (!mainActionDone) {
                                ActivateProductionMessage activateProduction = gson.fromJson(command.getParameters(), ActivateProductionMessage.class);
                                game.activateProduction(activateProduction, this);
                                mainActionDone = true;
                            } else {
                                this.send(new ErrorMessage("You've already done your main action in this turn"));
                            }
                            break;

                        case "discardLeaderAndExtraResBeginning":
                            if (playerState != PlayerState.PLAYINGBEGINNINGDECISIONS) {
                                this.send(new ErrorMessage("You can't do this action now"));
                                break;
                            }

                            DiscardLeaderAndExtraResBeginningMessage discardLeaderCardBeginning = gson.fromJson(command.getParameters(), DiscardLeaderAndExtraResBeginningMessage.class);
                            game.discardLeaderAndExtraResBeginning(discardLeaderCardBeginning, this);
                            beginningActionDone = true;

                            if (game.getState() != GameState.STARTED || game.getNumberOfPlayers() == 1)
                                playerState = PlayerState.PLAYING;
                            break;
                        case "endTurn":
                            if (playerState != PlayerState.PLAYING && playerState != PlayerState.PLAYINGBEGINNINGDECISIONS) {
                                this.send(new ErrorMessage("Wait your turn to do the action"));
                                break;
                            }
                            if (playerState == PlayerState.PLAYINGBEGINNINGDECISIONS && !beginningActionDone) {
                                this.send(new ErrorMessage("You must satisfy your liege's demand first"));
                                break;
                            }

                            if (game.getNumberOfPlayers() == 1) {
                                send(new GeneralInfoStringMessage("Lorenzos' Turn"));
                                //this.playerState = PlayerState.WAITING4TURN; //Ci sarà da dire al player che non è il suo turno?
                                game.drawSoloToken(this);
                                send(new GeneralInfoStringMessage("Now it's Your turn, Master " + nickname));
                            } else {
                                send(new GeneralInfoStringMessage("You ended the turn"));
                                game.specifyNextPlayer(this);

                            }
                            mainActionDone = false;
                            break;

                        case "moveBetweenShelves":
                            if (playerState != PlayerState.PLAYING) {
                                this.send(new ErrorMessage("Wait your turn to do the action"));
                                break;
                            }
                            MoveBetweenShelvesMessage moveBetweenShelves = gson.fromJson(command.getParameters(), MoveBetweenShelvesMessage.class);
                            game.moveResourcesBetweenShelves(moveBetweenShelves, this);
                            break;

                        case "moveLeaderToShelf":
                            if (playerState != PlayerState.PLAYING) {
                                this.send(new ErrorMessage("Wait your turn to do the action"));
                                break;
                            }
                            MoveLeaderToShelfMessage moveLeaderToShelf = gson.fromJson(command.getParameters(), MoveLeaderToShelfMessage.class);
                            game.moveResourcesToShelf(moveLeaderToShelf, this);
                            break;

                        case "moveShelfToLeader":
                            if (playerState != PlayerState.PLAYING) {
                                this.send(new ErrorMessage("Wait your turn to do the action"));
                                break;
                            }
                            MoveShelfToLeaderMessage moveShelfToLeader = gson.fromJson(command.getParameters(), MoveShelfToLeaderMessage.class);
                            game.moveResourcesToLeader(moveShelfToLeader, this);
                            break;

                        case "discardLeader":
                            if (playerState != PlayerState.PLAYING) {
                                this.send(new ErrorMessage("Wait your turn to do the action"));
                                break;
                            }
                            if (numLeaderActionDone < MAX_LEADER_ACTION) {
                                numLeaderActionDone++;
                                LeaderMessage discardLeader = gson.fromJson(command.getParameters(), LeaderMessage.class);
                                game.discardLeader(discardLeader, this);
                            } else {
                                this.send(new ErrorMessage("You've already done all the possible leader actions"));
                            }
                            break;

                        case "ActivateLeader":
                            if (playerState != PlayerState.PLAYING) {
                                this.send(new ErrorMessage("Wait your turn to do the action"));
                                break;
                            }
                            if (numLeaderActionDone < MAX_LEADER_ACTION) {
                                numLeaderActionDone++;
                                LeaderMessage activateLeader = gson.fromJson(command.getParameters(), LeaderMessage.class);
                                game.activateLeader(activateLeader, this);
                            } else {
                                this.send(new ErrorMessage("You've already done all the possible leader actions"));
                            }
                            break;

                        /*case "endTurn":
                            if (playerState != PlayerState.PLAYING && playerState != PlayerState.PLAYINGBEGINNINGDECISIONS) {
                                this.send(new ErrorMessage("Wait your turn to do the action"));
                                break;
                            }
                            if (game.getNumberOfPlayers() == 1) {
                                //this.playerState = PlayerState.WAITING4TURN; //Ci sarà da dire al player che non è il suo turno?
                                game.drawSoloToken(this);
                            } else {
                                game.specifyNextPlayer(this);
                            }
                            mainActionDone = false;
                            break;*/

                        default:
                            this.send(new ErrorMessage("No command found"));
                            break;
                    }

                    //line = scanner.nextLine();

                    line = in.readLine();
                    while (!(line.charAt(0) == '{' && line.contains("{\"cmd\":") && line.contains("\"parameters\":") && line.charAt(line.length() - 1) == '}')) {
                        this.send(new ErrorMessage("Command not correctly formatted"));
                        line = in.readLine();
                    }
                    command = gson.fromJson(line, Command.class);
                }
            } catch (SocketException e) {
                //If the socket is closed by the timer and the thread was waiting waiting a message from InputStream, this exception is launched
                //we simply print what happened and we finish the thread. The socket is closed only if the client haven't logged in yet
                System.out.println("Closed socket while waiting message from client or trying to send a response");

                keepRunning = false;
                //e.printStackTrace();
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

    public synchronized void send(String message) {
        out.println(message);
        //out.flush();
    }

    public synchronized void send(ResponseInterface response) {
        ResponseMessage responseMessage = new ResponseMessage(response.getResponseType(), gson.toJson(response));
        out.println(gson.toJson(responseMessage));
        //out.flush();
    }


    /* //To be deleted....
    private void pingClient(Socket socket) throws IOException {
        this.send(new PingMessage("Ping"));
        socket.setSoTimeout(2000);
        in.readLine();
        if (playerSate == PlayerState.DISCONNECTED) {
            switch (game.getState()) {
                case LASTTURN:
                    playerSate = PlayerState.WAITING4LASTTURN; //waiting and not playing4lastturn because we assume that the turn of a disconnected player is skipped
                    break;
                case INSESSION:
                    playerSate = PlayerState.WAITING4TURN;
                    break;
                case WAITING4PLAYERS:
                    playerSate = PlayerState.WAITINGGAMESTART;
                    break;
                case CONFIGURING:
                    playerSate = PlayerState.WAITING4NAME;
                    break;
                case STARTED:
                    playerSate = PlayerState.WAITING4BEGINNINGDECISIONS;
                    break;
                //TODO: WAITINGGAMEEND?
            }
            game.updatesAfterDisconnection(this);
            //TODO: cosa succede se il giocatore si disconnette mentre gioca? Ci sarà da fare il rollback dello state del game e da cambiare i turni!
        }
    }*/


    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public void setPlayerState(PlayerState state) {
        this.playerState = state;
    }

    public void setGame(GameController game) {
        this.game = game;
    }

    public BufferedReader getIn() {
        return in;
    }

    public PrintWriter getOut() {
        return out;
    }

    public String getNickname() {
        return this.nickname;
    }

    public PlayerState getPlayerState() {
        return playerState;
    }

    public boolean isBeginningActionDone() {
        return beginningActionDone;
    }

    //This method was used for purposes reasons
    public String getInput() throws IOException {
        //Scanner in = new Scanner(socket.getInputStream());
        return this.in.readLine();
    }

    //used only for testing purpose
    public void setBeginningActionDone(boolean value) {
        this.beginningActionDone = value;
    }

}
