package it.polimi.ingsw.view.gui.ViewComponents.DepotDragDrop;

import it.polimi.ingsw.model.ResourceType;
import it.polimi.ingsw.view.gui.DropResettable;
import it.polimi.ingsw.view.gui.ViewComponents.LimitedResourcesDrag;
import it.polimi.ingsw.view.gui.ViewComponents.RegisterDropFromFiniteRes;
import it.polimi.ingsw.view.gui.ViewComponents.Resettable;
import it.polimi.ingsw.view.gui.panels.PanelManager;
import it.polimi.ingsw.view.readOnlyModel.player.DepotShelf;

import javax.swing.*;
import java.util.ArrayList;
import java.util.List;

/**
 * This class represents a Depot where the player can drop resources.
 */
public class DepotDrop extends JPanel implements Resettable {
    private List<ShelfDrop> shelves;
    private List<MyDropTargetListener> targetListeners;

    public DepotDrop(){
        super();
        this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        this.shelves = new ArrayList<>();
        this.targetListeners = new ArrayList<>();

        ShelfDrop panel1 = new ShelfDrop(1);
        //this.targetListeners.add(new MyDropTargetListener(panel1, new RegisterDropFromInfiniteRes(panel1)));//this must be done or we wont be able to drop any image onto the empty panel
        this.add(panel1);
        shelves.add(panel1);

        ShelfDrop panel2 = new ShelfDrop(2);
        //this.targetListeners.add(new MyDropTargetListener(panel2, new RegisterDropFromInfiniteRes(panel2)));//this must be done or we wont be able to drop any image onto the empty panel
        this.add(panel2);
        shelves.add(panel2);

        ShelfDrop panel3 = new ShelfDrop(3);
        //this.targetListeners.add(new MyDropTargetListener(panel3, new RegisterDropFromInfiniteRes(panel3)));//this must be done or we wont be able to drop any image onto the empty panel
        this.add(panel3);
        shelves.add(panel3);

        fillDepot();
    }

    public List<ShelfDrop.Pair<Integer, ResourceType>> getDecisions(){
        List<ShelfDrop.Pair<Integer, ResourceType>> result = new ArrayList<>();
        for(ShelfDrop e: this.shelves)
            result.addAll(e.getResToDepot());
        return result;
    }

    public void resetState(){
        for(ShelfDrop e: this.shelves)
            e.resetState();
        fillDepot();
    }

    /**
     * Initiates the depot when the drag panel has infinite resources available (as in the Beginning Decisions panel)
     * @param checkDropFunction the function that checks whether the drop can be made in the depot
     */
    public void initFromInfiniteDrag(DropChecker checkDropFunction){
        for(ShelfDrop sDrop : this.shelves)
            this.targetListeners.add(new MyDropTargetListener(sDrop, new RegisterDropFromInfiniteRes(sDrop)));//this must be done or we wont be able to drop any image onto the empty panel
        this.setCheckDropFunction(checkDropFunction);
    }

    /**
     * Initiates the depot when the drag panel has finite resources available
     * @param checkDropFunction the function that checks whether the drop can be made in the depot
     */
    public void initFromFiniteDrag(DropChecker checkDropFunction, LimitedResourcesDrag resourcesDrag){
        for(ShelfDrop sDrop : this.shelves)
            this.targetListeners.add(new MyDropTargetListener(sDrop, new RegisterDropFromFiniteRes(sDrop, resourcesDrag)));//this must be done or we wont be able to drop any image onto the empty panel
        this.setCheckDropFunction(checkDropFunction);
    }

    public void setCheckDropFunction(DropChecker checkDropFunction){
        for(MyDropTargetListener listener : this.targetListeners)
            listener.setCheckDrop(checkDropFunction);
    }

    private void fillDepot(){
        List<DepotShelf> depotShelves = PanelManager.getInstance().getDepotShelves();
        int i = 0;
        for(DepotShelf dS: depotShelves) {
            //fillShelf(dS, this.shelves.get(i));
            this.shelves.get(i).fillShelf(dS);
            i++;
        }
    }

    /*private void fillShelf(DepotShelf depotShelf, ShelfDrop panel) {
        JLabel resource;
        for(int i = 0; i < depotShelf.getQuantity(); i++){
            resource = new JLabel(new ImageIcon(DepotDrop.getImagePathFromResource(depotShelf.getResourceType())));
            panel.add(resource);
        }
    }*/

    public static String getImagePathFromResource(ResourceType resource){
        switch (resource){
            case SHIELD:
                return "src/main/resources/shield small.png";
            case SERVANT:
                return "src/main/resources/servant small.png";
            case STONE:
                return "src/main/resources/stone small.png";
            case COIN:
                return "src/main/resources/coins small.png";
            case FAITHPOINT:
                return "src/main/resources/faithpoint small.png";
            default:
                return "";
        }
    }

}
