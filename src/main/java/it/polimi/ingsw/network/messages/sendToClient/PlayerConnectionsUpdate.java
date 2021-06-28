package it.polimi.ingsw.network.messages.sendToClient;

import it.polimi.ingsw.view.lightModel.Game;

/**
 * This message is used to send an update of the Model after a disconnected player has been put back into the game.
 */
public class PlayerConnectionsUpdate implements ResponseInterface{
    private Game update;
    private String changedPlayer;

    /**
     * Constructs a PlayerConnectionsUpdate
     * @param update the Model update
     * @param changedPlayer the player who used to be disconnected
     */
    public PlayerConnectionsUpdate(Game update, String changedPlayer) {
        this.update = update;
        this.changedPlayer = changedPlayer;
    }

    /**
     * Returns the Model update
     * @return the Model update
     */
    public Game getUpdate() {
        return update;
    }

    /**
     * Returns the player who used to be disconnected
     * @return the player who used to be disconnected
     */
    public String getChangedPlayer() {
        return changedPlayer;
    }

    @Override
    public ResponseType getResponseType() {
        return ResponseType.PLAYERCONNECTIONUPDATE;
    }
}
