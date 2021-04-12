package it.polimi.ingsw.soloGame;

import it.polimi.ingsw.DevCards.DevCardColour;
import it.polimi.ingsw.FaithTrack.ReportNum;
import it.polimi.ingsw.Interfaces.Observer;

/**
 * This class is the observer of discardTokens that applies the effect when notified
 */

public class DiscardTokenObserver implements Observer {
    private final SoloBoard soloBoard;

    public DiscardTokenObserver(SoloBoard soloBoard){
        this.soloBoard = soloBoard;
    }

    /**
     * Discards the DevCards with the characteristics described in the token
     * @param token the token that activates the effect
     * @return true if the action is performed without errors
     */
    @Override
    public boolean update(Object token) {
        DiscardToken soloToken;
        DevCardColour colour;
        int numCards;

        soloToken = (DiscardToken) token;

        colour = soloToken.getCardColour();
        numCards = soloToken.getNumCards();

        soloBoard.discardDevCards(colour, numCards);

        return true;
    }

    @Override
    public String update() {
        return null;
    }

    //questo metodo sarà eliminato
    @Override
    public boolean update(boolean tmp, ReportNum reportNum) {
        return false;
    }
}
