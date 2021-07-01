package it.polimi.ingsw.model.faithTrack;

import it.polimi.ingsw.exceptions.IllegalActionException;
import it.polimi.ingsw.model.Interfaces.Observer;
import it.polimi.ingsw.model.board.MainBoard;
//import sun.tools.jar.Main;

/**
 * This Observer observes the FaithTrack and is notified any time a Vatican Report occurs.
 */
public class PopeCellObserver implements Observer {
    private MainBoard mainBoard;

    /**
     * Constructs a PopeCellObserver which observes the FaithTrack of the specified MainBoard
     * @param mainBoard the MainBoard whose FaithTrack this PopeCellObserver observes
     */
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

    /**
     * Sets the MainBoard this Observer observes
     * @param mainBoard the MainBoard this Observer observes
     */
    public void setMainBoard(MainBoard mainBoard) {
        this.mainBoard = mainBoard;
    }

    //This method is only used for testing purposes
    public MainBoard getMainBoard() {
        return mainBoard;
    }

}
