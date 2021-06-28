package it.polimi.ingsw.network.messages.sendToClient;

/**
 * This interface is implemented by all the messages that are sent by the GameController to the players.
 */
public interface ResponseInterface {

    /**
     * Returns the type of this response message
     * @return the type of this response message
     */
    public ResponseType getResponseType();
}
