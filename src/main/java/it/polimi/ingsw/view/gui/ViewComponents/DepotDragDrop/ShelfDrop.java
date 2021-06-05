package it.polimi.ingsw.view.gui.ViewComponents.DepotDragDrop;

import it.polimi.ingsw.model.ResourceType;
import it.polimi.ingsw.view.gui.DropResettable;
import it.polimi.ingsw.view.readOnlyModel.player.DepotShelf;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.util.ArrayList;
import java.util.List;

public class ShelfDrop extends JPanel implements Droppable, DropResettable {
    //TODO: ci sarà da stampare le risorse già presenti nel depot
    private int shelfNumber;
    /**
     * In each depot panel we have this resToDepot list which takes track of all the resources moved to this depot. When  the confirm
     * button is clicked then all the list from all the three depot panels must be collected and sent to the model somehow
     */
    private List<Pair<Integer, ResourceType>> resToDepot;
    private List<JLabel> resources;
    /**
     * This list stores the references of the label that have been dropped onto this panel in order to be able to properly delete them
     * when we reset the state of the panel
     */
    private List<JLabel> droppedRes;


    class Pair<integer, resourceType> {
        private integer shelf;
        private resourceType res;

        public Pair(integer shelf, resourceType res) {
            this.shelf = shelf;
            this.res = res;
        }

        public integer getKey() {
            return shelf;
        }

        public resourceType getValue() {
            return res;
        }

        public void setKey(integer shelf) {
            this.shelf = shelf;
        }

        public void setValue(resourceType res) {
            this.res = res;
        }
    }

    public ShelfDrop(int shelfNumber){
        super();
        this.shelfNumber = shelfNumber;
        this.resToDepot = new ArrayList<>();
        this.resources = new ArrayList<>();
        this.droppedRes = new ArrayList<>();

        this.setBorder(new TitledBorder("Drop Resources onto shelf number " + this.shelfNumber));
        TransferHandler dnd = new TransferHandler() {
            @Override
            public boolean canImport(TransferSupport support) {
                if (!support.isDrop()) {
                    return false;
                }
                //only Strings
                if (!support.isDataFlavorSupported(DataFlavor.imageFlavor)) {
                    return false;
                }
                return true;
            }

            @Override
            public boolean importData(TransferSupport support) {
                if (!canImport(support)) {
                    return false;
                }

                Transferable tansferable = support.getTransferable();
                Icon ico;
                try {
                    ico = (Icon) tansferable.getTransferData(DataFlavor.imageFlavor);
                } catch (Exception e) {
                    e.printStackTrace();
                    return false;
                }
                ShelfDrop.this.add(new JLabel(ico));
                return true;
            }
        };

        this.setTransferHandler(dnd);

    }

    @Override
    public Dimension getPreferredSize() {
        return new Dimension(300, 300);
    }

    @Override
    public void addDecision(Integer shelf, ResourceType res){
        Pair<Integer, ResourceType> decision = new Pair<>(shelf, res);
        this.resToDepot.add(decision);
    }

    @Override
    public int getShelfNumber() {
        return shelfNumber;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(new ImageIcon(getDepotFileName(shelfNumber)).getImage(), 100, 100, null);
    }

    public static String getDepotFileName(int shelf){
        switch (shelf){
            case 1:
                return "src/main/resources/shelf 1.png";
            case 2:
                return "src/main/resources/shelf 2.png";
            case 3:
                return "src/main/resources/shelf 3.png";
        }
        return "";
    }

    public List<Pair<Integer, ResourceType>> getResToDepot() {
        return resToDepot;
    }


    public void resetState(){
        this.resToDepot = new ArrayList<>();
        for(JLabel label : this.resources)
            this.remove(label);
        for(JLabel label : this.droppedRes)
            this.remove(label);
        this.revalidate();
        this.repaint();
        this.resources = new ArrayList<>();
        this.droppedRes = new ArrayList<>();
    }

    @Override
    public void addDroppedLabel(JLabel label) {
        this.droppedRes.add(label);
    }


    public void fillShelf(DepotShelf depotShelf) {
        JLabel resource;
        for(int i = 0; i < depotShelf.getQuantity(); i++){
            resource = new JLabel(new ImageIcon(DepotDrop.getImagePathFromResource(depotShelf.getResourceType())));
            this.add(resource);
            this.resources.add(resource);
        }
    }
}

