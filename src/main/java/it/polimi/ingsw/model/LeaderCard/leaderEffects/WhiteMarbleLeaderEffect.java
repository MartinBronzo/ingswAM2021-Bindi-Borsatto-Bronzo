package it.polimi.ingsw.model.LeaderCard.leaderEffects;

import it.polimi.ingsw.exceptions.NegativeQuantityException;
import it.polimi.ingsw.model.ResourceType;
import it.polimi.ingsw.model.marble.Marble;
import it.polimi.ingsw.model.marble.MarbleType;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * This class implements the extra effect which enables the player to gain some resources when they get a WhiteMarble from the market
 */
//
public class WhiteMarbleLeaderEffect extends Effect {
    private final ResourceType extraResourceType;
    private final int extraResourceAmount;

    public WhiteMarbleLeaderEffect() {
        this.extraResourceType = null;
        this.extraResourceAmount = 1;
    }

    /**
     * Constructs a WhiteMarbleLeaderEffect which, by default, make a WhiteMarble worth one resource of the specified type
     *
     * @param extraResourceType the type of the resources the WhiteMarble will be worth
     */
    public WhiteMarbleLeaderEffect(ResourceType extraResourceType) {
        this.extraResourceType = extraResourceType;
        this.extraResourceAmount = 1;
    }

    /**
     * Constructs a clone of the specified WhiteMarbleLeaderEffect
     *
     * @param original the effect to be cloned
     */
    public WhiteMarbleLeaderEffect(WhiteMarbleLeaderEffect original) {
        this(original.extraResourceType);
    }

    @Override
    public Effect getClone() {
        return new WhiteMarbleLeaderEffect(this);
    }


    //With extra constructs (which enable to set the extraResourceAmount attribute of the object as needed), we are able to implement a more custom effect
    @Override
    public boolean whiteMarbleEffect(HashMap<ResourceType, Integer> resourceMap) throws NegativeQuantityException {
        //Gets the MarbleType corresponding to the ResourceType
        MarbleType tmp = this.extraResourceType.getCorrespondingMarble();
        //Gets the Marble of the just-found MarbleType
        Marble marble = tmp.getMarble();
        //Calls the onActivate method of the Marble because the WhiteMarble "acts like" that marble thanks to this effect
        boolean result = true;
        for (int i = 0; i < this.extraResourceAmount; i++)
            if (!marble.onActivate(resourceMap, new ArrayList<>()))
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

    @Override
    public boolean equals(Object obj) {
        if (obj == null)
            return false;
        if (obj == this)
            return true;
        if (!(obj instanceof WhiteMarbleLeaderEffect))
            return false;
        WhiteMarbleLeaderEffect tmp = (WhiteMarbleLeaderEffect) obj;
        return this.extraResourceType.equals(tmp.extraResourceType) && this.extraResourceAmount == tmp.extraResourceAmount;
    }

    @Override
    public boolean isOneShotCard() {
        return false;
    }
}
