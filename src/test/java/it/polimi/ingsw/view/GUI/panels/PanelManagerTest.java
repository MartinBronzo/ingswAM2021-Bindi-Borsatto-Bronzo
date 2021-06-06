package it.polimi.ingsw.view.GUI.panels;

import it.polimi.ingsw.exceptions.NegativeQuantityException;
import it.polimi.ingsw.model.DevCards.DevCard;
import it.polimi.ingsw.model.DevCards.DevGrid;
import it.polimi.ingsw.model.LeaderCard.LeaderCard;
import it.polimi.ingsw.model.LeaderCard.LeaderCardDeck;
import it.polimi.ingsw.model.Market.Market;
import it.polimi.ingsw.model.marble.MarbleType;
import it.polimi.ingsw.view.gui.GuiClient;
import it.polimi.ingsw.view.gui.ViewComponents.devGrid.DevCardPanel1;
import it.polimi.ingsw.view.gui.ViewComponents.market.BuyFromMarketPanel;
import it.polimi.ingsw.view.gui.panels.PanelManager;
import it.polimi.ingsw.view.readOnlyModel.Board;
import it.polimi.ingsw.view.readOnlyModel.Game;
import it.polimi.ingsw.view.readOnlyModel.Player;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;
import java.util.List;

class PanelManagerTest {

    Board board;
    DevGrid devGrid;
    File xmlConfig;

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
        xmlConfig = new File("DevCardConfig.xsd.xml");
        devGrid = new DevGrid(xmlConfig);
        DevCard[][] devMatrix = devGrid.getDevMatrix();
        this.board = new Board();
        this.board.setDevMatrix(devMatrix);
        gameModel.setMainBoard(this.board);
        PanelManager.getInstance().setGameModel(gameModel);
        Player player = new Player();

        LeaderCardDeck l1 = new LeaderCardDeck(new File("LeaderCardConfig.xml"));
        List<LeaderCard> leaderCards = l1.getCopyLeaderCards().subList(0,2);
        player.setUsedLeaders(leaderCards);

        DevCardPanel1 panel =PanelManager.getInstance().getDevCardCostPanel();
        panel.setPlayer(player);
        panel.selectCell(0,2);
        panel.print();
        panel.setVisible(true);

        System.in.read();
    }

    @Test
    void PrintGetMarketCost() throws IOException, InterruptedException, NegativeQuantityException, ParserConfigurationException, SAXException {
        Game gameModel = new Game();
        xmlConfig = new File("MarketConfig.xsd.xml");
        Market market = new Market(xmlConfig);
        MarbleType[][] marbleMatrix = market.getMarketMatrixWithMarbleType();
        MarbleType marbleOnSlide = market.getMarbleOnSlideWithMarbleType();
        this.board = new Board();
        this.board.setMarketMatrix(marbleMatrix);
        this.board.setMarbleOnSlide(marbleOnSlide);
        gameModel.setMainBoard(this.board);
        PanelManager.getInstance().setGameModel(gameModel);
        Player player = new Player();

        LeaderCardDeck l1 = new LeaderCardDeck(new File("LeaderCardConfig.xml"));
        List<LeaderCard> leaderCards = l1.getCopyLeaderCards().subList(0,2);
        player.setUsedLeaders(leaderCards);

        BuyFromMarketPanel panel = PanelManager.getInstance().getBuyFromMarketPanel();
        panel.setPlayer(player);
        panel.setBoard(this.board);
        panel.print();
        panel.setVisible(true);

        System.in.read();
    }


}