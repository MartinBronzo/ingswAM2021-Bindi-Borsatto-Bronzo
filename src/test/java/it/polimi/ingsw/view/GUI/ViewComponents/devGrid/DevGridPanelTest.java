package it.polimi.ingsw.view.GUI.ViewComponents.devGrid;

import it.polimi.ingsw.model.DevCards.DevCard;
import it.polimi.ingsw.view.gui.ViewComponents.devGrid.DevGridPanel;
import it.polimi.ingsw.view.readOnlyModel.Board;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.swing.*;
import java.io.IOException;

class DevGridPanelTest {
    Board board;

    @BeforeEach
     void Setup() throws IOException {
        DevCard[][] devGrid = new DevCard[3][4];
        this.board = new Board();
        this.board.setDevMatrix(devGrid);
    }

    @Test
    void PrintWaitingRoom() throws IOException, InterruptedException {
        JFrame jFrame = new JFrame();
        DevGridPanel devGridGui = new DevGridPanel(board);
        jFrame.add(devGridGui);
        jFrame.setSize(700, 700);
        jFrame.setVisible(true);
        devGridGui.setVisible(true);
        devGridGui.update();
        System.in.read();
    }

}