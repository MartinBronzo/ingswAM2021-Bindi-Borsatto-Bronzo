package it.polimi.ingsw.view.gui.ViewComponents.devCards;

import it.polimi.ingsw.model.devCards.DevCard;
import it.polimi.ingsw.model.devCards.DevSlot;


import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * This panel represents a DevSlot of the player that can only be shown (that is, it cannot be checked).
 */
public class DevSlotOnlyView extends JPanel {
    private List<JLabel> labelList;
    private final int spaceBtwCards = 40;
    private final int width = 200;
    private final int height = 400;

    /**
     * Constructs a DevSlotOnlyView representing the specified DevSlot
     * @param devSlot the DevSlot whose visual presentation is going to be displayed onto this panel
     */
    public DevSlotOnlyView(DevSlot devSlot) {
        String path = "src/main/resources/Masters of Renaissance_Cards_FRONT/";
        labelList = new ArrayList<>();

        DevCard devCard;

        //this.setPreferredSize(new Dimension( width, height));

        JLayeredPane layeredPane = new JLayeredPane();
        layeredPane.setPreferredSize(new Dimension(width, height));

        JLabel background = new JLabel();
        background.setIcon(scaleImage(getClass().getResource("/board/devSlot.png"), width, height));
        background.setBounds(0, 0, width, height);
        layeredPane.add(background, new Integer(-1));

        for (int i = 0; i < 3; i++) { //for number of devcard in a devslot
            devCard = devSlot.getDevCard(i + 1);
            if (devCard != null) {
                JLabel cardLabel = new JLabel();
                cardLabel.setIcon(scaleImage(getClass().getResource(path + devCard.getUrl()), width - 20, 230));
                //cardLabel.setPreferredSize(new Dimension(200,200));
                cardLabel.setBounds(10, (3 - i) * spaceBtwCards, width - 20, 230);
                layeredPane.add(cardLabel, new Integer(i));
            }
        }
        this.add(layeredPane);
    }

    private ImageIcon scaleImage(URL image, int width, int height) {
        //scale image
        BufferedImage img = null;
        try {
            img = ImageIO.read(image);
        } catch (IOException e) {
            e.printStackTrace();
        }
        Image dimg = img.getScaledInstance(width, height, Image.SCALE_SMOOTH);
        return new ImageIcon(dimg);
        //ImageIcon icon = new ImageIcon(leader);
    }

}
