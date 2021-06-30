package it.polimi.ingsw.model.leaderCard.LeaderCardRequirements;


import it.polimi.ingsw.exceptions.NegativeQuantityException;
import it.polimi.ingsw.model.board.PlayerResourcesAndCards;

/**
 * This class represents the type of the CardRequirement and ResourceRequirement. In order to play a
 * LeaderCard, the player must meet all of its requirements represented by those classes.
 */
public /*abstract*/ class Requirement {
    public /*abstract*/ boolean checkRequirement(PlayerResourcesAndCards playerResourcesAndCards) {
        return true;
    }

    /**
     * Returns a clone of this Requirement
     * @return a clone of this Requirement
     * @throws NegativeQuantityException if in the process of creating the clone some parameter which is not supposed to be
     * a negative number it actually is
     */
    public /*abstract*/ Requirement getClone() throws NegativeQuantityException {
        return new Requirement();
    }

    @Override
    public String toString() {
        return "Empty requirement";
    }
}
