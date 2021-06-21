package it.polimi.ingsw.view.gui.ViewComponents;

import it.polimi.ingsw.model.DevCards.DevCard;
import it.polimi.ingsw.model.DevCards.DevSlots;
import it.polimi.ingsw.view.gui.ViewComponents.OnlyViewPanels.DevSlotOnlyView;
import it.polimi.ingsw.view.readOnlyModel.Player;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class DevSlotCheckBox extends JPanel{
    private List<JCheckBox> checkBoxList;
    private static final String cardPath = "src/main/resources/Masters of Renaissance_Cards_FRONT/";

    public DevSlotCheckBox(Player player){
        this.setLayout(new BoxLayout(this, BoxLayout.LINE_AXIS));
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
                    img = ImageIO.read(new File(cardPath+devCard.getUrl()));
                } catch (IOException e) {
                    e.printStackTrace();
                }
                dimg = img.getScaledInstance(200, 300, Image.SCALE_SMOOTH);
                cardLabel.setIcon(new ImageIcon(dimg));
                verticalPanel.add(cardLabel);
                verticalPanel.add(Box.createRigidArea(new Dimension(5, 5)));
                verticalPanel.add(jCheckBox);
                this.add(verticalPanel);
                this.add(Box.createRigidArea(new Dimension(5, 5)));
            }
        }

    }

    public List<Integer> getSelectedDevSlotIndexes(){
        List<Integer> devSlotList = new ArrayList<>();

        for(int i = 0; i < checkBoxList.size(); i++){
            if(checkBoxList.get(i).isSelected())
                devSlotList.add(i+1);
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
