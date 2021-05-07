package it.polimi.ingsw.network.messages;

import it.polimi.ingsw.model.ResourceType;

public class DepotParams {
    private ResourceType resourceType;
    private int qt;
    private int shelf;

    public DepotParams(ResourceType resourceType, int qt, int shelf) {
        this.resourceType = resourceType;
        this.qt = qt;
        this.shelf = shelf;
    }

}
