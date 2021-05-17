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

import java.io.*;
import java.net.Socket;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.util.Scanner;
import java.util.Timer;
import java.util.TimerTask;

public class ClientHandler implements Runnable {
    private String nickname;
    private final Socket socket;
    private final BufferedReader in;
    private final PrintWriter out;
    private GameController game;
    private PlayerState playerSate;
    private boolean mainActionDone;
    private int numLeaderActionDone;
    private final Gson gson;

    /**
     * used only for test purpose
     */
    @Deprecated
    public ClientHandler(Socket socket, BufferedReader in, PrintWriter out) {
        this.socket = socket;
        this.in = in;
        this.out = new PrintWriter(out, true);
        this.playerSate = PlayerState.WAITING4NAME;
        mainActionDone = false;
        numLeaderActionDone = 0;

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
        this.playerSate = PlayerState.WAITING4NAME;

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
                try {
                    pingClient(socket);
                } catch (IOException e) {
                    //e.printStackTrace();
                    setPlayerState(PlayerState.DISCONNECTED);
                    game.updatesAfterDisconnection(ClientHandler.this);
                    //TODO: SE SI DISCONNETTE PRIMA DEL LOGIN?
                }
            }
        }, 0, 5000);

        try {
            //Scanner scanner = new Scanner(in);

            //String line = scanner.nextLine();
            String line = in.readLine();
            //System.out.println(line);
            command = gson.fromJson(line, Command.class);
            while (!command.getCmd().equals("quit")) {
                if (playerSate == PlayerState.PLAYING) {
                    switch (command.getCmd()) {

                        case "login":
                            LoginMessage loginMessage = gson.fromJson(command.getParameters(), LoginMessage.class);
                            this.nickname = loginMessage.getNickName();
                            this.game = GamesManagerSingleton.getInstance().joinOrCreateNewGame(this);
                            if (this.game == null)
                                this.send(new AskForNumPlayersMessage("You are creating a game! Tell me how many players you want in this game!"));
                            else if (this.game.getState() == GameState.INSESSION || this.game.getState() == GameState.STARTED) {
                                //TODO: we cannot still discern between whether this was the last player added or they had been added back in the game because they lost their connection
                                //Ho aggiunto l'uguaglianza con started (che nella mia stesa è quanto il game ha raggiunto tutti i giocatori e sta aspettando che anche mandino le leader cards
                                //da scartare ed etc. all'inizio che è != da in session che quando il meccanismo a turni è in atto
                                this.send(new GeneralInfoStringMessage("You are back in the game!"));
                                this.send(this.game.getWholeMessageUpdateToClient());
                            } else {
                                this.send(new GeneralInfoStringMessage("You are in Game! You'll soon start play with others!"));
                            }
                            break;

                        case "setNumPlayer":
                            if (game != null)
                                throw new IllegalActionException("You are not supposed to set the number of players for this game: it has already been set!");
                            SetNumPlayerMessage setNumPlayerMessage = gson.fromJson(command.getParameters(), SetNumPlayerMessage.class);
                            this.game = GamesManagerSingleton.getInstance().configureGame(this, setNumPlayerMessage.getNumPlayer());
                            //TODO: ma se il game è vuoto quando è che lo creiamo l'istanza del game?
                            game.setState(GameState.WAITING4PLAYERS);
                            //game.setPlayer(this);
                            break;

                        case "getResourcesFromMarket":
                            GetFromMatrixMessage resFromMkt = gson.fromJson(command.getParameters(), GetFromMatrixMessage.class);
                            game.getResFromMkt(resFromMkt, this);
                            break;

                        case "buyFromMarket":
                            if (!mainActionDone) {
                                mainActionDone = true;
                                BuyFromMarketMessage buyFromMarket = gson.fromJson(command.getParameters(), BuyFromMarketMessage.class);
                                game.buyFromMarket(buyFromMarket, this);
                            } else {
                                this.send(new ErrorMessage("You've already done your main action in this turn"));
                            }
                            break;

                        case "getCardCost":
                            GetFromMatrixMessage cardCost = gson.fromJson(command.getParameters(), GetFromMatrixMessage.class);
                            game.getCardCost(cardCost, this);
                            break;

                        case "buyDevCard":
                            if (!mainActionDone) {
                                mainActionDone = true;
                                BuyDevCardMessage buyDevCard = gson.fromJson(command.getParameters(), BuyDevCardMessage.class);
                                game.buyDevCard(buyDevCard, this);
                            } else {
                                this.send(new ErrorMessage("You've already done your main action in this turn"));
                            }
                            break;

                        case "getProductionCost":
                            GetProductionCostMessage productionCost = gson.fromJson(command.getParameters(), GetProductionCostMessage.class);
                            game.getProductionCost(productionCost, this);
                            break;

                        case "activateProductionMesssage":
                            if (!mainActionDone) {
                                mainActionDone = true;
                                ActivateProductionMessage activateProduction = gson.fromJson(command.getParameters(), ActivateProductionMessage.class);
                                game.activateProduction(activateProduction, this);
                            } else {
                                this.send(new ErrorMessage("You've already done your main action in this turn"));
                            }
                            break;

                        case "discardLeaderAndExtraResBeginning":
                            DiscardLeaderAndExtraResBeginningMessage discardLeaderCardBeginning = gson.fromJson(command.getParameters(), DiscardLeaderAndExtraResBeginningMessage.class);
                            game.discardLeaderAndExtraResBeginning(discardLeaderCardBeginning, this);
                            break;

                        case "moveBetweenShelves":
                            MoveBetweenShelvesMessage moveBetweenShelves = gson.fromJson(command.getParameters(), MoveBetweenShelvesMessage.class);
                            game.moveResourcesBetweenShelves(moveBetweenShelves, this);
                            break;

                        case "moveLeaderToShelf":
                            MoveLeaderToShelfMessage moveLeaderToShelf = gson.fromJson(command.getParameters(), MoveLeaderToShelfMessage.class);
                            game.moveResourcesToShelf(moveLeaderToShelf, this);
                            break;

                        case "moveShelfToLeader":
                            MoveShelfToLeaderMessage moveShelfToLeader = gson.fromJson(command.getParameters(), MoveShelfToLeaderMessage.class);
                            game.moveResourcesToLeader(moveShelfToLeader, this);
                            break;

                        case "discardLeader":
                            if (numLeaderActionDone < MAX_LEADER_ACTION) {
                                numLeaderActionDone++;
                                LeaderMessage discardLeader = gson.fromJson(command.getParameters(), LeaderMessage.class);
                                game.discardLeader(discardLeader, this);
                            } else {
                                this.send(new ErrorMessage("You've already done all the possible leader actions"));
                            }
                            break;

                        case "ActivateLeader":
                            if (numLeaderActionDone < MAX_LEADER_ACTION) {
                                numLeaderActionDone++;
                                LeaderMessage activateLeader = gson.fromJson(command.getParameters(), LeaderMessage.class);
                                game.activateLeader(activateLeader, this);
                            } else {
                                this.send(new ErrorMessage("You've already done all the possible leader actions"));
                            }
                            break;

                        case "endTurn":

                            if (game.getNumberOfPlayers() == 1) {
                                this.playerSate = PlayerState.WAITING4TURN; //Ci sarà da dire al player che non è il suo turno?
                                game.drawSoloToken(this);
                            } else {
                                game.specifyNextPlayer(this);
                            }
                            mainActionDone = false;
                            break;

                    }
                } else {
                    this.send(new ErrorMessage("Wait your turn to do the action"));
                }
                //line = scanner.nextLine();
                line = in.readLine();
                command = gson.fromJson(line, Command.class);

            }
            //Close stream and socket
            in.close();
            out.close();
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
            this.send(new ErrorMessage("An error occurred (IOException)"));
        } catch (IllegalActionException | IllegalArgumentException e) {
            e.printStackTrace();
            this.send(new ErrorMessage(e.getMessage()));
        } catch (InterruptedException e) {
            e.printStackTrace();
            this.send(new ErrorMessage("An error occurred (InterruptedException)"));
        } catch (NotAvailableNicknameException e) {
            e.printStackTrace();
            this.send(new ErrorMessage("This nickname isn't available!"));
        } catch (IllegalStateException e) {
            //TODO: cosa facciamo se non ci sono più player connessi?
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

    private void pingClient(Socket socket) throws IOException {
        //TODO: da testare
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
    }


    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public void setPlayerState(PlayerState state) {
        this.playerSate = state;
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
        return playerSate;
    }

    public String getInput() throws IOException {
        //Scanner in = new Scanner(socket.getInputStream());
        String s = this.in.readLine();
        return s;
    }

}
