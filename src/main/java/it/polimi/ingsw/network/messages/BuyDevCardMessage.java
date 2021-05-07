package it.polimi.ingsw.network.messages;

import it.polimi.ingsw.model.LeaderCard.LeaderCard;
import it.polimi.ingsw.model.ResourceType;

import java.util.List;
import java.util.Map;

public class BuyDevCardMessage {
    private int row, col;
    private List<Integer> leader;
    private DepotParams depotRes;
    private Map<ResourceType, Integer> leaderRes;
    private Map<ResourceType, Integer> strongboxRes;
    private int devSlot;

    public BuyDevCardMessage(int row, int col, List<Integer> leader, DepotParams depotRes, Map<ResourceType, Integer> leaderRes, Map<ResourceType, Integer> strongboxRes, int devSlot) {
        this.row = row;
        this.col = col;
        this.leader = leader;
        this.depotRes = depotRes;
        this.leaderRes = leaderRes;
        this.strongboxRes = strongboxRes;
        this.devSlot = devSlot;
    }
}
