package it.polimi.ingsw.view.gui.ViewComponents;

import it.polimi.ingsw.model.LeaderCard.LeaderCard;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class LeaderCardPanel extends JPanel {

    public LeaderCardPanel(ArrayList<LeaderCard> activeLeaders, ArrayList<LeaderCard> unusedLeaders){
        super();
        this.setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
        this.setAlignmentX(LEFT_ALIGNMENT);
        this.setBorder(new TitledBorder("LeaderCards"));

        ArrayList<JPanel> panelList = new ArrayList<>();
        ImageIcon image;
        for(LeaderCard leader : activeLeaders){
            JPanel activeCardPanel = new JPanel();
            activeCardPanel.setLayout(new BoxLayout(activeCardPanel, BoxLayout.PAGE_AXIS));

            JLabel label = new JLabel();
            image = scaleImage(leader.getUrl(), 180, 250);
            label.setIcon(image);
            label.setAlignmentX(LEFT_ALIGNMENT);

            activeCardPanel.add(label);
            activeCardPanel.setAlignmentX(LEFT_ALIGNMENT);
            panelList.add(activeCardPanel);
            this.add(activeCardPanel);
            this.add(Box.createRigidArea(new Dimension(0,20)));
        }

        for(LeaderCard leader : unusedLeaders){
            JPanel cardPanel = new JPanel();
            cardPanel.setLayout(new BoxLayout(cardPanel, BoxLayout.PAGE_AXIS));

            JLabel label = new JLabel();
            image = scaleImage(leader.getUrl(), 180, 250);
            label.setIcon(image);
            label.setAlignmentX(LEFT_ALIGNMENT);

            JPanel buttonPanel = new JPanel();
            buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.LINE_AXIS));
            JButton activateButton = new JButton("Activate");
            JButton discardButton = new JButton("Discard");
            buttonPanel.add(activateButton);
            buttonPanel.add(Box.createRigidArea(new Dimension(10,0)));
            buttonPanel.add(discardButton);
            buttonPanel.setAlignmentX(LEFT_ALIGNMENT);

            cardPanel.add(label);
            cardPanel.add(Box.createRigidArea(new Dimension(0,5)));
            cardPanel.add(buttonPanel);
            cardPanel.add(Box.createRigidArea(new Dimension(0,10)));
            cardPanel.setAlignmentX(LEFT_ALIGNMENT);
            panelList.add(cardPanel);
            this.add(cardPanel);
        }
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
