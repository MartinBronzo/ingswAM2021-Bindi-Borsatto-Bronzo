package it.polimi.ingsw.view.gui.ViewComponents;

import it.polimi.ingsw.model.ResourceType;
import it.polimi.ingsw.view.gui.ViewComponents.DepotDragDrop.DepotDrop;
import it.polimi.ingsw.view.gui.ViewComponents.DepotDragDrop.MyDragGestureListener;
import it.polimi.ingsw.view.gui.ViewComponents.interfaces.DragUpdatable;
import it.polimi.ingsw.view.gui.ViewComponents.interfaces.Resettable;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.dnd.DnDConstants;
import java.awt.dnd.DragSource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LimitedResourcesDrag extends JPanel implements DragUpdatable, Resettable {
    private List<JLabel> resources;
    /**
     * Contains a copy of the original resources put into the map to be used when we have to cancel the actions the player has done so far.
     */
    private HashMap<ResourceType, Integer> originalResources;
    private MyDragGestureListener dlistener;
    private JLabel faithPointReceivedInfo;

    public LimitedResourcesDrag(){
        super();
        this.setBorder(new TitledBorder("Drag Resources from here"));
        resources = new ArrayList<>();
        this.originalResources = null;
    }

    @Deprecated
    public void init(HashMap<ResourceType, Integer> resources, MyDragGestureListener dlistiner) {
        this.dlistener = dlistiner;
        //new UnWantedDropTarget(this, this);
        this.originalResources = new HashMap<>(resources);

        for(Map.Entry<ResourceType, Integer> e : resources.entrySet())
            addResourceLabel(e.getKey(), e.getValue());
    }

    public void init(HashMap<ResourceType, Integer> resources){
        this.dlistener = new MyDragGestureListener();
        //new UnWantedDropTarget(this, this);
        this.originalResources = new HashMap<>(resources);

        this.addResourceLabels(resources);
    }

    private void addResourceLabels(HashMap<ResourceType, Integer> resources){
        int faithCount = 0;
        String info = "You have ";
        for(Map.Entry<ResourceType, Integer> e : resources.entrySet())
            if(!e.getKey().equals(ResourceType.FAITHPOINT))
                addResourceLabel(e.getKey(), e.getValue());
            else{
                faithCount++;
                if(this.faithPointReceivedInfo == null){
                    this.faithPointReceivedInfo = new JLabel();
                    this.add(faithPointReceivedInfo);
                }
                if(faithCount == 1)
                    this.faithPointReceivedInfo.setText(info + faithCount + " FAITHPOINT!");
                else
                    this.faithPointReceivedInfo.setText(info + faithCount + " FAITHPOINTS!");


            }


    }

    public void addResourceLabel(ResourceType type, int quantity){
        ImageIcon resource;
        JLabel label;
        DragSource ds;
        for(int i = 0; i < quantity; i++){
            resource = new ImageIcon(DepotDrop.getImagePathFromResource(type));
            resource.setDescription("limitedRes " + type);
            label = new JLabel(resource);
            this.resources.add(label);
            ds = new DragSource();
            ds.createDefaultDragGestureRecognizer(label, DnDConstants.ACTION_MOVE, dlistener);
            //label.setTransferHandler(new TransferHandler("label"));
            this.add(label);
        }
    }

    /*public void setDlistener(DepotDrag depot) {
        this.dlistener = new DragGestureListenerOneShot(depot);
    }

    public void undoDrag(String type){
        String rType = type.split(" ")[1];
        ResourceType resType = ResourceType.valueOf(rType);
        this.resources.stream().filter(x -> ((ImageIcon )x.getIcon()).getDescription().split(" ")[1].equals(rType)).filter(x -> !x.isVisible()).findAny().get().setVisible(true);
    }

    //@Override
    public void updateAfterDragBegin(String info) {
        return;
    }*/

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
        this.addResourceLabels(originalResources);
        this.revalidate();
        this.repaint();
    }
}
