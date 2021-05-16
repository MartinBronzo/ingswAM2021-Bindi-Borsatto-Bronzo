package it.polimi.ingsw.network.messages.sendToClient;

public class GeneralInfoStringMessage implements ResponseInterface {
    String message;

    public GeneralInfoStringMessage(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    @Override
    public ResponseType getResponseType() {
        return ResponseType.INFOSTRING;
    }
}
