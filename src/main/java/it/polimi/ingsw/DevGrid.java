package it.polimi.ingsw;

import it.polimi.ingsw.exceptions.IllegalParameterException;
import it.polimi.ingsw.exceptions.NegativeQuantityException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.stream.Collectors;

public class DevGrid {
    private final DevDeck[][] devDecksGrid;


    public DevGrid(File config) throws ParserConfigurationException, IOException, SAXException, IllegalParameterException, NegativeQuantityException {
        this.devDecksGrid = new DevDeck[3][4];
        ArrayList<DevCard> devCards = createConfigurationList(config);
        for (int i=0; i<devDecksGrid.length; i++){
            for (int j=0; j<devDecksGrid[i].length; j++){
                int level = i;
                int color = j;
                devDecksGrid[i][j] = new DevDeck(devCards.stream().filter(card -> level == card.getLevel() && color == card.getColour().ordinal()).collect(Collectors.toList()));
                devDecksGrid[i][j].shuffle();
            }
        }

    }

    public DevGrid() {
        this.devDecksGrid = null;
    }

    public ArrayList<DevCard> createConfigurationList(File config) throws ParserConfigurationException, IOException, SAXException, IllegalParameterException, NegativeQuantityException {
        ArrayList<DevCard> devCards= new ArrayList<>();
        DevCard devCard;

        int level;
        DevCardColour devCardColour;
        int victoryPoints;
        HashMap<ResourceType,Integer> productionInput;
        HashMap<ResourceType,Integer> productionOutput;
        HashMap<ResourceType,Integer> cost;
        String url;

        DocumentBuilderFactory documentBuilderFactory =DocumentBuilderFactory.newInstance();
        DocumentBuilder docBuilder= documentBuilderFactory.newDocumentBuilder();
        Document doc= docBuilder.parse(config);
        doc.getDocumentElement().normalize();

        NodeList nodeDevCards = doc.getElementsByTagName("DevCard");
        for (int i=0; i< nodeDevCards.getLength(); i++){
            Node node= nodeDevCards.item(i);

            if (node.getNodeType() == Node.ELEMENT_NODE){
                Element baseElement= (Element) node;
                level=Integer.parseInt(baseElement.getElementsByTagName("Level").item(0).getTextContent());
                devCardColour = DevCardColour.valueOf(baseElement.getElementsByTagName("Colour").item(0).getTextContent());
                victoryPoints=Integer.parseInt(baseElement.getElementsByTagName("VictoryPoints").item(0).getTextContent());
                url=baseElement.getElementsByTagName("url").item(0).getTextContent();

                productionInput = new HashMap<>();
                Node productionInputNode = baseElement.getElementsByTagName("ProductionInput").item(0);
                Element productionInputElement = (Element) productionInputNode;
                NodeList productionInputResources = productionInputElement.getElementsByTagName("Resource");
                for (int c=0; c< productionInputResources.getLength(); c++) {
                    Node node2 = productionInputResources.item(c);

                    if (node2.getNodeType() == Node.ELEMENT_NODE) {
                        Element subElement = (Element) node2;
                        ResourceType resourceType = ResourceType.valueOf(subElement.getElementsByTagName("Type").item(0).getTextContent());
                        Integer quantity = Integer.parseInt(subElement.getElementsByTagName("Quantity").item(0).getTextContent());
                        productionInput.put(resourceType,quantity);
                    }
                }

                productionOutput = new HashMap<>();
                Node productionOutputNode = baseElement.getElementsByTagName("ProductionOutput").item(0);
                Element productionOutputElement = (Element) productionOutputNode;
                NodeList productionOutputResources = productionOutputElement.getElementsByTagName("Resource");
                for (int c=0; c< productionOutputResources.getLength(); c++) {
                    Node node2 = productionOutputResources.item(c);

                    if (node2.getNodeType() == Node.ELEMENT_NODE) {
                        Element subElement = (Element) node2;
                        ResourceType resourceType = ResourceType.valueOf(subElement.getElementsByTagName("Type").item(0).getTextContent());
                        Integer quantity = Integer.parseInt(subElement.getElementsByTagName("Quantity").item(0).getTextContent());
                        productionOutput.put(resourceType,quantity);
                    }
                }

                cost = new HashMap<>();
                Node costNode = baseElement.getElementsByTagName("Cost").item(0);
                Element costElement = (Element) costNode;
                NodeList costResources = costElement.getElementsByTagName("Resource");
                for (int c=0; c< costResources.getLength(); c++) {
                    Node node2 = costResources.item(c);

                    if (node2.getNodeType() == Node.ELEMENT_NODE) {
                        Element subElement = (Element) node2;
                        ResourceType resourceType = ResourceType.valueOf(subElement.getElementsByTagName("Type").item(0).getTextContent());
                        Integer quantity = Integer.parseInt(subElement.getElementsByTagName("Quantity").item(0).getTextContent());
                        cost.put(resourceType,quantity);
                    }
                }

                devCard =new DevCard(level,devCardColour,victoryPoints,productionInput,productionOutput,cost,url);
                devCards.add(devCard);
            }
        }

        return devCards;
    }
}
