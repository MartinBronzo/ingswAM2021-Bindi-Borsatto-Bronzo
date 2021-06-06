package it.polimi.ingsw.view.gui.ViewComponents.devGrid;

import it.polimi.ingsw.view.gui.ViewComponents.buttons.BackButton;
import it.polimi.ingsw.view.gui.panels.PanelManager;
import it.polimi.ingsw.view.readOnlyModel.Board;

import javax.swing.*;
import java.awt.*;

public class DevGridContainer extends JPanel {
    DevGridPanel devGridPanel;

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

    public void update(){
        devGridPanel.update();
    }

    public void update(int width, int height){
        devGridPanel.update(width, height);
    }
}
