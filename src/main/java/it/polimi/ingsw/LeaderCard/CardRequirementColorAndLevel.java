package it.polimi.ingsw.LeaderCard;

import it.polimi.ingsw.DevCards.DevCardColour;
import it.polimi.ingsw.PlayerBoard;
import it.polimi.ingsw.exceptions.NegativeQuantityException;

/**
 * This class represents a requirement of a particular DevCards which may be necessary for some LeaderCard. It can be required either two things:
 * a DevCard with a specified color and level or a DevCard with only a specified color. To meet this requirement, the player must
 * hold at least one DevCard with the specified attributes
 */
public class CardRequirementColorAndLevel extends Requirement{
    /**
     * The level required of the DevCard. If it is not mandatory, then it is set to -1
     */
    private final int level;
    /**
     * The required color of the DevCard
     */
    private final DevCardColour cardColour;
    private final int quantity;

    /**
     * Constructs a CardRequirement of DevCards with the specified color and level
     * @param level the required color of the DevCard
     * @param cardColour the required color of the DevCard
     * @param quantity the number of devCards required having this cardColour and level
     * @throws IllegalArgumentException if the specified level is out of range
     * @throws NegativeQuantityException if quantity is negative
     */
    public CardRequirementColorAndLevel(int level, DevCardColour cardColour, int quantity) throws IllegalArgumentException, NegativeQuantityException {
        if(level <= 0 || level >= 4)
            throw new IllegalArgumentException("The level must be a positive number from 1 to 3!");
        if(quantity<0)
            throw new NegativeQuantityException("CardRequirementColorandLevel: Quantity must be at least 0");
        this.level = level;
        this.cardColour = cardColour;
        this.quantity = quantity;

    }

    /**
     * Returns the number of DevCards required
     * @return the required quantity
     */
    public int getQuantity() {
        return quantity;
    }

    /**
     * Returns the required level of the DevCard
     * @return the required level
     */
    public int getLevel() {
        return level;
    }

    /**
     * Returns the required color of the DevCard
     * @return the required color
     */
    public DevCardColour getCardColour() {
        return cardColour;
    }

    @Override
    public boolean checkRequirement(PlayerBoard playerBoard) {
        return playerBoard.getDevSlots().getAllDevCards().stream().filter(card -> cardColour == card.getColour() && level == card.getLevel()).count() >= quantity;
    }
}
