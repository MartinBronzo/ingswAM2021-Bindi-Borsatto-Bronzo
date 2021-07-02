package it.polimi.ingsw.view.gui.ViewComponents.depot.moveResources;

import it.polimi.ingsw.controller.Command;
import it.polimi.ingsw.network.messages.fromClient.Message;
import it.polimi.ingsw.network.messages.fromClient.MoveBetweenShelvesMessage;
import it.polimi.ingsw.view.gui.mainViews.PanelManager;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * This ActionListener will listen to the Confirm button of the MoveBetweenShelves view when it is displayed onto the MoveResourceChoice.
 */
public class CollectMoveBetweenShelves implements ActionListener {
    private JComboBox sourceShelf;
    private JComboBox destShelf;

    /**
     * Constructs a CollectMoveBetweenShelves function which will take the information of the player's choice for the source shelf from the specified JComboBox
     * @param sourceShelf a JComboBox used by the player to specify the source shelf
     */
    public CollectMoveBetweenShelves(JComboBox sourceShelf) {
        this.sourceShelf = sourceShelf;
        this.destShelf = null;
    }

    /**
     * Sets the JComboBox which is used by the player to specify the destination shelf
     * @param destShelf the JComboBox used to specify the destination shelf
     */
    public void setDestShelf(JComboBox destShelf) {
        this.destShelf = destShelf;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if(destShelf == null){
            PanelManager.getInstance().printError("You must pick a source shelf first!");

            //JOptionPane.showMessageDialog(null, "You must pick a source shelf first!");
            return;
        }

        int source = Integer.parseInt(sourceShelf.getSelectedItem().toString().split(" ")[1]);
        int dest = Integer.parseInt(destShelf.getSelectedItem().toString().split(" ")[1]);


        System.out.println("FROM " + source + " " + dest);

        Message message = new MoveBetweenShelvesMessage(source, dest);
        PanelManager.getInstance().writeMessage(new Command("moveBetweenShelves", message));

    }
}
