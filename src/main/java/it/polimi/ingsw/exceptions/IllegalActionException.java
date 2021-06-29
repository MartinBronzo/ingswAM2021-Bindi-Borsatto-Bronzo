package it.polimi.ingsw.exceptions;

/**
 * This exception is thrown when the player does an action they are not supposed to.
 */
public class IllegalActionException extends Exception {

    /**
     * Constructs an IllegalActionException with the specified detail message
     * @param s the detail message
     */
    public IllegalActionException(String s) {
        super(s);
    }
}

