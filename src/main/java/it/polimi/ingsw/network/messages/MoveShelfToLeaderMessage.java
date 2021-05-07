package it.polimi.ingsw.network.messages;

public class MoveShelfToLeaderMessage {
    private int numShelf, quantity;

    public MoveShelfToLeaderMessage(int numShelf, int quantity) {
        this.numShelf = numShelf;
        this.quantity = quantity;
    }

    public int getNumShelf() {
        return numShelf;
    }

    public int getQuantity() {
        return quantity;
    }
}
