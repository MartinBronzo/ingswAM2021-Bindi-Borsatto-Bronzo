package it.polimi.ingsw.view.gui.ViewComponents.DepotDragDrop;

import it.polimi.ingsw.controller.Command;
import it.polimi.ingsw.model.ResourceType;
import it.polimi.ingsw.network.messages.fromClient.BuyFromMarketMessage;
import it.polimi.ingsw.network.messages.fromClient.DepotParams;
import it.polimi.ingsw.network.messages.fromClient.DiscardLeaderAndExtraResBeginningMessage;
import it.polimi.ingsw.network.messages.fromClient.Message;
import it.polimi.ingsw.view.gui.ViewComponents.DepotDragDrop.CollectBeginningChoices;
import it.polimi.ingsw.view.gui.ViewComponents.DepotDragDrop.DepotDrop;
import it.polimi.ingsw.view.gui.ViewComponents.DiscardedResDrop;
import it.polimi.ingsw.view.gui.ViewComponents.LeaderCardDrop;
import it.polimi.ingsw.view.gui.panels.PanelManager;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
