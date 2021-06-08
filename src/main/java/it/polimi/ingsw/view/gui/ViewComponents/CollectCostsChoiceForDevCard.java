package it.polimi.ingsw.view.gui.ViewComponents;

import it.polimi.ingsw.controller.Command;
import it.polimi.ingsw.model.ResourceType;
import it.polimi.ingsw.network.messages.fromClient.BuyDevCardMessage;
import it.polimi.ingsw.network.messages.fromClient.BuyFromMarketMessage;
import it.polimi.ingsw.network.messages.fromClient.DepotParams;
import it.polimi.ingsw.network.messages.fromClient.Message;
import it.polimi.ingsw.view.gui.panels.PanelManager;

import java.util.HashMap;
import java.util.List;

public class CollectCostsChoiceForDevCard extends CollectCostsChoiceAbstract{
    private int row, col;
    private List<Integer> leaderList;
    private int devSlot;

    public CollectCostsChoiceForDevCard(PanelDrop panelDrop){
        super.panelDrop = panelDrop;
    }

    @Override
    protected void sendMessage(List<DepotParams> fromDepot, HashMap<ResourceType, Integer> fromStrongBox, HashMap<ResourceType, Integer> fromLeaderCards) {
        //Sends the message to the server
        System.out.println("HOLA");
        Message message = new BuyDevCardMessage(this.row, this.col, this.leaderList, fromDepot, fromLeaderCards, fromStrongBox, this.devSlot);
        PanelManager.getInstance().writeMessage(new Command("buyDevCard", message));
    }
}
