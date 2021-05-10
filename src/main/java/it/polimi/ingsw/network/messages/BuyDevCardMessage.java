package it.polimi.ingsw.network.messages;

import it.polimi.ingsw.model.LeaderCard.LeaderCard;
import it.polimi.ingsw.model.ResourceType;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BuyDevCardMessage {
    private int row, col;
    private List<Integer> leader;
    private List<DepotParams> depotRes;
    private HashMap<ResourceType, Integer> leaderRes;
    private HashMap<ResourceType, Integer> strongboxRes;
    private int devSlot;

    public BuyDevCardMessage(int row, int col, List<Integer> leader, List<DepotParams> depotRes, HashMap<ResourceType, Integer> leaderRes, HashMap<ResourceType, Integer> strongboxRes, int devSlot) {
        this.row = row;
        this.col = col;
        this.leader = leader;
        this.depotRes = depotRes;
        this.leaderRes = leaderRes;
        this.strongboxRes = strongboxRes;
        this.devSlot = devSlot;
    }

    public int getRow() {
        return row;
    }

    public int getCol() {
        return col;
    }

    public List<Integer> getLeader() {
        return new ArrayList<>(leader);
    }

    public List<DepotParams> getDepotRes() {
        return new ArrayList<>(depotRes);
    }

    public HashMap<ResourceType, Integer> getLeaderRes() {
        return new HashMap<>(leaderRes);
    }

    public HashMap<ResourceType, Integer> getStrongboxRes() {
        return new HashMap<>(strongboxRes);
    }

    public int getDevSlot() {
        return devSlot;
    }
}
