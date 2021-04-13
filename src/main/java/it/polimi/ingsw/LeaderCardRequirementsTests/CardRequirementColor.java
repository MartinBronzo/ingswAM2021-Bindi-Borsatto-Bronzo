package it.polimi.ingsw.LeaderCardRequirementsTests;

import it.polimi.ingsw.DevCards.DevCardColour;
import it.polimi.ingsw.PlayerBoard;
import it.polimi.ingsw.PlayerResourcesAndCards;
import it.polimi.ingsw.exceptions.NegativeQuantityException;

/**
 * This class represents a requirement of a particular DevCards which may be necessary for some LeaderCard.
 * it requires 1 or more DevCards with a specified color. To meet this requirement, the player must
 * hold at least one DevCard with the specified attributes
 */
public class CardRequirementColor extends Requirement {

    /**
     * The required color of the DevCard
     */
    private final DevCardColour cardColour;
    private final int quantity;

    /**
     * Constructs a CardRequirement of DevCards with the specified color
     * @param cardColour the required color of the DevCard
     * @param quantity the number of devCards required having this cardColour
     * @throws NegativeQuantityException if quantity is negative
     */
    public CardRequirementColor(DevCardColour cardColour, int quantity) throws NegativeQuantityException {
        if(quantity<0)
            throw new NegativeQuantityException("CardRequirementColor: Quantity must be at least 0");
        this.quantity = quantity;
        this.cardColour = cardColour;
    }

    /**
     * Returns the number of DevCards required
     * @return the required quantity
     */
    public int getQuantity() {
        return quantity;
    }

    /**
     * Returns the required color of the DevCard
     * @return the required color
     */
    public DevCardColour getCardColour() {
        return cardColour;
    }

    @Override
    public boolean checkRequirement(PlayerResourcesAndCards playerResourcesAndCards) {
        return playerResourcesAndCards.getDevCards().stream().filter(card -> cardColour == card.getColour()).count() >= quantity;
    }

    /**
     * Constructs a clone of the specified CardRequirementColor
     * @param original the requirement to be cloned
     * @throws NegativeQuantityException if quantity is negative
     */
    public CardRequirementColor(CardRequirementColor original) throws NegativeQuantityException {
        this(original.cardColour, original.quantity);
    }

    @Override
    public Requirement getClone() throws NegativeQuantityException {
        return new CardRequirementColor(this);
    }

    @Override
    public boolean equals(Object obj) {
        if(obj == null)
            return false;
        if(obj == this)
            return true;
        if(!(obj instanceof CardRequirementColor))
            return false;
        CardRequirementColor tmp = (CardRequirementColor) obj;
        return this.cardColour.equals(tmp.cardColour) && this.quantity == tmp.quantity;
    }
}
