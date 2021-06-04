package it.polimi.ingsw.view.gui.ViewComponents.OldVersion;

import it.polimi.ingsw.view.gui.ViewComponents.DragUpdatable;

import javax.swing.*;
import java.awt.*;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.dnd.DnDConstants;
import java.awt.dnd.DropTarget;
import java.awt.dnd.DropTargetAdapter;
import java.awt.dnd.DropTargetDropEvent;

/**
 * This class listens to whether a resources is drop to a depot which is actually a drag source.
 */
public class UnWantedDropTarget extends DropTargetAdapter {

    private DropTarget dropTarget;
    private JPanel p;
    //private Consumer<Icon> makeCall;
    //private DropChecker checkDrop;
    private DragUpdatable depot;

    public UnWantedDropTarget(JPanel panel, DragUpdatable depot) {
        p = panel;
        dropTarget = new DropTarget(panel, DnDConstants.ACTION_COPY, this, true, null);
        this.depot = depot;
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
                    ImageIcon image = (ImageIcon) ico;
                    /*switch (image.getDescription()){
                        //TODO: questo metodo dipende molto dal fatto che su i singoli shelf ci siano solo risorse dello stesso tipo, come renderlo più estendibile?
                        //Guarda setResourceVisibleInShelf(): cerca un qualsiasi elemento invisibile e lo setta a true, se ci fossero elementi di tipi diversi questo
                        //darebbe problemi. Questa soluzione non è molto omogenea perché da altri parti NON abbiamo supposto che su un solo scaffale ci potessero
                        //stare risorse diverse
                        case "1":
                            System.out.println(1);
                            depot.undoDrag("1");
                            break;
                        case "2":
                            System.out.println(2);
                            depot.undoDrag("2");
                            break;
                        case "3":
                            System.out.println(3);
                            depot.undoDrag("3");

                            break;
                    }*/
                    //depot.undoDrag(image.getDescription());
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
}