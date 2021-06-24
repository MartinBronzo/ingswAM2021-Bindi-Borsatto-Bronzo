package it.polimi.ingsw.network.messages.sendToClient;

import it.polimi.ingsw.view.lightModel.Game;

public class PlayerConnectionsUpdate implements ResponseInterface{
    private Game update;
    private String changedPlayer;

    public PlayerConnectionsUpdate(Game update, String changedPlayer) {
        this.update = update;
        this.changedPlayer = changedPlayer;
    }

    public Game getUpdate() {
        return update;
    }

    public String getChangedPlayer() {
        return changedPlayer;
    }

    @Override
    public ResponseType getResponseType() {
        return ResponseType.PLAYERCONNECTIONUPDATE;
    }
}
