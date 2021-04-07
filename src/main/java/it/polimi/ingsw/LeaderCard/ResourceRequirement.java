package it.polimi.ingsw.LeaderCard;

import it.polimi.ingsw.ResourceType;

/**
 * This class represents a requirement of a particular amount of a certain type of resources which may be necessary for some LeaderCard.
 * The player must have at least the amount of the resources specified inside the object to meet this requirement.
 */

public class ResourceRequirement {
    /**
     * The type of the required resources.
     */
    private ResourceType resourceType;
    /**
     * The amount of the required resources. It is a positive integer greater than 0.
     */
    private int quantity;

    /**
     * Constructs a ResourceRequirement of the specified ResourceType and quantity
     * @param resourceType the required type of the resources
     * @param quantity the required amount of the resources
     * @throws IllegalArgumentException if the given quantity is not a positive integer greater than 0
     */
    public ResourceRequirement(ResourceType resourceType, int quantity) throws IllegalArgumentException{
        if(quantity <= 0)
            throw new IllegalArgumentException("The quantity must be a postive integer greater than 0!");
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
}
