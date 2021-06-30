package it.polimi.ingsw.view.lightModel;

import it.polimi.ingsw.model.devCards.DevCard;
import it.polimi.ingsw.model.marble.MarbleType;

import java.util.Arrays;

/**
 * This LightModel object contains all the information needed to represent the common parts of the game, that is the Market and the DevGrid.
 */
public class Board {
    private MarbleType[][] marketMatrix;
    private MarbleType marbleOnSlide;
    //private DevGrid devGrid;
    private DevCard[][] devMatrix;

    /**
     * Sets the Marble that is on the slide right now
     * @param marbleOnSlide the Marble that is on the slide right now
     */
    public void setMarbleOnSlide(MarbleType marbleOnSlide) {
        this.marbleOnSlide = marbleOnSlide;
    }

    /**
     * Sets the Market
     * @param marketMatrix the Market
     */
    public void setMarketMatrix(MarbleType[][] marketMatrix) {
        this.marketMatrix = marketMatrix;
    }

    /**
     * Returns the Market
     * @return the Market
     */
    public MarbleType[][] getMarketMatrix() {
        return marketMatrix;
    }

    /**
     * Returns the Marble that is on the slide right now
     * @return the Marble that is on the slide right now
     */
    public MarbleType getMarbleOnSlide() {
        return marbleOnSlide;
    }

    /*public DevGrid getDevGrid() {
        return devGrid;
    }*/

    /*public void setDevGrid(DevGrid devGrid) {
        this.devGrid = devGrid;
    }*/

    /**
     * Returns the DevGrid
     * @return the DevGrid
     */
    public DevCard[][] getDevMatrix() {
        return devMatrix;
    }

    /**
     * Sets the DevGrid
     * @param devMatrix the DevGrid
     */
    public void setDevMatrix(DevCard[][] devMatrix) {
        this.devMatrix = devMatrix;
    }

    @Override
    public String toString() {
        return "Board{" +
                "marketMatrix=" + Arrays.toString(marketMatrix) +
                ", marbleOnSlide=" + marbleOnSlide +
                ", devGrid=" + Arrays.deepToString(devMatrix) +
                '}';
    }


    /**
     * Returns how many White Marbles are in the specified row
     *
     * @param rowNumber the row where to count the number of White Marbles
     * @return the number of White Marbles in the row
     * @throws IllegalArgumentException if the specified row index is invalid (it is greater than 2 or less than 0)
     */
    public int getNumberOfWhiteMarbleInTheRow(int rowNumber) throws IllegalArgumentException {
        if (rowNumber < 0 || rowNumber >= 3)
            throw new IllegalArgumentException("getNWhiteRow Market: not valid rowNumber");

        int c = 0;
        for (int j = 0; j < marketMatrix[rowNumber].length; j++) {
            if (marketMatrix[rowNumber][j].isWhiteMarble())
                c++;
        }
        return c;
    }


    /**
     * Returns how many White Marbles are in the specified column
     *
     * @param columnNumber the column where to count the number of White Marbles
     * @return the number of White Marbles in the column
     * @throws IllegalArgumentException if the specified column index is invalid (it is greater than 3 or less than 0)
     */
    public int getNumberOfWhiteMarbleInTheColumn(int columnNumber) throws IllegalArgumentException {
        if (columnNumber < 0 || columnNumber >= 4)
            throw new IllegalArgumentException("getNWhiteColumn Market: not valid columnNumber");

        int c = 0;
        for (int j = 0; j < marketMatrix.length; j++) {
            if (marketMatrix[j][columnNumber].isWhiteMarble())
                c++;
        }
        return c;
    }

    /**
     * Merge this Board object with the new information contained in the specified Board object
     * @param updateBoard the Board object containing new information
     * @return false if the merge succeeded
     */
    public boolean merge(Board updateBoard) {
        MarbleType[][] marbleMarketUpdate = updateBoard.getMarketMatrix();
        if (marbleMarketUpdate != null) this.marketMatrix = marbleMarketUpdate;

        MarbleType marbleUpdate = updateBoard.getMarbleOnSlide();
        if (marbleUpdate != null) this.marbleOnSlide = marbleUpdate;

        //DevGrid updateDev = updateBoard.getDevGrid();
        //if (updateDev != null) this.devGrid = updateDev;
        DevCard[][] updateDev = updateBoard.getDevMatrix();
        if (updateDev != null) this.devMatrix = updateDev;

        return false;
    }
}
