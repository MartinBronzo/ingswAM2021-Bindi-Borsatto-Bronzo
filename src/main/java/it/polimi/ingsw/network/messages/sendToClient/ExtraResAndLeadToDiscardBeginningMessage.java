package it.polimi.ingsw.network.messages.sendToClient;

/**
 * This message is used to send the player the begging of the game information, that is the amount of LeaderCards to discard and of resources
 * to take, and the player's order in the game.
 */
public class ExtraResAndLeadToDiscardBeginningMessage implements ResponseInterface {
    private int numRes;
    private int numLeader;
    private int order;

    /**
     * Constructs an ExtraResAndLeadToDiscardBeginningMessage
     * @param numRes the resources the player must take
     * @param numLeader the number of LeaderCards needs to discard
     * @param order the player's order in the game
     */
    public ExtraResAndLeadToDiscardBeginningMessage(int numRes, int numLeader, int order) {
        this.numRes = numRes;
        this.numLeader = numLeader;
        this.order = order;
    }

    /**
     * Returns the resources the player must take
     * @return the resources the player must take
     */
    public int getNumRes() {
        return numRes;
    }

    /**
     * Returns the number of LeaderCards needs to discard
     * @return the number of LeaderCards needs to discard
     */
    public int getNumLeader() {
        return numLeader;
    }

    /**
     * Returns the player's order in the game
     * @return the player's order in the game
     */
    public int getOrder() {
        return order;
    }

    @Override
    public ResponseType getResponseType() {
        return ResponseType.EXTRARESOURCEANDLEADERCARDBEGINNING;
    }
}
