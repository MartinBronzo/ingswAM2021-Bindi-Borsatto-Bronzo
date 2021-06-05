package it.polimi.ingsw.view.gui.panels;

import it.polimi.ingsw.model.LeaderCard.LeaderCard;
import it.polimi.ingsw.view.gui.ViewComponents.LeaderCardPanel;
import it.polimi.ingsw.view.gui.ViewComponents.devGrid.DevGridPanel;
import it.polimi.ingsw.view.readOnlyModel.Board;
import it.polimi.ingsw.view.readOnlyModel.Game;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class ActualPlayerBoardPanel extends JPanel {
    DevGridPanel devGrid;
    LeaderCardPanel leaderCardPanel;

    public ActualPlayerBoardPanel(Board mainBoard, ArrayList<LeaderCard> activeLeaders, ArrayList<LeaderCard> unusedLeaders) {
        super();

        final int heightMargin = 300;
        ImageIcon image;

        this.setLayout(new BoxLayout(this, BoxLayout.LINE_AXIS));

        leaderCardPanel = new LeaderCardPanel(activeLeaders, unusedLeaders);
        leaderCardPanel.setAlignmentX(LEFT_ALIGNMENT);
        this.add(leaderCardPanel);

        JLabel playerBoardLabel = new JLabel();
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        //playerBoardLabel.setSize(screenSize.width / 2, screenSize.height - heightMargin);
        image = scaleImage("src/main/resources/board/Masters of Renaissance_PlayerBoard (11_2020)-1.png", screenSize.width / 2, screenSize.height - 300);
        playerBoardLabel.setIcon(image);
        playerBoardLabel.setBorder(new TitledBorder("playerboard"));
        this.add(playerBoardLabel);

       /* JPanel righSidePanel = new JPanel();
        righSidePanel.setLayout(new BoxLayout(righSidePanel, BoxLayout.PAGE_AXIS));

        JButton marketButton = new JButton();
        //marketLabel.setSize(screenSize.width/3, screenSize.height - heightMargin*2);
        image = scaleImage("src/main/resources//PUNCHBOARD/plancia portabiglie.png", screenSize.width / 4, screenSize.height - 600);
        marketButton.setIcon(image);
        marketButton.setAlignmentX(LEFT_ALIGNMENT);
        righSidePanel.add(marketButton);

        JPanel devGridPanel = new JPanel();
        //devGridPanel.setSize(screenSize.width / 4, screenSize.height - 600);
        devGrid = new DevGridPanel(mainBoard);
        devGridPanel.add(devGrid);
        devGridPanel.setBorder(new TitledBorder("devGrid"));
        devGridPanel.setAlignmentX(LEFT_ALIGNMENT);

        righSidePanel.add(devGridPanel);
        righSidePanel.setBorder(new TitledBorder("rightside"));
        righSidePanel.setAlignmentX(RIGHT_ALIGNMENT);
        this.add(righSidePanel);*/
    }

    public void updateGridView(int width, int height) {
        devGrid.update(width, height);
    }

    public void updatePlayerBoard(Game game){
        //for()
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
