package it.polimi.ingsw.exceptions;

/**
 * This exception is thrown when the player tries to draw a card from an empty deck.
 */
public class EmptyDeckException extends Exception {

    /**
     * Constructs an EmptyDeckException with the specified detail message
     * @param s the detail message
     */
    public EmptyDeckException(String s) {
        super(s);
    }


}
