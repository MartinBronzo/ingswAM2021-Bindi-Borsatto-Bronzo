package it.polimi.ingsw.view.gui.ViewComponents;

import it.polimi.ingsw.view.gui.ViewComponents.DepotDragDrop.DropChecker;
import it.polimi.ingsw.view.gui.ViewComponents.DepotDragDrop.MyDropTargetListener;
import it.polimi.ingsw.view.gui.ViewComponents.DepotDragDrop.RegisterDrop;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;

public class PanelDrop extends JPanel {
    private MyDropTargetListener targetListener;
    public PanelDrop(){
        super();

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
}
