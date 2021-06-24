package it.polimi.ingsw.model.board;

import it.polimi.ingsw.model.devCards.DevCard;
import it.polimi.ingsw.model.resources.ResourceType;

import java.util.Collection;
import java.util.HashMap;

public class PlayerResourcesAndCards {
    private final HashMap<ResourceType, Integer> resources;
    private final Collection<DevCard> devCards;

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
