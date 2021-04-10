package it.polimi.ingsw.soloGame;

import it.polimi.ingsw.DevCards.DevCardColour;
import it.polimi.ingsw.FaithTrack.ReportNum;
import it.polimi.ingsw.Interfaces.Observer;

public class DiscardTokenObserver implements Observer {
    private final SoloBoard soloBoard;

    public DiscardTokenObserver(SoloBoard soloBoard){
        this.soloBoard = soloBoard;
    }

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
