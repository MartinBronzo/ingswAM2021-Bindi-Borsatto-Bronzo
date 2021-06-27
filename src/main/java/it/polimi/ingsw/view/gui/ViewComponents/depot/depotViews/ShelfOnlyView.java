package it.polimi.ingsw.view.gui.ViewComponents.depot.depotViews;

import it.polimi.ingsw.view.gui.ViewComponents.depot.depotDrop.DepotDrop;
import it.polimi.ingsw.view.gui.ViewComponents.depot.depotDrop.ShelfDrop;
import it.polimi.ingsw.view.lightModel.player.DepotShelf;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * This panel represents a shelf of a Depot that is used only to be seen by the user (the resources are not draggable and no drop can occur onto the panel).
 */
public class ShelfOnlyView extends JPanel {
    private int shelfNumber;
    private List<JLabel> resources;

    /**
     * Constructs a view of the player's shelf
     * @param shelfNumber the number of a Depot shelf
     */
    public ShelfOnlyView(int shelfNumber){
        super();
        this.shelfNumber = shelfNumber;
        this.resources = new ArrayList<>();

        //this.setBorder(new TitledBorder("Shelf number " + this.shelfNumber));
    }

    /*@Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(new ImageIcon(ShelfDrop.getDepotFileName(shelfNumber)).getImage(), 100, 100, null);
    }*/

    /*public void filShelf(DepotShelf depotShelf){
        ImageIcon resource;
        JLabel label;
        for(int i = 0; i < depotShelf.getQuantity(); i++){
            resource = new ImageIcon(DepotDrop.getImagePathFromResource(depotShelf.getResourceType()));
            label = new JLabel(resource);
            this.resources.add(label);
            this.add(label);
        }
    }*/

    /**
     * Fills this shelf with the resources the player stores onto it. The information regarding the player's resources are retrieved from the specified DepotShelf object
     * (a part of the player's LightModel)
     * @param depotShelf the DepotShelf containing information for this shelf
     */
    public void filShelf(DepotShelf depotShelf){
        final int img_width = 40;
        final int img_height = 40;
        ImageIcon resource;
        JLabel label;
        JLayeredPane layeredPane = new JLayeredPane();
        layeredPane.setPreferredSize(new Dimension(180, 80));

        JLabel background = new JLabel();
        background.setIcon(scaleImage(ShelfDrop.getDepotFileName(shelfNumber), 180, 80));
        background.setBounds(0, 0, 180, 80);
        layeredPane.add(background, new Integer(-1));

        for(int i = 0; i < depotShelf.getQuantity(); i++){
            //resource = new ImageIcon(DepotDrop.getImagePathFromResource(depotShelf.getResourceType()));
            label = new JLabel(scaleImage(DepotDrop.getImagePathFromResource(depotShelf.getResourceType()),img_width,img_height));
            label.setBounds(((4- shelfNumber)*25)+img_width*i, 30, img_width, img_height);
            this.resources.add(label);
            layeredPane.add(label);
        }
        this.add(layeredPane);
    }

    private ImageIcon scaleImage(String image, int width, int height) {
        //scale image
        BufferedImage img = null;
        try {
            img = ImageIO.read(new File(image));
        } catch (IOException e) {
            e.printStackTrace();
        }
        Image dimg = img.getScaledInstance(width, height, Image.SCALE_SMOOTH);
        return new ImageIcon(dimg);
        //ImageIcon icon = new ImageIcon(leader);
    }
}
