package it.polimi.ingsw.network.messages.sendToClient;

/**
 * This message is used to let know that an error has occurred.
 */
public class ErrorMessage implements ResponseInterface {
    private String errorMessage;

    /**
     * Constructs an ErrorMessage
     * @param errorMessage a message to be shown to the player
     */
    public ErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    /**
     * Returns a message to be shown to the player
     * @return a message to be shown to the player
     */
    public String getErrorMessage() {
        return errorMessage;
    }

    @Override
    public ResponseType getResponseType() {
        return ResponseType.ERROR;
    }
}
