package it.polimi.ingsw.view.gui.ViewComponents;

import javax.swing.*;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.dnd.DragGestureEvent;
import java.awt.dnd.DragGestureListener;
import java.io.IOException;

/**
 * This class makes the label being dragged invisible in its origin panel
 */
public class DragGestureListenerOneShot implements DragGestureListener {
    private DragUpdatable updatable;

    public DragGestureListenerOneShot(DragUpdatable updatable){
        this.updatable = updatable;
    }

    @Override
    public void dragGestureRecognized(DragGestureEvent event) {
        JLabel label = (JLabel) event.getComponent();
        final Icon ico = label.getIcon();


        Transferable transferable = new Transferable() {
            @Override
            public DataFlavor[] getTransferDataFlavors() {
                return new DataFlavor[]{DataFlavor.imageFlavor};
            }

            @Override
            public boolean isDataFlavorSupported(DataFlavor flavor) {
                if (!isDataFlavorSupported(flavor)) {
                    return false;
                }
                return true;
            }

            @Override
            public Object getTransferData(DataFlavor flavor) throws UnsupportedFlavorException, IOException {
                return ico;
            }
        };
        System.out.println("Drag to start");
        label.setVisible(false);
        ImageIcon image = (ImageIcon) ico;
        this.updatable.updateAfterDragBegin(image.getDescription());
        event.startDrag(null, transferable);
    }
}