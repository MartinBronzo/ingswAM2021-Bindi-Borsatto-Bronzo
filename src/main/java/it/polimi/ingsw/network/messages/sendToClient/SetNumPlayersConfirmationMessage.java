package it.polimi.ingsw.network.messages.sendToClient;

/**
 * This message is used to confirm the player who is creating a game that the message containing the number of players they want in their game
 * has been received and correctly dealt with.
 */
public class SetNumPlayersConfirmationMessage implements ResponseInterface{
    private int confirmedNumPlayers;

    /**
     * Constructs a SetNumPlayersConfirmationMessage
     * @param confirmedNumPlayers the confirmed number of players the game creator has asked for
     */
    public SetNumPlayersConfirmationMessage(int confirmedNumPlayers) {
        this.confirmedNumPlayers = confirmedNumPlayers;
    }

    /**
     * Returns the confirmed number of players the game creator has asked for
     * @return the confirmed number of players the game creator has asked for
     */
    public int getConfirmedNumPlayers() {
        return confirmedNumPlayers;
    }

    @Override
    public ResponseType getResponseType() {
        return ResponseType.SETNUMPLAYERCONF;
    }
}
