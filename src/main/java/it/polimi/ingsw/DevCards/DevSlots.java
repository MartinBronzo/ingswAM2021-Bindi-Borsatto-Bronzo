package it.polimi.ingsw.DevCards;

import java.util.*;

/**
 * Groups more DevSlot in a SingleObject
 */
public class DevSlots {
    private final DevSlot[] devSlots;

    /**
     * Creates 3 Empty DevSlots
     */
    public DevSlots() {
        this.devSlots = new DevSlot[3];
        for (int i=0; i<devSlots.length; i++) {
            devSlots[i] = new DevSlot();
        }
    }


    /**Creates a copy of DevSlots
     * @param devSlots to be copied
     */
    public DevSlots( DevSlots devSlots) {
        this.devSlots = new DevSlot[3];
        for (int i=0; i<this.devSlots.length; i++) {
            this.devSlots[i] = new DevSlot(devSlots.devSlots[i]);
        }
    }

    /**
     * @param index is the DevSlot number starting from 0
     * @return a copy the DevSlot at te specified Index
     * @throws IndexOutOfBoundsException if index is not valid [0...2]
     */
    public DevSlot getDevSlot(int index) throws IndexOutOfBoundsException {
        if (index<0 || index>=devSlots.length) throw new IndexOutOfBoundsException("getDevSlots: Index must be between 0 and 2");
        return new DevSlot(this.devSlots[index]);
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
        return devCards;
    }

    /** @return the sum of Points of the Cards in the Slots
     * */
    public int getPoints(){
        int points=0;
        for (DevSlot devSlot:devSlots) points += devSlot.getPoints();
        return points;
    }


    /**
     * A card can be added in on of the slots only if not duplicated
     * @param devCard is the DevCard wanted to be added in one of the slots
     * @return true if you can add the Card in one of the slots
     */
    private boolean isCardAddable(DevCard devCard){
        Collection<DevCard> devCards = getAllDevCards();
        return devCards.stream().noneMatch(card -> card.equals(devCard));
    }

    /**Adds one devCard to the selected DevSlot according to GameRules
     * @param index is the DevSlot number starting from 0
     * @param devCard id DevCard to be added, can't be in one of the DevSlots
     * @return true if the DevCard is added in the desired Slot
     * @throws IndexOutOfBoundsException if index is not valid: must be between 0 and 2
     * @throws NullPointerException if devCard is null
     * @throws IllegalArgumentException if this card can't be added in the desiredSlot
     */
    public boolean addDevCard (int index,DevCard devCard ) throws IndexOutOfBoundsException, NullPointerException, IllegalArgumentException {
        if (index<0 || index>=devSlots.length) throw new IndexOutOfBoundsException("addDevCard: Index must be between 0 and 2");
        if (devCard==null) throw new NullPointerException("addDevCard: devCard can't be null");
        if (!isCardAddable(devCard)) throw new IllegalArgumentException("addDevCard: this Card is duplicated");
        devSlots[index].addDevCard(devCard);
        return true;
    }

    public Collection<DevCard> getUsableDevCards(){
        Collection<DevCard> devCards = new LinkedList<>();
        DevCard lastCard;
        for (DevSlot devSlot:devSlots) {
            try {
                lastCard = devSlot.getLastDevCard();
                devCards.add(lastCard);
            } catch (NoSuchElementException e){
                /*There is no card in the devSlot, so there isn't any need to add or do something else*/
            }
        }
        return devCards;
    }
}
