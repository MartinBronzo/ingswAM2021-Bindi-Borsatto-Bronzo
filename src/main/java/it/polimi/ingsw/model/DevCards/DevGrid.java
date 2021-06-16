package it.polimi.ingsw.model.DevCards;

import it.polimi.ingsw.exceptions.EmptyDeckException;
import it.polimi.ingsw.exceptions.NegativeQuantityException;
import it.polimi.ingsw.model.ResourceType;
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
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.stream.Collectors;

public class DevGrid {
    private final DevDeck[][] devDecksGrid;

    @Override
    public boolean equals(Object obj) {
        if (obj == null)
            return false;
        if (obj == this)
            return true;
        if (!(obj instanceof DevGrid))
            return false;
        DevGrid tmp = (DevGrid) obj;
        for (int i = 0; i < 3; i++)
            for (int j = 0; j < 4; j++)
                if (!(this.devDecksGrid[i][j].equals(tmp.devDecksGrid[i][j]))) {
                    return false;
                }

        //If the thread arrives here, then the two grids contain the same decks
        return true;
    }

    //This method was used for testing purposes: it says whether two decks are equals without any regard for the order the cards are
    //in the deck (two decks are considered equal if they store the same cards, the order the cards are in is irrelevant)
    public boolean equalsFake(Object obj) {
        if (obj == null)
            return false;
        if (obj == this)
            return true;
        if (!(obj instanceof DevGrid))
            return false;
        DevGrid tmp = (DevGrid) obj;

        for (int i = 0; i < 3; i++)
            for (int j = 0; j < 4; j++)
                if (!(this.devDecksGrid[i][j].containsDeck(tmp.devDecksGrid[i][j]))) {
                    return false;
                }

        //If the thread arrives here, then the two grids contain the same decks
        return true;
    }

    /**
     * This method distributes the devCards described the xml file passed as parameter according to gameRules in DecCardDecks on a grid
     *
     * @param config xml File containing DevCard configurations.
     * @throws ParserConfigurationException when it is present an error configuration in xml file
     * @throws IOException                  if config File is impossible to read
     * @throws SAXException                 if an error appeared parsing xmlFile
     * @throws IllegalArgumentException     if it's present a devCard configuration bad syntax in xml config File
     * @throws NegativeQuantityException    if it's present a devCard configuration bad syntax in xml config File. Resource tag contains a negative quantity tag associated
     */
    public DevGrid(File config) throws ParserConfigurationException, IOException, SAXException, IllegalArgumentException, NegativeQuantityException {
        this.devDecksGrid = new DevDeck[3][4];
        ArrayList<DevCard> devCards = createConfigurationList(config);
        for (int i = 0; i < devDecksGrid.length; i++) {
            for (int j = 0; j < devDecksGrid[i].length; j++) {
                int level = devDecksGrid.length - i;
                int color = j;
                devDecksGrid[i][j] = new DevDeck(devCards.stream().filter(card -> level == card.getLevel() && color == card.getColour().ordinal()).collect(Collectors.toList()));
                devDecksGrid[i][j].shuffle();
            }
        }

    }

    public DevGrid() {
        this.devDecksGrid = new DevDeck[3][4];
    }

    //This method creates a DevGrid by reading the DevCard from a file as the DevGrid(File config) does but this method doesn't shuffle the cards
    //This method is used only for testing purposes: by using this method we are able to test the equals method
    public DevGrid createDevGridWithoutShuffling(File config) throws NegativeQuantityException, ParserConfigurationException, IOException, SAXException {
        DevGrid result = new DevGrid();
        ArrayList<DevCard> devCards = createConfigurationList(config);
        for (int i = 0; i < result.devDecksGrid.length; i++) {
            for (int j = 0; j < result.devDecksGrid[i].length; j++) {
                int level = result.devDecksGrid.length - i;
                int color = j;
                result.devDecksGrid[i][j] = new DevDeck(devCards.stream().filter(card -> level == card.getLevel() && color == card.getColour().ordinal()).collect(Collectors.toList()));
            }
        }
        return result;
    }

    /**
     * Constructs a copy of the specified DevGrid
     *
     * @param original the DevGrid to be cloned
     */
    public DevGrid(DevGrid original) {
        this.devDecksGrid = new DevDeck[3][4];
        for (int i = 0; i < 3; i++)
            for (int j = 0; j < 4; j++)
                this.devDecksGrid[i][j] = new DevDeck(original.devDecksGrid[i][j]);
    }


    /**
     * This method reads from the xml file passed as parameter and it creates a list of the all DevCards described in the xml file
     *
     * @param config xml File containing DevCard configurations.
     * @return The List of DevCards described in the xml File
     */
    private ArrayList<DevCard> createConfigurationList(File config) throws ParserConfigurationException, IOException, SAXException, IllegalArgumentException, NegativeQuantityException {
        ArrayList<DevCard> devCards = new ArrayList<>();
        DevCard devCard;

        int level;
        DevCardColour devCardColour;
        int victoryPoints;
        HashMap<ResourceType, Integer> productionInput;
        HashMap<ResourceType, Integer> productionOutput;
        HashMap<ResourceType, Integer> cost;
        String url;

        DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder docBuilder = documentBuilderFactory.newDocumentBuilder();
        Document doc = docBuilder.parse(config);
        doc.getDocumentElement().normalize();

        Node devCardConfigNode = doc.getElementsByTagName("DevCardConfig").item(0);
        if (devCardConfigNode.getNodeType() == Node.ELEMENT_NODE) {
            Element rootElement = (Element) devCardConfigNode;
            NodeList nodeDevCards = rootElement.getElementsByTagName("DevCard");
            for (int i = 0; i < nodeDevCards.getLength(); i++) {
                Node node = nodeDevCards.item(i);

                if (node.getNodeType() == Node.ELEMENT_NODE) {
                    Element baseElement = (Element) node;
                    level = Integer.parseInt(baseElement.getElementsByTagName("Level").item(0).getTextContent());
                    devCardColour = DevCardColour.valueOf(baseElement.getElementsByTagName("Colour").item(0).getTextContent());
                    victoryPoints = Integer.parseInt(baseElement.getElementsByTagName("VictoryPoints").item(0).getTextContent());
                    url = baseElement.getElementsByTagName("url").item(0).getTextContent();

                    productionInput = new HashMap<>();
                    Node productionInputNode = baseElement.getElementsByTagName("ProductionInput").item(0);
                    addNodeResourcesToHashMap(productionInput, productionInputNode);

                    productionOutput = new HashMap<>();
                    Node productionOutputNode = baseElement.getElementsByTagName("ProductionOutput").item(0);
                    addNodeResourcesToHashMap(productionOutput, productionOutputNode);

                    cost = new HashMap<>();
                    Node costNode = baseElement.getElementsByTagName("Cost").item(0);
                    addNodeResourcesToHashMap(cost, costNode);

                    devCard = new DevCard(level, devCardColour, victoryPoints, productionInput, productionOutput, cost, url);
                    devCards.add(devCard);
                }
            }
        }
        return devCards;
    }

    /**
     * adds resources to hashMap from the node read in the xml File
     * this method is used exclusively in createConfigurationList because each devCard is composed by 3 different hashMap
     */
    private void addNodeResourcesToHashMap(HashMap<ResourceType, Integer> hashMap, Node node) {
        Element element = (Element) node;
        NodeList resources = element.getElementsByTagName("Resource");
        for (int c = 0; c < resources.getLength(); c++) {
            Node node2 = resources.item(c);

            if (node2.getNodeType() == Node.ELEMENT_NODE) {
                Element subElement = (Element) node2;
                ResourceType resourceType = ResourceType.valueOf(subElement.getElementsByTagName("Type").item(0).getTextContent());
                Integer quantity = Integer.parseInt(subElement.getElementsByTagName("Quantity").item(0).getTextContent());
                hashMap.put(resourceType, quantity);
            }
        }
    }

    /**
     * gets DevDeck on the grid in a specific position
     *
     * @param row    is the chosen row of the grid starting from 0
     * @param column is the chosen column of the grid starting from 0
     * @return the chosen DevDeck on the grid
     * @throws IllegalArgumentException when the chosen position in the grid is not valid [0...2]x[0...3]
     * @deprecated because is highly recommended use the methods to gain access exclusively to the first card in the deck
     */
    @Deprecated
    public DevDeck getDevDeckInTheGrid(int row, int column) throws IllegalArgumentException {
        if (row < 0 || column < 0 || row >= 3 || column >= 4)
            throw new IllegalArgumentException("getDevDeckInTheGrid:Not valid position in the grid 3x4");
        return devDecksGrid[row][column];
    }


    /**
     * gets DevCard on the grid in a specific position
     *
     * @param row    is the chosen row of the grid starting from 0
     * @param column is the chosen column of the grid starting from 0
     * @return the first DevCard in the chosen DevDeck on the grid. The Card is not Removed From the deck or NULL if devDeck is Empty
     * @throws IllegalArgumentException when the chosen position in the grid is not valid [0...2]x[0...3]
     */
    public DevCard getDevCardFromDeck(int row, int column) throws IllegalArgumentException {
        if (row < 0 || column < 0 || row >= 3 || column >= 4)
            throw new IllegalArgumentException("getDevCardFromDeck:Not valid position in the grid 3x4");
        if (devDecksGrid[row][column].isEmpty()) return null;
        return devDecksGrid[row][column].getFirst();
    }

    /**
     * draws DevCard on the grid in a specific position
     *
     * @param row    is the chosen row of the grid starting from 0
     * @param column is the chosen column of the grid starting from 0
     * @return the first DevCard in the chosen DevDeck on the grid. The Card is Removed From the deck.
     * @throws IllegalArgumentException when the chosen position in the grid is not valid [0...2]x[0...3]
     * @throws EmptyDeckException       if devDeck is Empty;
     */
    public DevCard drawDevCardFromDeck(int row, int column) throws IllegalArgumentException, EmptyDeckException {
        if (row < 0 || column < 0 || row >= 3 || column >= 4)
            throw new IllegalArgumentException("drawDevCardFromDeck:Not valid position in the grid 3x4");
        if (devDecksGrid[row][column].isEmpty()) throw new EmptyDeckException("drawDevCardFromDeck: deck is empty");
        return devDecksGrid[row][column].drawFromDeck();
    }

    /**
     * gets DevCard on the grid in a specific position
     *
     * @param level  is the chosen level of the desired card
     * @param colour is the chosen colour of the desired card
     * @return the first DevCard in the chosen DevDeck on the grid. The Card is not Removed From the deck or NULL if devDeck is Empty
     * @throws IllegalArgumentException when the level is not between 1 and 3 or colour null
     */
    public DevCard getDevCardFromDeck(int level, DevCardColour colour) throws IllegalArgumentException {
        if (level <= 0 || level > 3 || colour == null)
            throw new IllegalArgumentException("getDevCardFromDeck:Not valid color or level");
        return this.getDevCardFromDeck(devDecksGrid.length - level, colour.ordinal());
    }

    /**
     * draws DevCard on the grid in a specific position
     *
     * @param level  is the chosen level of the desired card
     * @param colour is the chosen colour of the desired card
     * @return the first DevCard in the chosen DevDeck on the grid. The Card is Removed From the deck.
     * @throws IllegalArgumentException when the chosen position in the grid is not valid [0...2]x[0...3]
     * @throws EmptyDeckException       if devDeck is Empty;
     */
    public DevCard drawDevCardFromDeck(int level, DevCardColour colour) throws IllegalArgumentException, EmptyDeckException {
        if (level <= 0 || level > 3 || colour == null)
            throw new IllegalArgumentException("drawDevCardFromDeck:Not valid color or level");
        return this.drawDevCardFromDeck(devDecksGrid.length - level, colour.ordinal());
    }


    /**
     * @return a list of the uncovered DevCard in the Gris
     */
    public Collection<DevCard> getDrawableCards() {
        Collection<DevCard> devCards = new LinkedList<>();
        DevCard devCard;
        for (int i = 0; i < devDecksGrid.length; i++) {
            for (int j = 0; j < devDecksGrid[i].length; j++) {
                devCard = getDevCardFromDeck(i, j);
                if (devCard != null)
                    devCards.add(getDevCardFromDeck(i, j));
            }
        }
        return devCards;
    }

    /**
     * @return the matrix containing the uncovered cards
     */
    public DevCard[][] getDevMatrix() {
        DevCard[][] devMatrix = new DevCard[3][4];
        for (int i = 0; i < 3; i++)
            for (int j = 0; j < 4; j++)
                if(!devDecksGrid[i][j].isEmpty())
                    devMatrix[i][j] = this.devDecksGrid[i][j].getFirst();
                else
                    devMatrix[i][j] = null;

        return devMatrix;
    }

    /**
     * Returns the size of the devDeck containing devCards of the specified color and max level
     *
     * @param colour the color of the devDeck
     * @return the size of the devDeck containing devCards of the specified color and max level
     */
    public int getDevDeckSize(DevCardColour colour) {
        return devDecksGrid[0][colour.ordinal()].size();
    }

    @Override
    public String toString() {
        String devDecksGridString;
        devDecksGridString = "";
        for (int i = 0; i < devDecksGrid.length; i++) {
            for (int j = 0; j < devDecksGrid[i].length; j++) {
                devDecksGridString = devDecksGridString.concat(i + "," + j + ":\t");
                devDecksGridString = devDecksGridString.concat(devDecksGrid[i][j].toString());
                devDecksGridString = devDecksGridString.concat("\n");
            }
            devDecksGridString = devDecksGridString.concat("\n");
        }

        return "DevGrid{\n" +
                "devDeckGrid=\n" +
                devDecksGridString +
                '}';
    }
}
