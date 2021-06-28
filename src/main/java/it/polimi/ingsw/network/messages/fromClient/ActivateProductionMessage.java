package it.polimi.ingsw.network.messages.fromClient;

import it.polimi.ingsw.model.resources.ResourceType;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * The message is used to activate the player's production players.
 */
public class ActivateProductionMessage extends Message {
    private List<Integer> devCards;
    private Map<Integer, ResourceType> leaders; //Map<Index of the LeaderCard, ExtraResource to be produced>
    private BaseProductionParams baseProduction;
    private List<DepotParams> depotInputRes;
    private Map<ResourceType, Integer> leaderSlotRes;
    private Map<ResourceType, Integer> strongboxInputRes;

    /**
     * Creates an ActivateProductionMessage
     * @param devCards the DevCards whose production methods the player wants to use
     * @param leaderMap the extra output the player has decided for the LeaderCards productions
     * @param baseProduction the BaseProductionParams which represent the player's choice for the BaseProduction
     * @param depotInputRes the input resources which come from the Depot
     * @param leaderSlotRes the input resources which come from the ExtraSlot LeaderCards
     * @param strongboxInputRes the input resources which come from the StrongBox
     */
    public ActivateProductionMessage(List<Integer> devCards, Map<Integer, ResourceType> leaderMap, BaseProductionParams baseProduction, List<DepotParams> depotInputRes, Map<ResourceType, Integer> leaderSlotRes, Map<ResourceType, Integer> strongboxInputRes) {
        this.devCards = devCards;
        this.leaders = leaderMap;
        this.baseProduction = baseProduction;
        this.depotInputRes = depotInputRes;
        this.leaderSlotRes = leaderSlotRes;
        this.strongboxInputRes = strongboxInputRes;
    }

    /**
     * Returns the DevCards whose production methods the player wants to use
     * @return the DevCards whose production methods the player wants to use
     */
    public List<Integer> getDevCards() {
        return new ArrayList<>(devCards);
    }

    /**
     * Returns the extra output the player has decided for the LeaderCards productions
     * @return the extra output the player has decided for the LeaderCards productions
     */
    public HashMap<Integer, ResourceType> getLeaders() {
        return new HashMap<>(leaders);
    }

    /**
     * Returns the BaseProductionParams which represent the player's choice for the BaseProduction
     * @return the BaseProductionParams which represent the player's choice for the BaseProduction
     */
    public BaseProductionParams getBaseProduction() {
        return baseProduction;
    }

    /**
     * Returns the input resources which come from the Depot
     * @return the input resources which come from the Depot
     */
    public List<DepotParams> getDepotInputRes() {
        return depotInputRes;
    }

    /**
     * Returns the input resources which come from the ExtraSlot LeaderCards
     * @return the input resources which come from the ExtraSlot LeaderCards
     */
    public HashMap<ResourceType, Integer> getLeaderSlotRes() {
        return new HashMap<>(leaderSlotRes);
    }

    /**
     * Returns the input resources which come from the StrongBox
     * @return the input resources which come from the StrongBox
     */
    public HashMap<ResourceType, Integer> getStrongboxInputRes() {
        return new HashMap<>(strongboxInputRes);
    }
}
