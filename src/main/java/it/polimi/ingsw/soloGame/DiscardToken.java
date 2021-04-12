package it.polimi.ingsw.soloGame;

import it.polimi.ingsw.DevCards.DevCardColour;
import it.polimi.ingsw.Interfaces.Observer;
import it.polimi.ingsw.Interfaces.Subject;

import java.util.ArrayList;
import java.util.List;

/**
 * This class represents the discardToken, that can be drew from the deck of token when you're playing a soloGame.
 * This token makes you discard numCards Development card with colour cardColour from the DevGrid.
 * It calls an observer to make the SoloTable discard the cards
 */

public class DiscardToken extends SoloActionToken implements Subject {
    private final List<Observer> observersList;
    private final DevCardColour cardColour;
    private final int numCards;

    public DiscardToken(DevCardColour devCardColour, int numCards){
        if(numCards < 0)
            throw new IllegalArgumentException("Can't discard negative cards");
        observersList = new ArrayList<>();
        this.cardColour = devCardColour;
        this.numCards = numCards;
    }

    /**
     * Activates the effect of the token notifying the observers to discard DevCards from DevGrid
     * @return true if the action is performed without errors
     */
    @Override
    public boolean playEffect() {
        notifyObservers();
        return true;
    }

    /**
     * Attaches an observer to the token, waiting to be notified
     * @param observer the Observer
     * @return true if the observer is added wothout errors
     */
    @Override
    public boolean attach(Observer observer) {
        return observersList.add(observer);
    }

    /**
     * Detaches the observer from the token
     * @param observer the Observer
     * @return true if the observer is removed wothout errors
     */
    @Override
    public boolean detach(Observer observer) {
        return observersList.remove(observer);
    }

    /**
     * Notifies all the observers that they have to apply the effect of the token
     * @return true if the action is performed without errors
     */
    @Override
    public boolean notifyObservers() {
        for(Observer o: observersList)
            o.update(this);
        return true;
    }


    /**
     * Returns the color of the DevCard to discard
     * @return the color of the DevCard to discard
     */
    public DevCardColour getCardColour(){
        return cardColour;
    }

    /**
     * Returns the number of DevCards to discard
     * @return the number of DevCards to discard
     */
    public int getNumCards(){
        return numCards;
    }

}
