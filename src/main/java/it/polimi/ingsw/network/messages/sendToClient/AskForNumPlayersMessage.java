package it.polimi.ingsw.network.messages.sendToClient;

public class AskForNumPlayersMessage implements ResponseInterface {
    private String message;

    public AskForNumPlayersMessage(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    @Override
    public ResponseType getResponseType() {
        return ResponseType.ASKFORNUMPLAYERS;
    }
}
