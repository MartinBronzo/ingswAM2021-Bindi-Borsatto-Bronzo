package it.polimi.ingsw.leaderEffects;

import it.polimi.ingsw.ResourceType;

public class ExtraSlotLeaderEffect {
    private final ResourceType type;
    private final int resourceNumber;

    public ExtraSlotLeaderEffect(ResourceType type, int resourceNumber){
        this.type = type;
        this.resourceNumber = resourceNumber;
    }

    public ResourceType getType() {
        return type;
    }

    public int getResourceNumber() {
        return resourceNumber;
    }

}
