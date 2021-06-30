package it.polimi.ingsw.network.messages.sendToClient;

/**
 * This message is used to ask the player who's constructing a game to specify the number of players in the game.
 */
public class AskForNumPlayersMessage implements ResponseInterface {
    private String message;

    /**
     * Constructs an AskFroNumPlayersMessage
     * @param message a message to be shown to the player
     */
    public AskForNumPlayersMessage(String message) {
        this.message = message;
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
        return ResponseType.ASKFORNUMPLAYERS;
    }
}
