package it.polimi.ingsw.view.lightModel.player;

import it.polimi.ingsw.model.devCards.DevCard;

import java.util.List;

/**
 * This LightModel class represents a lighter version of the DevSlots stored inside the Model. This class has not been used in this
 * application.
 */
public class Devslot {
    private List<DevCard> devCards;

    /**
     * Constructs a Devslot object containing the specified DevCards
     * @param devCards the DevCards the player stores inside a DevSlot
     */
    public Devslot(List<DevCard> devCards) {
        this.devCards = devCards;
    }

    /**
     * Returns the DevCards contained in this Devslot
     * @return the DevCards contained in this Devslot
     */
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
