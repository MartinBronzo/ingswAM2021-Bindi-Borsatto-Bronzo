package it.polimi.ingsw.network.messages.sendToClient;

/**
 * This message is used to communicate the final scores of a Solo Game.
 */
public class SoloGameResultMessage implements ResponseInterface {
    boolean victory;
    String message;

    /**
     * Constructs a SoloGameResultMessage
     * @param victory true if the player won, false if Lorenzo won
     * @param message a message to be shown to the player
     */
    public SoloGameResultMessage(boolean victory, String message){
        this.victory = victory;
        this.message = message;
    }

    /**
     * Returns whether the player won the game
     * @return true if the player won, false if Lorenzo won
     */
    public boolean isVictory() {
        return victory;
    }

    /**
     * Returns the message to be shown to the player
     * @return the message to be shown to the player
     */
    public String getMessage() {
        return message;
    }

    @Override
    public ResponseType getResponseType() {
        return ResponseType.SOLOGAMERESULT;
    }
}
