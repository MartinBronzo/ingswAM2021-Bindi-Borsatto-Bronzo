package it.polimi.ingsw;

import it.polimi.ingsw.exceptions.IllegalParameterException;
import org.junit.jupiter.api.Test;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

class MarketFactoryTest {
    MarketFactory marketFactory = new MarketFactory();
    File xmlMarketConfig = new File("MarketConfig.xsd.xml");

    @Test
    void getMarket() throws IllegalParameterException, ParserConfigurationException, SAXException, IOException {
        Market market = marketFactory.getMarket(xmlMarketConfig);
        System.out.println(market.toString());
    }
}