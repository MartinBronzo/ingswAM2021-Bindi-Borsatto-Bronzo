package it.polimi.ingsw.client.readOnlyModel;

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
}
