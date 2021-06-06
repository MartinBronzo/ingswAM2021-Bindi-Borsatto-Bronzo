package it.polimi.ingsw.view.gui.ViewComponents;

import it.polimi.ingsw.controller.Command;
import it.polimi.ingsw.model.LeaderCard.LeaderCard;
import it.polimi.ingsw.network.messages.fromClient.LeaderMessage;
import it.polimi.ingsw.view.gui.panels.PanelManager;
import it.polimi.ingsw.view.readOnlyModel.Player;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.security.cert.PolicyNode;
import java.util.ArrayList;

public class LeaderCardPanel extends JPanel {
    private ArrayList<LeaderCard> activeLeaders;
    private ArrayList<LeaderCard> unusedLeaders;

    public LeaderCardPanel(ArrayList<LeaderCard> activeLeaders, ArrayList<LeaderCard> unusedLeaders) {
        super();
        this.activeLeaders = activeLeaders;
        this.unusedLeaders = unusedLeaders;

        this.setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
        this.setAlignmentX(LEFT_ALIGNMENT);
        this.setBorder(new TitledBorder("LeaderCards"));

        //ArrayList<JPanel> panelList = new ArrayList<>();
       // ImageIcon image;
        addLeaderCards();
        /*for (LeaderCard leader : activeLeaders) {
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
            panelList.add(cardPanel);
            this.add(cardPanel);

            index++;
        }*/
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
