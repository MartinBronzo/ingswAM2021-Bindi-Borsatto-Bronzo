package it.polimi.ingsw.soloGame;

import it.polimi.ingsw.FaithTrack.ReportNum;
import it.polimi.ingsw.Interfaces.Observer;

/**
 * This class is the observer of FaithPointToken that applies the effect when notified
 */

public class FaithPointTokenObserver implements Observer {
    SoloBoard soloBoard; //questa implementa i metodi per eseguire le azioni del token

    public FaithPointTokenObserver(SoloBoard soloBoard) {
        this.soloBoard = soloBoard;
    }

    /**
     * Moves the faith point marker ahead of the number of step contained in the token. Shuffles the deck if the token is a ShuffleToken
     *
     * @param token the token that activates the effect
     * @return true if the action is performed without errors
     */
    @Override
    public boolean update(Object token) {
        FaithPointToken faithPointToken;
        int steps;

        faithPointToken = (FaithPointToken) token;
        steps = faithPointToken.getFaithPoints();

        soloBoard.moveLorenzosFaith(steps);
        if (faithPointToken.isShuffleToken())
            soloBoard.shuffleTokenDeck();

        return true;
    }

    @Override
    public String update() {
        return null;
    }

    //Da togliere in futuro
    @Override
    public boolean update(boolean tmp, ReportNum reportNum) {
        return false;
    }

}
