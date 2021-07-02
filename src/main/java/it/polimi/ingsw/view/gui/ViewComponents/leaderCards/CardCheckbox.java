package it.polimi.ingsw.view.gui.ViewComponents.leaderCards;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * This panel contains a list of LeaderCards that can be checked.
 */
public class CardCheckbox extends JPanel {
    private final List<JCheckBox> checkBoxList;

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
            //adds the image of the leadercard to the panel
            JLabel label = new JLabel();
            //label.setPreferredSize(new Dimension(200,300));

            //scale image
            BufferedImage img = null;
            try {
                img = ImageIO.read(getClass().getResource(leader));
            } catch (IOException e) {
                e.printStackTrace();
            }
            Image dimg = img.getScaledInstance(200, 300, Image.SCALE_SMOOTH);
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

    /**
     * Creates a new <code>JPanel</code> with a double buffer
     * and a flow layout.
     * an empty CardCheckbox use reset view to see the cards
     */
    public CardCheckbox() {
        super();
        checkBoxList = new ArrayList<>(2);

        this.setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
    }

    /**
     * Resets the current view by adding the specified cards and the specified text for the checkboxes
     * @param cardList the list with the absolute paths of the images to display
     * @param checkBoxText the text to display near the checkbox
     */
    public void resetView(List<String> cardList, String checkBoxText){
        for (Component component: this.getComponents()) {
            this.remove(component);
        }

        //Panel with the images of the cards (Horizontal orientation)
        JPanel imagePanel = new JPanel();
        imagePanel.setLayout(new BoxLayout(imagePanel, BoxLayout.LINE_AXIS));

        for(String leader : cardList){
            //adds the image of the leadercard to the panel
            JLabel label = new JLabel();
            //label.setPreferredSize(new Dimension(200,300));

            //scale image
            BufferedImage img = null;
            try {
                img = ImageIO.read(getClass().getResource(leader));
            } catch (IOException e) {
                e.printStackTrace();
            }
            Image dimg = img.getScaledInstance(200, 300, Image.SCALE_SMOOTH);
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

        this.validate();
    }

    /**
     * Returns a list of indexes of the cards that have been selected
     * @return a list of indexes of the cards that have been selected
     */
    public List<Integer> getSelectedLeaderIndexes(){
        List<Integer> leaderList = new ArrayList<>();

        for(int i = 0; i < checkBoxList.size(); i++){
            if(checkBoxList.get(i).isSelected())
                leaderList.add(i);
        }

        return leaderList;
    }





}
