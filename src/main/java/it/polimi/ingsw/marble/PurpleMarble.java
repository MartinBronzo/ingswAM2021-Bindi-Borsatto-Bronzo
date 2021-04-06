package it.polimi.ingsw.marble;

import it.polimi.ingsw.leaderEffects.Effect;
import it.polimi.ingsw.ResourceType;
import it.polimi.ingsw.exceptions.*;
import java.util.HashMap;

/*Warning: this class is not thread safe*/
public class PurpleMarble extends Marble {

    /**  increments the ResourceType.SERVANT counter in the Hashmap, if absent add the the key of the specific resource.
     *   @throws NegativeQuantityException if the mapped value accessed in the method is negative
     *   @throws NegativeQuantityException doesn't ensure that the hashMap is completely valid
     *   @throws NullPointerException if ResourceMap is null
     *   @param resourceMap is modified in this Method
     *   */
    @Override
    public boolean onActivate(HashMap <ResourceType, Integer> resourceMap, Effect effect) throws NegativeQuantityException{
        if (resourceMap==null) throw new NullPointerException("onActivate PurpleMarble: not expected NULL resourceMap");

        ResourceType resource=ResourceType.SERVANT;
        Integer resourceNumber;

        resourceNumber = resourceMap.getOrDefault(resource, 0);
        if (resourceNumber<0) throw new NegativeQuantityException("onActivate PurpleMarble: Negative numbers of resources in hashmap");

        resourceNumber++;
        resourceMap.put(resource,resourceNumber);
        return true;
    }

    @Override
    public String toString() {
        return "PurpleMarble{}";
    }
}
