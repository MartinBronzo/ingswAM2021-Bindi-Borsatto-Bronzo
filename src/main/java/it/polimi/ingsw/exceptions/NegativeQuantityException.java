package it.polimi.ingsw.exceptions;

/**
 * This exception is thrown when a method parameter is not supposed to be negative but it actually is.
 */
public class NegativeQuantityException extends Exception {

    /**
     * Constructs a NegativeQuantityException with the specified detail message
     * @param error the detail message
     */
    public NegativeQuantityException(String error) {
        super(error);
    }
}
