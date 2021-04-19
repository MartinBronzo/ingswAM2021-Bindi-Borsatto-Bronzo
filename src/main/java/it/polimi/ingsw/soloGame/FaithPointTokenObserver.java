package it.polimi.ingsw.soloGame;

import it.polimi.ingsw.exceptions.LastVaticanReportException;

/**
 * This class is the observer of FaithPointToken that applies the effect when notified
 */

public class FaithPointTokenObserver{
    SoloBoard soloBoard;

    public FaithPointTokenObserver(SoloBoard soloBoard) {
        this.soloBoard = soloBoard;
    }

    /**
     * Moves the faith point marker ahead of the number of step contained in the token. Shuffles the deck if the token is a ShuffleToken
     *
     * @param token the token that activates the effect
     * @return true if the action is performed without errors
     * @throws LastVaticanReportException if the last vatican report is reached
     */
    public boolean update(Object token) throws LastVaticanReportException {
        FaithPointToken faithPointToken;
        int steps;

        faithPointToken = (FaithPointToken) token;
        steps = faithPointToken.getFaithPoints();

        soloBoard.moveLorenzosFaith(steps);
        if (faithPointToken.isShuffleToken())
            soloBoard.shuffleTokenDeck();

        return true;
    }
}
