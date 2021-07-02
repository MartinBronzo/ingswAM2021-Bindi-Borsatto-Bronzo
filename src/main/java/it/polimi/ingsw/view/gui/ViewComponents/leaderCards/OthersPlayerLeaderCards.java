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
import java.net.URL;
import java.util.ArrayList;

/**
 * This panel contains the image of the LeaderCards a player's opponent has (active or not used yet).
 */
public class OthersPlayerLeaderCards extends JPanel {
    private ArrayList<LeaderCard> activeLeaders;
    private ArrayList<LeaderCard> unusedLeaders;

    /**
     * Constructs a OthersPlayerLeaderCards containing the specified LeaderCards
     * @param activeLeaders the player's opponent's active cards
     * @param unusedLeaders the player's opponent's unused cards
     */
    public OthersPlayerLeaderCards(ArrayList<LeaderCard> activeLeaders, ArrayList<LeaderCard> unusedLeaders) {
        super();
        this.activeLeaders = activeLeaders;
        this.unusedLeaders = unusedLeaders;

        this.setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
        this.setAlignmentX(LEFT_ALIGNMENT);
        this.setBorder(new TitledBorder("LeaderCards"));

        addLeaderCards();
    }

    /**
     * Creates a scaled ImageIcon of the specified image
     * @param image the path to the image to be displayed in the ImageIcon
     * @param width the width the ImageIcon needs to have
     * @param height the height the ImageIcon needs to have
     * @return a properly scaled ImageIcon
     */
    public static ImageIcon scaleImage(URL image, int width, int height) {
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

    private void addLeaderCards(){
        ImageIcon image;

        for (LeaderCard leader : activeLeaders) {
            JPanel activeCardPanel = new JPanel();
            activeCardPanel.setLayout(new BoxLayout(activeCardPanel, BoxLayout.PAGE_AXIS));

            JLabel label = new JLabel();
            image = scaleImage(getClass().getResource(leader.getUrl()), 180, 250);
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
            image = scaleImage(getClass().getResource("/back/Masters of Renaissance__Cards_BACK_3mmBleed-49-1.png"), 180, 250);
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

    /**
     * Updates the panel. It is used in the case the player's opponent changed their unused LeaderCards by activating or discarding one
     */
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
