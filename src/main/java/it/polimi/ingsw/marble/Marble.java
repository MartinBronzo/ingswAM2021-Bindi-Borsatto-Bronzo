package it.polimi.ingsw.marble;

import it.polimi.ingsw.Effect;
import it.polimi.ingsw.ResourceType;
import it.polimi.ingsw.exceptions.*;
import java.util.HashMap;

/*Warning: this class is not thread safe*/

/**Generic Marble, real marbles are implemented in subclasses*/
public abstract class Marble {
    /** increments the specific resource counter in the Hashmap, if absent add the the key of the specific resource.
     *   @throws NegativeQuantityException if the mapped value accessed in the method is negative
     *   @throws NegativeQuantityException doesn't ensure that the hashMap is completely valid
     *   @throws NullPointerException if the parameters are null
     *   @param resourceMap is modified in this Method
     *   */
    public abstract boolean onActivate(HashMap <ResourceType, Integer> resourceMap, Effect effect) throws NegativeQuantityException, NullPointerException;

    @Override
    public abstract String toString();
}
