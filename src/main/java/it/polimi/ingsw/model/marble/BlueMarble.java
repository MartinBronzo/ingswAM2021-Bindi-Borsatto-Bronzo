package it.polimi.ingsw.model.marble;

import it.polimi.ingsw.exceptions.NegativeQuantityException;
import it.polimi.ingsw.model.leaderCard.leaderEffects.Effect;
import it.polimi.ingsw.model.resources.ResourceType;

import java.util.HashMap;
import java.util.List;

/*Warning: this class is not thread safe*/

/**
 * This class represents Blue Marbles.
 */
public class BlueMarble extends Marble {

    /**
     * increments the ResourceType.SHIELD counter in the Hashmap, if absent add the the key of the specific resource.
     *
     * @param resourceMap is modified in this Method
     * @param effects     is the List of effects.
     * @throws NegativeQuantityException if the mapped value accessed in the method is negative
     * @throws NegativeQuantityException doesn't ensure that the hashMap is completely valid
     * @throws NullPointerException      if ResourceMap is null
     */
    @Override
    public synchronized boolean onActivate(HashMap<ResourceType, Integer> resourceMap, List<Effect> effects) throws NegativeQuantityException, NullPointerException {
        if (resourceMap == null) throw new NullPointerException("onActivate BlueMarble: not expected NULL resourceMap");

        ResourceType resource = ResourceType.SHIELD;
        Integer resourceNumber;

        resourceNumber = resourceMap.getOrDefault(resource, 0);
        if (resourceNumber < 0)
            throw new NegativeQuantityException("onActivate BlueMarble: Negative numbers of resources in hashmap");

        resourceNumber++;
        resourceMap.put(resource, resourceNumber);
        return true;
    }

    @Override
    public boolean isWhiteMarble() {
        return false;
    }

    @Override
    public String toString() {
        return "BlueMarble{}";
    }

    @Override
    public MarbleType getMarbleType() {
        return MarbleType.BLUEMARBLE;
    }
}
