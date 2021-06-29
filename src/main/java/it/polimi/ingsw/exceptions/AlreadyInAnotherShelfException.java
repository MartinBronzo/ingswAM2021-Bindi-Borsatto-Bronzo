package it.polimi.ingsw.exceptions;

/**
 * This exception is thrown when the player tries to add a resource onto a shelf when this kind of resource is being stored onto
 * another shelf.
 */
public class AlreadyInAnotherShelfException extends Exception {

    /**
     * Constructs an AlreadyInAnotherShelfException with the specified detail message
     * @param s the detail message
     */
    public AlreadyInAnotherShelfException(String s) {
        super(s);
    }
}
