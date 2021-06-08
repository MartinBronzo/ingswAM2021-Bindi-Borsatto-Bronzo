package it.polimi.ingsw.view.gui.panels;

import it.polimi.ingsw.model.LeaderCard.LeaderCard;
import it.polimi.ingsw.view.gui.ViewComponents.LeaderCardPanel;
import it.polimi.ingsw.view.gui.ViewComponents.OnlyViewPanels.DepotOnlyView;
import it.polimi.ingsw.view.gui.ViewComponents.OnlyViewPanels.DevSlotOnlyView;
import it.polimi.ingsw.view.gui.ViewComponents.OnlyViewPanels.StrongBoxOnlyView;
import it.polimi.ingsw.view.gui.ViewComponents.devGrid.DevGridPanel;
import it.polimi.ingsw.view.readOnlyModel.Board;
import it.polimi.ingsw.view.readOnlyModel.Game;
import it.polimi.ingsw.view.readOnlyModel.Player;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class ActualPlayerBoardPanel extends JPanel {
    LeaderCardPanel leaderCardPanel;

    public ActualPlayerBoardPanel(Player player) {
        super();

        final int heightMargin = 300;
        ImageIcon image;
        DevSlotOnlyView devSlotOnlyView = null;

        this.setLayout(new BoxLayout(this, BoxLayout.LINE_AXIS));

        leaderCardPanel = new LeaderCardPanel((ArrayList<LeaderCard>)player.getUsedLeaders(), (ArrayList<LeaderCard>)player.getUnUsedLeaders());
        leaderCardPanel.setAlignmentX(LEFT_ALIGNMENT);
        this.add(leaderCardPanel);

        /*JLabel playerBoardLabel = new JLabel();
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        //playerBoardLabel.setSize(screenSize.width / 2, screenSize.height - heightMargin);
        image = scaleImage("src/main/resources/board/Masters of Renaissance_PlayerBoard (11_2020)-1.png", screenSize.width / 2, screenSize.height - 300);
        playerBoardLabel.setIcon(image);
        playerBoardLabel.setBorder(new TitledBorder("playerboard"));*/

        JPanel playerboardPanel = new JPanel();
        //playerboardPanel.setBorder(new TitledBorder("Playerboard"));
        playerboardPanel.setLayout(new BoxLayout(playerboardPanel, BoxLayout.PAGE_AXIS));
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        playerboardPanel.setPreferredSize(new Dimension(screenSize.width-200, screenSize.height -heightMargin));

        JLabel faithtrack = new JLabel();
        //faithtrack.setBorder(new TitledBorder("faithtrack"));
        faithtrack.setIcon(scaleImage("src/main/resources/faithTrack.png", 1000,150));
        faithtrack.setAlignmentX(LEFT_ALIGNMENT);
        playerboardPanel.add(faithtrack);

        JPanel bottomPanel = new JPanel();
        //bottomPanel.setBorder(new TitledBorder("bottomPanel"));
        bottomPanel.setLayout(new BoxLayout(bottomPanel, BoxLayout.LINE_AXIS));

        JPanel resourcePanel = new JPanel();
        //resourcePanel.setBorder(new TitledBorder("resourcePanel"));
        resourcePanel.setLayout(new BoxLayout(resourcePanel, BoxLayout.PAGE_AXIS));
        DepotOnlyView depotOnlyView = new DepotOnlyView();
        depotOnlyView.setPreferredSize(new Dimension(300,300));
        resourcePanel.add(depotOnlyView);
        resourcePanel.add(new StrongBoxOnlyView());
        resourcePanel.setAlignmentX(LEFT_ALIGNMENT);
        bottomPanel.add(resourcePanel);

        JLabel baseProdLabel = new JLabel();
        //baseProdLabel.setBorder(new TitledBorder("baseProdLabel"));
        baseProdLabel.setIcon(scaleImage("src/main/resources/baseProd.png", 110, 330));
        baseProdLabel.setAlignmentX(LEFT_ALIGNMENT);
        bottomPanel.add(baseProdLabel);

        JPanel devSlotsPanel = new JPanel();
        //devSlotsPanel.setBorder(new TitledBorder("devSlotsPanel"));
        devSlotsPanel.setLayout(new BoxLayout(devSlotsPanel, BoxLayout.LINE_AXIS));
        for(int i = 0; i < 3; i++) {
            devSlotOnlyView = new DevSlotOnlyView(player.getDevSlots().getDevSlot(i));
            devSlotOnlyView.setAlignmentX(LEFT_ALIGNMENT);
            devSlotsPanel.add(devSlotOnlyView);
        }
        devSlotsPanel.setAlignmentX(LEFT_ALIGNMENT);
        devSlotsPanel.setPreferredSize(new Dimension(devSlotOnlyView.getWidth()*3, devSlotOnlyView.getHeight()));
        bottomPanel.add(devSlotsPanel);
        playerboardPanel.add(bottomPanel);

        this.add(playerboardPanel);
    }

    public void updatePlayerBoard(Game game){
        leaderCardPanel.update();
        this.validate();
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
