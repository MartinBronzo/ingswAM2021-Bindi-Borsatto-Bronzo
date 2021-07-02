package it.polimi.ingsw.view.gui.ViewComponents.production.baseProd;

import it.polimi.ingsw.model.resources.ResourceType;
import it.polimi.ingsw.view.gui.ViewComponents.interfaces.Resettable;

import javax.swing.*;
import java.awt.datatransfer.DataFlavor;

/**
 * This panel represents a input or output resource for the BaseProduction. The player can drop here the resources they want to
 * use.
 */
public class BaseProdPanel extends JPanel implements Resettable {
    private ResourceType resource;

    /**
     * Constructs a BaseProdPanel displaying a generic marble image
     */
    public BaseProdPanel() {
        super();
        this.setLayout(new BoxLayout(this, BoxLayout.LINE_AXIS));

        this.add(new JLabel(new ImageIcon(getClass().getResource("/genericMarble.png"))));

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

    /**
     * Takes track of the player's drop decision onto this panel
     * @param res the type of the resource that has been dropped onto this panel
     */
    public void addDecision(ResourceType res) {
        resource = res;
        //removes the previous image of the resource and repaints the panel
        this.remove(getComponentCount()-1);
        this.revalidate();
        this.repaint();
    }

    /**
     * Returns the type of the resource that has been dropped onto this panel
     * @return the type of the resource that has been dropped onto this panel
     */
    public ResourceType getResource() {
        return resource;
    }

    @Override
    public void resetState() {
        resource = null;
        //removes the previous image of the resource and repaints the panel
        this.remove(getComponentCount()-1);
        this.add(new JLabel(new ImageIcon(getClass().getResource("/genericMarble.png"))));
        this.revalidate();
        this.repaint();
    }
}



























