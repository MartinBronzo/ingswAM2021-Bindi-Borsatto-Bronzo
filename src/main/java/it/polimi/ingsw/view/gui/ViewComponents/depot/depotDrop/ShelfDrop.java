package it.polimi.ingsw.view.gui.ViewComponents.depot.depotDrop;

import it.polimi.ingsw.exceptions.IllegalActionException;
import it.polimi.ingsw.model.resources.ResourceType;
import it.polimi.ingsw.view.gui.ViewComponents.interfaces.DropResettable;
import it.polimi.ingsw.view.gui.ViewComponents.interfaces.Droppable;
import it.polimi.ingsw.view.lightModel.player.DepotShelf;

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
    //private List<Pair<Integer, ResourceType>> resToDepot;
    private ResourceType typeForShelf;
    private int quantityDropped;
    private List<JLabel> resources;
    /**
     * This list stores the references of the label that have been dropped onto this panel in order to be able to properly delete them
     * when we reset the state of the panel
     */
    private List<JLabel> droppedRes;
    private DepotDrop parentDepot;


    /*class Pair<integer, resourceType> {
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
    }*/

    public ShelfDrop(int shelfNumber){
        super();
        this.shelfNumber = shelfNumber;
        //this.resToDepot = new ArrayList<>();
        this.typeForShelf = null;
        this.quantityDropped = 0;
        this.resources = new ArrayList<>();
        this.droppedRes = new ArrayList<>();
        this.parentDepot = null;

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
    public void addDecision(Integer shelf, ResourceType res) throws IllegalActionException{
        //Pair<Integer, ResourceType> decision = new Pair<>(shelf, res);
        //this.resToDepot.add(decision);
        if(parentDepot != null)
            System.out.println(this.parentDepot.getStoredResources(this.shelfNumber));
        if(parentDepot != null)
            if(this.parentDepot.getStoredResources(this.shelfNumber).contains(res)) {
                System.out.println("YES");
                throw new IllegalActionException("This kind of resource is already stored in another shelf!");
            }
        if(typeForShelf == null)
            this.typeForShelf = res;
        else if(!res.equals(this.typeForShelf))
            throw new IllegalActionException("Only " + this.typeForShelf + "s can be dropped here!");

        this.quantityDropped++;
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

    /*public List<Pair<Integer, ResourceType>> getResToDepot() {
        return resToDepot;
    }*/

    public ResourceType getTypeForShelf() {
        return typeForShelf;
    }

    public int getQuantityDropped() {
        return quantityDropped;
    }

    public void resetState(){
        //this.resToDepot = new ArrayList<>();
        this.typeForShelf = null;
        this.quantityDropped = 0;
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
            ImageIcon image = new ImageIcon(DepotDrop.getImagePathFromResource(depotShelf.getResourceType()));
            image.setDescription("Shelf " + depotShelf.getResourceType() + " " + this.shelfNumber);
            resource = new JLabel(image);
            this.add(resource);
            this.resources.add(resource);
        }
        if(depotShelf.getQuantity() > 0)
            this.typeForShelf = depotShelf.getResourceType();
    }

    public void setParentDepot(DepotDrop parentDepot) {
        this.parentDepot = parentDepot;
    }
}

