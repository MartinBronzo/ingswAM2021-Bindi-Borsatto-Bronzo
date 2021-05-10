package it.polimi.ingsw.client.readOnlyModel.player;

import it.polimi.ingsw.model.DevCards.DevCard;

import java.util.List;

public class Devslot {
    public List<DevCard> getDevCards() {
        return devCards;
    }

    private List<DevCard> devCards;

    @Override
    public String toString() {
        return "Devslot{" +
                "devCards=" + devCards +
                '}';
    }
}
