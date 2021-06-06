package it.polimi.ingsw.view.gui.ViewComponents.MoveResourcesElements;

import it.polimi.ingsw.controller.Command;
import it.polimi.ingsw.network.messages.fromClient.Message;
import it.polimi.ingsw.network.messages.fromClient.MoveShelfToLeaderMessage;
import it.polimi.ingsw.view.gui.panels.PanelManager;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class CollectMoveShelfToLeader implements ActionListener {
    private JComboBox shelf;
    private JComboBox quantity;

    public CollectMoveShelfToLeader(JComboBox shelf, JComboBox quantity) {
        this.shelf = shelf;
        this.quantity = quantity;
    }

    public CollectMoveShelfToLeader(JComboBox shelf){
        this.shelf = shelf;
        this.quantity = null;
    }

    public void setQuantity(JComboBox quantity) {
        this.quantity = quantity;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if(quantity == null){
            //PanelManager.getInstance().printError("You must pick a shelf first!");
            //TODO: decomenttare quando ci sarà il panel manager runnante
            JOptionPane.showMessageDialog(null, "You must pick a shelf first!");
            return;
        }
        int shelfChosen = Integer.parseInt(shelf.getSelectedItem().toString().split(" ")[1]);
        int quantityChosen = Integer.parseInt(quantity.getSelectedItem().toString());

        System.out.println("FROM SHELF " + shelfChosen + " move " + quantityChosen + " resources");

        Message message = new MoveShelfToLeaderMessage(shelfChosen, quantityChosen);
        //PanelManager.getInstance().writeMessage(new Command("moveShelfToLeader", message));
    }
}
