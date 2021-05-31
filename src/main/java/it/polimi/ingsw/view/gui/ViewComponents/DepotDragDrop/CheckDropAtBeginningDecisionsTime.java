package it.polimi.ingsw.view.gui.ViewComponents.DepotDragDrop;

import it.polimi.ingsw.view.gui.panels.PanelManager;

import javax.swing.*;
import java.util.function.Predicate;

public class CheckDropAtBeginningDecisionsTime implements Predicate<JPanel> {
    private DepotDragAndDrop depot;

    public CheckDropAtBeginningDecisionsTime(DepotDragAndDrop depot){
        this.depot = depot;
    }

    @Override
    public boolean test(JPanel jPanel) {
        PanelManager manager = PanelManager.getInstance();
        MyDepotPanel depotPanel = (MyDepotPanel) jPanel;
        //Checks whether the user has already has already specified all the resources they are supposed to
        if(depot.getDecisions().size() == manager.getResourcesToTake())
            return false;
        if(depotPanel.getResToDepot().size() + manager.getDepotShelves().get(depotPanel.getShelfNumber() - 1).getQuantity() == getMaxShelfCapacity(depotPanel.getShelfNumber()))
            return false;
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
