package it.polimi.ingsw.network.messages.sendToClient;

public class SetNumPlayersConfirmationMessage implements ResponseInterface{
    private int confirmedNumPlayers;

    public SetNumPlayersConfirmationMessage(int confirmedNumPlayers) {
        this.confirmedNumPlayers = confirmedNumPlayers;
    }

    public int getConfirmedNumPlayers() {
        return confirmedNumPlayers;
    }

    @Override
    public ResponseType getResponseType() {
        return ResponseType.SETNUMPLAYERCONF;
    }
}
