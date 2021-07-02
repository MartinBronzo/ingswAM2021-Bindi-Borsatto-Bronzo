package it.polimi.ingsw.view.gui.ViewComponents.devCards;

import it.polimi.ingsw.model.devCards.DevCard;
import it.polimi.ingsw.view.lightModel.Player;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * This panel represents the player's DevSlots containing the DevCards that can be checked if the player wants to use it for some reason.
 */
public class DevSlotCheckBox extends JPanel{
    private List<JCheckBox> checkBoxList;
    private static final String cardPath = "/Masters of Renaissance_Cards_FRONT/";

    /**
     * Constructs a DevSlotCheckBox
     * @param player the LightModel object containing the information on the player's DevSlots
     */
    public DevSlotCheckBox(Player player){
        this.setLayout(new BoxLayout(this, BoxLayout.LINE_AXIS));
        checkBoxList = new ArrayList<>();
        HashMap<Integer, DevCard> devCards = player.getDevSlots().getTopCards();
        DevCard devCard;

        BufferedImage img;
        Image dimg;

        JPanel verticalPanel;
        JLabel cardLabel;
        JCheckBox jCheckBox;

        for (int i = 1; i<4; i++){
            devCard = devCards.get(i);
            //add invisible checkbox to list for empty devslots
            jCheckBox = new JCheckBox();
            checkBoxList.add(jCheckBox);
            if (devCard != null){
                verticalPanel = new JPanel();
                verticalPanel.setLayout(new BoxLayout(verticalPanel, BoxLayout.PAGE_AXIS));
                cardLabel = new JLabel();

                img = null;
                try {
                    img = ImageIO.read(getClass().getResource(cardPath + devCard.getUrl()));
                    dimg = img.getScaledInstance(200, 300, Image.SCALE_SMOOTH);
                    cardLabel.setIcon(new ImageIcon(dimg));
                } catch (IOException e) {
                    e.printStackTrace();
                }

                verticalPanel.add(cardLabel);
                verticalPanel.add(Box.createRigidArea(new Dimension(5, 5)));
                verticalPanel.add(jCheckBox);
                this.add(verticalPanel);
                this.add(Box.createRigidArea(new Dimension(5, 5)));
            }
        }

    }

    /**
     * Returns a list of the indexes of the DevCards the player has selected
     * @return a list of the indexes of the DevCards the player has selected
     */
    public List<Integer> getSelectedDevSlotIndexes(){
        List<Integer> devSlotList = new ArrayList<>();

        for(int i = 0; i < checkBoxList.size(); i++){
            if(checkBoxList.get(i).isSelected())
                devSlotList.add(i);
        }

        return devSlotList;
    }

/*
    public DevSlotCheckBox(DevSlotOnlyView devSlotOnlyView) {
        this.setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
        this.devSlotOnlyView = devSlotOnlyView;
        this.add(devSlotOnlyView);

        checkBoxList = new ArrayList<>();
        JPanel checkPanel = new JPanel();
        for(int i = 0; i<3; i++){
            //adds the checkbox to the panel
            JCheckBox checkBox = new JCheckBox();
            checkBoxList.add(checkBox);
            checkPanel.add(checkBox);
            checkPanel.add(Box.createRigidArea(new Dimension(60, 0)));
        }
        this.add(checkPanel);
    }

 */
}
