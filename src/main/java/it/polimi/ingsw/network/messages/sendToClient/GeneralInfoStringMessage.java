package it.polimi.ingsw.network.messages.sendToClient;

/**
 * This message is used to giving the player a generic info message.
 */
public class GeneralInfoStringMessage implements ResponseInterface {
    String message;

    /**
     * Constructs a GeneralInfoStringMessage
     * @param message the message to be given to the player
     */
    public GeneralInfoStringMessage(String message) {
        this.message = message;
    }

    /**
     * Returns the message to be given to the player
     * @return the message to be given to the player
     */
    public String getMessage() {
        return message;
    }

    @Override
    public ResponseType getResponseType() {
        return ResponseType.INFOSTRING;
    }
}
