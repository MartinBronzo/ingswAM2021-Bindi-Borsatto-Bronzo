package it.polimi.ingsw.network.messages.fromClient;

import it.polimi.ingsw.model.resources.ResourceType;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * The message used to buy a DevCard.
 */
public class BuyDevCardMessage extends Message {
    private int row, col;
    private List<Integer> leaders;
    private List<DepotParams> depotRes;
    private HashMap<ResourceType, Integer> leaderRes;
    private HashMap<ResourceType, Integer> strongboxRes;
    private int devSlot;

    /**
     * Constructs a BuyDevCardMessage
     * @param row the number of the row where the DevCard the player wants to buy is on
     * @param col the number of the column where the DevCard the player wants to buy is on
     * @param leader the list of the indexes of the Discount LeaderCards whose effects the player wants to use when buying the specified DevCard
     * @param depotRes the resources used to pay for the price of the card which come from the Depot
     * @param leaderRes the resources used to pay for the price of the card which come from the ExtraSlot LeaderCard
     * @param strongboxRes the resources used to pay for the price of the card which come from the StrongBox
     * @param devSlot the DevSlot number where the player wants to store the specify card once it is bought
     */
    public BuyDevCardMessage(int row, int col, List<Integer> leader, List<DepotParams> depotRes, HashMap<ResourceType, Integer> leaderRes, HashMap<ResourceType, Integer> strongboxRes, int devSlot) {
        this.row = row;
        this.col = col;
        this.leaders = leader;
        this.depotRes = depotRes;
        this.leaderRes = leaderRes;
        this.strongboxRes = strongboxRes;
        this.devSlot = devSlot;
    }

    /**
     * Returns the number of the row where the DevCard the player wants to buy is on
     * @return the number of the row where the DevCard the player wants to buy is on
     */
    public int getRow() {
        return row;
    }

    /**
     * Returns the number of the column where the DevCard the player wants to buy is on
     * @return the number of the column where the DevCard the player wants to buy is on
     */
    public int getCol() {
        return col;
    }

    /**
     * Returns the list of the indexes of the Discount LeaderCards whose effects the player wants to use when buying the specified DevCard
     * @return the list of the indexes of the Discount LeaderCards whose effects the player wants to use when buying the specified DevCard
     */
    public List<Integer> getLeaders() {
        return new ArrayList<>(leaders);
    }

    /**
     * Returns the resources used to pay for the price of the card which come from the Depot
     * @return the resources used to pay for the price of the card which come from the Depot
     */
    public List<DepotParams> getDepotRes() {
        return new ArrayList<>(depotRes);
    }

    /**
     * Returns the resources used to pay for the price of the card which come from the ExtraSlot LeaderCard
     * @return the resources used to pay for the price of the card which come from the ExtraSlot LeaderCard
     */
    public HashMap<ResourceType, Integer> getLeaderRes() {
        return new HashMap<>(leaderRes);
    }

    /**
     * Returns the resources used to pay for the price of the card which come from the StrongBox
     * @return the resources used to pay for the price of the card which come from the StrongBox
     */
    public HashMap<ResourceType, Integer> getStrongboxRes() {
        return new HashMap<>(strongboxRes);
    }

    /**
     * Returns the DevSlot number where the player wants to store the specify card once it is bought
     * @return the DevSlot number where the player wants to store the specify card once it is bought
     */
    public int getDevSlot() {
        return devSlot;
    }

}
