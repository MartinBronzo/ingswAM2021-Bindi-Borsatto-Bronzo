package it.polimi.ingsw.view.gui.mainViews.panels;

import it.polimi.ingsw.model.leaderCard.LeaderCard;
import it.polimi.ingsw.view.gui.ViewComponents.depot.depotViews.DepotOnlyView;
import it.polimi.ingsw.view.gui.ViewComponents.devCards.DevSlotOnlyView;
import it.polimi.ingsw.view.gui.ViewComponents.faith.FaithTrackOnlyView;
import it.polimi.ingsw.view.gui.ViewComponents.strongbox.StrongBoxOnlyView;
import it.polimi.ingsw.view.gui.ViewComponents.leaderCards.OthersPlayerLeaderCards;
import it.polimi.ingsw.view.lightModel.Player;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

/**
 * This panel shows to the player the board of one of their opponents.
 */
public class OthersPlayerPlayerBoardPanel extends JPanel {
    OthersPlayerLeaderCards leaderCardPanel;
    FaithTrackOnlyView faithTrackOnlyView;
    DepotOnlyView depotOnlyView;

    /**
     * Constructs an OthersPlayerPlayerBoardPanel showing the specified player's board
     * @param player the LightModel object representing the player's opponents whose board is to be shown
     */
    public OthersPlayerPlayerBoardPanel(Player player) {
        super();

        final int heightMargin = 300;
        ImageIcon image;
        DevSlotOnlyView devSlotOnlyView = null;

        this.setLayout(new BoxLayout(this, BoxLayout.LINE_AXIS));

        leaderCardPanel = new OthersPlayerLeaderCards((ArrayList<LeaderCard>)player.getUsedLeaders(), (ArrayList<LeaderCard>)player.getUnUsedLeaders());
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
