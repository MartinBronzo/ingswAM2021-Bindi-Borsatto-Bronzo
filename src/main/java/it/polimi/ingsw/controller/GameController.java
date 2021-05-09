package it.polimi.ingsw.controller;

import it.polimi.ingsw.controller.enums.GameState;
import it.polimi.ingsw.model.PlayerBoard;

import java.util.LinkedHashMap;
import java.util.Set;

public class GameController {

    private LinkedHashMap<ClientHandler, PlayerBoard> players;
    private ClientHandler actualActive;
    private int nDesiredPlayers;

    public ClientHandler getActualActive() {
        return actualActive;
    }

    public Set<ClientHandler> getClients(){
        return players.keySet();
    }


    //change the client with the client in game which has the same name
    public boolean substitutePlayer(ClientHandler client) {
        return true;
    }

    public GameState getState() {
        return GameState.ENDED;
    }

    public boolean addClient(ClientHandler client) {
        return true;
    }

    public int getNumberOfDesiredPlayers() {
        return nDesiredPlayers;
    }

    public void setState(GameState configuring) {
    }

    public void setNumberOfPlayers(int numberOfPlayers) {
        this.nDesiredPlayers=numberOfPlayers;
    }
}
