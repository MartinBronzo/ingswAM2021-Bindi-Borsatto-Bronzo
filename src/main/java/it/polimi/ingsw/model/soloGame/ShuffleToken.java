package it.polimi.ingsw.model.soloGame;

/**
 * This class represents the FaithPointToken that also shuffles the SoloActionDeck, that can be drew from the deck of token when you're playing a soloGame.
 * This token makes you move your faith point marker ahead of faithPoints steps, and makes you shuffle the SoloActionDeck
 * It calls an observer to make the SoloTable move the faith point marker
 */

public class ShuffleToken extends FaithPointToken {

    public ShuffleToken(int faithPoints, String name) {
        super(faithPoints, name);
        shuffleToken = true;
    }
}