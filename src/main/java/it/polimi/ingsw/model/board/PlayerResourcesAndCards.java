package it.polimi.ingsw.model.board;

import it.polimi.ingsw.model.devCards.DevCard;
import it.polimi.ingsw.model.resources.ResourceType;

import java.util.Collection;
import java.util.HashMap;

/**
 * utility class used to check whether the LeaderCards requirements are fulfilled by the player
 */
public class PlayerResourcesAndCards {
    private final HashMap<ResourceType, Integer> resources;
    private final Collection<DevCard> devCards;

    /**
     * Constructs a PlayerResourcesAndCards filled with the specified resources and DevCards
     * @param resources all the resources owned by the player
     * @param devCards all the devCards owned by the player
     */
    public PlayerResourcesAndCards(HashMap<ResourceType, Integer> resources, Collection<DevCard> devCards) {
        this.resources = resources;
        this.devCards = devCards;
    }

    /**
     * Returns the resources stored into this object
     * @return the resources stored into this object
     */
    public HashMap<ResourceType, Integer> getResources() {
        return resources;
    }

    /**
     * Returns the DevCards stored into this object
     * @return the DevCards stored into this object
     */
    public Collection<DevCard> getDevCards() {
        return devCards;
    }
}
