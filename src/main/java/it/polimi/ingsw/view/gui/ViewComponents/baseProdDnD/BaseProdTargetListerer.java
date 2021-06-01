/*package it.polimi.ingsw.view.gui.ViewComponents.baseProdDnD;

import it.polimi.ingsw.view.gui.ViewComponents.DepotDragDrop.ShelfDrop;

import javax.swing.*;
import java.awt.*;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.dnd.DnDConstants;
import java.awt.dnd.DropTarget;
import java.awt.dnd.DropTargetAdapter;
import java.awt.dnd.DropTargetDropEvent;
import java.util.function.Consumer;

        import javax.swing.*;
        import java.awt.*;
        import java.awt.datatransfer.DataFlavor;
        import java.awt.datatransfer.Transferable;
        import java.awt.dnd.DnDConstants;
        import java.awt.dnd.DropTarget;
        import java.awt.dnd.DropTargetAdapter;
        import java.awt.dnd.DropTargetDropEvent;
        import java.util.function.Consumer;

class BaseProdTargetListerer extends DropTargetAdapter {

    private DropTarget dropTarget;
    private BaseProdPanel p;
    private Consumer<Icon> makeCall;

    public BaseProdTargetListerer(BaseProdPanel panel) {
        p = panel;
        dropTarget = new DropTarget(panel, DnDConstants.ACTION_COPY, this, true, null);
        //makeCall = new RegisterDrop(panel);
    }

    public BaseProdTargetListerer(BaseProdPanel panel, Consumer<Icon> actionListener) {
        p = panel;
        dropTarget = new DropTarget(panel, DnDConstants.ACTION_COPY, this, true, null);
        makeCall = actionListener;
    }

    @Override
    public void drop(DropTargetDropEvent event) {
        try {
            DropTarget test = (DropTarget) event.getSource();
            Component ca = (Component) test.getComponent();
            Point dropPoint = ca.getMousePosition();
            Transferable tr = event.getTransferable();

            if (event.isDataFlavorSupported(DataFlavor.imageFlavor)) {
                Icon ico = (Icon) tr.getTransferData(DataFlavor.imageFlavor);

                if (ico != null) {

                    p.add(new JLabel(ico));
                    p.revalidate();
                    p.repaint();
                    event.dropComplete(true);
                    this.makeCall.accept(ico);
                }
            } else {
                event.rejectDrop();
            }
        } catch (Exception e) {
            e.printStackTrace();
            event.rejectDrop();
        }
    }

    public void addActionListener(Consumer<Icon> function){
        this.makeCall = function;
    }
}*/
