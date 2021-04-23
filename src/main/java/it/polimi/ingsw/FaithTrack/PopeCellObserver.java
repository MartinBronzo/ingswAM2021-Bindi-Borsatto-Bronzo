package it.polimi.ingsw.FaithTrack;

import it.polimi.ingsw.Interfaces.Observer;
import it.polimi.ingsw.MainBoard;
import it.polimi.ingsw.exceptions.IllegalActionException;
//import sun.tools.jar.Main;

public class PopeCellObserver implements Observer {
    MainBoard mainBoard;

    public PopeCellObserver(MainBoard mainBoard) {
        //TODO: chiamare costruttore copia della mainboard
        this.mainBoard = mainBoard;
    }

    @Override
    public String update() {
        return null;
    }

    @Override
    public boolean update(Object object) {
        //O chiama la mainBoard così da poter ottenere tutte le playerBoard o chiama lui direttamente le playerBoard
        try {
            //System.out.println("HELLO");
            this.mainBoard.dealWithVaticanReportAllPlayers((ReportNum) object);
        } catch (IllegalActionException e) {
            e.printStackTrace();
        }
        return true;
    }
}
