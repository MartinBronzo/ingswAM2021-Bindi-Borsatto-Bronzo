package it.polimi.ingsw.view.GUI.ViewComponents.devGrid;

import it.polimi.ingsw.exceptions.NegativeQuantityException;
import it.polimi.ingsw.model.DevCards.DevCard;
import it.polimi.ingsw.model.DevCards.DevDeck;
import it.polimi.ingsw.model.DevCards.DevGrid;
import it.polimi.ingsw.view.gui.ViewComponents.devGrid.DevGridContainer;
import it.polimi.ingsw.view.gui.ViewComponents.devGrid.DevGridPanel;
import it.polimi.ingsw.view.readOnlyModel.Board;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.xml.sax.SAXException;

import javax.swing.*;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;

class DevGridPanelTest {
    Board board;
    DevGrid devGrid;
    File xmlDevCardsConfig;

    @BeforeEach
     void Setup() throws IOException, NegativeQuantityException, ParserConfigurationException, SAXException {
        xmlDevCardsConfig = new File("DevCardConfig.xsd.xml");
        devGrid = new DevGrid(xmlDevCardsConfig);
        DevCard[][] devMatrix = devGrid.getDevMatrix();
        this.board = new Board();
        this.board.setDevMatrix(devMatrix);
    }

    @Test
    void PrintWaitingRoom() throws IOException, InterruptedException {
        JFrame jFrame = new JFrame();
        DevGridContainer devGridGui = new DevGridContainer(board);
        jFrame.add(devGridGui);
        jFrame.setSize(700, 700);
        jFrame.setVisible(true);
        devGridGui.setVisible(true);
        devGridGui.update();
        Thread.sleep(3000);
        DevCard[][] devCards = this.board.getDevMatrix();
        DevCard devCard;
        devCard = devCards[0][0];
        devCards[0][0] = devCards[2][2];
        devCards[2][2] = devCard;
        devGridGui.update();
        //System.in.read();

    }

}