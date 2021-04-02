package it.polimi.ingsw.marble;

import it.polimi.ingsw.Effect;
import it.polimi.ingsw.ResourceType;
import it.polimi.ingsw.exceptions.*;
import java.util.HashMap;

/*Warning: this class is not thread safe*/

/*Generic Marble, real marbles actually in the game are implemented in subclasses*/
public abstract class Marble {
    /*increments the specific resource counter in the Hashmap, if absent add the the key of the specific resource.
    * there can't be a negative quantity of resource in the map*/
    public abstract boolean onActivate(HashMap <ResourceType, Integer> resourceMap, Effect effect) throws NegativeQuantityException;

    @Override
    public abstract String toString();
}
