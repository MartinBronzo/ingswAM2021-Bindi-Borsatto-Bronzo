package it.polimi.ingsw.client.readOnlyModel.player;

import it.polimi.ingsw.model.ResourceType;

public class DepotShelf {
    private ResourceType resourceType;
    private int quantity;

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
