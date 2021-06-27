package it.polimi.ingsw.view.gui.ViewComponents.utils;

import it.polimi.ingsw.exceptions.IllegalActionException;
import it.polimi.ingsw.view.gui.ViewComponents.interfaces.DropChecker;
import it.polimi.ingsw.view.gui.ViewComponents.interfaces.DropResettable;
import it.polimi.ingsw.view.gui.ViewComponents.resources.resourcesDrop.CheckLimitedDrop;
import it.polimi.ingsw.view.gui.ViewComponents.interfaces.RegisterDropInterface;
import it.polimi.ingsw.view.gui.ViewComponents.interfaces.Resettable;

import javax.swing.*;
import java.awt.*;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.dnd.DnDConstants;
import java.awt.dnd.DropTarget;
import java.awt.dnd.DropTargetAdapter;
import java.awt.dnd.DropTargetDropEvent;

/**
 * This class is the DropTargetAdapter used in this application. It checks whether the drop can be made and if that is the
 * case then it takes note of that.
 */
public class MyDropTargetListener extends DropTargetAdapter {
    private DropTarget dropTarget;
    /**
     * The panel where the drop has been made
     */
    private JPanel p;
    /**
     * The function to be called in order to check whether a drop can be made
     */
    private DropChecker checkDrop;
    /**
     * The function to be called in order to register the drop
     */
    private RegisterDropInterface makeCall;

    /**
     * Constructs a MyDrpTargetListener which refers the specified panel and uses the specified RegisterDropInterface function
     * @param panel the panel the drop are made onto
     * @param actionListener the function used to register the drop
     */
    public MyDropTargetListener(JPanel panel, RegisterDropInterface actionListener) {
        p = panel;
        dropTarget = new DropTarget(panel, DnDConstants.ACTION_COPY, this, true, null);
        makeCall = actionListener;
    }

    /**
     * Constructs a MyDrpTargetListener which refers the specified panel and uses the specified RegisterDropInterface function and DropChecker function
     * @param panel the panel the drop are made onto
     * @param actionListener the function used to register the drop
     * @param checkDrop the function used to check whether the drop can be made
     */
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

    /**
     * Adds the specified RegisterDropInterface function
     * @param function the function to be added
     */
    public void addActionListener(RegisterDropInterface function){
        this.makeCall = function;
    }

    /**
     * Adds the specified DropChecker function
     * @param checkDrop the function to be added
     */
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

    /**
     * Resets the DropChecker function if it implements the Resettable interface
     */
    public void resetChecker(){
        if(this.checkDrop instanceof Resettable)
            ((Resettable) checkDrop).resetState();
    }

    /*public MyDropTargetListener(JPanel panel) {
        p = panel;
        dropTarget = new DropTarget(panel, DnDConstants.ACTION_COPY, this, true, null);
        //makeCall = new RegisterDropFromInfiniteRes(panel);
    }*/
}
