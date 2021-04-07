package it.polimi.ingsw.LeaderCard;

import it.polimi.ingsw.DevCards.DevCardColour;

/**
 * This class represents a requirement of a particular DevCards which may be necessary for some LeaderCard. It can be required either two things:
 * a DevCard with a specified color and level or a DevCard with only a specified color. To meet this requirement, the player must
 * hold at least one DevCard with the specified attributes
 */
public class CardRequirement extends Requirement{
    /**
     * The level required of the DevCard. If it is not mandatory, then it is set to -1
     */
    private int level;
    /**
     * The required color of the DevCard
     */
    private DevCardColour cardColour;

    /**
     * Constructs a CardRequirement of a DevCard with the specified color and level
     * @param level the required color of the DevCard
     * @param cardColour the required color of the DevCard
     * @throws IllegalArgumentException if the specified level is out of range
     */
    public CardRequirement(int level, DevCardColour cardColour) throws IllegalArgumentException {
        if(level <= 0 || level >= 4)
            throw new IllegalArgumentException("The level must be a positive number from 1 to 3!");
        this.level = level;
        this.cardColour = cardColour;
    }

    /**
     * Constructs a CardRequirement of a DevCard with the specified color
     * @param cardColour the required color of the DevCard
     */
    public CardRequirement(DevCardColour cardColour) {
        this.cardColour = cardColour;
        this.level = -1;
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
}
