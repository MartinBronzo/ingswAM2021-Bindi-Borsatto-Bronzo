package it.polimi.ingsw.view.gui.ViewComponents.depot.moveResources;

import it.polimi.ingsw.controller.Command;
import it.polimi.ingsw.network.messages.fromClient.Message;
import it.polimi.ingsw.network.messages.fromClient.MoveShelfToLeaderMessage;
import it.polimi.ingsw.view.gui.mainViews.PanelManager;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * This ActionListener will listen to the Confirm button of the MoveShelfToLeader view when it is displayed onto the MoveResourceChoice.
 */
public class CollectMoveShelfToLeader implements ActionListener {
    private JComboBox shelf;
    private JComboBox quantity;

    /*
    public CollectMoveShelfToLeader(JComboBox shelf, JComboBox quantity) {
        this.shelf = shelf;
        this.quantity = quantity;
    }*/

    /**
     * Constructs a CollectMoveShelfToLeader function which will take the player's choice for the source shelf from the specified JComboBox
     * @param shelf the JComboBox used to specify the source shelf
     */
    public CollectMoveShelfToLeader(JComboBox shelf){
        this.shelf = shelf;
        this.quantity = null;
    }

    /**
     * Sets the JComboBox used by the player to specify the quantity of the resources stored onto the shelf they have already specified they want to move
     * @param quantity the JComboBox used to specify the quantity of resources to be moved
     */
    public void setQuantity(JComboBox quantity) {
        this.quantity = quantity;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if(quantity == null){
            PanelManager.getInstance().printError("You must pick a shelf first!");
            //JOptionPane.showMessageDialog(null, "You must pick a shelf first!");
            return;
        }
        int shelfChosen = Integer.parseInt(shelf.getSelectedItem().toString().split(" ")[1]);
        int quantityChosen = Integer.parseInt(quantity.getSelectedItem().toString());

        System.out.println("FROM SHELF " + shelfChosen + " move " + quantityChosen + " resources");

        Message message = new MoveShelfToLeaderMessage(shelfChosen, quantityChosen);
        PanelManager.getInstance().writeMessage(new Command("moveShelfToLeader", message));
    }
}
