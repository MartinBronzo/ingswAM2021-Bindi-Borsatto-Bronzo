package it.polimi.ingsw.view.lightModel.player;

import it.polimi.ingsw.model.resources.ResourceType;

public class DepotShelf {
    private ResourceType resourceType;
    private Integer quantity;

    public DepotShelf(ResourceType resourceType, int quantity) {
        this.resourceType = resourceType;
        this.quantity = quantity;
    }

    public DepotShelf() {
        this.resourceType = null;
        this.quantity = -1;
    }

    public ResourceType getResourceType() {
        return resourceType;
    }

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
