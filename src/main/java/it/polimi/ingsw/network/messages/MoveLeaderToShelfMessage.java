package it.polimi.ingsw.network.messages;

import it.polimi.ingsw.model.ResourceType;

public class MoveLeaderToShelfMessage {
    private ResourceType res;
    private int destShelf;

    public MoveLeaderToShelfMessage(ResourceType res, int destShelf) {
        this.res = res;
        this.destShelf = destShelf;
    }

    public ResourceType getRes() {
        return res;
    }

    public int getDestShelf() {
        return destShelf;
    }
}
