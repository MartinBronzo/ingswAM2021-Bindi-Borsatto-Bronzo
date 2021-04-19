package it.polimi.ingsw.soloGame;

import it.polimi.ingsw.DevCards.DevCardColour;
import it.polimi.ingsw.exceptions.EmptyDevColumnException;

/**
 * This class is the observer of discardTokens that applies the effect when notified
 */

public class DiscardTokenObserver{
    private final SoloBoard soloBoard;

    public DiscardTokenObserver(SoloBoard soloBoard) {
        this.soloBoard = soloBoard;
    }

    /**
     * Discards the DevCards with the characteristics described in the token
     *
     * @param token the token that activates the effect
     * @return true if the action is performed without errors
     * @throws EmptyDevColumnException if an entire column of devGrid is empty
     */
    public boolean update(Object token) throws EmptyDevColumnException {
        DiscardToken soloToken;
        DevCardColour colour;
        int numCards;

        soloToken = (DiscardToken) token;

        colour = soloToken.getCardColour();
        numCards = soloToken.getNumCards();

        soloBoard.discardDevCards(colour, numCards);
        if(soloBoard.isDevColumnEmpty(colour))
            throw new EmptyDevColumnException("Empty column");

        return true;
    }
}
