package it.polimi.ingsw.view.gui.ViewComponents.baseProdDnD;

import it.polimi.ingsw.model.ResourceType;
import it.polimi.ingsw.view.gui.ViewComponents.DepotDragDrop.Droppable;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;

public class BaseProdPanel extends JPanel {
    private ResourceType resource;

    public BaseProdPanel() {
        super();
        this.setLayout(new BoxLayout(this, BoxLayout.LINE_AXIS));

        this.add(new JLabel(new ImageIcon("src/main/resources/genericMarble.png")));

        JPanel container = new JPanel();
        container.setLayout(new BoxLayout(container, BoxLayout.PAGE_AXIS));

        TransferHandler transferHandler = new TransferHandler() {
            @Override
            public boolean canImport(TransferSupport support) {
                if (!support.isDrop()) {
                    return false;
                }
                //only Strings
                return support.isDataFlavorSupported(DataFlavor.imageFlavor);
            }

            @Override
            public boolean importData(TransferSupport support) {
                return canImport(support);
            }
        };
        this.setTransferHandler(transferHandler);
    }

    public void addDecision(ResourceType res) {
        resource = res;
        //removes the previous image of the resource and repaints the panel
        this.remove(getComponentCount()-2);
        this.revalidate();
        this.repaint();
    }

    public ResourceType getResource() {
        return resource;
    }

}


























