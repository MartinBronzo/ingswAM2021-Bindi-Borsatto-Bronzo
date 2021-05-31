package it.polimi.ingsw.view.gui.ViewComponents.DepotDragDrop;

import it.polimi.ingsw.exceptions.IllegalActionException;
import it.polimi.ingsw.view.gui.panels.PanelManager;

import javax.swing.*;
import java.util.function.Predicate;

public class CheckDropAtBeginningDecisionsTime implements DropChecker {
    private DepotDragAndDrop depot;

    public CheckDropAtBeginningDecisionsTime(DepotDragAndDrop depot){
        this.depot = depot;
    }

    @Override
    public boolean test(JPanel jPanel) throws IllegalActionException {
        PanelManager manager = PanelManager.getInstance();
        MyDepotPanel depotPanel = (MyDepotPanel) jPanel;
        //Checks whether the user has already specified all the resources they are supposed to
        if(depot.getDecisions().size() == manager.getResourcesToTake())
            if(manager.getResourcesToTake() > 0)
                throw new IllegalActionException("You have already specified your choices!");
            else
                throw new IllegalActionException("You cannot choose any extra resource!");

        //Checks whether there is still room onto the shelf
        if(depotPanel.getResToDepot().size() + manager.getDepotShelves().get(depotPanel.getShelfNumber() - 1).getQuantity() == getMaxShelfCapacity(depotPanel.getShelfNumber()))
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
