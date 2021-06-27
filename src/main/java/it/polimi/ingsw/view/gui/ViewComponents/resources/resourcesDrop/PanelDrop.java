package it.polimi.ingsw.view.gui.ViewComponents.resources.resourcesDrop;

import it.polimi.ingsw.model.resources.ResourceType;
import it.polimi.ingsw.network.messages.fromClient.DepotParams;
import it.polimi.ingsw.view.gui.ViewComponents.interfaces.DropResettable;
import it.polimi.ingsw.view.gui.ViewComponents.strongbox.StrongBoxDrag;
import it.polimi.ingsw.view.gui.ViewComponents.depot.depotDrag.DepotDrag;
import it.polimi.ingsw.view.gui.ViewComponents.utils.MyDropTargetListener;
import it.polimi.ingsw.view.gui.ViewComponents.interfaces.RegisterDropInterface;
import it.polimi.ingsw.view.gui.ViewComponents.leaderCards.leaderCardsDrag.DragLeaderCards;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * This panel is where the player drops the limited resources they want to use for something and that they stored somewhere in their boards.
 */
public class PanelDrop extends JPanel implements DropResettable {
    private MyDropTargetListener targetListener;
    private HashMap<ResourceType, Integer> fromStrongBox;
    private List<DepotParams> fromDepot;
    private HashMap<ResourceType, Integer> fromLeaders;
    private List<JLabel> addedLabels;

    /**
     * Constructs a PanelDrop but not ready to be used yet: the user of this class must initiates this panel by using the init method
     */
    public PanelDrop(){
        super();

        this.setBorder(new TitledBorder("Drag here the resource you want to use"));

        this.fromStrongBox = new HashMap<>();
        this.fromDepot = new ArrayList<>();
        this.fromLeaders = new HashMap<>();
        this.addedLabels = new ArrayList<>();

        TransferHandler dnd = new TransferHandler() {
            @Override
            public boolean canImport(TransferSupport support) {
                if (!support.isDrop()) {
                    return false;
                }
                //only Strings
                if (!support.isDataFlavorSupported(DataFlavor.imageFlavor)) {
                    return false;
                }
                return true;
            }

            @Override
            public boolean importData(TransferSupport support) {
                if (!canImport(support)) {
                    return false;
                }

                Transferable tansferable = support.getTransferable();
                Icon ico;
                try {
                    ico = (Icon) tansferable.getTransferData(DataFlavor.imageFlavor);
                } catch (Exception e) {
                    e.printStackTrace();
                    return false;
                }
                PanelDrop.this.add(new JLabel(ico));
                return true;
            }
        };

        this.setTransferHandler(dnd);
        this.targetListener = null;

    }

    @Deprecated
    public void init(MyDropTargetListener targetListener){
        this.targetListener = targetListener;
    }

    @Deprecated
    public void init(CheckLimitedDrop checker, StrongBoxDrag strongBoxDrag, DepotDrag depotDrag, PanelDrop panelTarget){
        RegisterDropInterface registerDrop = new RegisterLimitedDropInPlainPanel(checker, strongBoxDrag, depotDrag, panelTarget);
        this.targetListener = new MyDropTargetListener(panelTarget, registerDrop, checker);
    }

    /**
     * Initiates this panel to accept limited drops
     * @param checker the DropChecker function used to check drops made onto this panel
     * @param depot the player's Depot which can be a source of the resources dropped onto this panel
     * @param strongBox the player's StrongBox which can be a source of the resources dropped onto this panel
     * @param leaders the player's ExtraSlot LeaderCards which can be a source of the resources dropped onto this panel
     */
    public void init(CheckLimitedDrop checker, DepotDrag depot, StrongBoxDrag strongBox, DragLeaderCards leaders){
        RegisterDropInterface registerDrop = new RegisterLimitedDropInPlainPanel(checker, strongBox, depot, leaders, this);
        this.targetListener = new MyDropTargetListener(this, registerDrop, checker);
    }

    /**
     * Updates this panel with the information that a drop of a resource who came from the player's Depot has been made onto the panel itself
     * @param res the dropped resource
     * @param shelf the origin shelf of the resource
     */
    public void updateDepotCounter(String res, String shelf) {
        int shelfNum = Integer.parseInt(shelf);
        ResourceType resDropped = ResourceType.valueOf(res);
        DepotParams originShelf = this.fromDepot.stream().filter(x -> x.getShelf() == shelfNum).findAny().orElse(null);
        if(originShelf == null){
            originShelf = new DepotParams(resDropped, 1, shelfNum);
            this.fromDepot.add(originShelf);
        }else{
            originShelf.setQt(originShelf.getQt() + 1);
        }

    }

    /**
     * Updates this panel with the information that a drop of a resource who came from the player's StrongBox has been made onto the panel itself
     * @param res the dropped resource
     */
    public void updateStrongBoxCounter(String res) {
        ResourceType resDropped = ResourceType.valueOf(res);
        if(fromStrongBox.get(resDropped) == null)
            fromStrongBox.put(resDropped, 1);
        else
            fromStrongBox.put(resDropped, fromStrongBox.get(resDropped) + 1);
    }

    /**
     * Updates this panel with the information that a drop of a resource who came from the player's ExtraSlot LeaderCards has been made onto the panel itself
     * @param res the dropped resource
     */
    public void updateLeaderCardsCounter(String res){
        ResourceType resDropped = ResourceType.valueOf(res);
        if(fromLeaders.get(resDropped) == null)
            fromLeaders.put(resDropped, 1);
        else
            fromLeaders.put(resDropped, fromLeaders.get(resDropped) + 1);
    }

    /**
     * Returns the player's drops from the StrongBox
     * @return the player's drops from the StrongBox
     */
    public HashMap<ResourceType, Integer> getFromStrongBox() {
        return fromStrongBox;
    }

    /**
     * Returns the player's drops from the Depot
     * @return the player's drops from the Depot
     */
    public List<DepotParams> getFromDepot() {
        return fromDepot;
    }

    /**
     * Returns the player's drops from the ExtraSlot LeaderCards
     * @return the player's drops from the ExtraSlot LeaderCards
     */
    public HashMap<ResourceType, Integer> getFromLeaders() {
        return fromLeaders;
    }

    /**
     * Returns whether the player has dropped all the resources they were supposed to
     * @return true if the player has dropped all the resources they were supposed to, false otherwise
     */
    public boolean hasPlayerSpecifiedEverything(){
        return this.targetListener.hasPlayerSpecifiedEverything();
    }

    @Override
    public void resetState() {
        this.fromStrongBox = new HashMap<>();
        this.fromDepot = new ArrayList<>();
        this.fromLeaders = new HashMap<>();
        for(JLabel label: this.addedLabels)
            this.remove(label);
        this.addedLabels = new ArrayList<>();
        this.targetListener.resetChecker();
        this.revalidate();
        this.repaint();
    }

    @Override
    public void addDroppedLabel(JLabel label) {
        this.addedLabels.add(label);
    }
}
