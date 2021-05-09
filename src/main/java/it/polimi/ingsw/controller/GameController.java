package it.polimi.ingsw.controller;

import it.polimi.ingsw.controller.enums.GameState;
import it.polimi.ingsw.model.PlayerBoard;

import java.util.ArrayList;
import java.util.List;

public class GameController {

    private List<Pair<ClientHandler, PlayerBoard>> players;
    private ClientHandler actualActive;
    private int numberOfPlayers;

    public ClientHandler getActualActive() {
        return actualActive;
    }

    public List<ClientHandler> getPlayersList(){
        return new ArrayList<>();
    }


    //change the client with the client in game which has the same name
    public boolean substitutePlayer(ClientHandler client) {
        return true;
    }

    public GameState getState() {
        return GameState.ENDED;
    }

    public boolean setPlayer(ClientHandler player) {
        return true;
    }

    public int getNumberOfPlayers() {
        return numberOfPlayers;
    }

    public void setState(GameState configuring) {
    }

    public void startMainBoard(int numberOfPlayers) {
        this.numberOfPlayers =numberOfPlayers;
    }

    private class Pair<T, T1> {
    }
}
