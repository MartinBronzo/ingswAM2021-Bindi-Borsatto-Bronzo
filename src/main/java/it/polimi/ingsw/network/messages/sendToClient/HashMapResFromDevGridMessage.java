package it.polimi.ingsw.network.messages.sendToClient;

import it.polimi.ingsw.model.ResourceType;

import java.util.HashMap;

public class HashMapResFromDevGridMessage implements ResponseInterface {
    private HashMap<ResourceType, Integer> resources;

    public HashMapResFromDevGridMessage(HashMap<ResourceType, Integer> resources) {
        this.resources = resources;
    }

    public HashMap<ResourceType, Integer> getResources() {
        return resources;
    }

    @Override
    public ResponseType getResponseType() {
        return ResponseType.HASHMAPRESOURCESFROMDEVGRID;
    }
}
