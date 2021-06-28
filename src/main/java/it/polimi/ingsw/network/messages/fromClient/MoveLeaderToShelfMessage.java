package it.polimi.ingsw.network.messages.fromClient;

import it.polimi.ingsw.model.resources.ResourceType;

/**
 * This message is used to move resources from an ExtraSlot LeaderCard to a Depot shelf.
 */
public class MoveLeaderToShelfMessage extends Message {
    private ResourceType res;
    private int quantity;
    private int destShelf;

    /**
     * Constructs a MoveLeaderToShelfMessage
     * @param res the type of the resources to be moved
     * @param quantity the amount of the resources to be moved
     * @param destShelf the destination shelf of the resources to be moved
     */
    public MoveLeaderToShelfMessage(ResourceType res, int quantity, int destShelf) {
        this.res = res;
        this.quantity = quantity;
        this.destShelf = destShelf;
    }

    /**
     * Returns the type of the resources to be moved
     * @return the type of the resources to be moved
     */
    public ResourceType getRes() {
        return res;
    }

    /**
     * Returns the amount of the resources to be moved
     * @return the amount of the resources to be moved
     */
    public int getQuantity() {
        return quantity;
    }

    /**
     * Returns the destination shelf of the resources to be moved
     * @return the destination shelf of the resources to be moved
     */
    public int getDestShelf() {
        return destShelf;
    }


}
