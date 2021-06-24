package it.polimi.ingsw.model.faithTrack;

import it.polimi.ingsw.exceptions.IllegalActionException;
import it.polimi.ingsw.model.Interfaces.Observer;
import it.polimi.ingsw.model.board.MainBoard;
//import sun.tools.jar.Main;

public class PopeCellObserver implements Observer {
    MainBoard mainBoard;

    public PopeCellObserver(MainBoard mainBoard) {
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

    public void setMainBoard(MainBoard mainBoard) {
        this.mainBoard = mainBoard;
    }

    //This method is only used for testing purposes
    public MainBoard getMainBoard() {
        return mainBoard;
    }

}
