package it.polimi.ingsw;

import java.util.Collection;
import java.util.Collections;
import java.util.LinkedList;
import java.util.NoSuchElementException;

/**
 * This class implements a List of not Ordered DevCard
 * */
public class DevDeck implements Deck{
    private LinkedList<DevCard> devCards;

    @Override
    public String toString() {
        return "DevDeck{" +
                "devCards=" + devCards +
                '}';
    }

    /**
     * creates a new devDeck with the cards in the Collections
     * @param cardCollection is the Collection of DevCards, which will compose the Deck, if it's null the deck will be empty
     * */
    public DevDeck(Collection<DevCard> cardCollection) {

        try {
            this.devCards = new LinkedList<>(cardCollection);
        } catch (NullPointerException cardCollectionNull){
            this.devCards = new LinkedList<>();
        }
    }

    /**@return the first card in the deck
     * this Card is removed from the deck
     * */
    @Override
    public DevCard drawFromDeck() throws NoSuchElementException{
        return devCards.removeFirst();
    }

    /**@return the first card in the deck
     * this Card is not removed from the deck
     * */
    @Override
    public DevCard getFirst() throws NoSuchElementException {
        return devCards.getFirst();
    }

    /** This Methods shuffles the deck
     * */
    @Override
    public boolean shuffle() {
        Collections.shuffle(devCards);
        return true;
    }
    /** @return true if the deck is empty;
     */
    @Override
    public boolean isEmpty() {
        return devCards.isEmpty();
    }

    /** @return the number of cards in the deck;
     */
    @Override
    public int size() {
        return devCards.size();
    }

}
