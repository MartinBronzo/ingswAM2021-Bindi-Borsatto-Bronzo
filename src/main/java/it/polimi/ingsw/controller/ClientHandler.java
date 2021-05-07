package it.polimi.ingsw.controller;

import com.google.gson.Gson;
import it.polimi.ingsw.controller.enums.PlayerState;
import it.polimi.ingsw.network.messages.*;

import java.io.*;
import java.net.Socket;
import java.util.Scanner;

public class ClientHandler implements Runnable {
    //private String nickname;
    private PlayerState state;
    private final Socket socket;
    private final BufferedReader in;
    private final PrintWriter out;
    private GameController game;


    public ClientHandler(Socket socket, BufferedReader in, PrintWriter out) {
        this.socket = socket;
        this.in = in;
        this.out = out;
        this.state = PlayerState.WAITING4NAME;
    }

    public ClientHandler(Socket socket) throws IOException {
        this.socket = socket;
        this.in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        this.out = new PrintWriter(socket.getOutputStream(), true);
        this.state = PlayerState.WAITING4NAME;
    }


    /**
     * When an object implementing interface {@code Runnable} is used
     * to create a thread, starting the thread causes the object's
     * {@code run} method to be called in that separately executing
     * thread.
     * <p>
     * The general contract of the method {@code run} is that it may
     * take any action whatsoever.
     *
     * @see Thread#run()
     */
    @Override
    public void run() {
        Gson gson = new Gson();
        Command command;
        try {
            Scanner in = new Scanner(socket.getInputStream());

            String line = in.nextLine();
            while (!line.equals("quit")) {

                out.println("Received: " + line);

                command = gson.fromJson(line, Command.class);
                switch (command.getCmd()) {

                    case "getResourcesFromMarket":
                        GetFromMatrixMessage resFromMkt = gson.fromJson(command.getParameters(), GetFromMatrixMessage.class);
                        //game.getResFromMkt(resFromMkt);
                        break;
                    case "buyFromMarket":
                        BuyFromMarketMessage buyFromMarket = gson.fromJson(command.getParameters(), BuyFromMarketMessage.class);
                        //game.buyFromMarket(buyFromMarket));
                        break;

                    case "getCardCost":
                        GetFromMatrixMessage cardCost = gson.fromJson(command.getParameters(), GetFromMatrixMessage.class);
                        //game.getCardCost(cardCost);
                        break;

                    case "buyDevCard":
                        BuyDevCardMessage buyDevCard = gson.fromJson(command.getParameters(), BuyDevCardMessage.class);
                        //game.buyDevCard(buyDevCard);
                        break;

                    case "getProductionCost":
                        GetProductionCostMessage productionCost = gson.fromJson(command.getParameters(), GetProductionCostMessage.class);
                        //game.getProductionCost(productionCost);
                        break;

                    case "activateProductionMesssage":
                        ActivateProductionMessage activateProduction = gson.fromJson(command.getParameters(), ActivateProductionMessage.class);
                        //game.activateProduction(activateProduction);
                        break;

                    case "discardLeaderBeginning":
                        DiscardLeaderCardBeginningMessage discardLeaderCardBeginning = gson.fromJson(command.getParameters(), DiscardLeaderCardBeginningMessage.class);
                        //game.discardLeaderCardBeginning(discardLeaderCardBeginning);
                        break;

                    case "moveBetweenShelves":
                        MoveBetweenShelvesMessage moveBetweenShelves = gson.fromJson(command.getParameters(), MoveBetweenShelvesMessage.class);
                        //game.moveBetweenShelves(moveBetweenShelves);
                        break;

                    case "moveLeaderToShelf":
                        MoveLeaderToShelfMessage moveLeaderToShelf = gson.fromJson(command.getParameters(), MoveLeaderToShelfMessage.class);
                        //game.moveLeaderToShelf(moveLeaderToShelf);
                        break;

                    case "moveShelfToLeader":
                        MoveShelfToLeaderMessage moveShelfToLeader = gson.fromJson(command.getParameters(), MoveShelfToLeaderMessage.class);
                        //game.moveShelfToLeader(moveShelfToLeader);
                        break;

                    case "discardLeader":
                        LeaderMessage discardLeader = gson.fromJson(command.getParameters(), LeaderMessage.class);
                        //game.discardLeader(discardLeader);
                        break;

                    case "ActivateLeader":
                        LeaderMessage activateLeader = gson.fromJson(command.getParameters(), LeaderMessage.class);
                        //game.activateLeader(activateLeader);
                        break;

                    case "endTurn":

                        break;

                }
                out.flush();
                line = in.nextLine();
            }

            //Close stream and socket
            in.close();
            out.close();
            socket.close();
        } catch (IOException e) {
            System.err.println(e.getMessage());
        }

    }

    /*public void setNickname(String nickname) {
        this.nickname = nickname;
    }*/

    public void setState(PlayerState state) {
        this.state = state;
    }

    public void setGame(GameController game) {
        this.game = game;
    }

    /*public String getNickname() {
        return nickname;
    }*/

    public PlayerState getState() {
        return state;
    }

    public BufferedReader getIn() {
        return in;
    }

    public PrintWriter getOut() {
        return out;
    }
}
