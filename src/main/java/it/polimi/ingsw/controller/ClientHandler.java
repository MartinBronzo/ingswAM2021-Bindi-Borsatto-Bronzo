package it.polimi.ingsw.controller;

import com.google.gson.Gson;
import it.polimi.ingsw.controller.enums.GameState;
import it.polimi.ingsw.controller.enums.PlayerState;
import it.polimi.ingsw.exceptions.IllegalActionException;
import it.polimi.ingsw.exceptions.NotAvailableNicknameException;
import it.polimi.ingsw.network.messages.fromClient.*;
import it.polimi.ingsw.network.messages.sendToClient.ResponseMessage;
import it.polimi.ingsw.network.messages.sendToClient.ResponseType;
import it.polimi.ingsw.view.readOnlyModel.Player;

import java.io.*;
import java.net.Socket;
import java.net.SocketException;
import java.util.Scanner;
import java.util.Timer;
import java.util.TimerTask;

public class ClientHandler implements Runnable {
    private String nickname;
    private PlayerState state;
    private final Socket socket;
    private final BufferedReader in;
    private final PrintWriter out;
    private GameController game;
    private PlayerState playerSate;
    private final Gson gson;

    /**
     * used only for test purpose
     */
    @Deprecated
    public ClientHandler(Socket socket, BufferedReader in, PrintWriter out) {
        this.socket = socket;
        this.in = in;
        this.out = out;
        this.state = PlayerState.WAITING4NAME;
        this.gson = new Gson();
    }

    public ClientHandler(Socket socket) throws IOException {
        this.socket = socket;
        this.in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        this.out = new PrintWriter(socket.getOutputStream(), true);
        this.state = PlayerState.WAITING4NAME;
        this.gson = new Gson();
    }

    /**
     * Creates a new thread for the communication with the client. This thread receives the message from the client,
     * parses the message in the Command class and reads the name of the command; based on this name it parses
     * the parameters of the command in the correct class and calls the methods of game to do the action
     */
    @Override
    public void run() {
        Command command;

        Timer pingTimer = new Timer();
        //Creates a timer that pings the client every 5 sec
        pingTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                try {
                    pingClient(socket);
                } catch (SocketException e) {
                    //e.printStackTrace();
                    setState(PlayerState.DISCONNECTED);
                }
            }
        }, 0, 5000);

        try {
            Scanner in = new Scanner(socket.getInputStream());

            String line = in.nextLine();
            while (!line.equals("quit")) {
                if (state == PlayerState.PLAYING) {
                    command = gson.fromJson(line, Command.class);
                    switch (command.getCmd()) {

                    case "login":
                        LoginMessage loginMessage = gson.fromJson(command.getParameters(), LoginMessage.class);
                        this.nickname = loginMessage.getNickName();
                        this.game = GamesManagerSingleton.getInstance().joinOrCreateNewGame(this);
                        if (this.game == null)
                            this.send("You are creating a game! Tell me how many players you want in this game!");
                        else if (this.game.getState() == GameState.INSESSION || this.game.getState() == GameState.STARTED) {
                            //TODO: we cannot still discern between whether this was the last player added or they had been added back in the game because they lost their connection
                            //Ho aggiunto l'uguaglianza con started (che nella mia stesa è quanto il game ha raggiunto tutti i giocatori e sta aspettando che anche mandino le leader cards
                            //da scartare ed etc. all'inizio che è != da in session che quando il meccanismo a turni è in atto
                            this.send("You are back in the game!");
                            ResponseMessage responseMessage = new ResponseMessage(ResponseType.UPDATE, gson.toJson(this.game.getWholeUpdateToClient()));
                            this.send(gson.toJson(responseMessage));
                        } else {
                            this.send("You are in Game! You'll soon start play with others!");
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
                            BuyFromMarketMessage buyFromMarket = gson.fromJson(command.getParameters(), BuyFromMarketMessage.class);
                            game.buyFromMarket(buyFromMarket, this);
                            break;

                        case "getCardCost":
                            GetFromMatrixMessage cardCost = gson.fromJson(command.getParameters(), GetFromMatrixMessage.class);
                            game.getCardCost(cardCost, this);
                            break;

                        case "buyDevCard":
                            BuyDevCardMessage buyDevCard = gson.fromJson(command.getParameters(), BuyDevCardMessage.class);
                            game.buyDevCard(buyDevCard, this);
                            break;

                        case "getProductionCost":
                            GetProductionCostMessage productionCost = gson.fromJson(command.getParameters(), GetProductionCostMessage.class);
                            game.getProductionCost(productionCost, this);
                            break;

                        case "activateProductionMesssage":
                            ActivateProductionMessage activateProduction = gson.fromJson(command.getParameters(), ActivateProductionMessage.class);
                            game.activateProduction(activateProduction, this);
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
                            LeaderMessage discardLeader = gson.fromJson(command.getParameters(), LeaderMessage.class);
                            game.discardLeader(discardLeader, this);
                            break;

                        case "ActivateLeader":
                            LeaderMessage activateLeader = gson.fromJson(command.getParameters(), LeaderMessage.class);
                            game.activateLeader(activateLeader, this);
                            break;

                        case "endTurn":

                            break;

                    }
                } else {
                    this.send(gson.toJson(new ResponseMessage(ResponseType.ERROR, "Wait your turn to do the action")));
                }
                line = in.nextLine();
            }
            //Close stream and socket
            in.close();
            out.close();
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
            this.send(gson.toJson(new ResponseMessage(ResponseType.ERROR, "An error occurred (IOException)")));
        } catch (IllegalActionException | IllegalArgumentException e) {
            send(gson.toJson(new ResponseMessage(ResponseType.ERROR, e.getMessage())));
        } catch (InterruptedException e) {
            e.printStackTrace();
            this.send(gson.toJson(new ResponseMessage(ResponseType.ERROR, "An error occurred (InterruptedException)")));
        } catch (NotAvailableNicknameException e) {
            this.send(gson.toJson(new ResponseMessage(ResponseType.ERROR, "This nickname isn't available!")));
        }
    }

    public synchronized void send(String message) {
        out.println(message);
        out.flush();
    }

    private void pingClient(Socket socket) throws SocketException {
        //TODO: da testare
        this.send(gson.toJson(new ResponseMessage(ResponseType.PING, "Ping")));
        socket.setSoTimeout(2000);
        try {
            in.readLine();
            if (state == PlayerState.DISCONNECTED) {
                switch (game.getState()) {
                    case LASTTURN:
                        state = PlayerState.WAITING4LASTTURN; //waiting and not playing4lastturn because we assume that the turn of a disconnected player is skipped
                        break;
                    case INSESSION:
                        state = PlayerState.WAITING4TURN;
                        break;
                    case WAITING4PLAYERS:
                        state = PlayerState.WAITINGGAMESTART;
                        break;
                    case CONFIGURING:
                        state = PlayerState.WAITING4NAME;
                        break;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            //TODO: CHE FARE?
        }
    }


    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public void setState(PlayerState state) {
        this.state = state;
    }

    public void setGame(GameController game) {
        this.game = game;
    }

    public void setPlayerSate(PlayerState playerSate) {
        this.playerSate = playerSate;
    }

    public PlayerState getState() {
        return state;
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

    public PlayerState getPlayerSate() {
        return playerSate;
    }

}
