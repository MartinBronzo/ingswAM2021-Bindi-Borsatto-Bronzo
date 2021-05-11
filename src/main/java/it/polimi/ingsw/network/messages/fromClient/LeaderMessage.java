package it.polimi.ingsw.network.messages.fromClient;

public class LeaderMessage {
    private int leader;

    public LeaderMessage(int leader) {
        this.leader = leader;
    }

    public int getLeader() {
        return leader;
    }
}
