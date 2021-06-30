package it.polimi.ingsw.model.leaderCard.leaderEffects;

import it.polimi.ingsw.model.resources.ResourceType;

import java.util.HashMap;

/**
 * This class implements the effect of a LeaderCard which let the player buy a DevCard at a discounted price. This class computes the discount on a specified cost.
 */
//
public class DiscountLeaderEffect extends Effect {
    private final ResourceType discountType;
    private final int discountAmount;

    /**
     * Constructs a DiscountLeaderEffect without the resource type it discounts and the amount of the discount set
     */
    public DiscountLeaderEffect() {
        this.discountType = null;
        this.discountAmount = 0;
    }

    /**
     * Constructs a DiscountLeaderEffect which will apply a discount of a specified amount on a specified resources type (if the given cost has that type)
     *
     * @param discountType   the type which will be discounted
     * @param discountAmount the amount of the discount
     * @throws IllegalArgumentException if the amount is less than or equal to 0 or if the discount type is FaithPoint
     */
    public DiscountLeaderEffect(ResourceType discountType, int discountAmount) throws IllegalArgumentException {
        if (discountAmount <= 0)
            throw new IllegalArgumentException("The amount of the discount must be a positive integer greater than 0!");
        if (discountType.equals(ResourceType.FAITHPOINT))
            throw new IllegalArgumentException("The cost of DevCards is not in FaithPoints!");
        this.discountType = discountType;
        this.discountAmount = discountAmount;
    }

    /**
     * Constructs a clone of the specified DiscountLeaderEffect
     *
     * @param original the effect to be cloned
     */
    public DiscountLeaderEffect(DiscountLeaderEffect original) {
        this(original.discountType, original.discountAmount);
    }

    @Override
    public Effect getClone() {
        return new DiscountLeaderEffect(this);
    }

    /**
     * Apply the discount this Effect represents to the specified cost
     * @param cost the cost of a DevCard
     * @return true if the discount has been successfully applied, false if there was no need to apply the discount
     */
    public boolean discountEffect(HashMap<ResourceType, Integer> cost) {
        if (cost == null)
            throw new NullPointerException("Cost is a null pointer!");
        if (cost.isEmpty())
            return false;
        //If the cost doesn't contain the type of the effect, then there is no need to compute the discount
        if (!cost.containsKey(this.discountType))
            return false;
        int futureCost = cost.get(this.discountType) - this.discountAmount;
        /*if (futureCost < 0)
            futureCost = 0;*/
        //If the discounted amount for the resource is less than or equal to zero, then the resource is removed from the cost
        if (futureCost <= 0) {
            cost.remove(this.discountType);
            return true;
        }
        //Updates the cost
        cost.put(this.discountType, futureCost);
        return true;
    }

    /**
     * Returns the resource type this Effect discounts for
     * @return the resource type this Effect discounts for
     */
    public ResourceType getDiscountType() {
        return discountType;
    }

    /**
     * Returns the amount this Effect discounts for
     * @return the amount this Effect discounts for
     */
    public int getDiscountAmount() {
        return discountAmount;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null)
            return false;
        if (obj == this)
            return true;
        if (!(obj instanceof DiscountLeaderEffect))
            return false;
        DiscountLeaderEffect tmp = (DiscountLeaderEffect) obj;
        return this.discountAmount == tmp.discountAmount && this.discountType.equals(tmp.discountType);
    }

    @Override
    public boolean isOneShotCard() {
        return false;
    }
}
