package it.polimi.ingsw.model;

import it.polimi.ingsw.exceptions.EmptyDeckException;
import it.polimi.ingsw.exceptions.IllegalActionException;
import it.polimi.ingsw.exceptions.LastVaticanReportException;
import it.polimi.ingsw.exceptions.NegativeQuantityException;
import it.polimi.ingsw.model.DevCards.DevCard;
import it.polimi.ingsw.model.DevCards.DevCardColour;
import it.polimi.ingsw.model.DevCards.DevGrid;
import it.polimi.ingsw.model.FaithTrack.FaithTrack;
import it.polimi.ingsw.model.FaithTrack.PopeCellObserver;
import it.polimi.ingsw.model.FaithTrack.PopeTile;
import it.polimi.ingsw.model.FaithTrack.ReportNum;
import it.polimi.ingsw.model.Interfaces.Deck;
import it.polimi.ingsw.model.Interfaces.Observer;
import it.polimi.ingsw.model.LeaderCard.LeaderCard;
import it.polimi.ingsw.model.LeaderCard.LeaderCardDeck;
import it.polimi.ingsw.model.LeaderCard.leaderEffects.Effect;
import it.polimi.ingsw.model.Market.Market;
import it.polimi.ingsw.model.marble.Marble;
import it.polimi.ingsw.model.marble.MarbleType;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;
import java.util.*;

public class MainBoard {
    protected FaithTrack faithTrack;
    protected List<List<PopeTile>> popeTiles;
    protected Deck leaderCardsDeck;
    protected DevGrid devGrid;
    protected Market market;
    protected int numberOfPlayers;
    protected List<PlayerBoard> playerBoardsList;
    protected int numberOfLeaderCardsToGive;
    protected int numberOfLeaderCardsToDiscardAtBeginning;
    //other fields...

    protected int[] extraFaithPointsAtBeginning;
    protected int[] extraResourcesAtBeginning;

    protected int stepForEachDiscardedRes;

    protected Observer cellObserver;


    /*
    ###########################################################################################################
     MARKET METHODS
    ###########################################################################################################
     */

    /**
     * Moves the specified row in the market
     *
     * @param rowNumber the row to be moved
     * @param effects   the effects of LeaderCards the player wants to use
     * @return the resources the player gets
     * @throws IllegalArgumentException if this move can't be made (the specified row number is invalid or there are not enough effects)
     * @throws NullPointerException     if there are no specified effects
     */
    //The NullPointerException must be catched in the controller because it's the controller which must remember to at least send an empty list
    public HashMap<ResourceType, Integer> moveRowInMarket(int rowNumber, List<Effect> effects) throws IllegalArgumentException, NullPointerException {
        return this.market.moveRow(rowNumber, effects);
    }

    /**
     * Moves the specified column in the market
     *
     * @param columnNumber the column to be moved
     * @param effects      the effects of LeaderCards the player wants to use
     * @return the resources the player gets
     * @throws IllegalArgumentException if this move can't be made (the specified column number is invalid or there are not enough effects)
     * @throws NullPointerException     if there are no specified effects
     */
    //The NullPointerException must be caught in the controller because it's the controller which must remember to at least send an empty list
    public HashMap<ResourceType, Integer> moveColumnInMarket(int columnNumber, List<Effect> effects) throws IllegalArgumentException, NullPointerException {
        return this.market.moveColumn(columnNumber, effects);
    }

    /**
     * Returns the resources the player would get if the chose to move the specified row
     *
     * @param rowNumber the row the player may want to change in the future
     * @param effects   the effects of LeaderCards the player wants to use
     * @return the resources this move would generate
     * @throws IllegalArgumentException if this move can't be made (the specified row number is invalid or there are not enough effects)
     * @throws NullPointerException     if there are no specified effects
     */
    //The NullPointerException must be caught in the controller because it's the controller which must remember to at least send an empty list
    public HashMap<ResourceType, Integer> getResourcesFromRowInMarket(int rowNumber, List<Effect> effects) throws IllegalArgumentException, NullPointerException {
        return this.market.getResourcesFromRow(rowNumber, effects);
    }

    /**
     * Returns the resources the player would get if the chose to move the specified column
     *
     * @param columnNumber the column the player may want to change in the future
     * @param effects      the effects of LeaderCards the player wants to use
     * @return the resources this move would generate
     * @throws IllegalArgumentException if this move can't be made (the specified column number is invalid or there are not enough effects)
     * @throws NullPointerException     if there are no specified effects
     */
    //The NullPointerException must be caught in the controller because it's the controller which must remember to at least send an empty list
    public HashMap<ResourceType, Integer> getResourcesFromColumnInMarket(int columnNumber, List<Effect> effects) throws IllegalArgumentException, NullPointerException {
        return this.market.getResourcesFromColumn(columnNumber, effects);
    }

    /**
     * Returns the marble in the specific position
     *
     * @param row    the row the marble is at
     * @param column the column the marble is at
     * @return the chosen Marble on the grid
     * @throws IllegalArgumentException when the position is invalid
     */
    public Marble getMarbleInMarket(int row, int column) throws IllegalArgumentException {
        return this.market.getMarbleInTheGrid(row, column);
    }

    /**
     * Discards the specified resources and gives extra Faith points to each player who is not the one discarding these resources
     *
     * @param resToDiscard the resources to be discarded
     * @param notToBeGiven the PlayerBoard of the player who is discarding the specified resources
     * @throws LastVaticanReportException if a player reaches the last Vatican Report
     */
    public void discardResources(Map<ResourceType, Integer> resToDiscard, PlayerBoard notToBeGiven) throws LastVaticanReportException {
        int numDiscardedRes = resToDiscard.size();
        for (PlayerBoard pB : playerBoardsList)
            if (pB != notToBeGiven)
                pB.moveForwardOnFaithTrack(numDiscardedRes * this.stepForEachDiscardedRes);
    }

    /**
     * Returns how many White Marbles are in the specified row
     *
     * @param rowNumber the row where to count the number of White Marbles
     * @return the number of White Marbles in the row (this number is in the range [0, number of rows - 1])
     * @throws IllegalArgumentException if the specified row index is invalid
     */
    public int getNumberOfWhiteMarbleInMarketRow(int rowNumber) throws IllegalArgumentException {
        return this.market.getNumberOfWhiteMarbleInTheRow(rowNumber);
    }

    /**
     * Returns how many White Marbles are in the specified column
     *
     * @param columnNumber the column where to count the number of White Marbles
     * @return the number of White Marbles in the column (this number is in the range [0, number of columns- 1])
     * @throws IllegalArgumentException if the specified column index is invalid
     */
    public int getNumberOfWhiteMarbleInTheColumn(int columnNumber) throws IllegalArgumentException {
        return this.market.getNumberOfWhiteMarbleInTheColumn(columnNumber);
    }

    /**
     * Returns how many White Marbles are in the specified row or column. Only one of the two parameters must be a number greater than 0 (the other must be equal to zero):
     * if the rowNumber is greater than 0, then the method computes how many White Marbles are in the row indicated by that number, otherwise the method computes
     * how many White Marbles are in the column specified by the columnNumber
     *
     * @param rowNumber    the eventual row where to count the number of White Marbles (this number is in the range [0, number of rows - 1])
     * @param columnNumber the eventual column where to count the number of White Marbles (this number is in the range [0, number of columns- 1])
     * @return the number of White Marble in the specified row or column
     */
    public int getNumberOfWhiteMarbleInMarketRowOrColumn(int rowNumber, int columnNumber) {
        if (rowNumber != 0)
            return this.getNumberOfWhiteMarbleInMarketRow(rowNumber);
        //then the rowColumn must be the only number greater than zero
        return this.getNumberOfWhiteMarbleInTheColumn(columnNumber);
    }

    /**
     * Returns a copy of the MarketMatrix but made of MarbleTypes instead of Marbles
     *
     * @return a copy of the MarketMatrix made of MarbleTypes
     */
    public MarbleType[][] getMarketMatrixWithMarbleType() {
        return this.market.getMarketMatrixWithMarbleType();
    }

    /**
     * Returns the MarbleType of the Marble on the slide
     *
     * @return the MarbleType of the Marble on the slide
     */
    public MarbleType getMarbleOnSlideWithMarbleType() {
        return this.market.getMarbleOnSlideWithMarbleType();
    }

    /*
    ###########################################################################################################
     DEVGRID METHODS
    ###########################################################################################################
     */

    /**
     * Returns the discounted cost of the specified DevCard by applying the specified effects
     *
     * @param devCard the DevCard to be discounted
     * @param effects the effects to apply
     * @return the discounted cost of the card
     */
    public HashMap<ResourceType, Integer> applyDiscountToDevCard(DevCard devCard, List<Effect> effects) {
        HashMap<ResourceType, Integer> cost = devCard.getCost();

        for (Effect effect : effects)
            effect.discountEffect(cost);

        return cost;
    }

    /**
     * Returns the DevCard at the specified position
     *
     * @param row    is the chosen row of the grid
     * @param column is the chosen column of the grid
     * @return the first DevCard at the chosen DevDeck on the grid (the Card is not Removed From the deck) or NULL if devDeck is Empty
     * @throws IllegalArgumentException when the chosen position in the grid is not valid
     */
    public DevCard getDevCardFromDeckInDevGrid(int row, int column) throws IllegalArgumentException {
        return this.devGrid.getDevCardFromDeck(row, column);
    }

    /**
     * Returns the DevCard with the specified level and color
     *
     * @param level  the level of the card
     * @param colour the color of the card
     * @return the first DevCard in the chosen DevDeck on the grid (the Card is not removed from the deck) or NULL if devDeck is Empty
     * @throws IllegalArgumentException when the chosen level or color are not valid
     */
    public DevCard getDevCardFromDeckInDevGrid(int level, DevCardColour colour) throws IllegalArgumentException {
        return this.devGrid.getDevCardFromDeck(level, colour);
    }

    /**
     * draws the DevCard on the grid at the specified position
     *
     * @param row    the row of the DevCard
     * @param column the column of the DevCard
     * @return the first DevCard in the chosen DevDeck on the grid. The Card is Removed From the deck.
     * @throws IllegalArgumentException when the chosen position on the grid is not valid
     * @throws IllegalActionException   if the specified DevDeck is empty
     */
    public DevCard drawDevCardFromDeckInDevGrid(int row, int column) throws IllegalArgumentException, IllegalActionException {
        DevCard result;

        try {
            result = this.devGrid.drawDevCardFromDeck(row, column);
        } catch (EmptyDeckException e) {
            throw new IllegalActionException(e.getMessage());
        }

        return result;
    }

    /**
     * draws a DevCard with the specified level and color
     *
     * @param level  the level of the card
     * @param colour the color of the card
     * @return the first DevCard in the chosen DevDeck on the grid. The Card is Removed From the deck.
     * @throws IllegalArgumentException when the chosen position in the grid is not valid
     * @throws IllegalActionException   if devDeck is Empty;
     */
    public DevCard drawDevCardFromDeckInDevGrid(int level, DevCardColour colour) throws IllegalArgumentException, IllegalActionException {
        DevCard result;

        try {
            result = this.devGrid.drawDevCardFromDeck(level, colour);
        } catch (EmptyDeckException e) {
            throw new IllegalActionException(e.getMessage());
        }

        return result;
    }

    /**
     * @return a list of the uncovered DevCard in the Gris
     */
    public Collection<DevCard> getDrawableCardsInDevGrid() {
        return this.devGrid.getDrawableCards();
    }

    /**
     * Returns the size of the devDeck containing devCards of the specified color and with the max level
     *
     * @param colour the color of the devDeck
     * @return the size of the devDeck containing devCards of the specified color and with the max level
     */
    public int getDevDeckSizeInDevGrid(DevCardColour colour) {
        return this.devGrid.getDevDeckSize(colour);
    }

    /*
    ###########################################################################################################
     FAITH-RELATED METHODS
    ###########################################################################################################
     */

    public void dealWithVaticanReportAllPlayers(ReportNum reportNum) throws IllegalActionException {
        for (PlayerBoard pB : this.playerBoardsList)
            pB.dealWithVaticanReport(reportNum);
    }

    /*
    ###########################################################################################################
     GENERAL GETTER
    ###########################################################################################################
     */

    /**
     * Returns the reference to the PlayerBoard at the specified position (it doesn't remove it from this MainBoard).
     *
     * @param position the index of the PlayerBoard
     * @return the reference to the desired PlayerBoard
     */
    public PlayerBoard getPlayerBoard(int position) {
        return this.playerBoardsList.get(position);
    }

    public int getPlayerBoardIndex(PlayerBoard playerBoard) {
        return this.playerBoardsList.indexOf(playerBoard);
    }

    public List<PlayerBoard> getPlayerBoardsList() {
        return playerBoardsList;
    }

    public int getPlayerBoardsNumber() {
        return this.playerBoardsList.size();
    }

    public FaithTrack getFaithTrack() {
        return new FaithTrack(this.faithTrack);
    }

    public FaithTrack getFaithTrackReference() {
        return this.faithTrack;
    }

    public Deck getLeaderCardsDeck() {
        return new LeaderCardDeck((LeaderCardDeck) this.leaderCardsDeck);
    }

    public DevGrid getDevGrid() {
        return new DevGrid(this.devGrid);
    }

    public Market getMarket() {
        return new Market(this.market);
    }

    public int getNumberOfPlayers() {
        return numberOfPlayers;
    }

    public int getNumberOfLeaderCardsToGive() {
        return numberOfLeaderCardsToGive;
    }

    public int[] getExtraFaithPointsAtBeginning() {
        int[] copy = new int[this.extraFaithPointsAtBeginning.length];
        System.arraycopy(this.extraFaithPointsAtBeginning, 0, copy, 0, this.extraFaithPointsAtBeginning.length);
        return copy;
    }

    public int[] getExtraResourcesAtBeginning() {
        int[] copy = new int[this.extraResourcesAtBeginning.length];
        System.arraycopy(this.extraResourcesAtBeginning, 0, copy, 0, this.extraResourcesAtBeginning.length);
        return copy;
    }

    public int getStepForEachDiscardedRes() {
        return stepForEachDiscardedRes;
    }

    public int getNumberOfLeaderCardsToDiscardAtBeginning() {
        return numberOfLeaderCardsToDiscardAtBeginning;
    }

    /**
     * Returns -1 because in the multiplayer mode there is no Lorenzo
     *
     * @return -1
     */
    public int getLorenzoFaithTrackPosition() {
        return -1;
    }

    /*
    ###########################################################################################################
     CONFIGURATION METHODS
    ###########################################################################################################
     */

    /**
     * Creates a ready-to-be-used MainBoard
     *
     * @param numberOfPlayers the number of player in this game
     * @throws IllegalArgumentException     if the number of players is lower than 0 or greater than 4 or in the configuration files there are some errors in the elements described
     * @throws ParserConfigurationException if there are problems in the parsing
     * @throws IOException                  if an IO operations fails
     * @throws SAXException                 if there is a general SAX error or warning
     */
    public MainBoard(int numberOfPlayers) throws IllegalArgumentException, ParserConfigurationException, IOException, SAXException {
        if (numberOfPlayers > 4 || numberOfPlayers < 0)
            throw new IllegalArgumentException("The number of players is illegal!");

        this.faithTrack = FaithTrack.instance(new File("FaithTrackConfig.xml"));
        this.popeTiles = PopeTile.popeTileConfig(new File("PopeTileConfig.xml"), this.faithTrack.getReportNumOrder());
        try {
            this.leaderCardsDeck = new LeaderCardDeck(LeaderCardDeck.initLeaderCards(new File("LeaderCardConfig.xml")));
            this.devGrid = new DevGrid(new File("DevCardConfig.xsd.xml"));
            this.market = new Market(new File("MarketConfig.xsd.xml"));
        } catch (NegativeQuantityException e) {
            throw new IllegalArgumentException("The given configuration file is wrong: " + e.getMessage());
        }

        this.numberOfPlayers = numberOfPlayers;
        this.playerBoardsList = new ArrayList<>();
        for (int i = 0; i < this.numberOfPlayers; i++)
            this.playerBoardsList.add(new PlayerBoard());
        //By default the number of LeaderCards to give each player at the beginning of the game is four
        this.numberOfLeaderCardsToGive = 4;
        this.extraFaithPointsAtBeginning = new int[]{0, 0, 1, 1};
        this.extraResourcesAtBeginning = new int[]{0, 1, 1, 2};
        this.stepForEachDiscardedRes = 1;
        this.numberOfLeaderCardsToDiscardAtBeginning = 2;

        this.cellObserver = new PopeCellObserver(this);

        initMainBoard();
    }

    /**
     * Initiates the FaithLevel of each PlayerBoard by setting their FaithTrack and giving them their PopeTiles. This method supposes that there are enough
     * PopeTiles for every PlayerBoard in the configuration file.
     */
    private void initMainBoard() {
        //Let's set the FaithTrack for all PlayerBoards
        for (PlayerBoard pB : this.playerBoardsList)
            pB.setPlayerFaithLevelFaithTrack(this.faithTrack);

        //Let's give each of the PlayerBoards their PopeTiles
        for (List<PopeTile> lPT : popeTiles)
            Collections.shuffle(lPT);
        List<PopeTile> tmp;
        for (PlayerBoard pB : this.playerBoardsList) {
            tmp = new ArrayList<>();
            for (List<PopeTile> lPT : popeTiles)
                tmp.add(lPT.remove(0));
            pB.setPlayerFaithLevelPopeTiles(tmp);
        }

        //Lets the Observer observes the PopeCells
        this.faithTrack.attachObserverToPopeTiles(this.cellObserver);
    }

    /**
     * Gives each player in the game the LeaderCards as required by the game rules
     */
    public void giveLeaderCardsToPlayerAtGameBeginning() {
        this.leaderCardsDeck.shuffle();
        //Collections.shuffle(this.playerBoardList);
        List<LeaderCard> tmpList;
        for (PlayerBoard pB : this.playerBoardsList) {
            tmpList = new ArrayList<>();
            for (int i = 0; i < this.numberOfLeaderCardsToGive; i++)
                tmpList.add((LeaderCard) this.leaderCardsDeck.drawFromDeck());
            pB.setNotPlayedLeaderCardsAtGameBeginning(tmpList);
        }
        //Da qualche parte nel controller implementare il fatto che di queste che riceve il player ne può mantenere solo una certa
    }

    /**
     * Gives each player in the game their extra Faith points starting from the specified first player
     *
     * @param firstPlayer the first player of the game
     * @throws LastVaticanReportException if the player reaches the end of the FaithTrack
     */
    public void giveExtraFaithPointAtBeginning(int firstPlayer) throws LastVaticanReportException {
        int playerIndex = firstPlayer;
        int i = 0;
        while (i < this.numberOfPlayers) {
            if (playerIndex == this.numberOfPlayers)
                playerIndex = 0;
            playerBoardsList.get(playerIndex).moveForwardOnFaithTrack(this.extraFaithPointsAtBeginning[i]);
            i++;
            playerIndex++;
        }
    }

    /**
     * Returns the number of extra resources the specified player gets in this game: it calculates this number basing the count on the specified first player (both indexes are the ones in the
     * list of players that the MainBoard holds).
     *
     * @param firstPlayer   the first player of the game
     * @param currentPlayer the player whose number of extra resources we want to compute
     * @return the number of extra resources the specified player gets in this game
     */
    public int getExtraResourcesAtBeginningForPlayer(int firstPlayer, int currentPlayer) {
        return this.extraResourcesAtBeginning[this.getPlayerOder(firstPlayer, currentPlayer)];
    }

    /**
     * Returns the order of the specified player in the game: it calculates this number basing the count on the specified first player (both indexes are the ones in the
     * list of players that the MainBoard holds).
     *
     * @param firstPlayer   the first player of the game
     * @param currentPlayer the player whose number of extra resources we want to compute
     * @return the player's order in the game (it is a number in the range [0, this.numberOfPlayers - 1], ends included)
     */
    public int getPlayerOder(int firstPlayer, int currentPlayer) {
        //Basically this method shifts the order of the player so that the first player is in position 0 because if we do so
        //we can easily get the desired number by directly accessing the extraResourcesAtBeginning array
        int currentPlayerOrder = currentPlayer - firstPlayer;
        //System.out.println("Cur: " + currentPlayerOrder);
        if (currentPlayerOrder < 0)
            currentPlayerOrder = this.numberOfPlayers - Math.abs(currentPlayerOrder);
        //System.out.println("Cur: " + currentPlayerOrder);
        return currentPlayerOrder;
    }

    /**
     * Returns a random player
     *
     * @return the randomly chosen player's position in the order of all players of the game
     */
    public int getFirstPlayerRandomly() {
        return new Random().nextInt(this.numberOfPlayers);
    }

    /**
     * Constructs a copy of the specified MainBoard. The new MainBoard and its PlayerBoards all reference to the same FaithTrack.
     *
     * @param original the MainBoard to be copied
     */
    public MainBoard(MainBoard original) {
        this.faithTrack = new FaithTrack(original.faithTrack);
        this.popeTiles = PopeTile.copyPopeTiles(original.popeTiles);
        this.leaderCardsDeck = new LeaderCardDeck((LeaderCardDeck) original.leaderCardsDeck);
        this.devGrid = new DevGrid(original.devGrid);
        this.market = new Market(original.market);
        this.numberOfPlayers = original.numberOfPlayers;

        this.playerBoardsList = new ArrayList<>();
        for (PlayerBoard pB : original.playerBoardsList)
            this.playerBoardsList.add(new PlayerBoard(pB, this.faithTrack));

        this.numberOfLeaderCardsToGive = original.numberOfLeaderCardsToGive;

        this.extraFaithPointsAtBeginning = new int[original.extraFaithPointsAtBeginning.length];
        System.arraycopy(original.extraFaithPointsAtBeginning, 0, this.extraFaithPointsAtBeginning, 0, original.extraFaithPointsAtBeginning.length);

        this.extraResourcesAtBeginning = new int[original.extraResourcesAtBeginning.length];
        System.arraycopy(original.extraResourcesAtBeginning, 0, this.extraResourcesAtBeginning, 0, original.extraResourcesAtBeginning.length);

        this.stepForEachDiscardedRes = original.stepForEachDiscardedRes;

        this.numberOfLeaderCardsToDiscardAtBeginning = original.numberOfLeaderCardsToDiscardAtBeginning;

        //We need a new PopeCellObserver because this kind of observer references to a MainBoard: if didn't make a copy of it,
        //then we would have one single Observer for two MainBoards
        this.cellObserver = new PopeCellObserver(this);
        //We delete the observers which are already present in the FaithTrack because the FaithTrack copy constructor method
        // simply creates a new list of the observers of the PopeTiles but doesn't create a new Observer object
        this.faithTrack.detachObserverToPopeTiles(original.cellObserver);
        this.faithTrack.attachObserverToPopeTiles(this.cellObserver);

    }

    public MainBoard getClone() {
        return new MainBoard(this);
    }

    /***************************** OLD STUFF TO BE CHECKED ****************************************/


    @Deprecated
    public MainBoard(DevGrid devGrid) {
        this.devGrid = devGrid;
    }

    //This method is only used for testing purposes
    public void addPlayerBoard(PlayerBoard playerBoard) {
        this.playerBoardsList.add(playerBoard);
    }

    /**
     * Checks whether the specified player, identified with their PlayerBoard, has discarded all the LeaderCard is supposed to
     *
     * @param playerBoard the player's PlayerBoard where the number of discarded card is checked
     * @return true if the player has discarded the right amount of LeaderCards, false otherwise
     */
    public boolean checkIfPlayerDiscardedCards(PlayerBoard playerBoard) {
        return playerBoard.checkDiscardedLeaderCard(this.numberOfLeaderCardsToGive, this.numberOfLeaderCardsToDiscardAtBeginning);
    }


}