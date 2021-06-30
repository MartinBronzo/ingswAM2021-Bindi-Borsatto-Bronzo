package it.polimi.ingsw.network.messages.fromClient;

/**
 * This message is used to move resources between Depot shelves.
 */
public class MoveBetweenShelvesMessage extends Message {
    private int sourceShelf, destShelf;

    /**
     * Constructs a MoveBetweenShelvesMessage
     * @param sourceShelf the origin shelf of the resources to be moved
     * @param destShelf the destination shelf of the resoruces to be moved
     */
    public MoveBetweenShelvesMessage(int sourceShelf, int destShelf) {
        this.sourceShelf = sourceShelf;
        this.destShelf = destShelf;
    }

    /**
     * Returns the origin shelf of the resources to be moved
     * @return the origin shelf of the resources to be moved
     */
    public int getSourceShelf() {
        return sourceShelf;
    }

    /**
     * Returns the destination shelf of the resoruces to be moved
     * @return the destination shelf of the resoruces to be moved
     */
    public int getDestShelf() {
        return destShelf;
    }
}
