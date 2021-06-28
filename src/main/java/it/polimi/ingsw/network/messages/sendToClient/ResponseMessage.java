package it.polimi.ingsw.network.messages.sendToClient;

/**
 * This is the container of the messages that the GameController wants to send to the player. This is what is actually being sent to the players.
 */
public class ResponseMessage implements ResponseInterface {
    private ResponseType responseType;
    private String responseContent;

    /**
     * Constructs a ResponseMessage which contains another ResponseInterface message with useful information for the player
     * @param responseType the type of the message this container keeps inside
     * @param responseContent the message this container keeps inside containing useful information for the client
     */
    public ResponseMessage(ResponseType responseType, String responseContent) {
        this.responseType = responseType;
        this.responseContent = responseContent;
    }

    /**
     * Returns the type of the message this container keeps inside
     * @return the type of the message this container keeps inside
     */
    public ResponseType getResponseType() {
        return responseType;
    }

    /**
     * Returns the message this container keeps inside containing useful information for the client
     * @return the message this container keeps inside containing useful information for the client
     */
    public String getResponseContent() {
        return responseContent;
    }
}
