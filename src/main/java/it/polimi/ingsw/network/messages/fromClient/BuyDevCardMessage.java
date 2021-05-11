package it.polimi.ingsw.network.messages.fromClient;

import it.polimi.ingsw.model.ResourceType;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class BuyDevCardMessage {
    private int row, col;
    private List<Integer> leaders;
    private List<DepotParams> depotRes;
    private HashMap<ResourceType, Integer> leaderRes;
    private HashMap<ResourceType, Integer> strongboxRes;
    private int devSlot;

    public BuyDevCardMessage(int row, int col, List<Integer> leader, List<DepotParams> depotRes, HashMap<ResourceType, Integer> leaderRes, HashMap<ResourceType, Integer> strongboxRes, int devSlot) {
        this.row = row;
        this.col = col;
        this.leaders = leader;
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

    public List<Integer> getLeaders() {
        return new ArrayList<>(leaders);
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
