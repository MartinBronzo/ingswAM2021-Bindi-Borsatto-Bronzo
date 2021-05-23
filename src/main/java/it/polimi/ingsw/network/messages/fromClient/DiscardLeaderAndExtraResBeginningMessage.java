package it.polimi.ingsw.network.messages.fromClient;

import java.util.ArrayList;
import java.util.List;

public class DiscardLeaderAndExtraResBeginningMessage extends Message {
    private List<Integer> leaderCard;
    private List<DepotParams> depotRes;

    public DiscardLeaderAndExtraResBeginningMessage(List<Integer> leaderCard, List<DepotParams> depotRes) {
        this.leaderCard = leaderCard;
        this.depotRes = depotRes;
    }

    public List<Integer> getLeaderCard() {
        return new ArrayList<>(leaderCard);
    }

    public List<DepotParams> getDepotRes() {
        return depotRes;
    }
}
