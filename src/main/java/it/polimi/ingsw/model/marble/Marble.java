package it.polimi.ingsw.model.marble;

import it.polimi.ingsw.exceptions.NegativeQuantityException;
import it.polimi.ingsw.model.LeaderCard.leaderEffects.Effect;
import it.polimi.ingsw.model.ResourceType;

import java.util.HashMap;
import java.util.List;

/*Warning: this class is not thread safe*/

/**
 * Generic Marble, real marbles are implemented in subclasses
 */
public abstract class Marble {
    /**
     * increments the specific resource counter in the Hashmap, if absent add the the key of the specific resource.
     *
     * @param resourceMap is modified in this Method
     * @param effects     is the List of effects.
     * @throws NegativeQuantityException if the mapped value accessed in the method is negative
     * @throws NegativeQuantityException doesn't ensure that the hashMap is completely valid
     * @throws NullPointerException      if the parameters are null
     */
    public abstract boolean onActivate(HashMap<ResourceType, Integer> resourceMap, List<Effect> effects) throws NegativeQuantityException, NullPointerException;

    public abstract boolean isWhiteMarble();

    public abstract MarbleType getMarbleType();

    @Override
    public abstract String toString();
}
