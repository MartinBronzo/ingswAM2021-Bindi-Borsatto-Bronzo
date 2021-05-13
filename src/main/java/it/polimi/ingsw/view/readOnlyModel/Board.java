package it.polimi.ingsw.view.readOnlyModel;

import it.polimi.ingsw.model.DevCards.DevCard;
import it.polimi.ingsw.model.DevCards.DevGrid;
import it.polimi.ingsw.model.marble.MarbleType;

import java.util.Arrays;
import java.util.List;

public class Board {
    private MarbleType[][] marketMatrix;
    private MarbleType marbleOnSlide;
    private DevGrid devGrid;

    public void setMarbleOnSlide(MarbleType marbleOnSlide) {
        this.marbleOnSlide = marbleOnSlide;
    }

    public void setMarketMatrix(MarbleType[][] marketMatrix) {
        this.marketMatrix = marketMatrix;
    }

    public MarbleType[][] getMarketMatrix() {
        return marketMatrix;
    }

    public MarbleType getMarbleOnSlide() {
        return marbleOnSlide;
    }

    public DevGrid getDevGrid() {
        return devGrid;
    }

    public void setDevGrid(DevGrid devGrid){
        this.devGrid = devGrid;
    }

    @Override
    public String toString() {
        return "Board{" +
                "marketMatrix=" + Arrays.toString(marketMatrix) +
                ", marbleOnSlide=" + marbleOnSlide +
                ", devGrid=" + devGrid +
                '}';
    }


    /**
     * Returns how many White Marbles are in the specified row
     * @param rowNumber the row where to count the number of White Marbles
     * @return the number of White Marbles in the row
     * @throws IllegalArgumentException if the specified row index is invalid (it is greater than 2 or less than 0)
     */
    public int getNumberOfWhiteMarbleInTheRow(int rowNumber) throws IllegalArgumentException{
        if (rowNumber < 0 || rowNumber >= 3) throw new IllegalArgumentException("getNWhiteRow Market: not valid rowNumber");

        int c=0;
        for (int j = 0; j < marketMatrix[rowNumber].length; j++) {
            if (marketMatrix[rowNumber][j].isWhiteMarble())
                c++;
        }
        return c;
    }


    /**
     * Returns how many White Marbles are in the specified column
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
}