package it.polimi.ingsw.network.messages;

import it.polimi.ingsw.model.LeaderCard.LeaderCard;
import it.polimi.ingsw.model.ResourceType;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BuyFromMarketMessage {
    private int row, col;
    private List<Integer> leaderList;
    private List<DepotParams> depotRes;
    private Map<ResourceType, Integer> leaderRes;
    private Map<ResourceType, Integer> discardRes;

    public BuyFromMarketMessage(int row, int col, List<Integer> leaderList, List<DepotParams> depotRes, Map<ResourceType, Integer> leaderRes, Map<ResourceType, Integer> discardRes) {
        this.row = row;
        this.col = col;
        this.leaderList = leaderList;
        this.depotRes = depotRes;
        this.leaderRes = leaderRes;
        this.discardRes = discardRes;
    }

    public int getRow() {
        return row;
    }

    public int getCol() {
        return col;
    }

    public List<Integer> getLeaderList() {
        return new ArrayList<>(leaderList);
    }

    public List<DepotParams> getDepotRes() {
        return depotRes;
    }

    public Map<ResourceType, Integer> getLeaderRes() {
        return new HashMap<>(leaderRes);
    }

    public Map<ResourceType, Integer> getDiscardRes() {
        return new HashMap<>(discardRes);
    }
}
