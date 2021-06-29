package it.polimi.ingsw.model.Interfaces;

/**
 * This interface is implemented by all the objects which want to be used as a deck of cards. For example, with a deck of cards, we can
 * draw the first card and we can shuffle it.
 */
public interface Deck {

    /**
     * Draws the first element of the Deck (it removes the object from the Deck)
     * @return the first element of the Deck
     */
    Object drawFromDeck();

    /**
     * Returns the first element of the Deck (it does not remove the object from the Deck)
     * @return the first element of the Deck
     */
    Object getFirst();

    /**
     * Shuffles the Deck
     * @return a boolean which can represent if this method worked correctly or not
     */
    boolean shuffle();

    /**
     * Returns whether the Deck is empty
     * @return true if the Deck is empty, false otherwise
     */
    boolean isEmpty();

    /**
     * Returns the number of elements that are left in the Deck
     * @return the number of elements that are left in the Deck
     */
    int size();
}
