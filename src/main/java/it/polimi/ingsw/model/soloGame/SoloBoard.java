package it.polimi.ingsw.model.soloGame;

import it.polimi.ingsw.exceptions.EmptyDeckException;
import it.polimi.ingsw.exceptions.EmptyDevColumnException;
import it.polimi.ingsw.exceptions.LastVaticanReportException;
import it.polimi.ingsw.model.devCards.DevCard;
import it.polimi.ingsw.model.devCards.DevCardColour;
import it.polimi.ingsw.model.devCards.DevGrid;
import it.polimi.ingsw.model.faithTrack.FaithLevelBasic;
import it.polimi.ingsw.model.board.MainBoard;
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
    private DiscardTokenObserver discardTokenObserver;
    private FaithPointTokenObserver faithPointTokenObserver;

    @Override
    public MainBoard getClone() {
        return new SoloBoard(this);
    }

    @Deprecated
    public SoloBoard(DevGrid devGrid, FaithLevelBasic lorenzosTrack, SoloActionDeck tokenDeck) {
        super(devGrid);
        this.lorenzosTrack = lorenzosTrack;
        this.tokenDeck = tokenDeck;
    }

    /**
     * Constructs a ready-to-be-used SoloBoard by reading the elements of this game from various files
     * @throws ParserConfigurationException if there are problems in the parsing
     * @throws IOException if an IO operations fails
     * @throws SAXException if there is a general SAX error or warning
     */
    public SoloBoard() throws ParserConfigurationException, IOException, SAXException {
        super(1);
        discardTokenObserver = new DiscardTokenObserver(this);
        faithPointTokenObserver = new FaithPointTokenObserver(this);
        this.lorenzosTrack = new FaithLevelBasic(this.faithTrack);
        this.tokenDeck = new SoloActionDeck(new File("SoloTokenConfig.xml"), discardTokenObserver, faithPointTokenObserver);
    }

    /**
     * Constructs a copy of the specified SoloBoard
     * @param original the SoloBoard to be cloned
     */
    public SoloBoard(SoloBoard original) {
        super(original);

        //copy of soloBoard objects
        this.lorenzosTrack = new FaithLevelBasic(original.lorenzosTrack);
        this.tokenDeck = new SoloActionDeck(original.tokenDeck);
        this.discardTokenObserver = new DiscardTokenObserver(this);
        this.faithPointTokenObserver = new FaithPointTokenObserver(this);
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
     *
     * @return true if the action is performed without errors
     * @throws LastVaticanReportException if a vatican report is called during the action
     * @throws EmptyDevColumnException    if an entire column of devGrid is empty
     */
    public SoloActionToken drawSoloToken() throws LastVaticanReportException, EmptyDevColumnException {
        SoloActionToken token = tokenDeck.drawFromDeck();
        token.playEffect();
        return token;
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

    /**
     * Returns if the column of the specified color in devGrid is empty
     *
     * @param colour: the color of the column
     * @return true if the column of the specified color in devGrid is empty
     */
    public boolean isDevCardColumnEmpty(DevCardColour colour) {
        int level = 1;

        while (level <= 3){
            if(devGrid.getDevDeckSize(colour) != 0)
                return false;
            level ++;
        }
        return true;
    }

}
