package it.polimi.ingsw.LeaderCardRequirementsTests;

import it.polimi.ingsw.PlayerBoard;
import it.polimi.ingsw.PlayerResourcesAndCards;
import it.polimi.ingsw.ResourceType;
import it.polimi.ingsw.exceptions.NegativeQuantityException;

/**
 * This class represents a requirement of a particular amount of a certain type of resources which may be necessary for some LeaderCard.
 * The player must have at least the amount of the resources specified inside the object to meet this requirement.
 */

public class CardRequirementResource extends Requirement {
    /**
     * The type of the required resources.
     */
    private ResourceType resourceType;
    /**
     * The amount of the required resources. It is a positive integer greater than 0.
     */
    private int quantity;

    /**
     * Constructs a CardRequirementResource of the specified ResourceType and quantity
     * @param resourceType the required type of the resources
     * @param quantity the required amount of the resources
     * @throws IllegalArgumentException if the given quantity is not a positive integer greater than 0
     */
    public CardRequirementResource(ResourceType resourceType, int quantity) throws IllegalArgumentException{
        if(quantity <= 0)
            throw new IllegalArgumentException("The quantity must be a positive integer greater than 0!");
        this.resourceType = resourceType;
        this.quantity = quantity;
    }

    /**
     * Returns the required type of the resources
     * @return the required type
     */
    public ResourceType getResourceType() {
        return resourceType;
    }

    /**
     * Returns the required amount of the resources
     * @return the required amount
     */
    public int getQuantity() {
        return quantity;
    }

    @Override
    public boolean checkRequirement(PlayerResourcesAndCards playerResourcesAndCards) {
        return playerResourcesAndCards.getResources().getOrDefault(resourceType,0) >= quantity;
    }

    /**
     * Constructs a clone of the specified CardRequirementResource
     * @param original the CardRequirementResource to be cloned
     */
    public CardRequirementResource(CardRequirementResource original){
        this(original.resourceType, original.quantity);
    }

    @Override
    public Requirement getClone() throws NegativeQuantityException {
        return new CardRequirementResource(this);
    }
}
