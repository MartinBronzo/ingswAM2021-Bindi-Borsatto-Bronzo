package it.polimi.ingsw.DevCards;

import java.util.Collection;
import java.util.LinkedList;
import java.util.NoSuchElementException;

/**
 * This Class implements a List of DevCards, it has a different structural logic respect DevDeck
 * Cards are sorted according to theirs level.
 * there cannot be 2 cards with the same level and adjacent cards differ by one level
 * */
public class DevSlot {
    LinkedList<DevCard> devCards;

    /** Creates an empty devSlot
     * */
    public DevSlot() {
        this.devCards = new LinkedList<>();
    }

    /** Creates a copy of the devSlot
     * @param devSlot the DevSlot to be copied
     * */
    public DevSlot( DevSlot devSlot) {
        this.devCards = new LinkedList<>(devSlot.devCards);
    }

    /**if it's possible adds the devCard to the devSlot
     * @param devCard is the dev card to be added to devSlot
     * @throws NullPointerException if devCard is Null
     * @throws IllegalArgumentException if devCard can't be added to the DevSlot
     * */
    public boolean addDevCard(DevCard devCard) throws NullPointerException, IllegalArgumentException {
        int actualLevel;
        if (devCard==null) throw new NullPointerException("DevCard passed is null");

        try {
            actualLevel=devCards.getLast().getLevel();
        }catch (NoSuchElementException ex){
            actualLevel=0;
        }
        if (devCard.getLevel()==actualLevel+1){
            devCards.addLast(devCard);
        } else {
            throw new IllegalArgumentException("addDevCard: This devCard can't be Add to DevSlot");
        }

        return true;
    }

    /** @return the sum of Points of the Cards in the Slot
     * */
    public int getPoints(){
        int points=0;
        for (DevCard devCard : devCards) points += devCard.getVictoryPoints();
        return points;
    }

    /** this method returns the devCard of the specific level if it is present in the slot
     * @param level the level of the desired Card
     * @return the desired Card if it is present or null if missing
     * */
    public DevCard getDevCard(int level){
        return devCards.stream().filter(x->x.getLevel()==level).findFirst().orElse(null);
    }

    /** Gets the LastDev Card in the Slot
     * @return the Card with the higher level in the Slot
     * @throws NoSuchElementException if there is no card in the devSlot
     * */
    public DevCard getLastDevCard() throws NoSuchElementException{
        return devCards.getLast();
    }

    /**
     * Gets A Collection containing the DevCard in the Slot. This Collection doesn't affect the cards in the DevSlot
     * @return a collection of Cards
     */
    public Collection<DevCard> getDevCards(){
        return new LinkedList<>(devCards);
    }

    /**
     * Gets the number of Cards in the DevSlot
     * @return is the number of Cards in the DevSlot
     */
    public int size(){
        return devCards.size();
    }

}
