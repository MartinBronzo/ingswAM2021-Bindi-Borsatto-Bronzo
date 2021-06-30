package it.polimi.ingsw.exceptions;

/**
 * This exception is thrown when the player gets their seventh DevCard.
 */
public class EndOfGameException extends Exception {

    /**
     * Constructs an EndOfGameException with the specified detail message
     * @param s the detail message
     */
    public EndOfGameException(String s) {
        super(s);
    }
}
