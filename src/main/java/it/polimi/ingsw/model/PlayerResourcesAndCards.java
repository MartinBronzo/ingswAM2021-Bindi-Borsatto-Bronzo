package it.polimi.ingsw.model;

import it.polimi.ingsw.model.DevCards.DevCard;

import java.util.Collection;
import java.util.HashMap;

public class PlayerResourcesAndCards {
    private HashMap<ResourceType, Integer> resources;
    private Collection<DevCard> devCards;

    public PlayerResourcesAndCards(HashMap<ResourceType, Integer> resources, Collection<DevCard> devCards) {
        this.resources = resources;
        this.devCards = devCards;
    }

    public HashMap<ResourceType, Integer> getResources() {
        return resources;
    }

    public Collection<DevCard> getDevCards() {
        return devCards;
    }
}
