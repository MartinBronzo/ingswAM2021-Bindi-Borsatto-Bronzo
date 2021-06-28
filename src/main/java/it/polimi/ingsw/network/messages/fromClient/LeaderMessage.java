package it.polimi.ingsw.network.messages.fromClient;

/**
 * This message is used to specify the index of a LeaderCard to be activated or discarded.
 */
public class LeaderMessage extends Message {
    private int leader;

    /**
     * Constructs a LeaderMessage
     * @param leader the index of the LeaderCard to be activated or discarded
     */
    public LeaderMessage(int leader) {
        this.leader = leader;
    }

    /**
     * Returns the index of the LeaderCard to be activated or discarded
     * @return the index of the LeaderCard to be activated or discarded
     */
    public int getLeader() {
        return leader;
    }
}
