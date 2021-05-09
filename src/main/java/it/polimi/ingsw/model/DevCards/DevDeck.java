package it.polimi.ingsw.model.DevCards;

import it.polimi.ingsw.model.FaithTrack.PopeCell;
import it.polimi.ingsw.model.Interfaces.Deck;

import java.util.*;

/**
 * This class implements a List of not Ordered DevCard
 */
public class DevDeck implements Deck {
    private List<DevCard> devCards;

    /**
     * Constructs a copy of the specified DevDeck
     * @param original the DevDeck to be cloned
     */
    public DevDeck(DevDeck original){
        this.devCards = new LinkedList<>();
        //The cards are not cloned because they are immutable
        for(DevCard dC: original.devCards)
            this.devCards.add(dC);
    }

    @Override
    public String toString() {
        return "DevDeck{" +
                "devCards=" + devCards +
                '}';
    }

    /**
     * creates a new devDeck with the cards in the Collections
     *
     * @param cardCollection is the Collection of DevCards, which will compose the Deck, if it's null the deck will be empty
     */
    public DevDeck(Collection<DevCard> cardCollection) {

        try {
            this.devCards = new LinkedList<>(cardCollection);
        } catch (NullPointerException cardCollectionNull) {
            this.devCards = new LinkedList<>();
        }
    }

    /**
     * @return the first card in the deck
     * this Card is removed from the deck
     */
    @Override
    public DevCard drawFromDeck() throws NoSuchElementException {
        return devCards.remove(0);
    }

    /**
     * @return the first card in the deck
     * this Card is not removed from the deck
     */
    @Override
    public DevCard getFirst() throws NoSuchElementException {
        return devCards.get(0);
    }

    /**
     * This Methods shuffles the deck
     */
    @Override
    public boolean shuffle() {
        Collections.shuffle(devCards);
        return true;
    }

    /**
     * @return true if the deck is empty;
     */
    @Override
    public boolean isEmpty() {
        return devCards.isEmpty();
    }

    /**
     * @return the number of cards in the deck;
     */
    @Override
    public int size() {
        return devCards.size();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null)
            return false;
        if (obj == this)
            return true;
        if (!(obj instanceof DevDeck))
            return false;
        DevDeck tmp = (DevDeck) obj;
        if(this.devCards.size() != tmp.devCards.size())
            return false;
        for(int i = 0; i < this.devCards.size(); i++)
            if(this.devCards.get(i) != tmp.devCards.get(i))
                return false;
        //If the thread arrives here, then the two deck must contain the same cards in the same order
        return true;
    }


    //This method was used only for testing purposes: checks whether two decks contain the same cards (without any regard to the order)
    public boolean containsDeck(DevDeck deck){
        return this.devCards.containsAll(deck.devCards) && deck.devCards.containsAll(this.devCards);
    }


}
