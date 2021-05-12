package it.polimi.ingsw.view.readOnlyModel;

import it.polimi.ingsw.model.DevCards.DevCard;
import it.polimi.ingsw.model.marble.MarbleType;

import java.util.Arrays;
import java.util.List;

public class Board {
    private MarbleType[][] marketMatrix;
    private MarbleType marbleOnSlide;
    private List<DevCard> devGrid;

    public MarbleType[][] getMarketMatrix() {
        return marketMatrix;
    }

    public MarbleType getMarbleOnSlide() {
        return marbleOnSlide;
    }

    public List<DevCard> getDevGrid() {
        return devGrid;
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
