package it.polimi.ingsw.model.marble;

import it.polimi.ingsw.exceptions.NegativeQuantityException;
import it.polimi.ingsw.model.LeaderCard.leaderEffects.Effect;
import it.polimi.ingsw.model.ResourceType;

import java.util.HashMap;
import java.util.List;

/*Warning: this class is not thread safe*/
public class WhiteMarble extends Marble {

    /**
     * change mapped values in resourceMap according to effect.whiteMarbleEffect() method, if resource mapping is absent add the the keys of the specific resources.
     *
     * @param resourceMap is indirectly modified in this Method
     * @param effects     is The List of effects where the first effect is activated and removed from the list
     * @throws NegativeQuantityException if the mapped value accessed in the method is negative
     * @throws NegativeQuantityException doesn't ensure that the hashMap is completely valid
     * @throws NullPointerException      if ResourceMap is null or effect is null
     */
    @Override
    public boolean onActivate(HashMap<ResourceType, Integer> resourceMap, List<Effect> effects) throws NegativeQuantityException, NullPointerException, IndexOutOfBoundsException {
        if (resourceMap == null || effects == null)
            throw new NullPointerException("onActivate WhiteMarble: not expected NULL parameters");

        effects.remove(0).whiteMarbleEffect(resourceMap);
        return true;
    }

    @Override
    public String toString() {
        return "WhiteMarble{}";
    }
}
