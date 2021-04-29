package it.polimi.ingsw.model.soloGame;

import it.polimi.ingsw.exceptions.LastVaticanReportException;

import java.util.ArrayList;
import java.util.List;

/**
 * This class represents the FaithPointToken, that can be drew from the deck of token when you're playing a soloGame.
 * This token makes you move your faith point marker ahead of faithPoints steps
 * It calls an observer to make the SoloTable move the faith point marker
 */
public class FaithPointToken extends SoloActionToken {
    private final List<FaithPointTokenObserver> observersList;
    private final int faithPoints;
    protected boolean shuffleToken;

    public FaithPointToken(int faithPoints) {
        if (faithPoints < 0)
            throw new IllegalArgumentException("Can't have negative faith points");
        observersList = new ArrayList<>();
        this.faithPoints = faithPoints;
        shuffleToken = false;
    }

    /**
     * Activates the effect of the token notifying the observers to move the faith point marker
     *
     * @return true if the action is performed without errors
     * @throws LastVaticanReportException if the last vatican report is reached
     */
    @Override
    public boolean playEffect() throws LastVaticanReportException {
        notifyObservers();
        return true;
    }

    /**
     * Attaches an observer to the token, waiting to be notified
     *
     * @param observer the Observer
     * @return true if the observer is added wothout errors
     */
    public boolean attach(FaithPointTokenObserver observer) {
        return observersList.add(observer);
    }

    /**
     * Detaches the observer from the token
     *
     * @param observer the Observer
     * @return true if the observer is removed wothout errors
     */
    public boolean detach(FaithPointTokenObserver observer) {
        return observersList.remove(observer);
    }

    /**
     * Notifies all the observers that they have to apply the effect of the token
     *
     * @return true if the action is performed without errors
     */
    public boolean notifyObservers() throws LastVaticanReportException {
        for (FaithPointTokenObserver o : observersList)
            o.update(this);
        return true;
    }

    /**
     * Returns the number of faith point of the token
     *
     * @return the number of faith point of the token
     */
    public int getFaithPoints() {
        return faithPoints;
    }

    /**
     * Returns true if the token is a shuffle token, so you have to shuffle the soloActionDeck
     *
     * @return true if the token is a shuffle token, so you have to shuffle the soloActionDeck
     */
    public boolean isShuffleToken() {
        return shuffleToken;
    }
}
