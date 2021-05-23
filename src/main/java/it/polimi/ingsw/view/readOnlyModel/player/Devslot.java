package it.polimi.ingsw.view.readOnlyModel.player;

import it.polimi.ingsw.model.DevCards.DevCard;

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
