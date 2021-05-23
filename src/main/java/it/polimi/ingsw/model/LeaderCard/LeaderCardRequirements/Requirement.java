package it.polimi.ingsw.model.LeaderCard.LeaderCardRequirements;


import it.polimi.ingsw.exceptions.NegativeQuantityException;
import it.polimi.ingsw.model.PlayerResourcesAndCards;

/**
 * This class represents the type of the CardRequirement and ResourceRequirement. In order to play a
 * LeaderCard, the player must meet all of its requirements represented by those classes.
 */
public /*abstract*/ class Requirement {
    public /*abstract*/ boolean checkRequirement(PlayerResourcesAndCards playerResourcesAndCards) {
        return true;
    }

    public /*abstract*/ Requirement getClone() throws NegativeQuantityException {
        return new Requirement();
    }

    @Override
    public String toString() {
        return "Empty requirement";
    }
}
