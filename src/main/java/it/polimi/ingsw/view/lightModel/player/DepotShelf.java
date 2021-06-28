package it.polimi.ingsw.view.lightModel.player;

import it.polimi.ingsw.model.resources.ResourceType;

/**
 * This LightModel class contains the information needed to represent a Depot shelf.
 */
public class DepotShelf {
    private ResourceType resourceType;
    private Integer quantity;

    /**
     * Constructs a DepotShelf object
     * @param resourceType the type of the resources stored onto the shelf
     * @param quantity the amount of the resources stored onto the shelf
     */
    public DepotShelf(ResourceType resourceType, int quantity) {
        this.resourceType = resourceType;
        this.quantity = quantity;
    }

    /**
     * Constructs a DepotShelf object without any information stored inside
     */
    public DepotShelf() {
        this.resourceType = null;
        this.quantity = -1;
    }

    /**
     * Returns the type of the resources stored onto the shelf
     * @return the type of the resources stored onto the shelf
     */
    public ResourceType getResourceType() {
        return resourceType;
    }

    /**
     * Returns the amount of the resources stored onto the shelf
     * @return the amount of the resources stored onto the shelf
     */
    public Integer getQuantity() {
        return quantity;
    }

    @Override
    public String toString() {
        return "DepotShelf{" +
                "resourceType=" + resourceType +
                ", quantity=" + quantity +
                '}';
    }
}
