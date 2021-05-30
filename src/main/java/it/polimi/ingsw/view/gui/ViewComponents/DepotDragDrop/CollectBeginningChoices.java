package it.polimi.ingsw.view.gui.ViewComponents.DepotDragDrop;

import it.polimi.ingsw.controller.Command;
import it.polimi.ingsw.model.Depot;
import it.polimi.ingsw.model.ResourceType;
import it.polimi.ingsw.network.messages.fromClient.BuyFromMarketMessage;
import it.polimi.ingsw.network.messages.fromClient.DepotParams;
import it.polimi.ingsw.network.messages.fromClient.DiscardLeaderAndExtraResBeginningMessage;
import it.polimi.ingsw.network.messages.fromClient.Message;
import it.polimi.ingsw.view.gui.panels.PanelManager;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * This ActionListener will listen to the Confirm button of the Beginning Decisions view
 */
public class CollectBeginningChoices implements ActionListener {
    private DepotDragAndDrop depotDragAndDrop;
    private static ResourceType[] resources= new ResourceType[]{ResourceType.COIN, ResourceType.STONE, ResourceType.SERVANT, ResourceType.SHIELD};

   public CollectBeginningChoices(DepotDragAndDrop dragAndDrop){
        this.depotDragAndDrop = dragAndDrop;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        System.out.println("Clicked!");
        //Collects the DepotChoices
        List<MyDepotPanel.Pair<Integer, ResourceType>> depotDecisions = this.depotDragAndDrop.getDecisions();

        List<DepotParams> depotInfo = this.getDepotInfoForModel(depotDecisions);

        this.printDecisions(depotInfo);

        //Collects the discarded LeaderCard
        //TODO: this class will need to collect both the Depot choices (saved in DepotParms) and the LeaderCard the player wants to discard
        Message message = new DiscardLeaderAndExtraResBeginningMessage(new ArrayList<>(), depotInfo);


        //PanelManager.getInstance().writeMessage(new Command("discardLeaderAndExtraResBeginning", message));

        //Resets the inner state
        this.depotDragAndDrop.resetState();
    }

    private List<DepotParams> getDepotInfoForModel(List<MyDepotPanel.Pair<Integer, ResourceType>> depotDecisions){
        List<MyDepotPanel.Pair<Integer, ResourceType>> someChoices;
        int quantity;
        List<DepotParams> result = new ArrayList<>();

        //Gets how many shelves are there
        List<Integer> presentShelves = depotDecisions.stream().map(MyDepotPanel.Pair::getKey).distinct().collect(Collectors.toList());

        //For each shelves we blend the many decisions
        for(Integer i: presentShelves){
            someChoices = depotDecisions.stream().filter(x -> x.getKey().equals(i)).collect(Collectors.toList());
            for(ResourceType r: resources){
                quantity = (int) someChoices.stream().filter(x -> x.getValue() == r).count();
                if(quantity > 0)
                    result.add(new DepotParams(r, quantity, i));
            }
        }

        return result;
    }

    private void printDecisions(List<DepotParams> dec){
       for(DepotParams d: dec)
           System.out.println("In " + d.getShelf() + " put " + d.getQt() + " of " + d.getResourceType());
    }
}
