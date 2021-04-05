package it.polimi.ingsw;

public interface Deck {
    Object drawFromDeck();
    Object getFirst();
    boolean shuffle();
    boolean isEmpty();
    int size();
}
