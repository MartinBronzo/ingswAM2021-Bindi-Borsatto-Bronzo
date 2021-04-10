package it.polimi.ingsw.LeaderCard.leaderEffects;

import it.polimi.ingsw.ResourceType;

import java.util.HashMap;

/**
 * This class implements the effect of a LeaderCard which let the player buy a DevCard at a discounted price. This class computes the discount on a specified cost.
 */
//
public class DiscountLeaderEffect extends Effect{
    private final ResourceType discountType;
    private final int discountAmount;

    /**
     * Constructs a DiscountLeaderEffect which will apply a discount of a specified amount on a specified resources type (if the given cost has that type)
     * @param discountType the type which will be discounted
     * @param discountAmount the amount of the discount
     * @throws IllegalArgumentException if the amount is less than or equal to 0 or if the discount type is FaithPoint
     */
    public DiscountLeaderEffect(ResourceType discountType, int discountAmount) throws IllegalArgumentException {
        if(discountAmount <= 0)
            throw new IllegalArgumentException("The amount of the discount must be a positive integer greater than 0!");
        if(discountType.equals(ResourceType.FAITHPOINT))
            throw new IllegalArgumentException("The cost of DevCards is not in FaithPoints!");
        this.discountType = discountType;
        this.discountAmount = discountAmount;
    }

    public boolean discountEffect(HashMap<ResourceType, Integer> cost){
        if(cost == null)
            throw new NullPointerException("Cost is a null pointer!");
        if(cost.isEmpty())
            return false;
        //If the cost doesn't contain the type of the effect, then there is no need to compute the discount
        if(!cost.containsKey(this.discountType))
            return false;
        int futureCost = cost.get(this.discountType) - this.discountAmount;
        if(futureCost < 0)
            futureCost = 0;
        //Updates the cost
        cost.put(this.discountType, futureCost);
        return true;
    }
}