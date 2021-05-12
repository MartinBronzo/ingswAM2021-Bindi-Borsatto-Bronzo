package it.polimi.ingsw.network.messages.fromClient;

public class LoginMessage extends Message {
    private String message;

    public LoginMessage(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
