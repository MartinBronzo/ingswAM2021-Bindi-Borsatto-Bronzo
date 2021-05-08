package it.polimi.ingsw.controller;

import it.polimi.ingsw.model.MainBoard;
import it.polimi.ingsw.model.PlayerBoard;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class GameController {
    private ClientHandler activePlayer;
    private MainBoard mainBoard;
    private Map<ClientHandler, PlayerBoard> players;

    /*
    ###########################################################################################################
     GENERAL GETTERS
    ###########################################################################################################
     */

    public List<ClientHandler> getPlayersList(){
        List<ClientHandler> result = new ArrayList<>();
        for (Map.Entry<ClientHandler, PlayerBoard> entry : players.entrySet())
            result.add(entry.getKey());
        return result;
    }




}
