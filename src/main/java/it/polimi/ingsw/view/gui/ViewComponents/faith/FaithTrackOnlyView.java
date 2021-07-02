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

/**
 * This panel represents the player's FaithTrack and PopeTiles when the player is in a multi-player game. This panel is only to be shown to the player.
 */
public class FaithTrackOnlyView extends JPanel {
    private List<JLabel> popeTileList;
    private Player player;

    /**
     * Constructs a FaithTrackOnlyView for the specified player
     * @param player the player whose FaithTrack and PopeTiles are going to be shown onto this panel
     */
    public FaithTrackOnlyView(Player player) {
        popeTileList = new ArrayList<>();
        this.player = player;

        addFaithTrack();
    }

    private void addFaithTrack() {
        String[] popeUrlsFront = {"/PUNCHBOARD/pope_favor1_front.png", "/PUNCHBOARD/pope_favor2_front.png", "/PUNCHBOARD/pope_favor3_front.png"};
        String[] popeUrlsBack = {"/PUNCHBOARD/pope_favor1_back.png", "/PUNCHBOARD/pope_favor2_back.png", "/PUNCHBOARD/pope_favor3_back.png"};
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
                popeLabel.setIcon(scaleImage(String.valueOf(getClass().getResource(popeUrlsFront[i])), width, height));
            } else if (!popeTile.isDiscarded()) { //if the tile is on the board but isn't activated
                popeLabel = new JLabel();
                popeLabel.setIcon(scaleImage(String.valueOf(getClass().getResource(popeUrlsBack[i])), width, height));
            }
            infoPanel.add(popeLabel);
            i++;
        }
        container.add(infoPanel);

        JLabel faithTrack = new JLabel();
        faithTrack.setIcon(scaleImage(String.valueOf(getClass().getResource("/faithTrack.png")), 1000, 150));
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

    /**
     * Updates this panel
     */
    public void update(){
        for (Component component: this.getComponents()) {
            this.remove(component);
        }

        this.player = PanelManager.getInstance().getPlayer();

        addFaithTrack();
        this.validate();
    }

}
