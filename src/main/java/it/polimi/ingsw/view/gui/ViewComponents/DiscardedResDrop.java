package it.polimi.ingsw.view.gui.ViewComponents;

import it.polimi.ingsw.model.ResourceType;
import it.polimi.ingsw.view.gui.DropResettable;
import it.polimi.ingsw.view.gui.ViewComponents.DepotDragDrop.*;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.List;

public class DiscardedResDrop extends JPanel implements Droppable, DropResettable {
    private Map<ResourceType, Integer> resToBeDiscarded;
    private MyDropTargetListener targetListener;
    private List<JLabel> labelResDropped;

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

    public void initFromInfiniteRes(DropChecker checkDropFunction){
        this.targetListener = new MyDropTargetListener(this, new RegisterDropFromInfiniteRes(this));
        this.targetListener.setCheckDrop(checkDropFunction);
    }

    public void initFromFiniteRes(DropChecker checkDropFunction, LimitedResourcesDrag resDrag){
        this.targetListener = new MyDropTargetListener(this, new RegisterDropFromFiniteRes(this, resDrag));
        this.targetListener.setCheckDrop(checkDropFunction);
    }

    @Override
    public void addDecision(Integer shelf, ResourceType res) {
        if(this.resToBeDiscarded.get(res) != null && this.resToBeDiscarded.get(res) > 0)
            this.resToBeDiscarded.put(res, this.resToBeDiscarded.get(res) + 1);
        else
            this.resToBeDiscarded.put(res, 1);
    }

    @Override
    public int getShelfNumber() {
        return 0;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(new ImageIcon("src/main/resources/trashcan medium.png").getImage(), 100, 100, null);
    }

    public void resetState(){
        this.resToBeDiscarded = new HashMap<>();
        for(JLabel label : this.labelResDropped)
            this.remove(label);
        this.revalidate();
        this.repaint();
        this.labelResDropped = new ArrayList<>();
    }

    public Map<ResourceType, Integer> getDecisions() {
        return resToBeDiscarded;
    }

    @Override
    public void addDroppedLabel(JLabel label) {
        this.labelResDropped.add(label);
    }
}
