package it.polimi.ingsw.exceptions;

/**
 * This exception is thrown when the player tries to add more ExtraSlot LeaderCards than they are supposed to.
 */
public class FullExtraSlotException extends Exception {

    /**
     * Constructs a FullExtraSlotException with the specified detail message
     * @param s the detail message
     */
    public FullExtraSlotException(String s) {
        super(s);
    }
}
