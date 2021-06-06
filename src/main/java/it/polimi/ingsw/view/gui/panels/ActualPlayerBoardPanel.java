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
    }

    public void updateGridView(int width, int height) {
        devGrid.update(width, height);
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
