package it.polimi.ingsw.marble;

import it.polimi.ingsw.Effect;
import it.polimi.ingsw.ResourceType;
import it.polimi.ingsw.exceptions.NegativeQuantityException;

import java.util.HashMap;

/*Warning: this class is not thread safe*/
public class GreyMarble extends Marble {
     public boolean onActivate(HashMap <ResourceType, Integer> resourceMap, Effect effect) throws NegativeQuantityException{
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
