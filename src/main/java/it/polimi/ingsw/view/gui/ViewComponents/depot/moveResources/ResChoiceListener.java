package it.polimi.ingsw.view.gui.ViewComponents.depot.moveResources;

import it.polimi.ingsw.model.resources.ResourceType;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.List;

/**
 * This ActionListener is used to create or update the JComboBox the player uses to specify the quantity they want to move from an ExtraSlot LeaderCard
 * to a Depot shelf when the MoveLeaderToShelf panel is displayed. This function is invoked every time the player specify
 * a resource type in the resource type drop-down menu in order to display only the amounts of the resources the player is actually able to
 * move from the LeaderCards. It also creates only once (at the first time this function is invoked) the JComboBox used by the
 * player to choose the destination shelf.
 */
public class ResChoiceListener implements ActionListener {
    private HashMap<ResourceType, Integer> leaderSlots;
    private JComboBox quantity;
    private JComboBox shelf;
    private CollectMoveLeaderToShelf collector;
    private JPanel menus;
    private List<Integer> addableShelf;

    /**
     * Constructs a ResChoiceListener ActionListener for the MoveLeaderToShelf panel
     * @param leaderSlots the resources the player has onto their ExtraSlot LeaderCard
     * @param collector the function used to collect the player's choices from the MoveLeaderToShelf panel
     * @param menus a panel containing all the JComboBox for the MoveLeaderToShelf panel
     * @param addableShelf a list containing the number of the shelves where resources can actually be added to
     */
    public ResChoiceListener(HashMap<ResourceType, Integer> leaderSlots, CollectMoveLeaderToShelf collector, JPanel menus, List<Integer> addableShelf) {
        this.leaderSlots = leaderSlots;
        this.collector = collector;
        this.menus = menus;
        this.addableShelf = addableShelf;
        this.quantity = null;
        this.shelf = null;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        JComboBox box = (JComboBox) e.getSource();
        ResourceType type = ResourceType.valueOf(box.getSelectedItem().toString());

        //Creating or updating the quantity menu
        int maxQuantity = leaderSlots.get(type);
        String[] quantityStrings = new String[maxQuantity];
        for(int i = 1; i <= maxQuantity; i++)
            quantityStrings[i - 1] = String.valueOf(i);
        if(quantity == null){
            quantity = new JComboBox(quantityStrings);
            collector.setQuantity(quantity);
        }else{
            menus.remove(quantity);
            quantity = new JComboBox<>(quantityStrings);
        }
        menus.add(quantity);

        //Creating only once the shelf menu
        if(shelf == null){
            String[] shelfChoices = new String[this.addableShelf.size()];
            int i = 0;
            for(Integer s : this.addableShelf){
                shelfChoices[i] = "Shelf " + s;
                i++;
            }
            shelf = new JComboBox(shelfChoices);
            collector.setShelf(shelf);
            menus.add(shelf);
        }

        menus.revalidate();
        menus.repaint();
    }
}
