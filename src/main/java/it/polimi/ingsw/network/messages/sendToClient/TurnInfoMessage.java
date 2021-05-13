package it.polimi.ingsw.network.messages.sendToClient;

public class TurnInfoMessage {
    private boolean toPlay;

    public TurnInfoMessage(boolean toPlay) {
        this.toPlay = toPlay;
    }

    public boolean isToPlay() {
        return toPlay;
    }
}
