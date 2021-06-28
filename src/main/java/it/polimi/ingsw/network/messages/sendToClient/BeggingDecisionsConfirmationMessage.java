package it.polimi.ingsw.network.messages.sendToClient;

/**
 * This message is used to let the player know that their beginning of the game choices have correctly received and dealt with.
 */
public class BeggingDecisionsConfirmationMessage implements ResponseInterface {

    @Override
    public ResponseType getResponseType() {
        return ResponseType.SETBEGINNINGDECISIONS;
    }
}
