package it.polimi.ingsw.marble;

import it.polimi.ingsw.Effect;
import it.polimi.ingsw.ResourceType;
import it.polimi.ingsw.exceptions.NegativeQuantityException;

import java.util.HashMap;

/*Warning: this class is not thread safe*/
public class WhiteMarble extends Marble {
     public boolean onActivate(HashMap <ResourceType, Integer> resourceMap, Effect effect) throws NegativeQuantityException, NullPointerException{
         if(effect==null) throw new NullPointerException("onActivate WhiteMarble: not expected NULL effect");

         effect.whiteMarbleEffect(resourceMap);
         return true;
    }

    @Override
    public String toString() {
        return "WhiteMarble{}";
    }
}
