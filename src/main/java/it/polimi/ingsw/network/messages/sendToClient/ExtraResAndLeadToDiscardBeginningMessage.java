package it.polimi.ingsw.network.messages.sendToClient;

public class ExtraResAndLeadToDiscardBeginningMessage {
    private final String status;
    private int numRes;
    private int numLeader;
    private int order;

    public ExtraResAndLeadToDiscardBeginningMessage(int numRes, int numLeader, int order) {
        this.status = "Ok";
        this.numRes = numRes;
        this.numLeader = numLeader;
        this.order = order;
    }

    public int getNumRes() {
        return numRes;
    }

    public int getNumLeader() {
        return numLeader;
    }

    public int getOrder() {
        return order;
    }
}
