package it.polimi.ingsw.network.messages.fromClient;

public class SetNumPlayerMessage {
    private int numPlayer;

    public SetNumPlayerMessage(int numPlayer) {
        this.numPlayer = numPlayer;
    }

    public int getNumPlayer() {
        return numPlayer;
    }
}