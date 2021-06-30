package it.polimi.ingsw.network.messages.sendToClient;

/**
 * This message is used to periodically ping the player.
 */
public class PingMessage implements ResponseInterface {
    private String message;

    /**
     * Constructs a PingMessage
     * @param message a string message
     */
    public PingMessage(String message) {
        this.message = message;
    }

    /**
     * Returns the string message stored into this PingMessage
     * @return the string message stored into this PingMessage
     */
    public String getMessage() {
        return message;
    }

    @Override
    public ResponseType getResponseType() {
        return ResponseType.PING;
    }
}
