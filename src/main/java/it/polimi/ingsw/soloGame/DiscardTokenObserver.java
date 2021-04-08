package it.polimi.ingsw.soloGame;

import it.polimi.ingsw.DevCards.DevCard;
import it.polimi.ingsw.DevCards.DevCardColour;
import it.polimi.ingsw.DevCards.DevGrid;
import it.polimi.ingsw.FaithTrack.ReportNum;
import it.polimi.ingsw.Interfaces.Observer;
import it.polimi.ingsw.exceptions.EmptyDeckException;

public class DiscardTokenObserver implements Observer {
    DiscardToken soloToken;
    DevGrid devGrid;

    public DiscardTokenObserver(DevGrid devGrid, DiscardToken token){
        this.soloToken = token;
        this.devGrid = devGrid;
    }

    @Override
    public String update() {
        DevCardColour colour;
        int numCards, level;
        DevCard card;

        level = 1;
        colour = soloToken.getCardColour();
        numCards = soloToken.getNumCards();

        for(int i = 0; i < numCards; i++){
            card = devGrid.getDevCardFromDeck(level, colour);
            while(card == null && level < 4){
                level++;
                card = devGrid.getDevCardFromDeck(level, colour);
            }
            if(level == 4)
                return null;  //TODO: dovrebbe essere false per dire che non è andato a buon fine

            try {
                devGrid.drawDevCardFromDeck(level, colour);
            } catch (EmptyDeckException e) {
                e.printStackTrace();
                return null; //TODO: dovrebbe essere false per dire che non è andato a buon fine
            }
        }

        return null; //TODO: dovrebbe essere TRUE per dire che è andato a buon fine
    }

    //questo metodo sarà eliminato
    @Override
    public boolean update(boolean tmp, ReportNum reportNum) {
        return false;
    }
}
