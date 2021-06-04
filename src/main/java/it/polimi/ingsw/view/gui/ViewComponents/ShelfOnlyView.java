package it.polimi.ingsw.view.gui.ViewComponents;

import it.polimi.ingsw.view.gui.ViewComponents.DepotDragDrop.DepotDrop;
import it.polimi.ingsw.view.gui.ViewComponents.DepotDragDrop.ShelfDrop;
import it.polimi.ingsw.view.readOnlyModel.player.DepotShelf;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.dnd.DnDConstants;
import java.awt.dnd.DragSource;
import java.util.ArrayList;
import java.util.List;

public class ShelfOnlyView extends JPanel {
    private int shelfNumber;
    private List<JLabel> resources;

    public ShelfOnlyView(int shelfNumber){
        super();
        this.shelfNumber = shelfNumber;
        this.resources = new ArrayList<>();

        this.setBorder(new TitledBorder("Shelf number " + this.shelfNumber));
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(new ImageIcon(ShelfDrop.getDepotFileName(shelfNumber)).getImage(), 100, 100, null);
    }

    public void filShelf(DepotShelf depotShelf){
        ImageIcon resource;
        JLabel label;
        for(int i = 0; i < depotShelf.getQuantity(); i++){
            resource = new ImageIcon(DepotDrop.getImagePathFromResource(depotShelf.getResourceType()));
            label = new JLabel(resource);
            this.resources.add(label);
            this.add(label);
        }
    }
}
