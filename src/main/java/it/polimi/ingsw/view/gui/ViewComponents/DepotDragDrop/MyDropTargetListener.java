package it.polimi.ingsw.view.gui.ViewComponents.DepotDragDrop;

import it.polimi.ingsw.exceptions.IllegalActionException;
import it.polimi.ingsw.view.gui.DropResettable;
import it.polimi.ingsw.view.gui.ViewComponents.CheckLimitedDrop;
import it.polimi.ingsw.view.gui.ViewComponents.RegisterDropInterface;
import it.polimi.ingsw.view.gui.ViewComponents.Resettable;

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
        //makeCall = new RegisterDropFromInfiniteRes(panel);
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
                            JLabel icoLabel = new JLabel(ico);
                            p.add(icoLabel);
                            if(p instanceof DropResettable)
                                ((DropResettable) p).addDroppedLabel(icoLabel);
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

    /**
     * Returns whether the player has specified all the resources they are supposed to in the case they are dropping limited resources in a PanelDrop
     * (in this case the checkDrop function is a CheckLimitedDrop)
     * @return true if the player has specified all the resources they are supposed to, false otherwise or if this MyDropTargetListener is used in another situation
     */
    public boolean hasPlayerSpecifiedEverything(){
        if(!(checkDrop instanceof CheckLimitedDrop))
            return false;
        return ((CheckLimitedDrop) checkDrop).hasPlayerSpecifiedEverything();
    }

    public void resetChecker(){
        if(this.checkDrop instanceof Resettable)
            ((Resettable) checkDrop).resetState();
    }
}
