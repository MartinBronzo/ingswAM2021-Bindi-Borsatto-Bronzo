package it.polimi.ingsw.soloGame;

import it.polimi.ingsw.DevCards.DevCard;
import it.polimi.ingsw.DevCards.DevCardColour;
import it.polimi.ingsw.DevCards.DevGrid;
import it.polimi.ingsw.FaithTrack.FaithLevelBasic;
import it.polimi.ingsw.MainBoard;
import it.polimi.ingsw.exceptions.EmptyDeckException;
import it.polimi.ingsw.exceptions.EmptyDevColumnException;
import it.polimi.ingsw.exceptions.LastVaticanReportException;

/**
 * This class represents the SoloBoard, which is the container of all the elements of the game
 */

public class SoloBoard extends MainBoard {
    private final FaithLevelBasic lorenzosTrack;
    private final SoloActionDeck tokenDeck;

    public SoloBoard(DevGrid devGrid, FaithLevelBasic lorenzosTrack, SoloActionDeck tokenDeck) {
        super(devGrid);
        this.lorenzosTrack = lorenzosTrack;
        this.tokenDeck = tokenDeck;
    }

    /**
     * Moves the lorenzo's faith point marker ahead of "steps" steps in his faith track
     *
     * @param steps the steps to move lorenzo's faith point marker
     * @return true if the action is performed without errors
     */
    public boolean moveLorenzosFaith(int steps) throws LastVaticanReportException {
        lorenzosTrack.moveFaithMarker(steps);
        return true;
    }

    /**
     * Shuffles the token deck
     *
     * @return true if the action is performed without errors
     */
    public boolean shuffleTokenDeck() {
        tokenDeck.shuffle();
        return true;
    }

    /**
     * Discards "numCards" DevCards with color "colour" from the devGrid
     *
     * @param colour   the color of the card to discard
     * @param numCards the number of cards to discard
     * @return true if the action is performed without errors
     * @throws EmptyDevColumnException if the entire column of the specified color in devGrid is empty
     */
    public boolean discardDevCards(DevCardColour colour, int numCards) throws EmptyDevColumnException {
        DevCard card;
        int level;

        for (int i = 0; i < numCards; i++) {
            level = 1;
            card = devGrid.getDevCardFromDeck(level, colour);
            while (card == null && level < 4) {
                level++;
                card = devGrid.getDevCardFromDeck(level, colour);
            }

            //redundant check
            /*if (level == 4)
                throw new EmptyDevColumnException("Empty column");*/

            try {
                devGrid.drawDevCardFromDeck(level, colour);
            } catch (EmptyDeckException e) {
                e.printStackTrace();
                throw new EmptyDevColumnException("Empty column");
            }
        }
        return true;
    }

    /**
     * Returns the position of the lorenzo's Faith point marker in his Faith Treack
     *
     * @return the position of the lorenzo's Faith point marker in his Faith Treack
     */
    public int getFaithTrackPosition() {
        return lorenzosTrack.getPosition();
    }

    /**
     * Returns if the column of the specified color in devGrid is empty
     * @param colour: the color of the column
     * @return true if the column of the specified color in devGrid is empty
     */
    public boolean isDevColumnEmpty(DevCardColour colour){
        return devGrid.getDevDeckSize(colour) == 0;
    }

}
