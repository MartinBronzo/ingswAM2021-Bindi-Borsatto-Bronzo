package it.polimi.ingsw.network.messages.sendToClient;

public class SoloGameResultMessage implements ResponseInterface {
    boolean victory;
    String message;

    public SoloGameResultMessage(boolean victory, String message){
        this.victory = victory;
        this.message = message;
    }

    public boolean isVictory() {
        return victory;
    }

    public String getMessage() {
        return message;
    }

    @Override
    public ResponseType getResponseType() {
        return ResponseType.SOLOGAMERESULT;
    }
}
