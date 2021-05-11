package it.polimi.ingsw.network.messages.sendToClient;

public class ErrorMessage {
    private String errorMessage;

    public ErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public String getErrorMessage() {
        return errorMessage;
    }
}
