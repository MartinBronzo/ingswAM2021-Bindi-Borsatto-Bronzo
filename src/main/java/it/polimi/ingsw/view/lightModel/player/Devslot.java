package it.polimi.ingsw.view.lightModel.player;

import it.polimi.ingsw.model.devCards.DevCard;

import java.util.List;

public class Devslot {
    private List<DevCard> devCards;

    public List<DevCard> getDevCards() {
        return devCards;
    }

    @Override
    public String toString() {
        return "Devslot{" +
                "devCards=" + devCards +
                '}';
    }
}
