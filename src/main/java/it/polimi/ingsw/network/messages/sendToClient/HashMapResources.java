package it.polimi.ingsw.network.messages.sendToClient;

import it.polimi.ingsw.model.ResourceType;

import java.util.HashMap;

public class HashMapResources {
    private HashMap<ResourceType, Integer> resources;

    public HashMapResources(HashMap<ResourceType, Integer> resources) {
        this.resources = resources;
    }

    public HashMap<ResourceType, Integer> getResources() {
        return resources;
    }
}
