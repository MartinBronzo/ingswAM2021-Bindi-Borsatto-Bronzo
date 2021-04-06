package it.polimi.ingsw;

import it.polimi.ingsw.exceptions.IllegalParameterException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;


/**Market Factory Class used to Create Farmer using parameters from xml File*/
public class MarketFactory {

    /**
     * @return Market Entity with the parameters described in the xmlFile
     * @param config is the xml File containing Market Config tag well formatted:
     * in the xmlFile WHITEMARBLE,REDMARBLE,GREYMARBLE,BLUEMARBLE,YELLOWMARBLE,PURPLEMARBLE tags must appear and the test Content must be a non negative integer
     * @throws NullPointerException when one the marble tags is missing
     * @throws ParserConfigurationException when it is present an error configuration in xml file
     * @throws IOException if config File is impossible to read
     * @throws SAXException if an error appeared parsing xmlFile
     * @throws IllegalParameterException if a negative number of a marble is used in the xmlFile
     * @throws IllegalParameterException if the sum of the marbles is not 13
     * */
    public Market getMarket(File config) throws IllegalParameterException, ParserConfigurationException, IOException, SAXException {
        int nWhite=0;
        int nRed=0;
        int nGrey=0;
        int nBlue=0;
        int nYellow=0;
        int nPurple=0;

        DocumentBuilderFactory documentBuilderFactory =DocumentBuilderFactory.newInstance();
        DocumentBuilder docBuilder= documentBuilderFactory.newDocumentBuilder();
        Document doc= docBuilder.parse(config);
        doc.getDocumentElement().normalize();

        Node marketConfig = doc.getElementsByTagName("MarketConfig").item(0);
        if (marketConfig.getNodeType() == Node.ELEMENT_NODE){
            Element baseElement= (Element) marketConfig;
            nWhite=Integer.parseInt(baseElement.getElementsByTagName("WHITEMARBLE").item(0).getTextContent());
            nRed=Integer.parseInt(baseElement.getElementsByTagName("REDMARBLE").item(0).getTextContent());
            nGrey=Integer.parseInt(baseElement.getElementsByTagName("GREYMARBLE").item(0).getTextContent());
            nBlue=Integer.parseInt(baseElement.getElementsByTagName("BLUEMARBLE").item(0).getTextContent());
            nYellow=Integer.parseInt(baseElement.getElementsByTagName("YELLOWMARBLE").item(0).getTextContent());
            nPurple=Integer.parseInt(baseElement.getElementsByTagName("PURPLEMARBLE").item(0).getTextContent());
        }
        return new Market(nWhite,nRed,nGrey,nBlue,nYellow,nPurple);
    }
}
