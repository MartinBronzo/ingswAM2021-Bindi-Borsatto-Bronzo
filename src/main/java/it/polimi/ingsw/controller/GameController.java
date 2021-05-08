package it.polimi.ingsw.controller;

import it.polimi.ingsw.model.MainBoard;
import it.polimi.ingsw.model.PlayerBoard;
import java.util.ArrayList;
import java.util.List;


public class GameController {
    private ClientHandler activePlayer;
    private MainBoard mainBoard;
    private List<Pair<ClientHandler, PlayerBoard>> players;
    private int numberOfPlayers;
    private int maxPlayersNum;

    /**
     * This class represents the relationship between the ClientHandler of the player and the their PlayerBoard.
     * @param <clientHandler> the ClientHandler of the player
     * @param <playerBoard> the PlayerBoard of the player
     */
    class Pair<clientHandler, playerBoard> {
        private clientHandler l;
        private playerBoard r;

        public Pair(clientHandler l, playerBoard r) {
            this.l = l;
            this.r = r;
        }

        public clientHandler getKey() {
            return l;
        }

        public playerBoard getValue() {
            return r;
        }

        public void setL(clientHandler l) {
            this.l = l;
        }

        public void setR(playerBoard r) {
            this.r = r;
        }
    }

    /*
    ###########################################################################################################
     GENERAL SETTERS
    ###########################################################################################################
     */

    /**
     * Constructs an empty GameController, without anything set yet.
     */
    public GameController(){
        this.activePlayer = null;
        this.mainBoard = null;
        this.players = new ArrayList<>();
        this.numberOfPlayers = - 1;
        this.maxPlayersNum = 4;
    }

    /**
     * Creates the MainBoard for this game with as many players as the parameter specifies
     * @param numberOfPlayers the number of players for this game
     */
    public void startMainBoard(int numberOfPlayers) {
        if(numberOfPlayers <= 0 || numberOfPlayers > this.maxPlayersNum)
            throw new IllegalArgumentException("The number of players must be a number between 1 and " + this.maxPlayersNum + " included!");
        if(this.numberOfPlayers > 0)
            return;
        try {
            this.mainBoard = new MainBoard(numberOfPlayers);
        } catch (Exception e){
            //System.out.println("Can't create the MainBoard because there are problems with the configuration files!");
            e.printStackTrace();
        }
        this.numberOfPlayers = numberOfPlayers;
    }

    /**
     * Adds a player represented by the specified ClientHandler to the game if the player hasn't been added, yet, and if the game hasn't reach its maximum
     * capacity, yet.
     * @param player the ClientHandler of the player to be added at the game
     */
    public void setPlayer(ClientHandler player) {
        //We can't add more players than the one given by the numberOfPlayers number
        if(this.players.size() == this.numberOfPlayers)
            return;
        //We can't add an already added player
        //if(this.findClientHandler(player))
        if(this.getPlayerBoardOfPlayer(player) != null)
            return;
        PlayerBoard playerBoard = this.mainBoard.getPlayerBoard(this.players.size());
        players.add(new Pair<>(player, playerBoard));
    }

    /*
    ###########################################################################################################
     GENERAL GETTERS
    ###########################################################################################################
     */

    /**
     * Returns a list of all the players that are added to the game in the moment this method is invoked
     * @return a list of the player's ClientHandler added to the game
     */
    public List<ClientHandler> getPlayersList(){
        List<ClientHandler> result = new ArrayList<>();
        for (Pair<ClientHandler, PlayerBoard> entry : players)
            result.add(entry.getKey());
        return result;
    }

    /**
     * Returns the number of players this game can hold
     * @return the number of players for this game
     */
    public int getNumberOfPlayers() {
        return numberOfPlayers;
    }


    /**
     * Returns the reference to the player's PlayerBoard. The player is determined by the specified ClientHandler
     * @param clientHandler the ClientHandler of the player
     * @return the PlayerBoard of the player
     */
    private PlayerBoard getPlayerBoardOfPlayer(ClientHandler clientHandler){
        for(Pair<ClientHandler, PlayerBoard> e: players)
            if(e.getKey() == clientHandler)
                return e.getValue();
        return null;
    }

    /*
    ###########################################################################################################
     TO BE USED....
    ###########################################################################################################
     */

    public ClientHandler getActivePlayer() {
        return activePlayer;
    }

    private boolean findClientHandler(ClientHandler clientHandler){
        for(Pair<ClientHandler, PlayerBoard> e: players)
            if(e.getKey() == clientHandler)
                return true;
        return false;
    }

}
