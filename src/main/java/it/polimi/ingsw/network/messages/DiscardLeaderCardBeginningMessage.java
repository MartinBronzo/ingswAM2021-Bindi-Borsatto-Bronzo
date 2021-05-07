package it.polimi.ingsw.network.messages;

import it.polimi.ingsw.model.ResourceType;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DiscardLeaderCardBeginningMessage {
    private List<Integer> leaderCard;
    private Map<ResourceType, Integer> resToDep;

    public DiscardLeaderCardBeginningMessage(List<Integer> leaderCard, Map<ResourceType, Integer> resToDep) {
        this.leaderCard = leaderCard;
        this.resToDep = resToDep;
    }

    public List<Integer> getLeaderCard() {
        return new ArrayList<>(leaderCard);
    }

    public Map<ResourceType, Integer> getResToDep() {
        return new HashMap<>(resToDep);
    }
}
