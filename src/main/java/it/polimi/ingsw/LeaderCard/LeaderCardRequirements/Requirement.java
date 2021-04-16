package it.polimi.ingsw.LeaderCard.LeaderCardRequirements;


import it.polimi.ingsw.PlayerResourcesAndCards;
import it.polimi.ingsw.exceptions.NegativeQuantityException;

/**
 * This class represents the type of the CardRequirement and ResourceRequirement. In order to play a
 * LeaderCard, the player must meet all of its requirements represented by those classes.
 */
public abstract class Requirement {
    public abstract boolean checkRequirement(PlayerResourcesAndCards playerResourcesAndCards);

    public abstract Requirement getClone() throws NegativeQuantityException;

}
