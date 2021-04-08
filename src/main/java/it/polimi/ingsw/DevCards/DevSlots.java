package it.polimi.ingsw.DevCards;

import java.util.Collection;
import java.util.LinkedList;

public class DevSlots {
    private final DevSlot[] devSlots;

    public DevSlots() {
        this.devSlots = new DevSlot[3];
        for (int i=0; i<devSlots.length; i++) {
            devSlots[i] = new DevSlot();
        }
    }

    public DevSlot getDevSlot(int index) throws IndexOutOfBoundsException {
        if (index<0 || index>=devSlots.length) throw new IndexOutOfBoundsException("getDevSlots: Index must be between 0 and 2");
        return devSlots[index];
    }

    /**
     * Gets A Collection containing all the DevCards in the Slots. This Collection doesn't affect the cards in the DevSlot
     * @return a collection of Cards
     */
    public Collection<DevCard> getAllDevCards(){
        Collection<DevCard> devCards = new LinkedList<>();
        for (DevSlot devSlot:devSlots) {
            devCards.addAll(devSlot.getDevCards());
        }
        return new LinkedList<>(devCards);
    }

    /** @return the sum of Points of the Cards in the Slots
     * */
    public int getPoints(){
        int points=0;
        for (DevSlot devSlot:devSlots) points += devSlot.getPoints();
        return points;
    }
}
