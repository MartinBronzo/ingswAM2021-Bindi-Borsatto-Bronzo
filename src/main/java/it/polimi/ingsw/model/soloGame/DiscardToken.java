package it.polimi.ingsw.model.soloGame;

import it.polimi.ingsw.exceptions.EmptyDevColumnException;
import it.polimi.ingsw.model.DevCards.DevCardColour;

import java.util.ArrayList;
import java.util.List;

/**
 * This class represents the discardToken, that can be drew from the deck of token when you're playing a soloGame.
 * This token makes you discard numCards Development card with colour cardColour from the DevGrid.
 * It calls an observer to make the SoloTable discard the cards
 */

public class DiscardToken extends SoloActionToken {
    private transient final List<DiscardTokenObserver> observersList;
    private final DevCardColour cardColour;
    private final int numCards;
    private final String name;

    public DiscardToken(DevCardColour devCardColour, int numCards, String name) {
        this.name = name;
        if (numCards < 0)
            throw new IllegalArgumentException("Can't discard negative cards");
        observersList = new ArrayList<>();
        this.cardColour = devCardColour;
        this.numCards = numCards;
    }

    /**
     * Activates the effect of the token notifying the observers to discard DevCards from DevGrid
     *
     * @return true if the action is performed without errors
     * @throws EmptyDevColumnException if an entire column in devGrid is empty
     */
    @Override
    public boolean playEffect() throws EmptyDevColumnException {
        notifyObservers();
        return true;
    }

    @Override
    public String getName() {
        return name;
    }

    /**
     * Attaches an observer to the token, waiting to be notified
     *
     * @param observer the Observer
     * @return true if the observer is added without errors
     */
    public boolean attach(DiscardTokenObserver observer) {
        return observersList.add(observer);
    }

    /**
     * Detaches the observer from the token
     *
     * @param observer the Observer
     * @return true if the observer is removed without errors
     */
    public boolean detach(DiscardTokenObserver observer) {
        return observersList.remove(observer);
    }

    /**
     * Notifies all the observers that they have to apply the effect of the token
     *
     * @return true if the action is performed without errors
     */
    public boolean notifyObservers() throws EmptyDevColumnException {
        for (DiscardTokenObserver o : observersList)
            o.update(this);
        return true;
    }

    /**
     * Returns the color of the DevCard to discard
     *
     * @return the color of the DevCard to discard
     */
    public DevCardColour getCardColour() {
        return cardColour;
    }

    /**
     * Returns the number of DevCards to discard
     *
     * @return the number of DevCards to discard
     */
    public int getNumCards() {
        return numCards;
    }

}
