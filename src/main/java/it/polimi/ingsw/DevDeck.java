package it.polimi.ingsw;

import java.util.Collection;
import java.util.Collections;
import java.util.LinkedList;
import java.util.NoSuchElementException;

public class DevDeck implements Deck{
    private LinkedList<DevCard> devCards;

    public DevDeck(Collection<DevCard> cardCollection) {
        try {
            this.devCards = new LinkedList<>(cardCollection);
        } catch (NullPointerException cardCollectionNull){
            this.devCards = new LinkedList<>();
        }
    }

    @Override
    public DevCard drawFromDeck() throws NoSuchElementException{
        return devCards.removeFirst();
    }

    @Override
    public DevCard getFirst() throws NoSuchElementException {
        return devCards.getFirst();
    }

    @Override
    public boolean shuffle() {
        Collections.shuffle(devCards);
        return true;
    }

    @Override
    public boolean isEmpty() {
        return devCards.isEmpty();
    }

}
