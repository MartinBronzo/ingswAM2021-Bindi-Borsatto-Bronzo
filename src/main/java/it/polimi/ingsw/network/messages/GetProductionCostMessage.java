package it.polimi.ingsw.network.messages;

import it.polimi.ingsw.model.LeaderCard.LeaderCard;

import java.util.ArrayList;
import java.util.List;

public class GetProductionCostMessage {
    private List<Integer> devCards;
    private List<Integer> leader;
    private BaseProductionParams baseProd;

    public GetProductionCostMessage(List<Integer> devCards, List<Integer> leader, BaseProductionParams baseProd) {
        this.devCards = devCards;
        this.leader = leader;
        this.baseProd = baseProd;
    }

    public List<Integer> getDevCards() {
        return new ArrayList<>(devCards);
    }

    public List<Integer> getLeader() {
        return new ArrayList<>(leader);
    }

    public BaseProductionParams getBaseProd() {
        return baseProd;
    }
}
