package it.polimi.ingsw.network.messages;

public class LoginMessage {
    private String message;

    public LoginMessage(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
