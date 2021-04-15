package it.polimi.ingsw.marble;

import it.polimi.ingsw.LeaderCard.leaderEffects.Effect;
import it.polimi.ingsw.ResourceType;
import it.polimi.ingsw.exceptions.NegativeQuantityException;

import java.util.HashMap;

/*Warning: this class is not thread safe*/
public class RedMarble extends Marble {

    /**
     * increments the ResourceType.FAITHPOINT counter in the Hashmap, if absent add the the key of the specific resource.
     *
     * @param resourceMap is modified in this Method
     * @throws NegativeQuantityException if the mapped value accessed in the method is negative
     * @throws NegativeQuantityException doesn't ensure that the hashMap is completely valid
     * @throws NullPointerException      if ResourceMap is null
     */
    @Override
    public synchronized boolean onActivate(HashMap<ResourceType, Integer> resourceMap, Effect effect) throws NegativeQuantityException {
        if (resourceMap == null)
            throw new NullPointerException("onActivate PurpleMarble: not expected NULL resourceMap");

        ResourceType resource = ResourceType.FAITHPOINT;
        Integer resourceNumber;

        resourceNumber = resourceMap.getOrDefault(resource, 0);
        if (resourceNumber < 0)
            throw new NegativeQuantityException("onActivate RedMarble: Negative numbers of resources in hashmap");

        resourceNumber++;
        resourceMap.put(resource, resourceNumber);
        return true;
    }

    @Override
    public String toString() {
        return "RedMarble{}";
    }
}
