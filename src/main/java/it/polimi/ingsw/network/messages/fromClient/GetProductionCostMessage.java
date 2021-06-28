package it.polimi.ingsw.network.messages.fromClient;

import java.util.ArrayList;
import java.util.List;

/**
 * This message is used to ask the GameController for the outcome of the productions the player manifested desire for.
 */
public class GetProductionCostMessage extends Message {
    private List<Integer> devCards; //the index of the slot of the leader card
    private List<Integer> leaders;
    private BaseProductionParams baseProd;

    /**
     * Constructs a GetProductionCostMessage
     * @param devCards the list of indexes of DevCards whose production powers the player wants to use
     * @param leader the list of indexes of ExtraProduction LeaderCards whose effects the player wants to use
     * @param baseProd the BaseProductionParams representing the player's choices for the BaseProduction
     */
    public GetProductionCostMessage(List<Integer> devCards, List<Integer> leader, BaseProductionParams baseProd) {
        this.devCards = devCards;
        this.leaders = leader;
        this.baseProd = baseProd;
    }

    /**
     * Returns the list of indexes of DevCards whose production powers the player wants to use
     * @return the list of indexes of DevCards whose production powers the player wants to use
     */
    public List<Integer> getDevCards() {
        return new ArrayList<>(devCards);
    }

    /**
     * Returns the list of indexes of ExtraProduction LeaderCards whose effects the player wants to use
     * @return the list of indexes of ExtraProduction LeaderCards whose effects the player wants to use
     */
    public List<Integer> getLeaders() {
        return new ArrayList<>(leaders);
    }

    /**
     * Returns the BaseProductionParams representing the player's choices for the BaseProduction
     * @return the BaseProductionParams representing the player's choices for the BaseProduction
     */
    public BaseProductionParams getBaseProd() {
        return baseProd;
    }
}
