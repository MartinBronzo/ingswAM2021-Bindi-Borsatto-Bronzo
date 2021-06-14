package it.polimi.ingsw.view.gui.panels;

import it.polimi.ingsw.model.LeaderCard.LeaderCard;
import it.polimi.ingsw.view.gui.ViewComponents.LeaderCardPanel;
import it.polimi.ingsw.view.gui.ViewComponents.OnlyViewPanels.DepotOnlyView;
import it.polimi.ingsw.view.gui.ViewComponents.OnlyViewPanels.DevSlotOnlyView;
import it.polimi.ingsw.view.gui.ViewComponents.OnlyViewPanels.FaithTrackOnlyView;
import it.polimi.ingsw.view.gui.ViewComponents.OnlyViewPanels.StrongBoxOnlyView;
import it.polimi.ingsw.view.readOnlyModel.Player;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class ActualPlayerBoardPanel extends JPanel {
    LeaderCardPanel leaderCardPanel;
    FaithTrackOnlyView faithTrackOnlyView;
    DepotOnlyView depotOnlyView;

    public ActualPlayerBoardPanel(Player player) {
        super();

        final int heightMargin = 300;
        DevSlotOnlyView devSlotOnlyView = null;

        this.setLayout(new BoxLayout(this, BoxLayout.LINE_AXIS));

        leaderCardPanel = new LeaderCardPanel((ArrayList<LeaderCard>)player.getUsedLeaders(), (ArrayList<LeaderCard>)player.getUnUsedLeaders());
        leaderCardPanel.setAlignmentX(LEFT_ALIGNMENT);
        this.add(leaderCardPanel);

        JPanel playerboardPanel = new JPanel();
        //playerboardPanel.setBorder(new TitledBorder("Playerboard"));
        playerboardPanel.setLayout(new BoxLayout(playerboardPanel, BoxLayout.PAGE_AXIS));
        playerboardPanel.setAlignmentX(LEFT_ALIGNMENT);
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        playerboardPanel.setPreferredSize(new Dimension(screenSize.width-200, screenSize.height -heightMargin));

        faithTrackOnlyView = new FaithTrackOnlyView(player);
        playerboardPanel.add(faithTrackOnlyView);

        JPanel bottomPanel = new JPanel();
        //bottomPanel.setBorder(new TitledBorder("bottomPanel"));
        bottomPanel.setLayout(new BoxLayout(bottomPanel, BoxLayout.LINE_AXIS));

        JPanel resourcePanel = new JPanel();
        //resourcePanel.setBorder(new TitledBorder("resourcePanel"));
        resourcePanel.setLayout(new BoxLayout(resourcePanel, BoxLayout.PAGE_AXIS));
        depotOnlyView = new DepotOnlyView(player.getNickName());
        depotOnlyView.setPreferredSize(new Dimension(300,300));
        resourcePanel.add(depotOnlyView);
        StrongBoxOnlyView strongBoxOnlyView = new StrongBoxOnlyView(player.getNickName());
        resourcePanel.add(strongBoxOnlyView);
        resourcePanel.setAlignmentX(LEFT_ALIGNMENT);
        bottomPanel.add(resourcePanel);

        JLabel baseProdLabel = new JLabel();
        //baseProdLabel.setBorder(new TitledBorder("baseProdLabel"));
        baseProdLabel.setIcon(scaleImage("src/main/resources/baseProd.png", 150, 400));
        baseProdLabel.setAlignmentX(LEFT_ALIGNMENT);
        baseProdLabel.setAlignmentY(TOP_ALIGNMENT);

        JPanel devSlotsPanel = new JPanel();
        devSlotsPanel.add(baseProdLabel);
        //devSlotsPanel.setBorder(new TitledBorder("devSlotsPanel"));
        devSlotsPanel.setLayout(new BoxLayout(devSlotsPanel, BoxLayout.LINE_AXIS));
        devSlotsPanel.setAlignmentX(LEFT_ALIGNMENT);
        for(int i = 0; i < 3; i++) {
            devSlotOnlyView = new DevSlotOnlyView(player.getDevSlots().getDevSlot(i));
            devSlotOnlyView.setAlignmentX(LEFT_ALIGNMENT);
            //devSlotOnlyView.setBorder(new TitledBorder("aaa"));
            devSlotsPanel.add(devSlotOnlyView);
        }
        devSlotsPanel.setAlignmentX(LEFT_ALIGNMENT);
        devSlotsPanel.setPreferredSize(new Dimension(devSlotOnlyView.getWidth()*3, devSlotOnlyView.getHeight()));
        bottomPanel.add(devSlotsPanel);
        playerboardPanel.add(bottomPanel);

        this.add(playerboardPanel);
    }

    /*public synchronized void updatePlayerBoard(Game game){
        leaderCardPanel.update();
        //faithTrackOnlyView.update();
        depotOnlyView.update();
        this.validate();
    }*/

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

