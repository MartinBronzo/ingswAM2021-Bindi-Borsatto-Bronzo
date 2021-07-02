package it.polimi.ingsw.view.gui.ViewComponents.leaderCards;

import it.polimi.ingsw.controller.Command;
import it.polimi.ingsw.model.leaderCard.LeaderCard;
import it.polimi.ingsw.model.leaderCard.leaderEffects.ExtraSlotLeaderEffect;
import it.polimi.ingsw.network.messages.fromClient.LeaderMessage;
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

/**
 * This panel contains the image of the LeaderCards the player has (active or not used yet).
 */
public class LeaderCardPanel extends JPanel {
    private ArrayList<LeaderCard> activeLeaders;
    private ArrayList<LeaderCard> unusedLeaders;

    /**
     * Constructs a LeaderCardPanel containing the specified LeaderCards
     * @param activeLeaders the player's active cards
     * @param unusedLeaders the player's unused cards
     */
    public LeaderCardPanel(ArrayList<LeaderCard> activeLeaders, ArrayList<LeaderCard> unusedLeaders) {
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

            if (leader.getEffect() instanceof  ExtraSlotLeaderEffect){
                LeaderCardOnlyView label = new LeaderCardOnlyView(leader, PanelManager.getInstance().getAlreadyStoredInLeaderSlot(leader.getEffect().extraSlotGetType()));
                activeCardPanel.add(label);
            }
            else {
                JLabel label = new JLabel();
                image = scaleImage(leader.getUrl(), 180, 250);
                label.setIcon(image);
                label.setAlignmentX(LEFT_ALIGNMENT);
                activeCardPanel.add(label);
            }

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
            image = scaleImage(leader.getUrl(), 180, 250);
            label.setIcon(image);
            label.setAlignmentX(LEFT_ALIGNMENT);

            JPanel buttonPanel = new JPanel();
            buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.LINE_AXIS));
            JButton activateButton = new JButton("Activate");
            int i = index;
            activateButton.addActionListener(event -> PanelManager.getInstance().writeMessage(
                    new Command("ActivateLeader", new LeaderMessage(i))));

            JButton discardButton = new JButton("Discard");
            discardButton.addActionListener(event -> PanelManager.getInstance().writeMessage(
                    new Command("discardLeader", new LeaderMessage(i))));

            buttonPanel.add(activateButton);
            buttonPanel.add(Box.createRigidArea(new Dimension(10, 0)));
            buttonPanel.add(discardButton);
            buttonPanel.setAlignmentX(LEFT_ALIGNMENT);

            cardPanel.add(label);
            cardPanel.add(Box.createRigidArea(new Dimension(0, 5)));
            cardPanel.add(buttonPanel);
            cardPanel.add(Box.createRigidArea(new Dimension(0, 10)));
            cardPanel.setAlignmentX(LEFT_ALIGNMENT);
            //panelList.add(cardPanel);
            this.add(cardPanel);

            index++;
        }
    }

    /**
     * Updates the panel. It is used in the case the player changed their unused LeaderCards by activating or discarding one
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
