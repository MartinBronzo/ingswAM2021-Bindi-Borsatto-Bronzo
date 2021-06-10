package it.polimi.ingsw.view.gui.panels;


import it.polimi.ingsw.model.DevCards.DevCard;
import it.polimi.ingsw.model.soloGame.SoloActionToken;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

/**
 * used to see lorenzo's actions
 */
public class ImageInfoDialog extends InfoDialog{

    private JLabel image;
    private static String punchBoardDir ="src/main/resources/PUNCHBOARD/";

    public ImageInfoDialog(Frame owner) {
        super(owner);
        image = new JLabel();
        this.add(image);
    }

    public void setPathImage(String path){
        BufferedImage img;
        Image dimg;
        ImageIcon imageIcon;
        try {
            img = ImageIO.read(new File(punchBoardDir+path));
            dimg = img.getScaledInstance(65, 65, Image.SCALE_SMOOTH);
            imageIcon = new ImageIcon(dimg);
            this.image.setIcon(imageIcon);
        } catch (IOException e) {
            image.setIcon(null);
        }
    }

    /**
     * @param token to be set as image
     */
    public void setToken(SoloActionToken token){
        this.setPathImage(token.getName());
        if (image.getIcon() == null)
            image.setText(token.toString());
    }
}
