package it.polimi.ingsw.LeaderCard.leaderEffects;

import it.polimi.ingsw.ResourceType;

/**
 * This class has the goal of implementing the extra effect which gives extra slot where the player can store their resources.
 */
//
public class ExtraSlotLeaderEffect extends Effect {
    private final ResourceType slotType;
    private final int slotNumber;

    /**
     * Constructs an ExtraLeaderEffect which will give a specified number of extra slots that can hold a specified type of resource
     *
     * @param slotType   the given type of resources the slots will hold
     * @param slotNumber the amount of slots given by this effect
     * @throws IllegalArgumentException if the amount of slots given as an input is less than or equal to zero
     */
    public ExtraSlotLeaderEffect(ResourceType slotType, int slotNumber) throws IllegalArgumentException {
        if (slotNumber <= 0)
            throw new IllegalArgumentException("The number of slots must be a positive integer greater than 0!");
        if (slotType.equals(ResourceType.FAITHPOINT))
            throw new IllegalArgumentException("FaithPoints can't be stored!");
        this.slotType = slotType;
        this.slotNumber = slotNumber;
    }

    /**
     * Construcst a clone of the specified ExtraSlotLeaderEffect
     *
     * @param original the effect to be cloned
     */
    public ExtraSlotLeaderEffect(ExtraSlotLeaderEffect original) {
        this(original.slotType, original.slotNumber);
    }

    @Override
    public Effect getClone() {
        return new ExtraSlotLeaderEffect(this);
    }

    @Override
    public ResourceType extraSlotGetType() {
        return this.slotType;
    }

    @Override
    public int extraSlotGetResourceNumber() {
        return this.slotNumber;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null)
            return false;
        if (obj == this)
            return true;
        if (!(obj instanceof ExtraSlotLeaderEffect))
            return false;
        ExtraSlotLeaderEffect tmp = (ExtraSlotLeaderEffect) obj;
        return this.slotType.equals(tmp.slotType) && this.slotNumber == tmp.slotNumber;
    }

    @Override
    public boolean isOneShotCard() {
        return true;
    }
}
