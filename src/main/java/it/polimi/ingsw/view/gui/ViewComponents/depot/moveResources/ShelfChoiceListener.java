package it.polimi.ingsw.view.gui.ViewComponents.depot.moveResources;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;

/**
 * This ActionListener is used to create or update the JComboBox the player uses to specify the quantity they want to move from a Depot shelf
 * to an ExtraSlot LeaderCard when the MoveShelfToLeader panel is displayed. This function is invoked every time the player specify
 * a shelf in the source shelf drop-down menu in order to display only the amounts of the resources the player is actually able to
 * move from the specified shelf.
 */
public class ShelfChoiceListener implements ActionListener {
    private JPanel menus;
    private HashMap<Integer, Integer> numResToShelf;
    private JComboBox quantity;
    private CollectMoveShelfToLeader collector;

    /**
     * Constructs a ShelfChoiceListener ActionListener
     * @param menus a panel containing all the JComboBox for the MoveShelfToLeader panel
     * @param numResToShelf a map containing the resources the player stores onto their shelves
     * @param collector the function used to collect the player's choices from the MoveShelfToLeader panel
     */
    public ShelfChoiceListener(JPanel menus, HashMap<Integer, Integer> numResToShelf, CollectMoveShelfToLeader collector) {
        this.menus = menus;
        this.numResToShelf = numResToShelf;
        this.quantity = null;
        this.collector = collector;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        JComboBox box = (JComboBox) e.getSource();
        String selectedItem = box.getSelectedItem().toString();

        int present = this.numResToShelf.get(Integer.parseInt(selectedItem.split(" ")[1]) - 1);
        String [] res = new String[present];
        for(int i = 1; i <= present; i++)
            res[i - 1] = String.valueOf(i);

        if(quantity == null){
            quantity = new JComboBox<>(res);
            collector.setQuantity(quantity);
        }else{
            menus.remove(quantity);
            quantity = new JComboBox<>(res);
        }
        menus.add(quantity);
        menus.revalidate();
        menus.repaint();
    }
}
