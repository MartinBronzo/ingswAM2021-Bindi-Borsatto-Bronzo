package it.polimi.ingsw.view.gui.ViewComponents.OnlyViewPanels;

import it.polimi.ingsw.model.ResourceType;
import it.polimi.ingsw.view.gui.ViewComponents.DepotDragDrop.DepotDrop;
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

public class StrongBoxOnlyView extends JPanel {
    private List<JLabel> resources;

    public StrongBoxOnlyView(){
        super();
        this.setBorder(new TitledBorder("Your StrongBox"));
        this.resources = new ArrayList<>();

        this.fillStrongBox();
    }

    private void fillStrongBox(){
        HashMap<ResourceType, Integer> resources = PanelManager.getInstance().getStrongBox();
        for(Map.Entry<ResourceType, Integer> e: resources.entrySet())
            this.addOneKindRes(e.getKey(), e.getValue());
    }

    private void addOneKindRes(ResourceType type, int quantity){
        ImageIcon resource = new ImageIcon(DepotDrop.getImagePathFromResource(type));;
        JLabel label = new JLabel(resource);
        label.setText(String.valueOf(quantity));
        label.setFont(new Font("Serif", Font.BOLD, 20));
        label.setForeground(Color.BLUE);
        this.resources.add(label);
        this.add(label);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(new ImageIcon("src/main/resources/strongbox medium.png").getImage(), 100, 100, null);
    }
}
