package it.polimi.ingsw.view.GUI.ViewComponents.market;

import it.polimi.ingsw.exceptions.NegativeQuantityException;
import it.polimi.ingsw.model.DevCards.DevCard;
import it.polimi.ingsw.model.DevCards.DevGrid;
import it.polimi.ingsw.model.Market.Market;
import it.polimi.ingsw.model.marble.MarbleType;
import it.polimi.ingsw.view.cli.CliView;
import it.polimi.ingsw.view.gui.ViewComponents.devGrid.DevGridPanel;
import it.polimi.ingsw.view.gui.ViewComponents.market.MarketGridPanel;
import it.polimi.ingsw.view.readOnlyModel.Board;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.xml.sax.SAXException;

import javax.swing.*;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;

class MarketGridPanelTest {
    Board board;
    Market market;
    File xmlMarketConfig;

    @BeforeEach
     void Setup() throws IOException, NegativeQuantityException, ParserConfigurationException, SAXException {
        xmlMarketConfig = new File("MarketConfig.xsd.xml");
        market = new Market(xmlMarketConfig);
        MarbleType[][] marbleMatrix = market.getMarketMatrixWithMarbleType();
        MarbleType marbleOnSlide = market.getMarbleOnSlideWithMarbleType();
        this.board = new Board();
        this.board.setMarketMatrix(marbleMatrix);
        this.board.setMarbleOnSlide(marbleOnSlide);
    }

    @Test
    void PrintWaitingRoom() throws IOException, InterruptedException {
        JFrame jFrame = new JFrame();
        MarketGridPanel marketGridGui = new MarketGridPanel(board);
        jFrame.add(marketGridGui);
        jFrame.setSize(700, 700);
        jFrame.setVisible(true);
        marketGridGui.setVisible(true);
        CliView.printMarket(board);
        marketGridGui.update();
        Thread.sleep(3000);
        /*
        DevCard[][] devCards = this.board.getDevMatrix();
        DevCard devCard;
        devCard = devCards[0][0];
        devCards[0][0] = devCards[2][2];
        devCards[2][2] = devCard;
        devGridGui.update();

         */
        System.in.read();

    }

}