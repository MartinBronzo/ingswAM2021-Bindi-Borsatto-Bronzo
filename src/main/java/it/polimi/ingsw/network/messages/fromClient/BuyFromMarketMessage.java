package it.polimi.ingsw.network.messages.fromClient;

import it.polimi.ingsw.model.resources.ResourceType;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * This message is used to buy resources from the Market.
 */
public class BuyFromMarketMessage extends Message {
    private int row, col;
    private List<Integer> leaderList;
    private List<DepotParams> depotRes;
    private Map<ResourceType, Integer> leaderRes;
    private Map<ResourceType, Integer> discardRes;

    /**
     * Constructs a BuyFromMarketMessage
     * @param row the number of the Market row the player wants to buy or 0
     * @param col the number of the Market column the player wants to buy or 0
     * @param leaderList the list of the indexes of the WhiteMarble LeaderCards whose effects the player wants to use when buying from the Market
     * @param depotRes the resources got from the Market that are going to be placed onto which Depot shelves
     * @param leaderRes the resources got from the Market that are going to be placed onto the ExtraSlot LeaderCards
     * @param discardRes the resources got from the Market that are going to be discarded
     */
    public BuyFromMarketMessage(int row, int col, List<Integer> leaderList, List<DepotParams> depotRes, Map<ResourceType, Integer> leaderRes, Map<ResourceType, Integer> discardRes) {
        this.row = row;
        this.col = col;
        this.leaderList = leaderList;
        this.depotRes = depotRes;
        this.leaderRes = leaderRes;
        this.discardRes = discardRes;
    }

    /**
     * Returns the number of the Market row the player wants to buy or 0
     * @return the number of the Market row the player wants to buy or 0
     */
    public int getRow() {
        return row;
    }

    /**
     * Returns the number of the Market column the player wants to buy or 0
     * @return the number of the Market column the player wants to buy or 0
     */
    public int getCol() {
        return col;
    }

    /**
     * Returns the list of the indexes of the WhiteMarble LeaderCards whose effects the player wants to use when buying from the Market
     * @return the list of the indexes of the WhiteMarble LeaderCards whose effects the player wants to use when buying from the Market
     */
    public List<Integer> getLeaderList() {
        return new ArrayList<>(leaderList);
    }

    /**
     * Returns the resources got from the Market that are going to be placed onto which Depot shelves
     * @return the resources got from the Market that are going to be placed onto which Depot shelves
     */
    public List<DepotParams> getDepotRes() {
        return depotRes;
    }

    /**
     * Returns the resources got from the Market that are going to be placed onto the ExtraSlot LeaderCards
     * @return the resources got from the Market that are going to be placed onto the ExtraSlot LeaderCards
     */
    public Map<ResourceType, Integer> getLeaderRes() {
        return new HashMap<>(leaderRes);
    }

    /**
     * Returns the resources got from the Market that are going to be discarded
     * @return the resources got from the Market that are going to be discarded
     */
    public Map<ResourceType, Integer> getDiscardRes() {
        return new HashMap<>(discardRes);
    }

    @Override
    public String toString() {
        String result = "BFM Message:\nr:" + row + " c: " + col + "\nLEADER LIST: ";
        for(Integer i : this.leaderList)
            result = result + i + ", ";
        result += "\nDEPOT PARAMS:\n";
        for(DepotParams d : this.depotRes)
            result += "res: "+d.getResourceType() + ", qt: " + d.getQt() + ", shelf: " + d.getShelf() + "\n";
        result += "LEADER RES\n";
        for(Map.Entry<ResourceType, Integer> e : this.leaderRes.entrySet())
            result += e.getKey() + " " + e.getValue() + "\n";
        result += "DISCARD RES\n";
        for(Map.Entry<ResourceType, Integer> e : this.discardRes.entrySet())
            result += e.getKey() + " " + e.getValue() + "\n";

        return result;

    }

}
