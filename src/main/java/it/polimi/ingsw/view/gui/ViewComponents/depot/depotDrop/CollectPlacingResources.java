package it.polimi.ingsw.view.gui.ViewComponents.depot.depotDrop;

import it.polimi.ingsw.controller.Command;
import it.polimi.ingsw.model.resources.ResourceType;
import it.polimi.ingsw.network.messages.fromClient.BuyFromMarketMessage;
import it.polimi.ingsw.network.messages.fromClient.DepotParams;
import it.polimi.ingsw.network.messages.fromClient.Message;
import it.polimi.ingsw.view.gui.ViewComponents.resources.resourcesDrop.DiscardedResDrop;
import it.polimi.ingsw.view.gui.ViewComponents.leaderCards.leaderCardsDrop.LeaderCardDrop;
import it.polimi.ingsw.view.gui.mainViews.PanelManager;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * This ActionListener will listen to the Confirm button of the MarketPlacingResources view
 */
public class CollectPlacingResources implements ActionListener {
    private DepotDrop depot;
    private DiscardedResDrop trashCan;
    private List<LeaderCardDrop> leaders;
    private int row;
    private int col;
    private List<Integer> leaderList;

    @Deprecated
    public CollectPlacingResources(DepotDrop depot, DiscardedResDrop trashCan) {
        this.depot = depot;
        this.trashCan = trashCan;
        this.leaders = null; //The player has no LeaderCard with the ExtraSlot effect
    }

    @Deprecated
    public CollectPlacingResources(DepotDrop depot, DiscardedResDrop trashCan, List<LeaderCardDrop> leaders){
        this.depot = depot;
        this.trashCan = trashCan;
        this.leaders = leaders;
    }

    /**
     * Constructs a CollectPlacingResources ActionListener which will collect all the information the user has specified for buying something at the Market
     * @param depot the DepotDrop the player uses to drop what they have bought from the Market and want to keep in their Depot
     * @param trashCan the DiscardedResDrop the player uses to drop what they have bought from the Market and cannot or want to store in their Depot
     * @param leaders a list of the LeaderCardDrop the player can have and uses to drop what they have bought from the Market
     * @param row the row of the Market the player wants to buy from or zero
     * @param col the column of the Market the player wants to buy from or zero
     * @param leaderList a list of the indexes of the player's LeaderCards whose effects the player wants to use for buying from the Market
     */
    public CollectPlacingResources(DepotDrop depot, DiscardedResDrop trashCan, List<LeaderCardDrop> leaders, int row, int col, List<Integer> leaderList) {
        this.depot = depot;
        this.trashCan = trashCan;
        this.leaders = leaders;
        this.row = row;
        this.col = col;
        this.leaderList = leaderList;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        System.out.println("Clicked");

        //Collects the DepotChoices
        //List<ShelfDrop.Pair<Integer, ResourceType>> depotDecisions = this.depot.getDecisions();
        //List<DepotParams> depotInfo = CollectBeginningChoices.getDepotInfoForModel(depotDecisions);
        List<DepotParams> depotInfo = this.depot.getDecisions();

        //Collects the resources dropped onto the LeaderCards
        HashMap<ResourceType, Integer> leaderRes = new HashMap<>();
        if(leaders != null){
            for(LeaderCardDrop drop : this.leaders)
                if(drop.getDroppedHereAmount() > 0) {
                    if (leaderRes.get(drop.getResStored()) != null)
                        leaderRes.put(drop.getResStored(), leaderRes.get(drop.getResStored()) + drop.getDroppedHereAmount());
                    else
                        leaderRes.put(drop.getResStored(), drop.getDroppedHereAmount());
                }
        }

        //Collects the Disarded resources
        Map<ResourceType, Integer> discardedRes = trashCan.getDecisions();

        this.printDecisions(depotInfo, discardedRes, leaderRes);

        //Sends the message to the server
        Message message = new BuyFromMarketMessage(this.row, this.col, this.leaderList, depotInfo, leaderRes, discardedRes);
        PanelManager.getInstance().writeMessage(new Command("buyFromMarket", message));
    }

    private void printDecisions(List<DepotParams> dec, Map<ResourceType, Integer> discardedRes, HashMap<ResourceType, Integer> leaderRes ) {
        System.out.println("BUY FROM MARKET\nFROM row "+ row +", FROM col "+ col);
        System.out.print("USING THE FOLLOWING LEADERCARDS: ");
        for(Integer i: leaderList)
            System.out.print(i + ", ");

        System.out.println("\nIN DEPOT: ");
        for (DepotParams d : dec)
            System.out.println("In " + d.getShelf() + " put " + d.getQt() + " of " + d.getResourceType());

        System.out.println("TO LEADER: ");
        for(Map.Entry<ResourceType, Integer> e : leaderRes.entrySet())
            System.out.println(e.getValue() + " " + e.getKey());

        System.out.println("DISCARDED: ");
        for(Map.Entry<ResourceType, Integer> e : discardedRes.entrySet())
            System.out.println(e.getValue() + " " + e.getKey());

    }
}
