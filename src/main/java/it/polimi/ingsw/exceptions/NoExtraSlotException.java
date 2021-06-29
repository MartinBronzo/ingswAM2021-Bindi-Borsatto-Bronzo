package it.polimi.ingsw.exceptions;

/**
 * This exception is thrown when the player tries to add a resource to the ExtraSlot LeaderCards but there is no card which can hold that
 * kind of resources.
 */
public class NoExtraSlotException extends Exception {

    /**
     * Constructs a NoExtraSlotException with the specified detail message
     * @param s the detail message
     */
    public NoExtraSlotException(String s) {
        super(s);
    }
}