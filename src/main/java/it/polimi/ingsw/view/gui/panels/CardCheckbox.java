package it.polimi.ingsw.view.gui.panels;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class CardCheckbox extends JPanel {
    List<JCheckBox> checkBoxList;

    /**
     * Prints the images of the leaderCards in a horizontal list
     *
     * @param cardList the list with the absolute paths of the images to display
     * @param checkBoxText the text to display near the checkbox
     */
    public CardCheckbox(List<String> cardList, String checkBoxText){

        checkBoxList = new ArrayList<>();

        //orientation of the outside panel
        this.setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));

        //Panel with the images of the cards (Horizontal orientation)
        JPanel imagePanel = new JPanel();
        imagePanel.setLayout(new BoxLayout(imagePanel, BoxLayout.LINE_AXIS));

        for(String leader : cardList){
            //TODO: CHECK THIS
            //adds the image of the leadercard to the panel
            JLabel label = new JLabel();
            label.setPreferredSize(new Dimension(50,50));

            //scale image
            BufferedImage img = null;
            try {
                img = ImageIO.read(new File(leader));
            } catch (IOException e) {
                e.printStackTrace();
            }
            Image dimg = img.getScaledInstance(300, 300, Image.SCALE_SMOOTH);
            ImageIcon icon = new ImageIcon(dimg);
            //ImageIcon icon = new ImageIcon(leader);
            label.setIcon(icon);

            imagePanel.add(label);

            imagePanel.add(Box.createRigidArea(new Dimension(25,0)));
        }

        JPanel checkPanel = new JPanel();
        for(String leader : cardList){
            //adds the checkbox to the panel
            JCheckBox checkBox = new JCheckBox(checkBoxText);
            checkBoxList.add(checkBox);
            checkPanel.add(checkBox);
            checkPanel.add(Box.createRigidArea(new Dimension(60,0)));
        }

        this.add(imagePanel);
        //White space between components
        this.add(Box.createRigidArea(new Dimension(0,5)));
        this.add(checkPanel);
    }

    public List<Integer> getSelectedLeaderIndexes(){
        List<Integer> leaderList = new ArrayList<>();

        for(int i = 0; i < checkBoxList.size(); i++){
            if(checkBoxList.get(i).isSelected())
                leaderList.add(i);
        }

        return leaderList;
    }



}
