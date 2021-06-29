package it.polimi.ingsw.exceptions;

/**
 * This exception is thrown an entire DevGrid column is empty.
 */
public class EmptyDevColumnException extends Exception {

    /**
     * Constructs an EmptyDevColumnException with the specified detail message
     * @param s the detail message
     */
    public EmptyDevColumnException(String s) {
        super(s);
    }

}
