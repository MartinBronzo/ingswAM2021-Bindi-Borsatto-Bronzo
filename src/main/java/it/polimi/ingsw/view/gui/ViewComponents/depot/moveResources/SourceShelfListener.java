package it.polimi.ingsw.view.gui.ViewComponents.depot.moveResources;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * This ActionListener is used to create or update the JComboBox the player uses to specify the destination shelf
 * when the MoveBetweenShelves panel is displayed. This function is invoked every time the player changes the source shelf in order
 * to display in the destination drop-down menu only the shelves that aren't the one the player has just selected.
 */
public class SourceShelfListener implements ActionListener {
    private int shelfSize;
    private JComboBox dest;
    private CollectMoveBetweenShelves collector;
    private JPanel menus;

    /**
     * Constructs a SourceShelfListener ActionListener
     * @param shelfSize the number of shelves present in the Depot
     * @param collector the function used to collect the player's choices from the MoveBetweenShelves panel
     * @param menus a panel containing all the JComboBox for the MoveShelfToLeader panel
     */
    public SourceShelfListener(int shelfSize, CollectMoveBetweenShelves collector, JPanel menus) {
        this.shelfSize = shelfSize;
        this.collector = collector;
        this.menus = menus;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        JComboBox box = (JComboBox) e.getSource();
        int sourceShelf = Integer.parseInt(box.getSelectedItem().toString().split(" ")[1]);

        String[] destShelf = new String[shelfSize - 1];
        for(int j = 0, i = 1; j < destShelf.length; i++)
            if(i!= sourceShelf) {
                destShelf[j] = "Shelf " + i;
                j++;
            }

        if(dest == null){
            dest = new JComboBox(destShelf);
            collector.setDestShelf(dest);
        }else{
            menus.remove(dest);
            dest = new JComboBox(destShelf);
        }

        menus.add(dest);
        menus.revalidate();
        menus.repaint();
    }
}
