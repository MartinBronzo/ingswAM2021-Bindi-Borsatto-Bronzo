package it.polimi.ingsw.view.gui.mainViews.dialogs;


import it.polimi.ingsw.model.soloGame.SoloActionToken;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

/**
 * This dialog is used to see Lorenzo's actions in a Solo Game.
 */
public class ImageInfoDialog extends InfoDialog{

    private JLabel image;
    private static String punchBoardDir ="/PUNCHBOARD/";

    /**
     * Constructs an ImageInfoDialog
     * @param owner the Frame of the player
     */
    public ImageInfoDialog(Frame owner) {
        super(owner);
        image = new JLabel();
        this.add(image);
    }

    /**
     * Sets the image of the Lorenzo's token, which has been just used, to be shown
     * @param path the relative path of the used Lorenzo's token
     */
    public void setPathImage(String path){
        BufferedImage img;
        Image dimg;
        ImageIcon imageIcon;
        try {
            String string = punchBoardDir + path;
            img = ImageIO.read(getClass().getResource(string));
            dimg = img.getScaledInstance(65, 65, Image.SCALE_SMOOTH);
            imageIcon = new ImageIcon(dimg);
            this.image.setIcon(imageIcon);
        } catch (IOException e) {
            image.setIcon(null);
        }
    }

    /**
     * Sets the Lorenzo's token, which has been just used, to be shown
     * @param token the Lorenzo's token which has been just used
     */
    public void setToken(SoloActionToken token){
        this.setPathImage(token.getName());
        if (image.getIcon() == null)
            image.setText(token.toString());
    }
}
