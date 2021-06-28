package it.polimi.ingsw.network.messages.fromClient;

import java.util.ArrayList;
import java.util.List;

/**
 * This message is used to specify the player's begging of the game decisions.
 */
public class DiscardLeaderAndExtraResBeginningMessage extends Message {
    private List<Integer> leaderCard;
    private List<DepotParams> depotRes;

    /**
     * Constructs a DiscardLeaderAndExtraResBeginningMessage
     * @param leaderCard the list of indexes of the LeaderCards to be discarded
     * @param depotRes the resources to be put onto the Depot shelves
     */
    public DiscardLeaderAndExtraResBeginningMessage(List<Integer> leaderCard, List<DepotParams> depotRes) {
        this.leaderCard = leaderCard;
        this.depotRes = depotRes;
    }

    /**
     * Returns the list of indexes of the LeaderCards to be discarded
     * @return the list of indexes of the LeaderCards to be discarded
     */
    public List<Integer> getLeaderCard() {
        return new ArrayList<>(leaderCard);
    }

    /**
     * Returns the resources to be put onto the Depot shelves
     * @return the resources to be put onto the Depot shelves
     */
    public List<DepotParams> getDepotRes() {
        return depotRes;
    }
}
