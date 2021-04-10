package it.polimi.ingsw.soloGame;

import it.polimi.ingsw.DevCards.DevCard;
import it.polimi.ingsw.DevCards.DevCardColour;
import it.polimi.ingsw.DevCards.DevGrid;
import it.polimi.ingsw.FaithTrack.FaithLevelBasic;
import it.polimi.ingsw.MainBoard;
import it.polimi.ingsw.Market.Market;
import it.polimi.ingsw.exceptions.EmptyDeckException;
import it.polimi.ingsw.exceptions.LastVaticanReportException;

public class SoloBoard extends MainBoard {
    private FaithLevelBasic lorenzosTrack;
    private SoloActionDeck tokenDeck;

    public SoloBoard(DevGrid devGrid, FaithLevelBasic lorenzosTrack, SoloActionDeck tokenDeck){
        super(devGrid);
        this.lorenzosTrack = lorenzosTrack;
        this.tokenDeck = tokenDeck;

    }

    //TODO: Decide how to handle the lastVaticanReport exception
    public boolean moveLorenzosFaith(int steps) {
        try {
            lorenzosTrack.moveFaithMarker(steps);
        } catch (LastVaticanReportException e) {
            e.printStackTrace();
            //TODO: DO SOMETHING
        }
        return true;
    }

    public boolean shuffleTokenDeck(){
        tokenDeck.shuffle();
        return true;
    }

    public boolean discardDevCards(DevCardColour colour, int numCards){
        DevCard card;
        int level;

        for(int i = 0; i < numCards; i++){
            level = 1;
            card = devGrid.getDevCardFromDeck(level, colour);
            while(card == null && level < 4){
                level++;
                card = devGrid.getDevCardFromDeck(level, colour);
            }
            if(level == 4)
                return false; //TODO: CAN ALSO THROW AN EXCEPTION

            //TODO: THINK ABOUT HOW TO HANDLE THE EXCEPTION
            //The exception is thrown only if the entire column of dev card of that color is already empty
            try {
                devGrid.drawDevCardFromDeck(level, colour);
            } catch (EmptyDeckException e) {
                e.printStackTrace();
                return false;
            }
        }

        return true;
    }

}
