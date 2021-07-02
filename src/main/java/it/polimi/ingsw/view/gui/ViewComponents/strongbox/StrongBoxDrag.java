package it.polimi.ingsw.view.gui.ViewComponents.strongbox;

import it.polimi.ingsw.model.resources.ResourceType;
import it.polimi.ingsw.view.gui.ViewComponents.depot.depotDrop.DepotDrop;
import it.polimi.ingsw.view.gui.ViewComponents.utils.MyDragGestureListener;
import it.polimi.ingsw.view.gui.ViewComponents.interfaces.DragUpdatable;
import it.polimi.ingsw.view.gui.ViewComponents.interfaces.Resettable;
import it.polimi.ingsw.view.gui.mainViews.PanelManager;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.dnd.DnDConstants;
import java.awt.dnd.DragSource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * This panel represents the player's StrongBox where the already stored resources can be dragged away.
 */
public class StrongBoxDrag extends JPanel implements DragUpdatable, Resettable {
    private List<JLabel> resources;
    //DragGestureListenerOneShot dlistener;
    private MyDragGestureListener dlistener;
    private HashMap<ResourceType, Integer> storedHere;

    /**
     * Constructs an empty StrongBoxDrag which must be initiated via the init method after construction.
     */
    public StrongBoxDrag(){
        super();
        this.setBorder(new TitledBorder("Drag Resources from this strongbox"));
        this.resources = new ArrayList<>();
        this.storedHere = new HashMap<>();
    }

    @Deprecated
    public void init(HashMap<ResourceType, Integer> res, MyDragGestureListener dlistener){
        this.dlistener = dlistener;
        this.fillStrongBox(res);
    }

    /**
     * Initiates this StrongBoxDrag by filling it with the resources the player has stored in their StrongBox
     */
    public void init(){
        this.dlistener = new MyDragGestureListener();
        this.fillStrongBox(PanelManager.getInstance().getStrongBox());
    }

    private void fillStrongBox(HashMap<ResourceType, Integer> res){
        for(Map.Entry<ResourceType, Integer> e: res.entrySet())
            this.addOneKindRes(e.getKey(), e.getValue());
    }

    private void addOneKindRes(ResourceType type, int quantity){
        ImageIcon resource;
        JLabel label;
        DragSource ds;
        /*for(int i = 0; i < quantity; i++){
            resource = new ImageIcon(DepotDrop.getImagePathFromResource(type));
            resource.setDescription("strongbox " + type);
            label = new JLabel(resource);
            this.resources.add(label);
            ds = new DragSource();
            ds.createDefaultDragGestureRecognizer(label, DnDConstants.ACTION_MOVE, dlistener);
            label.setTransferHandler(new TransferHandler("label"));
            this.add(label);
        }*/
        resource = new ImageIcon(getClass().getResource(DepotDrop.getImagePathFromResource(type)));
        resource.setDescription("strongbox " + type);
        label = new JLabel(resource);
        label.setText(String.valueOf(quantity));
        label.setBounds(10,10,45,45);
        label.setFont(new Font("Serif", Font.BOLD, 30));
        label.setForeground(Color.BLUE);
        this.resources.add(label);
        ds = new DragSource();
        ds.createDefaultDragGestureRecognizer(label, DnDConstants.ACTION_MOVE, dlistener);
        label.setTransferHandler(new TransferHandler("label"));
        this.add(label);
        this.storedHere.put(type, quantity);
    }

    @Override
    public void resetState(){
        for(JLabel label: this.resources)
            this.remove(label);
        this.resources = new ArrayList<>();
        this.storedHere = new HashMap<>();
        this.fillStrongBox(PanelManager.getInstance().getStrongBox());
        this.revalidate();
        this.repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(new ImageIcon("src/main/resources/strongbox medium.png").getImage(), 100, 100, null);
    }

    @Override
    public void updateAfterDrop(String info) {
        /*JLabel draggedAway = this.resources.stream().filter(x -> ((ImageIcon)x.getIcon()).getDescription().split(" ")[1].equals(info) && x.isVisible()).findAny().get();
        draggedAway.setVisible(false);*/
        this.storedHere.put(ResourceType.valueOf(info), this.storedHere.get(ResourceType.valueOf(info)) - 1);
        if(this.storedHere.get(ResourceType.valueOf(info)) == 0){
            this.resources.stream().filter(x -> ((ImageIcon)x.getIcon()).getDescription().split(" ")[1].equals(info) && x.isVisible()).findAny().get().setVisible(false);
        }else
            this.resources.stream().filter(x -> ((ImageIcon)x.getIcon()).getDescription().split(" ")[1].equals(info) && x.isVisible()).findAny().get().setText(String.valueOf(this.storedHere.get(ResourceType.valueOf(info))));
        this.revalidate();
        this.repaint();
    }
}
