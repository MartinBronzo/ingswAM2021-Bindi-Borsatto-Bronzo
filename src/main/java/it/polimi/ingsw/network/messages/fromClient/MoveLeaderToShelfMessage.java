package it.polimi.ingsw.network.messages.fromClient;

import it.polimi.ingsw.model.resources.ResourceType;

public class MoveLeaderToShelfMessage extends Message {
    private ResourceType res;
    private int quantity;
    private int destShelf;

    public MoveLeaderToShelfMessage(ResourceType res, int quantity, int destShelf) {
        this.res = res;
        this.quantity = quantity;
        this.destShelf = destShelf;
    }

    public ResourceType getRes() {
        return res;
    }

    public int getDestShelf() {
        return destShelf;
    }

    public int getQuantity() {
        return quantity;
    }
}
