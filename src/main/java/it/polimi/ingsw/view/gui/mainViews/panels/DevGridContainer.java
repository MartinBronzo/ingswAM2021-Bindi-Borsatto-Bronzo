package it.polimi.ingsw.view.gui.mainViews.panels;

import it.polimi.ingsw.view.gui.ViewComponents.buttons.BackButton;
import it.polimi.ingsw.view.gui.ViewComponents.devCards.DevGridPanel;
import it.polimi.ingsw.view.gui.mainViews.PanelManager;
import it.polimi.ingsw.view.lightModel.Board;

import javax.swing.*;
import java.awt.*;

/**
 * This panel shows the DevGrid to the player. By clicking on the DevCards, the player manifests their interest in buying the
 * clicked DevCard.
 */
public class DevGridContainer extends JPanel {
    DevGridPanel devGridPanel;

    /**
     * Constructs a DevGridContainer panel
     * @param board the LightModel object which contains the information on the game DevGrid
     */
    public DevGridContainer(Board board){
        devGridPanel = new DevGridPanel(board, this);
        this.setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
        this.setBorder(BorderFactory.createEmptyBorder(0, 0, 30, 0));

        this.add(devGridPanel);
        BackButton backButton = new BackButton("Back");
        backButton.addActionListener(event -> PanelManager.getInstance().showPlayerBoard(this));
        backButton.setSize(new Dimension(50,20));
        this.add(backButton);
    }

    /**
     * Updates the DevGrid view
     */
    public void update(){
        devGridPanel.update();
    }

    /**
     * Updates the DevGrid view
     * @param width the width the DevCards must have
     * @param height the height the DevCards must have
     */
    public void update(int width, int height){
        devGridPanel.update(width, height);
    }
}
