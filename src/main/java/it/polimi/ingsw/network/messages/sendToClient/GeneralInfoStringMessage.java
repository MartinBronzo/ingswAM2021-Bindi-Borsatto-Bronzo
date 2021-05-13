package it.polimi.ingsw.network.messages.sendToClient;

public class GeneralInfoStringMessage {
    String message;

    public GeneralInfoStringMessage(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
