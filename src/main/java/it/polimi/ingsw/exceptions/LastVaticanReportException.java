package it.polimi.ingsw.exceptions;

/**
 * This exception is thrown by the FaithTrack when the last Vatican has been activated. This is needed in order to signal that the game must end.
 */
public class LastVaticanReportException extends Exception {
    /**
     * The value returned by the effect of the last PopeCell (the one which activates the Vatican Report)
     */
    private boolean lastValueReturnedByCell;

    /**
     * Constructs a LastVaticanReportException with the specified message and boolean
     *
     * @param message                 the detail message
     * @param lastValueReturnedByCell the value returned by the effect of the last PopeCell
     */
    public LastVaticanReportException(String message, boolean lastValueReturnedByCell) {
        super(message);
        this.lastValueReturnedByCell = lastValueReturnedByCell;
    }

    /**
     * Returns the value returned by the effect of the last PopeCell
     *
     * @return the value returned by the effect of the last PopeCell
     */
    public boolean getLastValue() {
        return this.lastValueReturnedByCell;
    }
}
