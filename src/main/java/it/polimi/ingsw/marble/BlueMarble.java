package it.polimi.ingsw.marble;

import it.polimi.ingsw.Effect;
import it.polimi.ingsw.ResourceType;
import it.polimi.ingsw.exceptions.NegativeQuantityException;

import java.util.HashMap;

/*Warning: this class is not thread safe*/

public class BlueMarble extends Marble {

     /**  increments the ResourceType.SHIELD counter in the Hashmap, if absent add the the key of the specific resource.
      *   @throws NegativeQuantityException if the mapped value accessed in the method is negative
      *   @throws NegativeQuantityException doesn't ensure that the hashMap is completely valid
      *   @throws NullPointerException if ResourceMap is null
      *   @param resourceMap is modified in this Method
      *   */
     @Override
     public boolean onActivate(HashMap <ResourceType, Integer> resourceMap, Effect effect) throws NegativeQuantityException, NullPointerException{
         if (resourceMap==null) throw new NullPointerException("onActivate BlueMarble: not expected NULL resourceMap");
         
         ResourceType resource=ResourceType.SHIELD;
         Integer resourceNumber;

         resourceNumber = resourceMap.getOrDefault(resource, 0);
         if (resourceNumber<0) throw new NegativeQuantityException("onActivate BlueMarble: Negative numbers of resources in hashmap");

        resourceNumber++;
        resourceMap.put(resource,resourceNumber);
        return true;
    }

    @Override
    public String toString() {
        return "BlueMarble{}";
    }
}
