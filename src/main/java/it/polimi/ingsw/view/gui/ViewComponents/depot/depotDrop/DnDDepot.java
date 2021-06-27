package it.polimi.ingsw.view.gui.ViewComponents.depot.depotDrop;

import it.polimi.ingsw.view.gui.ViewComponents.interfaces.DropChecker;
import it.polimi.ingsw.view.gui.ViewComponents.resources.resourcesDrag.InfiniteResourcesDrag;
import it.polimi.ingsw.view.gui.ViewComponents.resources.resourcesDrag.LimitedResourcesDrag;
import it.polimi.ingsw.view.gui.ViewComponents.interfaces.Resettable;

import javax.swing.*;

/**
 * This panel is made of a DepotDrop and a panel of draggable resources (either filled with infinite or finite resources).
 */
public class DnDDepot extends JPanel implements Resettable {
    DepotDrop depot;
    InfiniteResourcesDrag resources;

    /**
     * Creates a DnDDepot with only the DepotDrop ready. Next, the user of this class must specify whether the draggable resources are finite
     * or infinite
     */
    public DnDDepot(){
        super();
        this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        depot = new DepotDrop();

        this.add(depot);


    }

    /**
     * Returns a reference to the DepotDrop stored into this object
     * @return a reference to the DepotDrop stored into this object
     */
    public DepotDrop getDepot() {
        return depot;
    }

    /**
     * Initiates the DnDDepot with a panel with infinite draggable resources
     * @param checker the DropChecker function used to check the drop onto the DepotDrop from the panel with infinite resources
     */
    public void initFromInfiniteDrag(DropChecker checker){
        //this.depot.setCheckDropFunction(checker);
        try {
            this.add(new InfiniteResourcesDrag());
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        this.depot.initFromInfiniteDrag(checker);
    }

    /**
     * Initiates the DnDDepot with a panel with finite draggable resources
     * @param checker the DropChecker function used to check the drop onto the DepotDrop from the panel with finite resources
     */
    public void initFromFiniteDrag(DropChecker checker){
        //this.depot.setCheckDropFunction(checker);
        LimitedResourcesDrag limitedRes = new LimitedResourcesDrag();
        try {
            this.add(limitedRes);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        this.depot.initFromFiniteDrag(checker, limitedRes);
    }

    @Override
    public void resetState() {
        this.depot.resetState();
    }
}
