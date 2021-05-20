package it.polimi.ingsw.model.soloGame;

import it.polimi.ingsw.exceptions.EmptyDeckException;
import it.polimi.ingsw.exceptions.EmptyDevColumnException;
import it.polimi.ingsw.exceptions.LastVaticanReportException;
import it.polimi.ingsw.model.DevCards.DevCard;
import it.polimi.ingsw.model.DevCards.DevCardColour;
import it.polimi.ingsw.model.DevCards.DevGrid;
import it.polimi.ingsw.model.FaithTrack.FaithLevelBasic;
import it.polimi.ingsw.model.MainBoard;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;

/**
 * This class represents the SoloBoard, which is the container of all the elements of the game
 */

public class SoloBoard extends MainBoard {
    private final FaithLevelBasic lorenzosTrack;
    private final SoloActionDeck tokenDeck;
    DiscardTokenObserver discardTokenObserver;
    FaithPointTokenObserver faithPointTokenObserver;

    /*@Override
    public MainBoard getClone() {
        return new SoloBoard(this);
    }*/

    @Deprecated
    public SoloBoard(DevGrid devGrid, FaithLevelBasic lorenzosTrack, SoloActionDeck tokenDeck) {
        super(devGrid);
        this.lorenzosTrack = lorenzosTrack;
        this.tokenDeck = tokenDeck;
    }

    public SoloBoard() throws ParserConfigurationException, IOException, SAXException {
        super(1);
        discardTokenObserver = new DiscardTokenObserver(this);
        faithPointTokenObserver = new FaithPointTokenObserver(this);
        this.lorenzosTrack = new FaithLevelBasic(this.faithTrack);
        this.tokenDeck = new SoloActionDeck(new File("SoloTokenConfig.xml"), discardTokenObserver, faithPointTokenObserver);
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
     * Draws the token from the token deck and activates its effect
     * @return true if the action is performed without errors
     * @throws LastVaticanReportException if a vatican report is called during the action
     * @throws EmptyDevColumnException if an entire column of devGrid is empty
     */
    public boolean drawSoloToken() throws LastVaticanReportException, EmptyDevColumnException {
        tokenDeck.drawFromDeck().playEffect();
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
            while (card == null && level < 3) {
                level++;
                card = devGrid.getDevCardFromDeck(level, colour);
            }

            //redundant check
            /*if (level == 4)
                throw new EmptyDevColumnException("Empty column");*/

            try {
                devGrid.drawDevCardFromDeck(level, colour);
            } catch (EmptyDeckException e) {
                //e.printStackTrace();
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
    @Override
    public int getLorenzoFaithTrackPosition() {
        return lorenzosTrack.getPosition();
    }

    /**
     * Returns if the column of the specified color in devGrid is empty
     *
     * @param colour: the color of the column
     * @return true if the column of the specified color in devGrid is empty
     */
    public boolean isDevColumnEmpty(DevCardColour colour) {
        return devGrid.getDevDeckSize(colour) == 0;
    }

}
