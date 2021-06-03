package it.polimi.ingsw.view.gui.ViewComponents.DepotDragDrop;

import it.polimi.ingsw.exceptions.IllegalActionException;
import it.polimi.ingsw.view.gui.ViewComponents.RegisterDropInterface;

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

public class MyDropTargetListener extends DropTargetAdapter {

    private DropTarget dropTarget;
    private JPanel p;
    private RegisterDropInterface makeCall;
    private DropChecker checkDrop;

    public MyDropTargetListener(JPanel panel) {
        p = panel;
        dropTarget = new DropTarget(panel, DnDConstants.ACTION_COPY, this, true, null);
        //makeCall = new RegisterDrop(panel);
    }

    public MyDropTargetListener(JPanel panel, RegisterDropInterface actionListener) {
        p = panel;
        dropTarget = new DropTarget(panel, DnDConstants.ACTION_COPY, this, true, null);
        makeCall = actionListener;
    }

    public MyDropTargetListener(JPanel panel, RegisterDropInterface actionListener, DropChecker checkDrop){
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
                    try {
                        if (checkDrop.test(p)) {
                            this.makeCall.accept(ico);
                            p.add(new JLabel(ico));
                            p.revalidate();
                            p.repaint();
                            event.dropComplete(true);
                        } else {
                            event.rejectDrop();
                            JOptionPane.showMessageDialog(null, "You can't do this!");
                            System.out.println("Nope");
                        }
                    }catch (IllegalActionException e){
                        JOptionPane.showMessageDialog(null, e.getMessage());
                    }
                }
            } else {
                System.out.println("Drop rejected");
                event.rejectDrop();
            }
        } catch (Exception e) {
            e.printStackTrace();
            event.rejectDrop();
        }
    }

    public void addActionListener(RegisterDropInterface function){
        this.makeCall = function;
    }

    public void setCheckDrop(DropChecker checkDrop) {
        this.checkDrop = checkDrop;
    }
}
