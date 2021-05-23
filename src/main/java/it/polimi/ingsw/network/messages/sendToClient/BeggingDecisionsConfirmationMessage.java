package it.polimi.ingsw.network.messages.sendToClient;

public class BeggingDecisionsConfirmationMessage implements ResponseInterface {

    @Override
    public ResponseType getResponseType() {
        return ResponseType.SETBEGINNINGDECISIONS;
    }
}
