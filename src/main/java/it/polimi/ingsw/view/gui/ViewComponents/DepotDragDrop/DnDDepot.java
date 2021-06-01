package it.polimi.ingsw.view.gui.ViewComponents.DepotDragDrop;

import javax.swing.*;

/**
 * This panel is made of a DepotDrop and a panel of draggable resources.
 */
public class DnDDepot extends JPanel {
    DepotDrop depot;
    InfiniteResourcesDrag resources;

    public DnDDepot(){
        super();
        this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        depot = new DepotDrop();

        this.add(depot);

        try {
            this.add(new InfiniteResourcesDrag());
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public DepotDrop getDepot() {
        return depot;
    }

    public void setCheckDropFunction(DropChecker checker){
        this.depot.setCheckDropFunction(checker);
    }
}
