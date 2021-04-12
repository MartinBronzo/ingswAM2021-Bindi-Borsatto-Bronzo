package it.polimi.ingsw.marble;

import it.polimi.ingsw.LeaderCard.leaderEffects.Effect;
import it.polimi.ingsw.ResourceType;
import it.polimi.ingsw.exceptions.NegativeQuantityException;

import java.util.HashMap;

/*Warning: this class is not thread safe*/
public class GreyMarble extends Marble {

    /**  increments the ResourceType.STONE counter in the Hashmap, if absent add the the key of the specific resource.
     *   @throws NegativeQuantityException if the mapped value accessed in the method is negative
     *   @throws NegativeQuantityException doesn't ensure that the hashMap is completely valid
     *   @throws NullPointerException if ResourceMap is null
     *   @param resourceMap is modified in this Method
     *   */
    @Override
     public synchronized boolean onActivate(HashMap <ResourceType, Integer> resourceMap, Effect effect) throws NegativeQuantityException{
        if (resourceMap==null) throw new NullPointerException("onActivate GreyMarble: not expected NULL resourceMap");


        ResourceType resource=ResourceType.STONE;
         Integer resourceNumber;

         resourceNumber = resourceMap.getOrDefault(resource, 0);
         if (resourceNumber<0) throw new NegativeQuantityException("onActivate GreyMarble:Negative numbers of resources in hashmap");

        resourceNumber++;
        resourceMap.put(resource,resourceNumber);
        return true;
    }

    @Override
    public String toString() {
        return "GreyMarble{}";
    }
}
