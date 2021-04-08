package it.polimi.ingsw.LeaderCard.leaderEffects;

import it.polimi.ingsw.ResourceType;

/**
 * This class has the goal of implementing the extra effect which gives extra slot where the player can store their resources.
 */
public class ExtraSlotLeaderEffect extends Effect{
    private final ResourceType slotType;
    private final int slotNumber;

    /**
     * Constructs an ExtraLeaderEffect which will give a specified number of extra slots that can hold a specified type of resource
     * @param slotType the given type of resources the slots will hold
     * @param slotNumber the amount of slots given by this effect
     * @throws IllegalArgumentException if the amount of slots given as an input is less than or equal to zero
     */
    public ExtraSlotLeaderEffect(ResourceType slotType, int slotNumber) throws IllegalArgumentException{
        if(slotNumber <= 0)
            throw new IllegalArgumentException("The number of slots must be a positive integer greater than 0!");
        if(slotType.equals(ResourceType.FAITHPOINT))
            throw new IllegalArgumentException("FaithPoints can't be stored!");
        this.slotType = slotType;
        this.slotNumber = slotNumber;
    }

    @Override
    public ResourceType extraSlotGetType() {
        return this.slotType;
    }

    @Override
    public int extraSlotGetResourceNumber() {
        return this.slotNumber;
    }

}
