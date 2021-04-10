package it.polimi.ingsw.soloGame;

import it.polimi.ingsw.FaithTrack.ReportNum;
import it.polimi.ingsw.Interfaces.Observer;

public class FaithPointTokenObserver implements Observer {
    SoloBoard soloBoard; //questa implementa i metodi per eseguire le azioni del token

    @Override
    public boolean update(Object token) {
        FaithPointToken faithPointToken;
        int steps;

        faithPointToken = (FaithPointToken) token;
        steps = faithPointToken.getFaithPoints();

        soloBoard.moveLorenzosFaith(steps);
        if(faithPointToken.isShuffleToken())
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
