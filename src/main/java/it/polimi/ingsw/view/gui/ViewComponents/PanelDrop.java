package it.polimi.ingsw.view.gui.ViewComponents;

import it.polimi.ingsw.model.ResourceType;
import it.polimi.ingsw.network.messages.fromClient.DepotParams;
import it.polimi.ingsw.view.gui.ViewComponents.DepotDragDrop.MyDropTargetListener;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class PanelDrop extends JPanel {
    private MyDropTargetListener targetListener;
    private HashMap<ResourceType, Integer> fromStrongBox;
    private List<DepotParams> fromDepot;

    public PanelDrop(){
        super();

        this.setBorder(new TitledBorder("Drag here the resource you want to store in your StrongBox"));

        this.fromStrongBox = new HashMap<>();
        this.fromDepot = new ArrayList<>();

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

    public void init(MyDropTargetListener targetListener){
        this.targetListener = targetListener;
    }

    public void updateDepot(String res, String shelf) {
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

    public void updateStrongBox(String res) {
        ResourceType resDropped = ResourceType.valueOf(res);
        if(fromStrongBox.get(resDropped) == null)
            fromStrongBox.put(resDropped, 1);
        else
            fromStrongBox.put(resDropped, fromStrongBox.get(resDropped) + 1);
    }

    public HashMap<ResourceType, Integer> getFromStrongBox() {
        return fromStrongBox;
    }

    public List<DepotParams> getFromDepot() {
        return fromDepot;
    }

    public boolean hasPlayerSpecifiedEverything(){
        return this.targetListener.hasPlayerSpecifiedEverything();
    }
}