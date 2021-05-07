package it.polimi.ingsw.network.messages;

import it.polimi.ingsw.model.BaseProduction;
import it.polimi.ingsw.model.ResourceType;

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
}
