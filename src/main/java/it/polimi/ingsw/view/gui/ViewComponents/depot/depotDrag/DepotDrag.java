package it.polimi.ingsw.view.gui.ViewComponents.depot.depotDrag;

import it.polimi.ingsw.view.gui.ViewComponents.utils.MyDragGestureListener;
import it.polimi.ingsw.view.gui.ViewComponents.interfaces.DragUpdatable;
import it.polimi.ingsw.view.gui.ViewComponents.interfaces.Resettable;
import it.polimi.ingsw.view.gui.mainViews.PanelManager;
import it.polimi.ingsw.view.lightModel.player.DepotShelf;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.util.ArrayList;
import java.util.List;

/**
 * This panel represents a depot where resources can be dragged from the shelves.
 */
public class DepotDrag extends JPanel implements DragUpdatable, Resettable {
    private List<ShelfDrag> shelves;
    private List<MyDragGestureListener> dragListeners;

    /**
     * Constructs a Depot where resources can be dragged from the shelves
     */
    public DepotDrag(){
        super();
        this.setBorder(new TitledBorder("Drag Resources from your depot"));
        this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        this.shelves = new ArrayList<>();
        this.dragListeners = new ArrayList<>();

        ShelfDrag shelf = new ShelfDrag(1);
        //new UnWantedDropTarget(shelf);
        shelf.setDlistener();
        this.shelves.add(shelf);
        this.add(shelf);

        shelf = new ShelfDrag(2);
        //new UnWantedDropTarget(shelf);
        shelf.setDlistener();
        this.shelves.add(shelf);
        this.add(shelf);

        shelf = new ShelfDrag(3);
        //new UnWantedDropTarget(shelf);
        shelf.setDlistener();
        this.shelves.add(shelf);
        this.add(shelf);
    }

    /**
     * Initiates this object by filling it with the resources that the player holds in their depot
     */
    public void init() {
       /* for (ShelfDrag s : this.shelves) {
            //new UnWantedDropTarget(s, this);
            //s.setDlistener(this);

        }*/

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


    @Override
    public void updateAfterDrop(String info) {
        String strings [] = info.split(" ");
        int shelf = Integer.parseInt(strings[1]);
        this.shelves.get(shelf - 1).updateAfterDrop(strings[0]);
    }


    //For testing purposes
    public void printChoices(){
        for(ShelfDrag s: this.shelves)
            s.printChoices();
    }

    @Override
    public void resetState() {
        for(ShelfDrag sD: this.shelves)
            sD.resetState();
        fillDepot();
        this.revalidate();
        this.repaint();
    }



        /*public void undoDrag(String shelf){
        this.shelves.get(Integer.parseInt(shelf) - 1).setResourceVisible();
    }

    //@Override
    public void updateAfterDragBegin(String info){
        this.shelves.get(Integer.parseInt(info) - 1).updateResourcesRemovedCounter();
    }*/

}
