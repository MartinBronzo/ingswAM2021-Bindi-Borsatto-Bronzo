package it.polimi.ingsw.view.GUI.ViewComponents.market;

import it.polimi.ingsw.exceptions.NegativeQuantityException;
import it.polimi.ingsw.model.market.Market;
import it.polimi.ingsw.model.marble.MarbleType;
import it.polimi.ingsw.view.lightModel.Board;
import org.junit.jupiter.api.BeforeEach;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;

class MarketGridPanelTest {
    Board board;
    Market market;
    InputStream xmlMarketConfig;

    @BeforeEach
     void Setup() throws IOException, NegativeQuantityException, ParserConfigurationException, SAXException {
        xmlMarketConfig = getClass().getResourceAsStream("/XMLs/MarketConfig.xsd.xml");
        market = new Market(xmlMarketConfig);
        MarbleType[][] marbleMatrix = market.getMarketMatrixWithMarbleType();
        MarbleType marbleOnSlide = market.getMarbleOnSlideWithMarbleType();
        this.board = new Board();
        this.board.setMarketMatrix(marbleMatrix);
        this.board.setMarbleOnSlide(marbleOnSlide);
    }

  /*  @Test
    void PrintWaitingRoom() throws IOException, InterruptedException {
        JFrame jFrame = new JFrame();
        MarketGridPanel marketGridGui = new MarketGridPanel(board);
        jFrame.add(marketGridGui);
        jFrame.setSize(700, 700);
        jFrame.setVisible(true);
        marketGridGui.setVisible(true);
        CliView.printMarket(board);
        marketGridGui.update();
        Thread.sleep(5000);

        List<Effect> effects = new LinkedList<>();
        for (int i =0; i<market.getNumberOfWhiteMarbleInTheColumn(0); i++) effects.add(new Effect());
        market.moveColumn(0, effects);
        MarbleType[][] marbleMatrix = market.getMarketMatrixWithMarbleType();
        MarbleType marbleOnSlide = market.getMarbleOnSlideWithMarbleType();
        this.board.setMarketMatrix(marbleMatrix);
        this.board.setMarbleOnSlide(marbleOnSlide);
        marketGridGui.update();
        Thread.sleep(3000);

        effects = new LinkedList<>();
        for (int i =0; i<market.getNumberOfWhiteMarbleInTheRow(1); i++) effects.add(new Effect());
        market.moveRow(1, effects);
        marbleMatrix = market.getMarketMatrixWithMarbleType();
        marbleOnSlide = market.getMarbleOnSlideWithMarbleType();
        this.board.setMarketMatrix(marbleMatrix);
        this.board.setMarbleOnSlide(marbleOnSlide);
        marketGridGui.update();

       // System.in.read();

    }
*/
}