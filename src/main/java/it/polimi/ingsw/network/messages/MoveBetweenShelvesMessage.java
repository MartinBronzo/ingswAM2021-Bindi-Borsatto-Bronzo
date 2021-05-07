package it.polimi.ingsw.network.messages;

public class MoveBetweenShelvesMessage {
    private int sourceShelf, destShelf;

    public MoveBetweenShelvesMessage(int sourceShelf, int destShelf) {
        this.sourceShelf = sourceShelf;
        this.destShelf = destShelf;
    }

    public int getSourceShelf() {
        return sourceShelf;
    }

    public int getDestShelf() {
        return destShelf;
    }
}
