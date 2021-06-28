package it.polimi.ingsw.network.messages.fromClient;

/**
 * This message is used to move resources from a Depot shelf to an ExtraSlot LeaderCard.
 */
public class MoveShelfToLeaderMessage extends Message {
    private int numShelf, quantity;

    /**
     * Constructs a MoveShelfToLeaderMessage
     * @param numShelf the number of the origin shelf of the resources to be moved
     * @param quantity the amount of the resources to be moved
     */
    public MoveShelfToLeaderMessage(int numShelf, int quantity) {
        this.numShelf = numShelf;
        this.quantity = quantity;
    }

    /**
     * Returns the number of the origin shelf of the resources to be moved
     * @return the number of the origin shelf of the resources to be moved
     */
    public int getNumShelf() {
        return numShelf;
    }

    /**
     * Returns the amount of the resources to be moved
     * @return the amount of the resources to be moved
     */
    public int getQuantity() {
        return quantity;
    }
}
