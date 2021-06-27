package it.polimi.ingsw.view.gui.ViewComponents.depot.depotDrag;

import it.polimi.ingsw.view.gui.ViewComponents.depot.depotDrop.DepotDrop;
import it.polimi.ingsw.view.gui.ViewComponents.utils.MyDragGestureListener;
import it.polimi.ingsw.view.gui.ViewComponents.depot.depotDrop.ShelfDrop;
import it.polimi.ingsw.view.gui.ViewComponents.interfaces.DragUpdatable;
import it.polimi.ingsw.view.gui.ViewComponents.interfaces.Resettable;
import it.polimi.ingsw.view.lightModel.player.DepotShelf;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.dnd.DnDConstants;
import java.awt.dnd.DragSource;
import java.util.ArrayList;
import java.util.List;

/**
 * This panel represents a shelf of the player's depot where resources can be dragged from.
 */
public class ShelfDrag extends JPanel implements DragUpdatable, Resettable {
    private int shelfNumber;
    /**
     * All the labels of the resources stored at the moment in the shelf
     */
    private List<JLabel> resources;
    //private int resourceRemoved;
    MyDragGestureListener dlistener;

    /**
     * Constructs a shelf with the specified shelf number
     * @param shelfNumber the number of the shelf
     */
    public ShelfDrag(int shelfNumber){
        super();
        this.shelfNumber = shelfNumber;
        this.resources = new ArrayList<>();
        //this.resourceRemoved = 0;

        this.setBorder(new TitledBorder("Drag Resources from shelf number " + this.shelfNumber));
        this.dlistener = null;
    }

    /**
     * Fills this shelf by getting the information from the specified DepotShelf object (a part of the LightModel)
     * @param depotShelf the DepotShelf object where are stored the information for this shelf (that is, the DepotShelf contains
     *                   information for the shelf whose number is the same as the one of this object)
     */
    public void filShelf(DepotShelf depotShelf){
        ImageIcon resource;
        JLabel label;
        DragSource ds;
        for(int i = 0; i < depotShelf.getQuantity(); i++){
            resource = new ImageIcon(DepotDrop.getImagePathFromResource(depotShelf.getResourceType()));
            resource.setDescription("depot " + depotShelf.getResourceType() + " " + this.shelfNumber );
            label = new JLabel(resource);
            this.resources.add(label);
            ds = new DragSource();
            ds.createDefaultDragGestureRecognizer(label, DnDConstants.ACTION_MOVE, dlistener);
            //label.setTransferHandler(new TransferHandler("label"));
            this.add(label);
        }
    }

    //For testing purposes:
    public void printChoices(){
        System.out.println("From shelf " + this.shelfNumber + ", " + /*this.resourceRemoved*/ 0 + " removed");
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(new ImageIcon(ShelfDrop.getDepotFileName(shelfNumber)).getImage(), 100, 100, null);
    }

    /**
     * Sets the DragGestureListener of this object by creating a new MyDragGestureListner object
     */
    public void setDlistener() {
        this.dlistener = new MyDragGestureListener();
    }

    @Override
    public void updateAfterDrop(String info) {
        JLabel draggedAway = this.resources.stream().filter(x -> ((ImageIcon)x.getIcon()).getDescription().split(" ")[1].equals(info) && x.isVisible()).findAny().get();
        draggedAway.setVisible(false);
        this.revalidate();
        this.repaint();
    }

    @Override
    public void resetState() {
        for(JLabel label : this.resources)
            this.remove(label);
        this.resources = new ArrayList<>();
        //this.resourceRemoved = 0;
    }





   /* public void setDlistener(DepotDrag depot) {
        this.dlistener = new DragGestureListenerOneShot(depot);
    }

    public void setResourceVisible(){
        JLabel label = this.resources.stream().filter(x -> x.isVisible() == false).findAny().get();
        label.setVisible(true);
        resourceRemoved--; //The user didn't finish the drag
    }

    public void resetState(){
        this.resources = new ArrayList<>();
    }

    /**
     * Increases the number of resources removed by one unit.
     *//*
    public void updateResourcesRemovedCounter(){
        //TODO: anche questo fa leva sul fatto che sul depot ci possono essere solo risorse dello steso tipo, come renderlo più estendibile? Con una mappa?
        this.resourceRemoved++;
    }*/



}

   /* MouseListener listener = new DragMouseEvent();


    label1.addMouseListener(listener);
            label2.addMouseListener(listener);

            label1.setTransferHandler(new TransferHandler("icon"));*/
