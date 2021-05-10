package it.polimi.ingsw.network.messages;

public class ErrorMessage {
    private String result;
    private String errorMessage;

    public ErrorMessage(String result, String errorMessage) {
        this.result = result;
        this.errorMessage = errorMessage;
    }

    public String getResult() {
        return result;
    }

    public String getErrorMessage() {
        return errorMessage;
    }
}
