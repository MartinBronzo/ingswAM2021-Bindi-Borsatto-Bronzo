package it.polimi.ingsw.network.messages.sendToClient;

public class ErrorMessage {
    private String status;
    private String errorMessage;

    public ErrorMessage(String errorMessage) {
        this.status = "Error";
        this.errorMessage = errorMessage;
    }

    public String getErrorMessage() {
        return errorMessage;
    }
}
