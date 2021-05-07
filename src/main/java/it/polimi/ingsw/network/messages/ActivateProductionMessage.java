package it.polimi.ingsw.network.messages;

import it.polimi.ingsw.model.BaseProduction;
import it.polimi.ingsw.model.ResourceType;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ActivateProductionMessage {
    private List<Integer> devCards;
    private Map<ResourceType, Integer> leader;
    private BaseProductionParams baseProduction;
    private DepotParams depotInputRes;
    private Map<ResourceType, Integer> leaderSlotRes;
    private Map<ResourceType, Integer> strongboxInputRes;

    public ActivateProductionMessage(List<Integer> devCards, Map<ResourceType, Integer> leaderMap, BaseProductionParams baseProduction, DepotParams depotInputRes, Map<ResourceType, Integer> leaderSlotRes, Map<ResourceType, Integer> strongboxInputRes) {
        this.devCards = devCards;
        this.leader = leaderMap;
        this.baseProduction = baseProduction;
        this.depotInputRes = depotInputRes;
        this.leaderSlotRes = leaderSlotRes;
        this.strongboxInputRes = strongboxInputRes;
    }

    public List<Integer> getDevCards() {
        return new ArrayList<>(devCards);
    }

    public Map<ResourceType, Integer> getLeader() {
        return new HashMap<>(leader);
    }

    public BaseProductionParams getBaseProduction() {
        return baseProduction;
    }

    public DepotParams getDepotInputRes() {
        return depotInputRes;
    }

    public Map<ResourceType, Integer> getLeaderSlotRes() {
        return new HashMap<>(leaderSlotRes);
    }

    public Map<ResourceType, Integer> getStrongboxInputRes() {
        return new HashMap<>(strongboxInputRes);
    }
}
