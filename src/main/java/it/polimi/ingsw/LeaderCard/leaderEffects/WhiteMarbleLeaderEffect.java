package it.polimi.ingsw.LeaderCard.leaderEffects;

import it.polimi.ingsw.ResourceType;
import it.polimi.ingsw.exceptions.NegativeQuantityException;
import it.polimi.ingsw.marble.Marble;
import it.polimi.ingsw.marble.MarbleType;

import java.util.HashMap;

/**
 * This class implements the extra effect which enables the player to gain some resources when they get a WhiteMarble from the market
 */
//
public class WhiteMarbleLeaderEffect extends Effect{
    private final ResourceType extraResourceType;
    private final int extraResourceAmount;

    /**
     * Constructs a WhiteMarbleLeaderEffect which, by default, make a WhiteMarble worth one resource of the specified type
     * @param extraResourceType the type of the resources the WhiteMarble will be worth
     * @throws InterruptedException if the amount isn't a positive integer greater than 0
     */
    public WhiteMarbleLeaderEffect(ResourceType extraResourceType){
        this.extraResourceType = extraResourceType;
        this.extraResourceAmount = 1;
    }

    //With extra constructs (which enable to set the extraResourceAmount attribute of the object as needed), we are able to implement a more custom effect

    public boolean whiteMarbleEffect(HashMap<ResourceType, Integer> resourceMap) throws NegativeQuantityException {
        //Gets the MarbleType corresponding to the ResourceType
        MarbleType tmp = this.extraResourceType.getCorrespondingMarble();
        //Gets the Marbe of the just-found MarbleType
        Marble marble = tmp.getMarble();
        //Calls the onActivate method of the Marble because the WhiteMarble "acts like" that marble thanks to this effect
        boolean result = true;
        for(int i = 0; i < this.extraResourceAmount; i++)
            if(!marble.onActivate(resourceMap, new Effect()))
                result = false;
        return result;

    }

    //This method is used only for testing purposes
    public ResourceType getExtraResourceType() {
        return extraResourceType;
    }

    //This method is used only for testing purposes
    public int getExtraResourceAmount() {
        return extraResourceAmount;
    }
}
