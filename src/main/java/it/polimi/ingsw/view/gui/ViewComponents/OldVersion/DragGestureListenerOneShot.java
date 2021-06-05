package it.polimi.ingsw.view.gui.ViewComponents.OldVersion;

import it.polimi.ingsw.view.gui.ViewComponents.interfaces.DragUpdatable;

import java.awt.dnd.DragGestureEvent;
import java.awt.dnd.DragGestureListener;

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
        /*JLabel label = (JLabel) event.getComponent();
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
        event.startDrag(null, transferable);*/
    }
}