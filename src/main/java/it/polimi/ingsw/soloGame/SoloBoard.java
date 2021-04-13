package it.polimi.ingsw.soloGame;

import it.polimi.ingsw.DevCards.DevCard;
import it.polimi.ingsw.DevCards.DevCardColour;
import it.polimi.ingsw.DevCards.DevGrid;
import it.polimi.ingsw.FaithTrack.FaithLevelBasic;
import it.polimi.ingsw.MainBoard;
import it.polimi.ingsw.exceptions.EmptyDeckException;
import it.polimi.ingsw.exceptions.LastVaticanReportException;

/**
 * This class represents the SoloBoard, which is the container of all the elements of the game
 */

public class SoloBoard extends MainBoard {
    private final FaithLevelBasic lorenzosTrack;
    private final SoloActionDeck tokenDeck;

    public SoloBoard(DevGrid devGrid, FaithLevelBasic lorenzosTrack, SoloActionDeck tokenDeck){
        super(devGrid);
        this.lorenzosTrack = lorenzosTrack;
        this.tokenDeck = tokenDeck;
    }

    /**
     * Moves the lorenzo's faith point marker ahead of "steps" steps in his faith track
     * @param steps the steps to move lorenzo's faith point marker
     * @return true if the action is performed without errors
     */
    //TODO: Decide how to handle the lastVaticanReport exception
    public boolean moveLorenzosFaith(int steps) {
        try {
            lorenzosTrack.moveFaithMarker(steps);
        }
        catch (LastVaticanReportException e){
            e.printStackTrace();
            //TODO: DO SOMETHING
        }
        return true;
    }

    /**
     * Shuffles the token deck
     * @return true if the action is performed without errors
     */
    public boolean shuffleTokenDeck(){
        tokenDeck.shuffle();
        return true;
    }

    /**
     * Discards "numCards" DevCards with color "colour" from the devGrid
     * @param colour the color of the card to discard
     * @param numCards the number of cards to discard
     * @return true if the action is performed without errors
     */
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

    /**
     * Returns the position of the lorenzo's Faith point marker in his Faith Treack
     * @return the position of the lorenzo's Faith point marker in his Faith Treack
     */
    public int getFaithTrackPosition(){
        return lorenzosTrack.getPosition();
    }

}
