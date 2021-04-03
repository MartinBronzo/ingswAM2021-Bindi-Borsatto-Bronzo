package it.polimi.ingsw.marble;

import it.polimi.ingsw.Effect;
import it.polimi.ingsw.ResourceType;
import it.polimi.ingsw.exceptions.NegativeQuantityException;

import java.util.HashMap;

/*Warning: this class is not thread safe*/
public class WhiteMarble extends Marble {

    /**  change mapped values in resourceMap according to effect.whiteMarbleEffect() method, if resource mapping is absent add the the keys of the specific resources.
     *   @throws NegativeQuantityException if the mapped value accessed in the method is negative
     *   @throws NegativeQuantityException doesn't ensure that the hashMap is completely valid
     *   @throws NullPointerException if ResourceMap is null or effect is null
     *   @param resourceMap is indirectly modified in this Method
     *   @param effect determines how resourceMap is modified
     *   */
    @Override
    public boolean onActivate(HashMap <ResourceType, Integer> resourceMap, Effect effect) throws NegativeQuantityException, NullPointerException{
        if(resourceMap==null || effect==null) throw new NullPointerException("onActivate WhiteMarble: not expected NULL parameters");

        effect.whiteMarbleEffect(resourceMap);
        return true;
    }

    @Override
    public String toString() {
        return "WhiteMarble{}";
    }
}
