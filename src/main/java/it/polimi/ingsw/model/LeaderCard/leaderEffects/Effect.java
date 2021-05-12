package it.polimi.ingsw.model.LeaderCard.leaderEffects;

import it.polimi.ingsw.exceptions.NegativeQuantityException;
import it.polimi.ingsw.model.ResourceType;

import java.io.Serializable;
import java.util.HashMap;

/**
 * This class represents all the possible effects a LeaderCard may have. Right now, there are four of them:
 * discounting the cost of a DevCard, adding extra slots to the Depot, making WhiteMarbles be worth some Resources,
 * and increasing the production power.
 */
//
public class Effect implements Serializable {

    /**
     * Makes the WithMarble worth of some Resources
     *
     * @param resourceMap the map of the resources so far found this effect modifies
     * @return a boolean
     * @throws NegativeQuantityException
     */
    public boolean whiteMarbleEffect(HashMap<ResourceType, Integer> resourceMap) throws NegativeQuantityException {
        return true;
    }

    /**
     * Returns the type of the resources the extra slots given by the effect of the LeaderCard can hold
     *
     * @return the type of the resources
     */
    public ResourceType extraSlotGetType() {
        return null;
    }

    /**
     * Returns the number of extra slots which the effect of the LeaderCard enables the player to have
     *
     * @return the number of extra slots
     */
    public int extraSlotGetResourceNumber() {
        return 0;
    }

    /**
     * Discounts the given cost of a DevCard (it applies the discount directly on the specified cost)
     *
     * @param cost the cost of a DevCard
     * @return true if the discount has been applied, false otherwise (the specified cost didn't have an amount for the type of this effect)
     */
    public boolean discountEffect(HashMap<ResourceType, Integer> cost) {
        return true;
    }

    /**
     * Gives the player an extra Production Power which let the player choose the type of the production output and adds other extra outputs
     *
     * @param desiredOutput what the player wants to be produced
     * @return a boolean
     */
    public HashMap<ResourceType, Integer> extraProductionEffect(ResourceType desiredOutput) {
        return new HashMap<>();
    }

    /**
     * Returns a clone of this Effect
     *
     * @return a clone
     */
    public Effect getClone() {
        return new Effect();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null)
            return false;
        if (obj == this)
            return true;
        if (!(obj instanceof Effect))
            return false;
        if (obj instanceof DiscountLeaderEffect)
            return false;
        if (obj instanceof ExtraProductionLeaderEffect)
            return false;
        if (obj instanceof ExtraSlotLeaderEffect)
            return false;
        return !(obj instanceof WhiteMarbleLeaderEffect);
    }

    public boolean isOneShotCard() {
        return false;
    }

    /**
     * Returns the resources required in order to produce using this effect
     *
     * @return the required resources
     */
    public HashMap<ResourceType, Integer> getRequiredInput() {
        return new HashMap<>();
    }
}
