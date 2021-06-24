package it.polimi.ingsw.view.gui.ViewComponents.depot.depotDrop;

import it.polimi.ingsw.view.gui.ViewComponents.interfaces.DropChecker;
import it.polimi.ingsw.view.gui.ViewComponents.resources.resourcesDrag.InfiniteResourcesDrag;
import it.polimi.ingsw.view.gui.ViewComponents.resources.resourcesDrag.LimitedResourcesDrag;
import it.polimi.ingsw.view.gui.ViewComponents.interfaces.Resettable;

import javax.swing.*;

/**
 * This panel is made of a DepotDrop and a panel of draggable resources.
 */
public class DnDDepot extends JPanel implements Resettable {
    DepotDrop depot;
    InfiniteResourcesDrag resources;

    public DnDDepot(){
        super();
        this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        depot = new DepotDrop();

        this.add(depot);


    }

    public DepotDrop getDepot() {
        return depot;
    }

    public void initFromInfiniteDrag(DropChecker checker){
        //this.depot.setCheckDropFunction(checker);
        try {
            this.add(new InfiniteResourcesDrag());
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        this.depot.initFromInfiniteDrag(checker);
    }

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
