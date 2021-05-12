package it.polimi.ingsw.view.readOnlyModel.player;

import it.polimi.ingsw.model.ResourceType;

public class DepotShelf {
    private ResourceType resourceType;
    private int quantity;

    public DepotShelf(ResourceType resourceType, int quantity) {
        this.resourceType = resourceType;
        this.quantity = quantity;
    }

    public DepotShelf(){
        this.resourceType = null;
        this.quantity = -1;
    }

    public ResourceType getResourceType() {
        return resourceType;
    }

    public int getQuantity() {
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
