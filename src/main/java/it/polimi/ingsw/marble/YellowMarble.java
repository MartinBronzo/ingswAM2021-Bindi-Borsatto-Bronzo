package it.polimi.ingsw.marble;

import it.polimi.ingsw.LeaderCard.leaderEffects.Effect;
import it.polimi.ingsw.ResourceType;
import it.polimi.ingsw.exceptions.NegativeQuantityException;

import java.util.HashMap;
import java.util.List;

/*Warning: this class is not thread safe*/
public class YellowMarble extends Marble {

    /**
     * increments the ResourceType.COIN counter in the Hashmap, if absent add the the key of the specific resource.
     *
     * @param resourceMap is modified in this Method
     * @param effects
     * @throws NegativeQuantityException if the mapped value accessed in the method is negative
     * @throws NegativeQuantityException doesn't ensure that the hashMap is completely valid
     * @throws NullPointerException      if ResourceMap is null
     */
    public synchronized boolean onActivate(HashMap<ResourceType, Integer> resourceMap, List<Effect> effects) throws NegativeQuantityException {
        if (resourceMap == null)
            throw new NullPointerException("onActivate YellowMarble: not expected NULL resourceMap");

        ResourceType resource = ResourceType.COIN;
        Integer resourceNumber;

        resourceNumber = resourceMap.getOrDefault(resource, 0);
        if (resourceNumber < 0)
            throw new NegativeQuantityException("onActivate yellowMarble: Negative numbers of resources in hashmap");

        resourceNumber++;
        resourceMap.put(resource, resourceNumber);
        return true;
    }

    @Override
    public String toString() {
        return "YellowMarble{}";
    }
}
