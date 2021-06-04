package it.polimi.ingsw.view.GUI.panels;

import it.polimi.ingsw.exceptions.NegativeQuantityException;
import it.polimi.ingsw.model.DevCards.DevCard;
import it.polimi.ingsw.model.DevCards.DevGrid;
import it.polimi.ingsw.model.LeaderCard.LeaderCard;
import it.polimi.ingsw.view.gui.GuiClient;
import it.polimi.ingsw.view.gui.ViewComponents.devGrid.DevCardPanel1;
import it.polimi.ingsw.view.gui.panels.PanelManager;
import it.polimi.ingsw.view.gui.panels.WaitingRoomPanel;
import it.polimi.ingsw.view.readOnlyModel.Board;
import it.polimi.ingsw.view.readOnlyModel.Game;
import it.polimi.ingsw.view.readOnlyModel.Player;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class PanelManagerTest {

    Board board;
    DevGrid devGrid;
    File xmlDevCardsConfig;

    @BeforeAll
    static void Setup() throws IOException {
        PanelManager.createInstance(new GuiClient()).init();
        PanelManager.getInstance().getEntryPanel().setVisible(false);
        PanelManager.getInstance().getWaitingRoomPanel().setVisible(false);
        PanelManager.getInstance().getDevCardCostPanel().setVisible(false);
    }

    @Test
    void PrintWaitingRoom() throws IOException, InterruptedException {
        PanelManager.getInstance().getWaitingRoomPanel().setVisible(true);
        System.in.read();
    }

    @Test
    void PrintDevCardPanel1() throws IOException, InterruptedException, NegativeQuantityException, ParserConfigurationException, SAXException {
        Game gameModel = new Game();
        xmlDevCardsConfig = new File("DevCardConfig.xsd.xml");
        devGrid = new DevGrid(xmlDevCardsConfig);
        DevCard[][] devMatrix = devGrid.getDevMatrix();
        this.board = new Board();
        this.board.setDevMatrix(devMatrix);
        gameModel.setMainBoard(this.board);
        PanelManager.getInstance().setGameModel(gameModel);
        Player player = new Player();
        List<LeaderCard> leaderCards = new LinkedList<>();
        player.setUsedLeaders(leaderCards);

        DevCardPanel1 panel =PanelManager.getInstance().getDevCardCostPanel();
        panel.setPlayer(player);
        panel.selectCell(0,0);
        panel.print();
        panel.setVisible(true);

        System.in.read();
    }

}