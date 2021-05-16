package it.polimi.ingsw.network.messages.sendToClient;

public class PingMessage implements ResponseInterface{
    private String message;

    public PingMessage(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    @Override
    public ResponseType getResponseType() {
        return ResponseType.PING;
    }
}
