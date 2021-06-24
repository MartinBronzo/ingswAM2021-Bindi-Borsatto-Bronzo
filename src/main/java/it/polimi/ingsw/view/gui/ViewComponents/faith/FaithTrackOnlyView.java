package it.polimi.ingsw.view.gui.ViewComponents.faith;

import it.polimi.ingsw.model.faithTrack.PopeTile;
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
import java.util.List;

public class FaithTrackOnlyView extends JPanel {
    private List<JLabel> popeTileList;
    private Player player;

    public FaithTrackOnlyView(Player player) {
        popeTileList = new ArrayList<>();
        this.player = player;

        addFaithTrack();
    }

    private void addFaithTrack() {
        String[] popeUrlsFront = {"src/main/resources/PUNCHBOARD/pope_favor1_front.png", "src/main/resources/PUNCHBOARD/pope_favor2_front.png", "src/main/resources/PUNCHBOARD/pope_favor3_front.png"};
        String[] popeUrlsBack = {"src/main/resources/PUNCHBOARD/pope_favor1_back.png", "src/main/resources/PUNCHBOARD/pope_favor2_back.png", "src/main/resources/PUNCHBOARD/pope_favor3_back.png"};
        final int height = 70;
        final int width = 70;

        JPanel container = new JPanel();
        container.setLayout(new BoxLayout(container, BoxLayout.PAGE_AXIS));

        JPanel infoPanel = new JPanel();
        infoPanel.setLayout(new BoxLayout(infoPanel, BoxLayout.LINE_AXIS));

        JLabel position = new JLabel("Faith position: " + player.getFaithPosition());
        position.setBorder(new TitledBorder("Position"));
        infoPanel.add(position);
        infoPanel.add(Box.createRigidArea(new Dimension(10,0)));

        JLabel infoText = new JLabel("Your tiles: ");
        infoPanel.add(infoText);
        List<PopeTile> list = player.getPopeTiles();
        int i = 0;
        for (PopeTile popeTile : list) {
            JLabel popeLabel = new JLabel();
            if (popeTile.isActivated()) { //if the tile is activated
                popeLabel.setIcon(scaleImage(popeUrlsFront[i], width, height));
            } else if (!popeTile.isDiscarded()) { //if the tile is on the board but isn't activated
                popeLabel = new JLabel();
                popeLabel.setIcon(scaleImage(popeUrlsBack[i], width, height));
            }
            infoPanel.add(popeLabel);
            i++;
        }
        container.add(infoPanel);

        JLabel faithTrack = new JLabel();
        faithTrack.setIcon(scaleImage("src/main/resources/faithTrack.png", 1000, 150));
        faithTrack.setAlignmentX(CENTER_ALIGNMENT);
        container.add(faithTrack);

        this.add(container);
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

    public void update(){
        for (Component component: this.getComponents()) {
            this.remove(component);
        }

        this.player = PanelManager.getInstance().getPlayer();

        addFaithTrack();
        this.validate();
    }

}
