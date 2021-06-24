package it.polimi.ingsw.view.gui.ViewComponents.depot.moveResources;

import it.polimi.ingsw.controller.Command;
import it.polimi.ingsw.network.messages.fromClient.Message;
import it.polimi.ingsw.network.messages.fromClient.MoveBetweenShelvesMessage;
import it.polimi.ingsw.view.gui.mainViews.PanelManager;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class CollectMoveBetweenShelves implements ActionListener {
    private JComboBox sourceShelf;
    private JComboBox destShelf;

    public CollectMoveBetweenShelves(JComboBox sourceShelf) {
        this.sourceShelf = sourceShelf;
        this.destShelf = null;
    }

    public void setDestShelf(JComboBox destShelf) {
        this.destShelf = destShelf;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if(destShelf == null){
            //PanelManager.getInstance().printError("You must pick a source shelf first!");
            //TODO: decomenttare quando ci sarà il panel manager runnante
            JOptionPane.showMessageDialog(null, "You must pick a source shelf first!");
            return;
        }

        int source = Integer.parseInt(sourceShelf.getSelectedItem().toString().split(" ")[1]);
        int dest = Integer.parseInt(destShelf.getSelectedItem().toString().split(" ")[1]);


        System.out.println("FROM " + source + " " + dest);

        Message message = new MoveBetweenShelvesMessage(source, dest);
        PanelManager.getInstance().writeMessage(new Command("moveBetweenShelves", message));

    }
}
