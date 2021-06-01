package it.polimi.ingsw.view.gui.ViewComponents.DepotDragDrop;

import it.polimi.ingsw.model.ResourceType;
import it.polimi.ingsw.view.gui.panels.PanelManager;
import it.polimi.ingsw.view.readOnlyModel.player.DepotShelf;

import javax.swing.*;
import java.util.ArrayList;
import java.util.List;

/**
 * This class represents a Depot where the player can drop resources.
 */
public class DepotDrop extends JPanel {
    private List<ShelfDrop> depots;
    private List<MyDropTargetListener> targetListeners;

    public DepotDrop(){
        super();
        this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        this.depots = new ArrayList<>();
        this.targetListeners = new ArrayList<>();

        ShelfDrop panel1 = new ShelfDrop(1);
        this.targetListeners.add(new MyDropTargetListener(panel1, new RegisterDrop(panel1)));//this must be done or we wont be able to drop any image onto the empty panel
        this.add(panel1);
        depots.add(panel1);

        ShelfDrop panel2 = new ShelfDrop(2);
        this.targetListeners.add(new MyDropTargetListener(panel2, new RegisterDrop(panel2)));//this must be done or we wont be able to drop any image onto the empty panel
        this.add(panel2);
        depots.add(panel2);

        ShelfDrop panel3 = new ShelfDrop(3);
        this.targetListeners.add(new MyDropTargetListener(panel3, new RegisterDrop(panel3)));//this must be done or we wont be able to drop any image onto the empty panel
        this.add(panel3);
        depots.add(panel3);

        fillDepot();
    }

    public List<ShelfDrop.Pair<Integer, ResourceType>> getDecisions(){
        List<ShelfDrop.Pair<Integer, ResourceType>> result = new ArrayList<>();
        for(ShelfDrop e: this.depots)
            result.addAll(e.getResToDepot());
        return result;
    }

    public void resetState(){
        for(ShelfDrop e: this.depots)
            e.resetState();
    }

    public void setCheckDropFunction(DropChecker checkDropFunction){
        for(MyDropTargetListener listener : this.targetListeners)
            listener.setCheckDrop(checkDropFunction);
    }

    private void fillDepot(){
        List<DepotShelf> depotShelves = PanelManager.getInstance().getDepotShelves();
        int i = 0;
        for(DepotShelf dS: depotShelves) {
            fillShelf(dS, this.depots.get(i));
            i++;
        }
    }

    private void fillShelf(DepotShelf depotShelf, ShelfDrop panel) {
        JLabel resource;
        for(int i = 0; i < depotShelf.getQuantity(); i++){
            resource = new JLabel(new ImageIcon(DepotDrop.getImagePathFromResource(depotShelf.getResourceType())));
            panel.add(resource);
        }
    }

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
