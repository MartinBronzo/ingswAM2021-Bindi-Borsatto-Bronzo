package it.polimi.ingsw.view.gui.ViewComponents;

import it.polimi.ingsw.model.ResourceType;
import it.polimi.ingsw.view.gui.ViewComponents.DepotDragDrop.DropChecker;
import it.polimi.ingsw.view.gui.ViewComponents.DepotDragDrop.Droppable;
import it.polimi.ingsw.view.gui.ViewComponents.DepotDragDrop.MyDropTargetListener;
import it.polimi.ingsw.view.gui.ViewComponents.DepotDragDrop.RegisterDrop;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.util.HashMap;
import java.util.Map;

public class StrongBoxDrop extends JPanel implements Droppable {
    private Map<ResourceType, Integer> resToStrongBox;
    private MyDropTargetListener targetListener;
    
    public StrongBoxDrop(){
        super();
        resToStrongBox = new HashMap<>();

        this.setBorder(new TitledBorder("Drag here the resource you want to store in your StrongBox"));

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
                StrongBoxDrop.this.add(new JLabel(ico));
                return true;
            }
        };

        this.setTransferHandler(dnd);
        this.targetListener = null;

    }

    public void setTargetListenerAndCheckDropFunction(DropChecker checkDropFunction){
        this.targetListener = new MyDropTargetListener(this, new RegisterDrop(this));
        this.targetListener.setCheckDrop(checkDropFunction);
    }

    @Override
    public void addDecision(Integer shelf, ResourceType res) {
        if(this.resToStrongBox.get(res) != null && this.resToStrongBox.get(res) > 0)
            this.resToStrongBox.put(res, this.resToStrongBox.get(res) + 1);
        else
            this.resToStrongBox.put(res, 1);
    }

    @Override
    public int getShelfNumber() {
        return 0;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(new ImageIcon("src/main/resources/strongbox medium.png").getImage(), 100, 100, null);
    }

    public void resetState(){
        this.resToStrongBox = new HashMap<>();
    }

    public Map<ResourceType, Integer> getDecisions() {
        return resToStrongBox;
    }

}
