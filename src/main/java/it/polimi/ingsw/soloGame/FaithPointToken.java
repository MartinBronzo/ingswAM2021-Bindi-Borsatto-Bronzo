package it.polimi.ingsw.soloGame;

import it.polimi.ingsw.Interfaces.Observer;
import it.polimi.ingsw.Interfaces.Subject;

import java.util.ArrayList;
import java.util.List;

public class FaithPointToken extends SoloActionToken implements Subject {
    private final List<Observer> observersList;
    private final int faithPoints;
    protected boolean shuffleToken;

    public FaithPointToken(int faithPoints){
        if(faithPoints < 0)
            throw new IllegalArgumentException("Can't have negative faith points");
        observersList = new ArrayList<>();
        this.faithPoints = faithPoints;
        shuffleToken = false;
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
            o.update(this);
        return true;
    }

    public int getFaithPoints() {
        return faithPoints;
    }

    public boolean isShuffleToken() {
        return shuffleToken;
    }
}
