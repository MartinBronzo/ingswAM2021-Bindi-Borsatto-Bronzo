package it.polimi.ingsw.network.messages.sendToClient;

import it.polimi.ingsw.model.ResourceType;

import java.util.HashMap;

public class HashMapResFromProdCostMessage implements ResponseInterface {
    private HashMap<ResourceType, Integer> resources;

    public HashMapResFromProdCostMessage(HashMap<ResourceType, Integer> resources) {
        this.resources = resources;
    }

    public HashMap<ResourceType, Integer> getResources() {
        return resources;
    }

    @Override
    public ResponseType getResponseType() {
        return ResponseType.HASHMAPRESOURCESFROMPRODCOST;
    }
}
