package it.polimi.ingsw.view.gui.ViewComponents.production.baseProd;

import it.polimi.ingsw.model.resources.ResourceType;
import it.polimi.ingsw.view.gui.ViewComponents.interfaces.Resettable;

import javax.swing.*;
import java.awt.datatransfer.DataFlavor;

public class BaseProdPanel extends JPanel implements Resettable {
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
        this.remove(getComponentCount()-1);
        this.revalidate();
        this.repaint();
    }

    public ResourceType getResource() {
        return resource;
    }

    @Override
    public void resetState() {
        resource = null;
        //removes the previous image of the resource and repaints the panel
        this.remove(getComponentCount()-1);
        this.add(new JLabel(new ImageIcon("src/main/resources/genericMarble.png")));
        this.revalidate();
        this.repaint();
    }
}



























