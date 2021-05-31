package it.polimi.ingsw.view.gui.ViewComponents.DepotDragDrop;

import javax.swing.*;
import java.awt.*;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.dnd.DnDConstants;
import java.awt.dnd.DropTarget;
import java.awt.dnd.DropTargetAdapter;
import java.awt.dnd.DropTargetDropEvent;
import java.util.function.Consumer;
import java.util.function.Predicate;

class MyDropTargetListener extends DropTargetAdapter {

    private DropTarget dropTarget;
    private JPanel p;
    private Consumer<Icon> makeCall;
    private Predicate<JPanel> checkDrop;

    public MyDropTargetListener(MyDepotPanel panel) {
        p = panel;
        dropTarget = new DropTarget(panel, DnDConstants.ACTION_COPY, this, true, null);
        //makeCall = new RegisterDrop(panel);
    }

    public MyDropTargetListener(MyDepotPanel panel, Consumer<Icon> actionListener) {
        p = panel;
        dropTarget = new DropTarget(panel, DnDConstants.ACTION_COPY, this, true, null);
        makeCall = actionListener;
    }

    public MyDropTargetListener(MyDepotPanel panel, Consumer<Icon> actionListener, Predicate<JPanel> checkDrop){
        p = panel;
        dropTarget = new DropTarget(panel, DnDConstants.ACTION_COPY, this, true, null);
        makeCall = actionListener;
        this.checkDrop = checkDrop;
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
                    if(checkDrop.test(p)) {
                        p.add(new JLabel(ico));
                        p.revalidate();
                        p.repaint();
                        event.dropComplete(true);
                        this.makeCall.accept(ico);
                    }else{
                        event.rejectDrop();
                        System.out.println("Nope");
                    }
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

    public void setCheckDrop(Predicate<JPanel> checkDrop) {
        this.checkDrop = checkDrop;
    }
}
