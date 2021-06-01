package it.polimi.ingsw.view.gui.ViewComponents;

import it.polimi.ingsw.view.gui.ViewComponents.DepotDragDrop.DepotDrop;
import it.polimi.ingsw.view.gui.ViewComponents.DepotDragDrop.MyDragGestureListener;
import it.polimi.ingsw.view.readOnlyModel.player.DepotShelf;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.dnd.DnDConstants;
import java.awt.dnd.DragSource;
import java.util.ArrayList;
import java.util.List;

public class ShelfDrag extends JPanel {
    private int shelfNumber;
    private List<JLabel> resources;
    private int resourceRemoved;
    DragGestureListenerOneShot dlistener;

    public ShelfDrag(int shelfNumber){
        super();
        this.shelfNumber = shelfNumber;
        this.resources = new ArrayList<>();
        this.resourceRemoved = 0;

        this.setBorder(new TitledBorder("Drag Resources from shelf number " + this.shelfNumber));
        this.dlistener = null;
    }

    public void filShelf(DepotShelf depotShelf){
        ImageIcon resource;
        JLabel label;
        DragSource ds;
        for(int i = 0; i < depotShelf.getQuantity(); i++){
            resource = new ImageIcon(DepotDrop.getImagePathFromResource(depotShelf.getResourceType()));
            resource.setDescription("" + this.shelfNumber);
            label = new JLabel(resource);
            this.resources.add(label);
            ds = new DragSource();
            ds.createDefaultDragGestureRecognizer(label, DnDConstants.ACTION_MOVE, dlistener);
            //label.setTransferHandler(new TransferHandler("label"));
            this.add(label);
        }
    }

    public void setDlistener(DepotDrag depot) {
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
     */
    public void updateResourcesRemovedCounter(){
        //TODO: anche questo fa leva sul fatto che sul depot ci possono essere solo risorse dello steso tipo, come renderlo più estendibile? Con una mappa?
        this.resourceRemoved++;
    }

    //For testing purposes:
    public void printChoices(){
        System.out.println("From shelf " + this.shelfNumber + ", " + this.resourceRemoved + " removed");
    }

}

   /* MouseListener listener = new DragMouseEvent();


    label1.addMouseListener(listener);
            label2.addMouseListener(listener);

            label1.setTransferHandler(new TransferHandler("icon"));*/
