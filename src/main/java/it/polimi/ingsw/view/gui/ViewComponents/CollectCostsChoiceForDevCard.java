package it.polimi.ingsw.view.gui.ViewComponents;

import it.polimi.ingsw.controller.Command;
import it.polimi.ingsw.model.ResourceType;
import it.polimi.ingsw.network.messages.fromClient.BuyDevCardMessage;
import it.polimi.ingsw.network.messages.fromClient.BuyFromMarketMessage;
import it.polimi.ingsw.network.messages.fromClient.DepotParams;
import it.polimi.ingsw.network.messages.fromClient.Message;
import it.polimi.ingsw.view.gui.panels.PanelManager;

import javax.swing.*;
import java.util.HashMap;
import java.util.List;

public class CollectCostsChoiceForDevCard extends CollectCostsChoiceAbstract{
    private int row, col;
    private List<Integer> leaderList;
    private JComboBox devSlotMenu;

    public CollectCostsChoiceForDevCard(PanelDrop panelDrop, int row, int col, List<Integer> leaderList, JComboBox devSlotMenu){
        super.panelDrop = panelDrop;
        this.row = row;
        this.col = col;
        this.leaderList = leaderList;
        this.devSlotMenu = devSlotMenu;
    }

    @Override
    protected void sendMessage(List<DepotParams> fromDepot, HashMap<ResourceType, Integer> fromStrongBox, HashMap<ResourceType, Integer> fromLeaderCards) {
        //Sends the message to the server
        //System.out.println("HOLA");
        Message message = new BuyDevCardMessage(this.row + 1, this.col + 1, this.leaderList, fromDepot, fromLeaderCards, fromStrongBox, Integer.parseInt(this.devSlotMenu.getSelectedItem().toString().split(" ")[1]));

        this.printExtraInfo(row, col, leaderList, Integer.parseInt(this.devSlotMenu.getSelectedItem().toString().split(" ")[1]));
        
        PanelManager.getInstance().writeMessage(new Command("buyDevCard", message));
    }

    private void printExtraInfo(int row, int col, List<Integer> leaderList, int parseInt) {
        System.out.println("FROM row " + row + ", FROM col " + col+"\nUSING THE FOLLOWING LEADERS: ");
        for(Integer i : leaderList)
            System.out.println(i);
        System.out.println("PUT THE DEV CARD INTO SLOT " + parseInt);

    }
}
