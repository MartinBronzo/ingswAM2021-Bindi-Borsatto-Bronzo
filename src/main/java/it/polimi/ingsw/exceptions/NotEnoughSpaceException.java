package it.polimi.ingsw.exceptions;

/**
 * This exception is thrown when the player tries to add more resources than there is room for.
 */
public class NotEnoughSpaceException extends Exception {
    int availableSpace;

    /**
     * Constructs a NotEnoughSpaceException with the specified detail message
     * @param s the detail message
     * @param remainingSpace the space that is left where the player is trying to add to
     */
    public NotEnoughSpaceException(String s, int remainingSpace) {
        super(s);
        this.availableSpace = remainingSpace;
    }

    /**
     * Returns the space that is left where the player is trying to add to
     * @return the space that is left where the player is trying to add to
     */
    public int getAvailableSpace() {
        return availableSpace;
    }
}
