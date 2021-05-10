package it.polimi.ingsw.network.messages;

import it.polimi.ingsw.model.ResourceType;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DiscardLeaderAndExtraResBeginningMessage {
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
