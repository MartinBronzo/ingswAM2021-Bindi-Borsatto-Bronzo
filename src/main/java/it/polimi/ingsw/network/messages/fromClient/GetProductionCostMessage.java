package it.polimi.ingsw.network.messages.fromClient;

import java.util.ArrayList;
import java.util.List;

public class GetProductionCostMessage extends Message {
    private List<Integer> devCards; //the index of the slot of the leader card
    private List<Integer> leaders;
    private BaseProductionParams baseProd;

    public GetProductionCostMessage(List<Integer> devCards, List<Integer> leader, BaseProductionParams baseProd) {
        this.devCards = devCards;
        this.leaders = leader;
        this.baseProd = baseProd;
    }

    public List<Integer> getDevCards() {
        return new ArrayList<>(devCards);
    }

    public List<Integer> getLeaders() {
        return new ArrayList<>(leaders);
    }

    public BaseProductionParams getBaseProd() {
        return baseProd;
    }
}
