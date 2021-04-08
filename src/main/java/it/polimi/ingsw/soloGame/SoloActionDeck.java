package it.polimi.ingsw.soloGame;

import it.polimi.ingsw.Interfaces.Deck;

import java.util.Collection;
import java.util.Collections;
import java.util.LinkedList;

public class SoloActionDeck implements Deck {
    private final LinkedList<SoloActionToken> soloDeck;

    //TODO: fare un xml da cui leggere i token
    public SoloActionDeck(Collection<SoloActionToken> tokens){
        soloDeck = new LinkedList<>(tokens);
    }

    @Override
    public Object drawFromDeck() {
        SoloActionToken drewToken;
        drewToken = soloDeck.removeFirst();
        soloDeck.addLast(drewToken);
        return drewToken;
    }

    @Override
    public Object getFirst() {
        return soloDeck.getFirst();
    }

    @Override
    public boolean shuffle() {
        Collections.shuffle(soloDeck);
        return true;
    }

    @Override
    public boolean isEmpty() {
        return soloDeck.isEmpty();
    }

    @Override
    public int size() {
        return soloDeck.size();
    }
}
