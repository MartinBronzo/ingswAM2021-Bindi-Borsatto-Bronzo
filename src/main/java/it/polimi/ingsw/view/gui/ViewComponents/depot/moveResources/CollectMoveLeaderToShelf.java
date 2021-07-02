package it.polimi.ingsw.view.gui.ViewComponents.depot.moveResources;

import it.polimi.ingsw.controller.Command;
import it.polimi.ingsw.model.resources.ResourceType;
import it.polimi.ingsw.network.messages.fromClient.Message;
import it.polimi.ingsw.network.messages.fromClient.MoveLeaderToShelfMessage;
import it.polimi.ingsw.view.gui.mainViews.PanelManager;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * This ActionListener will listen to the Confirm button of the MoveLeaderToShelf view when it is displayed onto the MoveResourceChoice.
 */
public class CollectMoveLeaderToShelf implements ActionListener {
    private JComboBox res;
    private JComboBox quantity;
    private JComboBox shelf;

    /**
     * Constructs the CollectMoveLeaderToShelf function which will take the player's choice for the resource type to be moved from the specified JComboBox
     * @param res the JComboBox used for specifying the resource type to be moved
     */
    public CollectMoveLeaderToShelf(JComboBox res) {
        this.res = res;
        this.quantity = null;
        this.shelf = null;
    }

    /*
    public void setQuantityAndShelf(JComboBox quantity, JComboBox shelf) {
        this.quantity = quantity;
        this.shelf = shelf;
    }*/

    /**
     * Sets the JComboBox to be used by the player to specify the quantity of the resource they have already specified to be moved
     * @param quantity the JComboBox to be used to specify the quantity to be moved
     */
    public void setQuantity(JComboBox quantity) {
        this.quantity = quantity;
    }

    /**
     * Sets the JComboBox to be used by the player to specify the destination shelf where the resources to be moved are going to be put
     * @param shelf the JComboBox to be used to specify the destination shelf
     */
    public void setShelf(JComboBox shelf) {
        this.shelf = shelf;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if(quantity == null || shelf == null){
            PanelManager.getInstance().printError("You must pick a shelf first!");
            //JOptionPane.showMessageDialog(null, "You must pick a resource type first!");
            return;
        }

        ResourceType resType = ResourceType.valueOf(res.getSelectedItem().toString());
        int amount = Integer.parseInt(quantity.getSelectedItem().toString());
        int shelfChosen = Integer.parseInt(shelf.getSelectedItem().toString().split(" ")[1]);

        System.out.println(amount + " " + resType + " to shelf " + shelfChosen);

        Message message = new MoveLeaderToShelfMessage(resType, amount, shelfChosen);
        PanelManager.getInstance().writeMessage(new Command("moveLeaderToShelf", message));
    }
}
