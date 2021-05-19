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
                    if (playerState == PlayerState.WAITING4NAME) {
                        try {
                            socket.close();
                            pingTimer.cancel();
                            keepRunning = false;
                        } catch (IOException ioException) {
                            ioException.printStackTrace();
                            keepRunning = false;
                            System.out.println("Error while trying to close the socket");
                            //TODO: cosa fare se ho errore in chiusura socket?
                        }
                    } else {
                        if (playerState == PlayerState.WAITING4BEGINNINGDECISIONS) {
                            game.registerPlayerDisconnectionBeforeStarting(ClientHandler.this);
                        }

                        if (playerState != PlayerState.DISCONNECTED) {
                            setPlayerState(PlayerState.DISCONNECTED);
                            game.updatesAfterDisconnection(ClientHandler.this);
                        }
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
        }, 0, 300000);

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
                            if (this.game == null)
                                this.send(new AskForNumPlayersMessage("You are creating a game! Tell me how many players you want in this game!"));
                                //TODO: AGGIUNGEREI UNO STATO PER DIRE CHE STO ASPETTANDO SOLO UN SETNUMPLAYER COME PROSSIMO MESSAGGIO
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
                            if (playerState != PlayerState.WAITING4NAME) { //TODO: CHE STATO DEVO METTERE?
                                this.send(new ErrorMessage("You can't do this action now"));
                                break;
                            }
                            //TODO: QUESTO IF MI SEMBRA SBAGLIATO
                            if (game != null)
                                throw new IllegalActionException("You are not supposed to set the number of players for this game: it has already been set!");
                            SetNumPlayerMessage setNumPlayerMessage = gson.fromJson(command.getParameters(), SetNumPlayerMessage.class);
                            this.game = GamesManagerSingleton.getInstance().configureGame(this, setNumPlayerMessage.getNumPlayer());
                            //TODO: ma se il game è vuoto quando è che lo creiamo l'istanza del game?
                            game.setState(GameState.WAITING4PLAYERS);
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
                                mainActionDone = true;
                                BuyFromMarketMessage buyFromMarket = gson.fromJson(command.getParameters(), BuyFromMarketMessage.class);
                                game.buyFromMarket(buyFromMarket, this);
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
                                mainActionDone = true;
                                BuyDevCardMessage buyDevCard = gson.fromJson(command.getParameters(), BuyDevCardMessage.class);
                                game.buyDevCard(buyDevCard, this);
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
                                mainActionDone = true;
                                ActivateProductionMessage activateProduction = gson.fromJson(command.getParameters(), ActivateProductionMessage.class);
                                game.activateProduction(activateProduction, this);
                            } else {
                                this.send(new ErrorMessage("You've already done your main action in this turn"));
                            }
                            break;

                        case "discardLeaderAndExtraResBeginning":
                            if (playerState != PlayerState.WAITING4BEGINNINGDECISIONS) {
                                this.send(new ErrorMessage("You can't do this action now"));
                                break;
                            }
                            DiscardLeaderAndExtraResBeginningMessage discardLeaderCardBeginning = gson.fromJson(command.getParameters(), DiscardLeaderAndExtraResBeginningMessage.class);
                            game.discardLeaderAndExtraResBeginning(discardLeaderCardBeginning, this);
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

                        case "endTurn":
                            if (playerState != PlayerState.PLAYING) {
                                this.send(new ErrorMessage("Wait your turn to do the action"));
                                break;
                            }
                            if (game.getNumberOfPlayers() == 1) {
                                this.playerState = PlayerState.WAITING4TURN; //Ci sarà da dire al player che non è il suo turno?
                                game.drawSoloToken(this);
                            } else {
                                game.specifyNextPlayer(this);
                            }
                            mainActionDone = false;
                            break;

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
                //TODO: cosa facciamo se non ci sono più player connessi?
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


    /*private void pingClient(Socket socket) throws IOException {
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

    public String getInput() throws IOException {
        //Scanner in = new Scanner(socket.getInputStream());
        return this.in.readLine();
    }

}
