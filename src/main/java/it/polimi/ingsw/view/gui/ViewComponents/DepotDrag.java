package it.polimi.ingsw.view.gui.ViewComponents;

import it.polimi.ingsw.view.gui.panels.PanelManager;
import it.polimi.ingsw.view.readOnlyModel.player.DepotShelf;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.util.ArrayList;
import java.util.List;

/**
 * This panel represents a depot where we can drag the resources from the shelves.
 */
public class DepotDrag extends JPanel {
    private List<ShelfDrag> shelves;

    public DepotDrag(){
        super();
        this.setBorder(new TitledBorder("Drag Resources from here to the Depot above"));
        this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        this.shelves = new ArrayList<>();

        ShelfDrag shelf = new ShelfDrag(1);
        //new UnWantedDropTargetInDepot(shelf);
        this.shelves.add(shelf);
        this.add(shelf);

        shelf = new ShelfDrag(2);
        //new UnWantedDropTargetInDepot(shelf);
        this.shelves.add(shelf);
        this.add(shelf);

        shelf = new ShelfDrag(3);
        //new UnWantedDropTargetInDepot(shelf);
        this.shelves.add(shelf);
        this.add(shelf);
    }

    public void initDepotDrag() {
        for (ShelfDrag s : this.shelves) {
            new UnWantedDropTargetInDepot(s, this);
            s.setDlistener(this);
        }

        fillDepot();
    }
    private void fillDepot() {
        List<DepotShelf> depotShelves = PanelManager.getInstance().getDepotShelves();
        int i = 0;
        for(ShelfDrag sD: this.shelves) {
            sD.filShelf(depotShelves.get(i));
            i++;
        }
    }

    public void setResourceVisibleInShelf(int shelf){
        this.shelves.get(shelf - 1).setResourceVisible();
    }

    public void updateResourceRemovedCounterInShelf(int shelf){
        this.shelves.get(shelf - 1).updateResourcesRemovedCounter();
    }

    //For testing purposes
    public void printChoices(){
        for(ShelfDrag s: this.shelves)
            s.printChoices();
    }


}
