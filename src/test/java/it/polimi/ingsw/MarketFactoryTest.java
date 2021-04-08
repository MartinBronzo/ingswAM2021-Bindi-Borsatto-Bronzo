package it.polimi.ingsw;

import it.polimi.ingsw.Market.Market;
import it.polimi.ingsw.Market.MarketFactory;
import org.junit.jupiter.api.Test;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;


class MarketFactoryTest {
    MarketFactory marketFactory = new MarketFactory();
    File xmlMarketConfig = new File("MarketConfigOld.xsd.xml");

    /*This Test is Used to see if the xmlFile is correctly Read
    * To se the correct implementation of Market see MarketTest
    * */
    @Test
    void getMarket() throws IllegalArgumentException, ParserConfigurationException, SAXException, IOException {
        Market market = marketFactory.getMarket(xmlMarketConfig);
        System.out.println(market.toString());
    }
}