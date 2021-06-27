package it.polimi.ingsw.view.gui.ViewComponents.resources.resourcesDrop;

import it.polimi.ingsw.controller.Command;
import it.polimi.ingsw.model.resources.ResourceType;
import it.polimi.ingsw.network.messages.fromClient.BuyDevCardMessage;
import it.polimi.ingsw.network.messages.fromClient.DepotParams;
import it.polimi.ingsw.network.messages.fromClient.Message;
import it.polimi.ingsw.view.gui.ViewComponents.utils.CollectCostsChoiceAbstract;
import it.polimi.ingsw.view.gui.mainViews.PanelManager;

import javax.swing.*;
import java.util.HashMap;
import java.util.List;

/**
 * This ActionListener will listen to the Confirm button of the DevGridPayingCost view
 */
public class CollectCostsChoiceForDevCard extends CollectCostsChoiceAbstract {
    private int row, col;
    private List<Integer> leaderList;
    private JComboBox devSlotMenu;

    /**
     * Constructs a CollectCostsChoiceForDevCard ActionListener
     * @param panelDrop the panel where the player drops the resources they want to use to pay for the DevCard
     * @param row the row of the DevCard the player wants to buy
     * @param col the column of the DevCard the player wants to buy
     * @param leaderList the list of indexes of Discount LeaderCards the player wants to use to pay for this card
     * @param devSlotMenu the JComboBox the player uses to specify the DevSlot where they want to put the DevCard once they have bought it
     */
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
