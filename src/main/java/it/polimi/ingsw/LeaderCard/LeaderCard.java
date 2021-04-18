package it.polimi.ingsw.LeaderCard;

import it.polimi.ingsw.LeaderCard.leaderEffects.Effect;
import it.polimi.ingsw.LeaderCard.LeaderCardRequirements.Requirement;
import it.polimi.ingsw.PlayerResourcesAndCards;
import it.polimi.ingsw.ResourceType;
import it.polimi.ingsw.exceptions.NegativeQuantityException;
import it.polimi.ingsw.exceptions.UnmetRequirementException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * This class represents the Leader Card which can give the player extra effects. They can be played or discarded only once throughout the game:
 * a played card can't be discarded, a discarded card can be played. In order to play the card, some requirements must be met. If the card is played,
 * then the player can benefit from its extra effects for the rest of the game. When the player discards a card, they get some benefits (like getting
 * a FaithPoint.
 */
public class LeaderCard {
    private final int victoryPoints;
    private final ResourceType outputTypeWhenDiscarded;
    private final int outputAmountWhenDiscarded;
    private final List<Requirement> requirementsList;
    private final Effect effect;

    /**
     * Constructs a LeaderCard with the specified victoryPoints, list of requirements and effect
     *
     * @param victoryPoints    the Victory points this card may gives the player at the end of the game if the player plays the card
     * @param requirementsList the requirements which must be met in order to be able to play this card
     * @param effect           the extra effect this LeaderCard has
     * @throws NullPointerException if either the requirementsList or the effect is a null pointer
     */
    public LeaderCard(int victoryPoints, List<Requirement> requirementsList, Effect effect) {
        if (requirementsList == null)
            throw new NullPointerException("The requirement list can't be a null pointer!");
        if (effect == null)
            throw new NullPointerException("The effect can't be a null pointer!");
        this.victoryPoints = victoryPoints;
        this.requirementsList = getCloneList(requirementsList);
        this.effect = effect;
        this.outputTypeWhenDiscarded = ResourceType.FAITHPOINT;
        this.outputAmountWhenDiscarded = 1;
    }

    /**
     * Constructs a clone of the specified LeaderCard
     *
     * @param original the LeaderCard to be cloned
     */
    public LeaderCard(LeaderCard original) {
        this.victoryPoints = original.victoryPoints;
        this.requirementsList = getCloneList(original.requirementsList);
        this.effect = original.effect.getClone();
        this.outputTypeWhenDiscarded = ResourceType.FAITHPOINT;
        this.outputAmountWhenDiscarded = 1;
    }

    public static List<Requirement> getCloneList(List<Requirement> originalList) {
        List<Requirement> result = new ArrayList<>();
        for (Requirement req : originalList) {
            try {
                result.add(req.getClone());
            } catch (NegativeQuantityException e) {
                e.printStackTrace();
            }
        }
        return result;
    }

    /**
     * Checks if all the requirements are met
     *
     * @param playerResourcesAndCards the Resources and the DevCards owned by the player in the PlayerBoard
     * @return true if the player meets all the requirements
     * @throws UnmetRequirementException if the player doesn't meet a requirement which is then stored in the exception itself
     */
    public boolean checkRequirements(PlayerResourcesAndCards playerResourcesAndCards) throws UnmetRequirementException {
        for (Requirement req : requirementsList)
            if (!req.checkRequirement(playerResourcesAndCards))
                throw new UnmetRequirementException("The player can't activate the LeaderCard!", req);
        return true;
    }


    //Only used during testing
    @Deprecated
    public List<Requirement> getRequirementsList() {
        return new ArrayList<>(this.requirementsList);
    }

    /**
     * Returns a copy of all of the requirements
     *
     * @return a list of the requirements for this LeaderCard
     */
    public List<Requirement> getRequirementsListSafe() {
        List<Requirement> result = new ArrayList<>();
        for (Requirement req : this.requirementsList) {
            try {
                result.add(req.getClone());
            } catch (NegativeQuantityException e) {
                e.printStackTrace();
            }
        }
        return result;
    }

    /**
     * Returns the Victory points of this LeaderCard
     *
     * @return the Victory points
     */
    public int getVictoryPoints() {
        return victoryPoints;
    }

    /**
     * Returns the effect of this LeaderCard
     *
     * @return the effect of the LeaderCard
     */
    public Effect getEffect() {
        return effect.getClone();
    }

    /**
     * Returns the resources the player gets when they discard this LeaderCard
     *
     * @return the resources the player gets
     */
    public HashMap<ResourceType, Integer> getOutputWhenDiscarded() {
        HashMap<ResourceType, Integer> output = new HashMap<>();
        output.put(this.outputTypeWhenDiscarded, this.outputAmountWhenDiscarded);
        return output;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null)
            return false;
        if (obj == this)
            return true;
        if (!(obj instanceof LeaderCard))
            return false;
        LeaderCard tmp = (LeaderCard) obj;
        return this.victoryPoints == tmp.victoryPoints && this.outputTypeWhenDiscarded.equals(tmp.outputTypeWhenDiscarded) &&
                this.outputAmountWhenDiscarded == tmp.outputAmountWhenDiscarded &&
                this.requirementsList.containsAll(tmp.requirementsList) && tmp.requirementsList.containsAll(this.requirementsList) &&
                this.effect.equals(tmp.effect);
    }
}
