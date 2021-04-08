package it.polimi.ingsw.soloGame;

import it.polimi.ingsw.DevCards.DevCardColour;
import it.polimi.ingsw.Interfaces.Observer;
import it.polimi.ingsw.Interfaces.Subject;

import java.util.ArrayList;
import java.util.List;

public class DiscardToken extends SoloActionToken implements Subject {
    private final List<Observer> observersList;
    private final DevCardColour cardColour;
    private final int numCards;

    public DiscardToken(DevCardColour devCardColour, int numCards){
        observersList = new ArrayList<>();
        this.cardColour = devCardColour;
        this.numCards = numCards;
    }

    @Override
    public boolean playEffect() {
        notifyObservers();
        return true;
    }

    @Override
    public boolean attach(Observer observer) {
        return observersList.add(observer);
    }

    @Override
    public boolean detach(Observer observer) {
        return observersList.remove(observer);
    }

    @Override
    public boolean notifyObservers() {
        for(Observer o: observersList)
            o.update();
        return true;
    }



    public DevCardColour getCardColour(){
        return cardColour;
    }

    public int getNumCards(){
        return numCards;
    }

}
