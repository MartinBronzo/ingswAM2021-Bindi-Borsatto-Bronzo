package it.polimi.ingsw.network.messages.fromClient;

import it.polimi.ingsw.model.ResourceType;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ActivateProductionMessage extends Message {
    private List<Integer> devCards;
    private Map<Integer, ResourceType> leaders; //Map<Index of the LeaderCard, ExtraResource to be produced>
    private BaseProductionParams baseProduction;
    private List<DepotParams> depotInputRes;
    private Map<ResourceType, Integer> leaderSlotRes;
    private Map<ResourceType, Integer> strongboxInputRes;

    public ActivateProductionMessage(List<Integer> devCards, Map<Integer, ResourceType> leaderMap, BaseProductionParams baseProduction, List<DepotParams> depotInputRes, Map<ResourceType, Integer> leaderSlotRes, Map<ResourceType, Integer> strongboxInputRes) {
        this.devCards = devCards;
        this.leaders = leaderMap;
        this.baseProduction = baseProduction;
        this.depotInputRes = depotInputRes;
        this.leaderSlotRes = leaderSlotRes;
        this.strongboxInputRes = strongboxInputRes;
    }

    public List<Integer> getDevCards() {
        return new ArrayList<>(devCards);
    }

    public HashMap<Integer, ResourceType> getLeaders() {
        return new HashMap<>(leaders);
    }

    public BaseProductionParams getBaseProduction() {
        return baseProduction;
    }

    public List<DepotParams> getDepotInputRes() {
        return depotInputRes;
    }

    public HashMap<ResourceType, Integer> getLeaderSlotRes() {
        return new HashMap<>(leaderSlotRes);
    }

    public HashMap<ResourceType, Integer> getStrongboxInputRes() {
        return new HashMap<>(strongboxInputRes);
    }
}
