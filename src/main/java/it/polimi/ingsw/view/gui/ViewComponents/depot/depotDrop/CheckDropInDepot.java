package it.polimi.ingsw.view.gui.ViewComponents.depot.depotDrop;

import it.polimi.ingsw.exceptions.IllegalActionException;
import it.polimi.ingsw.view.gui.ViewComponents.interfaces.DropChecker;
import it.polimi.ingsw.view.gui.mainViews.PanelManager;

import javax.swing.*;

/**
 * This checker is used when we drop resources in the Depot when we place resources from the Market
 */
public class CheckDropInDepot implements DropChecker {

    private DepotDrop depot;

    /**
     * Constructs a CheckDropInDepot used to check whether drop can be made onto the specified DepotDrop panel
     * @param depot a DepotDrop panel
     */
    public CheckDropInDepot(DepotDrop depot){
        this.depot = depot;
    }

    @Override
    public boolean test(JPanel jPanel) throws IllegalActionException {
        PanelManager manager = PanelManager.getInstance();
        ShelfDrop depotPanel = (ShelfDrop) jPanel;

        //Checks whether there is still room onto the shelf
       // if(depotPanel.getResToDepot().size() + manager.getDepotShelves().get(depotPanel.getShelfNumber() - 1).getQuantity() == getMaxShelfCapacity(depotPanel.getShelfNumber()))
        if(depotPanel.getQuantityDropped() + manager.getDepotShelves().get(depotPanel.getDropInfo() - 1).getQuantity() == getMaxShelfCapacity(depotPanel.getDropInfo()))
            throw new IllegalActionException("There is no room on this shelf!");
        return true;
    }

    private int getMaxShelfCapacity(int shelfNumber){
        switch (shelfNumber){
            case 1:
                return 1;
            case 2:
                return 2;
            case 3:
                return 3;
            default:
                return 0;
        }
    }
}
