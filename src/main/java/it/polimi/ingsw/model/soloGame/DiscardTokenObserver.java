package it.polimi.ingsw.model.soloGame;

import it.polimi.ingsw.exceptions.EmptyDevColumnException;
import it.polimi.ingsw.model.devCards.DevCardColour;

/**
 * This class is the observer of discardTokens that applies the effect when notified
 */

public class DiscardTokenObserver {
    private final SoloBoard soloBoard;

    /**
     * Constructs a DiscardTokenObserver which observes the DiscardTokens contained in the specified SoloBoard
     * @param soloBoard the SoloBoard whose DiscardTokens this observer observes
     */
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
        if (soloBoard.isDevCardColumnEmpty(colour)) {
            System.out.println("Empty column");
            throw new EmptyDevColumnException("Empty column");
        }

        return true;
    }
}
