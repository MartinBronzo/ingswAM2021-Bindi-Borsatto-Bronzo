package it.polimi.ingsw.network.messages.sendToClient;

@Deprecated
public class TurnInfoMessage implements ResponseInterface{
    private boolean toPlay;

    public TurnInfoMessage(boolean toPlay) {
        this.toPlay = toPlay;
    }

    public boolean isToPlay() {
        return toPlay;
    }

    @Override
    public ResponseType getResponseType() {
        return ResponseType.TURNINFO;
    }
}
