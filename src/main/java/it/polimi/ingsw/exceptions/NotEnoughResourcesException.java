package it.polimi.ingsw.exceptions;

/**
 * This exception is thrown when the player tries to remove more resources than they posses.
 */
public class NotEnoughResourcesException extends Exception {

    /**
     * Constructs a NotEnoughResourcesException with the specified detail message
     * @param s the detail message
     */
    public NotEnoughResourcesException(String s) {
        super(s);
    }
}
