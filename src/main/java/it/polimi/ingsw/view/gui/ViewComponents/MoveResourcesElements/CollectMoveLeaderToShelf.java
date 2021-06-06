package it.polimi.ingsw.view.gui.ViewComponents.MoveResourcesElements;

import it.polimi.ingsw.controller.Command;
import it.polimi.ingsw.model.ResourceType;
import it.polimi.ingsw.network.messages.fromClient.Message;
import it.polimi.ingsw.network.messages.fromClient.MoveLeaderToShelfMessage;
import it.polimi.ingsw.view.gui.panels.PanelManager;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class CollectMoveLeaderToShelf implements ActionListener {
    private JComboBox res;
    private JComboBox quantity;
    private JComboBox shelf;

    public CollectMoveLeaderToShelf(JComboBox res) {
        this.res = res;
        this.quantity = null;
        this.shelf = null;
    }

    public void setQuantityAndShelf(JComboBox quantity, JComboBox shelf) {
        this.quantity = quantity;
        this.shelf = shelf;
    }

    public void setQuantity(JComboBox quantity) {
        this.quantity = quantity;
    }

    public void setShelf(JComboBox shelf) {
        this.shelf = shelf;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if(quantity == null || shelf == null){
            //PanelManager.getInstance().printError("You must pick a shelf first!");
            //TODO: decomenttare quando ci sarà il panel manager runnante
            JOptionPane.showMessageDialog(null, "You must pick a resource type first!");
            return;
        }

        ResourceType resType = ResourceType.valueOf(res.getSelectedItem().toString());
        int amount = Integer.parseInt(quantity.getSelectedItem().toString());
        int shelfChosen = Integer.parseInt(shelf.getSelectedItem().toString().split(" ")[1]);

        System.out.println(amount + " " + resType + " to shelf " + shelfChosen);

        Message message = new MoveLeaderToShelfMessage(resType, amount, shelfChosen);
        //PanelManager.getInstance().writeMessage(new Command("moveLeaderToShelf", message));
    }
}
