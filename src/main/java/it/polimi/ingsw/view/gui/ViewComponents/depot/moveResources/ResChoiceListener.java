package it.polimi.ingsw.view.gui.ViewComponents.depot.moveResources;

import it.polimi.ingsw.model.resources.ResourceType;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.List;

public class ResChoiceListener implements ActionListener {
    private HashMap<ResourceType, Integer> leaderSlots;
    private JComboBox quantity;
    private JComboBox shelf;
    private CollectMoveLeaderToShelf collector;
    private JPanel menus;
    private List<Integer> addableShelf;

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
