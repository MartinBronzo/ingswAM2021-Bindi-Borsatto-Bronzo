package it.polimi.ingsw.exceptions;

/**
 * This exception is thrown when the player specifies a nickname that is already used by another user logged into the server.
 */
public class NotAvailableNicknameException extends Exception {

    /**
     * Constructs a NotAvailableNicknameException with the specified detail message
     * @param s the detail message
     */
    public NotAvailableNicknameException(String s) {
        super(s);
    }
}
