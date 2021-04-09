package it.polimi.ingsw.exceptions;

import it.polimi.ingsw.LeaderCardRequirementsTests.Requirement;

/**
 * This exception is thrown by the LeaderCard if the player doesn't meet a requirement which is then saved in this exception.
 */
public class UnmetRequirementException extends Exception{
    private Requirement unmetRequirement;

    /**
     * Constructs a UnmetRequirementException which holds a specified message and a requirement for LeaderCards which was not met by the player
     * @param message the detail message
     * @param unmetRequirement the requirement which wasn't met by the player
     */
    public UnmetRequirementException(String message, Requirement unmetRequirement) {
        super(message);
        this.unmetRequirement = unmetRequirement;
    }
}
