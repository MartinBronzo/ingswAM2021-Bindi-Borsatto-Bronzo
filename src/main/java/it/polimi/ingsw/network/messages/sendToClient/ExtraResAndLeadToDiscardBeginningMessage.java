package it.polimi.ingsw.network.messages.sendToClient;

public class ExtraResAndLeadToDiscardBeginningMessage implements ResponseInterface{
    private int numRes;
    private int numLeader;
    private int order;

    public ExtraResAndLeadToDiscardBeginningMessage(int numRes, int numLeader, int order) {
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

    @Override
    public ResponseType getResponseType() {
        return ResponseType.EXTRARESOURCEANDLEADERCARDBEGINNING;
    }
}
