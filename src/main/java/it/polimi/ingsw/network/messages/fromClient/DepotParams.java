package it.polimi.ingsw.network.messages.fromClient;

import it.polimi.ingsw.model.resources.ResourceType;

/**
 * This object represents information of resources stored onto a Depot shelf. It is used to specify the amount of resources that are
 * to be stored onto or removed from a Depot shelf.
 */
public class DepotParams {
    private ResourceType resourceType;
    private int qt;
    private int shelf;

    /**
     * Constructs a DepotParams object
     * @param resourceType the resource type of the resources at issue
     * @param qt the amount of the resources at issue
     * @param shelf the number of the shelf at issue
     */
    public DepotParams(ResourceType resourceType, int qt, int shelf) {
        this.resourceType = resourceType;
        this.qt = qt;
        this.shelf = shelf;
    }

    /**
     * Returns the resource type of the resources at issue
     * @return the resource type of the resources at issue
     */
    public ResourceType getResourceType() {
        return resourceType;
    }

    /**
     * Returns the amount of the resources at issue
     * @return the amount of the resources at issue
     */
    public int getQt() {
        return qt;
    }

    /**
     * Returns the number of the shelf at issue
     * @return the number of the shelf at issue
     */
    public int getShelf() {
        return shelf;
    }

    /**
     * Sets the amount of the resources at issue with the specified value
     * @param qt the amount to be set into the message
     */
    public void setQt(int qt) {
        this.qt = qt;
    }
}
