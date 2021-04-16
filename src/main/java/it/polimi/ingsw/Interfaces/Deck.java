package it.polimi.ingsw.Interfaces;

public interface Deck {
    Object drawFromDeck();

    Object getFirst();

    boolean shuffle();

    boolean isEmpty();

    int size();
}
