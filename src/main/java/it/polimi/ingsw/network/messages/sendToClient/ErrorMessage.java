package it.polimi.ingsw.network.messages.sendToClient;

public class ErrorMessage implements ResponseInterface{
    private String errorMessage;

    public ErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    @Override
    public ResponseType getResponseType() {
        return ResponseType.ERROR;
    }
}
