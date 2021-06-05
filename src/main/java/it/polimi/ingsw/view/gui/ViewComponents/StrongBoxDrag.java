package it.polimi.ingsw.view.gui.ViewComponents;

import it.polimi.ingsw.model.ResourceType;
import it.polimi.ingsw.view.gui.ViewComponents.DepotDragDrop.DepotDrop;
import it.polimi.ingsw.view.gui.ViewComponents.DepotDragDrop.MyDragGestureListener;
import it.polimi.ingsw.view.gui.ViewComponents.DepotDragDrop.ShelfDrop;
import it.polimi.ingsw.view.gui.panels.PanelManager;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.dnd.DnDConstants;
import java.awt.dnd.DragSource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class StrongBoxDrag extends JPanel implements DragUpdatable, Resettable{
    private List<JLabel> resources;
    //DragGestureListenerOneShot dlistener;
    MyDragGestureListener dlistener;

    public StrongBoxDrag(){
        super();
        this.setBorder(new TitledBorder("Drag Resources from this strongbox"));
        this.resources = new ArrayList<>();
    }

    @Deprecated
    public void init(HashMap<ResourceType, Integer> res, MyDragGestureListener dlistener){
        this.dlistener = dlistener;
        this.fillStrongBox(res);
    }

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
        for(int i = 0; i < quantity; i++){
            resource = new ImageIcon(DepotDrop.getImagePathFromResource(type));
            resource.setDescription("strongbox " + type);
            label = new JLabel(resource);
            this.resources.add(label);
            ds = new DragSource();
            ds.createDefaultDragGestureRecognizer(label, DnDConstants.ACTION_MOVE, dlistener);
            label.setTransferHandler(new TransferHandler("label"));
            this.add(label);
        }
    }

    @Override
    public void resetState(){
        for(JLabel label: this.resources)
            this.remove(label);
        this.resources = new ArrayList<>();
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
        JLabel draggedAway = this.resources.stream().filter(x -> ((ImageIcon)x.getIcon()).getDescription().split(" ")[1].equals(info) && x.isVisible()).findAny().get();
        draggedAway.setVisible(false);
        this.revalidate();
        this.repaint();
    }
}
