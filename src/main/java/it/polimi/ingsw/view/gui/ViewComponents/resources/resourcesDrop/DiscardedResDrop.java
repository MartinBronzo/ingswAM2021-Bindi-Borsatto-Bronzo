package it.polimi.ingsw.view.gui.ViewComponents.resources.resourcesDrop;

import it.polimi.ingsw.model.resources.ResourceType;
import it.polimi.ingsw.view.gui.ViewComponents.interfaces.DropChecker;
import it.polimi.ingsw.view.gui.ViewComponents.interfaces.DropResettable;
import it.polimi.ingsw.view.gui.ViewComponents.depot.depotDrop.RegisterDropFromFiniteRes;
import it.polimi.ingsw.view.gui.ViewComponents.depot.depotDrop.*;
import it.polimi.ingsw.view.gui.ViewComponents.interfaces.Droppable;
import it.polimi.ingsw.view.gui.ViewComponents.resources.resourcesDrag.LimitedResourcesDrag;
import it.polimi.ingsw.view.gui.ViewComponents.utils.MyDropTargetListener;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.List;

/**
 * This panel is where the player drops the resources bought in the market they cannot or don't want to store in the Depot.
 */
public class DiscardedResDrop extends JPanel implements Droppable, DropResettable {
    private Map<ResourceType, Integer> resToBeDiscarded;
    private MyDropTargetListener targetListener;
    private List<JLabel> labelResDropped;

    /**
     * Constructs a DiscardResDrop panel but not yet ready to be used: the user of this class must initiates the panel with either
     * the initFromInfiniteRes or initFromFiniteRes method
     */
    public DiscardedResDrop(){
        super();
        resToBeDiscarded = new HashMap<>();
        labelResDropped = new ArrayList<>();

        this.setBorder(new TitledBorder("Drop here the resource you want to discard"));

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
                DiscardedResDrop.this.add(new JLabel(ico));
                return true;
            }
        };

        this.setTransferHandler(dnd);
        this.targetListener = null;


    }

    /**
     * Initiates this panel to accept drops of resources who come from a panel containing infinite resources
     * @param checkDropFunction the DropChecker function used to check whether the drops are acceptable
     */
    public void initFromInfiniteRes(DropChecker checkDropFunction){
        this.targetListener = new MyDropTargetListener(this, new RegisterDropFromInfiniteRes(this));
        this.targetListener.setCheckDrop(checkDropFunction);
    }

    /**
     * Initiates this panel to accept drops of resources who come from the specified panel containing finite resources
     * @param checkDropFunction the DropChecker function used to check whether the drops are acceptable
     * @param resDrag the panel containing the limited resources that can be dropped onto this panel
     */
    public void initFromFiniteRes(DropChecker checkDropFunction, LimitedResourcesDrag resDrag){
        this.targetListener = new MyDropTargetListener(this, new RegisterDropFromFiniteRes(this, resDrag));
        this.targetListener.setCheckDrop(checkDropFunction);
    }

    @Override
    public void addDecision(Integer intInfo, ResourceType res) {
        if(this.resToBeDiscarded.get(res) != null && this.resToBeDiscarded.get(res) > 0)
            this.resToBeDiscarded.put(res, this.resToBeDiscarded.get(res) + 1);
        else
            this.resToBeDiscarded.put(res, 1);
    }

    @Override
    public int getDropInfo() {
        return 0;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(new ImageIcon("src/main/resources/trashcan medium.png").getImage(), 100, 100, null);
    }

    @Override
    public void resetState(){
        this.resToBeDiscarded = new HashMap<>();
        for(JLabel label : this.labelResDropped)
            this.remove(label);
        this.revalidate();
        this.repaint();
        this.labelResDropped = new ArrayList<>();
    }

    /**
     * Returns the resources the player decided do discard
     * @return the resources the player decided do discard
     */
    public Map<ResourceType, Integer> getDecisions() {
        return resToBeDiscarded;
    }

    @Override
    public void addDroppedLabel(JLabel label) {
        this.labelResDropped.add(label);
    }
}
