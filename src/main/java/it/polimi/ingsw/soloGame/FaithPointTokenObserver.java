package it.polimi.ingsw.soloGame;

import it.polimi.ingsw.FaithTrack.FaithLevelBasic;
import it.polimi.ingsw.FaithTrack.FaithTrack;
import it.polimi.ingsw.FaithTrack.ReportNum;
import it.polimi.ingsw.FaithTrack.ReportNumOrder;
import it.polimi.ingsw.Interfaces.Observer;
import it.polimi.ingsw.exceptions.LastVaticanReportException;

public class FaithPointTokenObserver implements Observer {
    FaithPointToken faithPointToken;
    FaithLevelBasic lorenzosTrack;
    SoloActionDeck soloActionDeck;

    public FaithPointTokenObserver(){
        FaithTrack faithTrack = FaithTrack.instance(ReportNumOrder.instance());
        lorenzosTrack = new FaithLevelBasic(faithTrack);
    }

    @Override
    public String update() {
        int steps;

        steps = faithPointToken.getFaithPoints();
        try {
            lorenzosTrack.moveFaithMarker(steps);
        } catch (LastVaticanReportException e) {
            e.printStackTrace();
            return null; //TODO: dovrebbe essere false per dire che non è andato a buon fine
        }
        if(faithPointToken.isShuffleToken())
            soloActionDeck.shuffle();
        return null; //TODO: dovrebbe essere TRUE per dire che non è andato a buon fine
    }

    //Da togliere in futuro
    @Override
    public boolean update(boolean tmp, ReportNum reportNum) {
        return false;
    }
}
