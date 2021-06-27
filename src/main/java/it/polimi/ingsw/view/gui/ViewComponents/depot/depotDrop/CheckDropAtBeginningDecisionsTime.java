package it.polimi.ingsw.view.gui.ViewComponents.depot.depotDrop;

import it.polimi.ingsw.exceptions.IllegalActionException;
import it.polimi.ingsw.view.gui.ViewComponents.interfaces.DropChecker;
import it.polimi.ingsw.view.gui.mainViews.PanelManager;

import javax.swing.*;

/**
 * This DropChecker function checks whether the player can make the drop onto the Depot in the BeginningDecisionPanel
 */
public class CheckDropAtBeginningDecisionsTime implements DropChecker {
    private DepotDrop depot;

    /**
     * Constructs a CheckDropAtBeginningDecisionsTime for the specified DepotDrop
     * @param depot the DepotDrop whose drops the function checks
     */
    public CheckDropAtBeginningDecisionsTime(DepotDrop depot){
        this.depot = depot;
    }

    @Override
    public boolean test(JPanel jPanel) throws IllegalActionException {
        PanelManager manager = PanelManager.getInstance();
        ShelfDrop depotPanel = (ShelfDrop) jPanel;
        //Checks whether the user has already specified all the resources they are supposed to
        if(depot.getDecisions().size() == manager.getResourcesToTake())
            if(manager.getResourcesToTake() > 0)
                throw new IllegalActionException("You have already specified your choices!");
            else
                throw new IllegalActionException("You cannot choose any extra resource!");

        //Checks whether there is still room onto the shelf
        //if(depotPanel.getResToDepot().size() + manager.getDepotShelves().get(depotPanel.getShelfNumber() - 1).getQuantity() == getMaxShelfCapacity(depotPanel.getShelfNumber()))
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
