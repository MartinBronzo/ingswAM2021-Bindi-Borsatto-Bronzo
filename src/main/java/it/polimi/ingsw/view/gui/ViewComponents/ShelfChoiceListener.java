package it.polimi.ingsw.view.gui.ViewComponents;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;

public class ShelfChoiceListener implements ActionListener {
    private JPanel menus;
    private HashMap<Integer, Integer> numResToShelf;
    private JComboBox quantity;
    private CollectMoveShelfToLeader collector;

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
