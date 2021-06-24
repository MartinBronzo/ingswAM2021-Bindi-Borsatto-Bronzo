package it.polimi.ingsw.view.gui.ViewComponents.leaderCards;

import it.polimi.ingsw.model.leaderCard.LeaderCard;
import it.polimi.ingsw.view.gui.mainViews.PanelManager;
import it.polimi.ingsw.view.lightModel.Player;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class OthersPlayerLeaderCards extends JPanel {
    private ArrayList<LeaderCard> activeLeaders;
    private ArrayList<LeaderCard> unusedLeaders;

    public OthersPlayerLeaderCards(ArrayList<LeaderCard> activeLeaders, ArrayList<LeaderCard> unusedLeaders) {
        super();
        this.activeLeaders = activeLeaders;
        this.unusedLeaders = unusedLeaders;

        this.setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
        this.setAlignmentX(LEFT_ALIGNMENT);
        this.setBorder(new TitledBorder("LeaderCards"));

        addLeaderCards();
    }

    public static ImageIcon scaleImage(String image, int width, int height) {
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

    private void addLeaderCards(){
        ImageIcon image;

        for (LeaderCard leader : activeLeaders) {
            JPanel activeCardPanel = new JPanel();
            activeCardPanel.setLayout(new BoxLayout(activeCardPanel, BoxLayout.PAGE_AXIS));

            JLabel label = new JLabel();
            image = scaleImage(leader.getUrl(), 180, 250);
            label.setIcon(image);
            label.setAlignmentX(LEFT_ALIGNMENT);

            activeCardPanel.add(label);
            activeCardPanel.setAlignmentX(LEFT_ALIGNMENT);
            //panelList.add(activeCardPanel);
            this.add(activeCardPanel);
            this.add(Box.createRigidArea(new Dimension(0, 20)));
        }

        int index = 0;
        for (LeaderCard leader : unusedLeaders) {
            JPanel cardPanel = new JPanel();
            cardPanel.setLayout(new BoxLayout(cardPanel, BoxLayout.PAGE_AXIS));

            JLabel label = new JLabel();
            image = scaleImage("src/main/resources/back/Masters of Renaissance__Cards_BACK_3mmBleed-49-1.png", 180, 250);
            label.setIcon(image);
            label.setAlignmentX(LEFT_ALIGNMENT);

            cardPanel.add(label);
            cardPanel.add(Box.createRigidArea(new Dimension(0, 5)));
            cardPanel.add(Box.createRigidArea(new Dimension(0, 10)));
            cardPanel.setAlignmentX(LEFT_ALIGNMENT);
            //panelList.add(cardPanel);
            this.add(cardPanel);

            index++;
        }
    }

    public void update(){
        for (Component component: this.getComponents()) {
            this.remove(component);
        }

        Player actualPlayer = PanelManager.getInstance().getPlayer();
        this.activeLeaders = (ArrayList<LeaderCard>) actualPlayer.getUsedLeaders();
        this.unusedLeaders = (ArrayList<LeaderCard>) actualPlayer.getUnUsedLeaders();
        addLeaderCards();
        this.validate();
    }

}
