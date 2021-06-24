package it.polimi.ingsw.network.messages.sendToClient;

import it.polimi.ingsw.model.resources.ResourceType;

import java.util.HashMap;

public class HashMapResFromMarketMessage implements ResponseInterface {
    private HashMap<ResourceType, Integer> resources;

    public HashMapResFromMarketMessage(HashMap<ResourceType, Integer> resources) {
        this.resources = resources;
    }

    public HashMap<ResourceType, Integer> getResources() {
        return resources;
    }

    @Override
    public ResponseType getResponseType() {
        return ResponseType.HASHMAPRESOURCESFROMMARKET;
    }
}
