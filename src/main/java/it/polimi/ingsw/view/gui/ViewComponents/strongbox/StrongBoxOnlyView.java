package it.polimi.ingsw.view.gui.ViewComponents.strongbox;

import it.polimi.ingsw.model.resources.ResourceType;
import it.polimi.ingsw.view.gui.ViewComponents.depot.depotDrop.DepotDrop;
import it.polimi.ingsw.view.gui.mainViews.PanelManager;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * This panel represents a StrongBox that can only be seen by the user (the stored resources cannot be dragged away and no
 * resource can be dropped onto it).
 */
public class StrongBoxOnlyView extends JPanel {
    private List<JLabel> resources;
    private final int width = 180;
    private final int height = 180;
    private String nickname;

    /**
     * Constructs the StrongBoxOnlyView which represents the StrongBox of the specified player
     * @param nickname a player's nickname
     */
    public StrongBoxOnlyView(String nickname){
        super();
        //this.setBorder(new TitledBorder("Your StrongBox"));
        this.resources = new ArrayList<>();
        this.nickname = nickname;
        JLayeredPane backgroundPane = new JLayeredPane();
        backgroundPane.setPreferredSize(new Dimension(width, height));

        JLabel backgroundLabel = new JLabel();
        backgroundLabel.setIcon(scaleImage(String.valueOf(getClass().getResource("/strongboxOnlyView.png")), width, height));
        backgroundLabel.setBounds(0, 0, width, height);
        backgroundPane.add(backgroundLabel, new Integer(-1));

        JLayeredPane layeredPane = new JLayeredPane();
        layeredPane.setPreferredSize(new Dimension(width, height));
        layeredPane.setBounds(0, 0, width,height);
        layeredPane.setLayout(new GridLayout(2,2, 5, 5));

        HashMap<ResourceType, Integer> resources = PanelManager.getInstance().getStrongBox(nickname);
        for(Map.Entry<ResourceType, Integer> e: resources.entrySet()){
            ImageIcon resource = new ImageIcon(getClass().getResource(DepotDrop.getImagePathFromResource(e.getKey())));
            JLabel label = new JLabel(resource);
            label.setText(String.valueOf(e.getValue()));
            label.setBounds(10,10,45,45);
            label.setFont(new Font("Serif", Font.BOLD, 30));
            label.setForeground(Color.WHITE);
            layeredPane.add(label,new Integer(0));
        }
        backgroundPane.add(layeredPane, new Integer(0));
        this.add(backgroundPane);

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
    }
}
