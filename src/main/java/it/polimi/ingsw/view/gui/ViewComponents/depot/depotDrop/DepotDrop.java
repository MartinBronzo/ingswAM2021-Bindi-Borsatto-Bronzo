package it.polimi.ingsw.view.gui.ViewComponents.depot.depotDrop;

import it.polimi.ingsw.model.resources.ResourceType;
import it.polimi.ingsw.network.messages.fromClient.DepotParams;
import it.polimi.ingsw.view.gui.ViewComponents.interfaces.DropChecker;
import it.polimi.ingsw.view.gui.ViewComponents.resources.resourcesDrag.LimitedResourcesDrag;
import it.polimi.ingsw.view.gui.ViewComponents.interfaces.Resettable;
import it.polimi.ingsw.view.gui.ViewComponents.utils.MyDropTargetListener;
import it.polimi.ingsw.view.gui.mainViews.PanelManager;
import it.polimi.ingsw.view.lightModel.player.DepotShelf;

import javax.swing.*;
import java.util.ArrayList;
import java.util.List;

/**
 * This class represents a Depot where the player can drop resources. In order to use this class, the PanelManager needs to have the
 * depot shelves stored, even if they are empty (that is resourceType == null and quantity == 0).
 */
public class DepotDrop extends JPanel implements Resettable {
    private List<ShelfDrop> shelves;
    private List<MyDropTargetListener> targetListeners;

    /**
     * Constructs a DepotDrop the player can use to drop resources on. It contains the resources the player already has on the shelves
     */
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

    /*public List<ShelfDrop.Pair<Integer, ResourceType>> getDecisions(){
        List<ShelfDrop.Pair<Integer, ResourceType>> result = new ArrayList<>();
        for(ShelfDrop e: this.shelves)
            result.addAll(e.getResToDepot());
        return result;
    }*/

    /**
     * Returns a list of DepotParams which represents the choices the player has made, that is what resources the player has dropped onto which shelf
     * @return a list of DepotParams representing the player's choices
     */
    public List<DepotParams> getDecisions(){
        List<DepotParams> result = new ArrayList<>();
        DepotParams tmp;
        for(ShelfDrop sD : this.shelves)
            if(sD.getQuantityDropped() > 0) {
                tmp = new DepotParams(sD.getTypeForShelf(), sD.getQuantityDropped(), sD.getDropInfo());
                result.add(tmp);
            }
        return result;
    }

    @Override
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
        for(ShelfDrop sDrop : this.shelves) {
            this.targetListeners.add(new MyDropTargetListener(sDrop, new RegisterDropFromFiniteRes(sDrop, resourcesDrag)));//this must be done or we wont be able to drop any image onto the empty panel
            sDrop.setParentDepot(this);
        }
        this.setCheckDropFunction(checkDropFunction);
    }

    /**
     * Sets the DropChecker used to check whether the drop can be made onto the Depot
     * @param checkDropFunction a DropChecker function
     */
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

    /**
     * Returns the path to the image representing the specified type of resource
     * @param resource a resource type
     * @return the path to the image for the specified resource
     */
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

    /**
     * Returns a list of the resource types stored in the shelves that are not the specified one
     * @param currentDrop the number of a Depot shelf (from 1 to 3)
     * @return a list of the resource types stored in the other shelves than the one specified
     */
    public List<ResourceType> getStoredResources(int currentDrop){
        List<ResourceType> result = new ArrayList<>();
        for(int i = 0; i < this.shelves.size(); i++)
            if(i + 1 != currentDrop)
            result.add(this.shelves.get(i).getTypeForShelf());
        return result;
    }

}
