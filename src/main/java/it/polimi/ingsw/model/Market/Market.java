package it.polimi.ingsw.model.Market;

import it.polimi.ingsw.exceptions.NegativeQuantityException;
import it.polimi.ingsw.model.FaithTrack.PopeCell;
import it.polimi.ingsw.model.LeaderCard.leaderEffects.Effect;
import it.polimi.ingsw.model.ResourceType;
import it.polimi.ingsw.model.marble.*;
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
import java.util.*;


/**
 * Implements the the game structure of the market. when buying it use the marbles to give back the resources bought in a HashMap
 */
public class Market {
    private final Marble[][] marketMatrix;
    private Marble marbleOnSlide;

    @Override
    public boolean equals(Object obj) {
        if (obj == null)
            return false;
        if (obj == this)
            return true;
        if (!(obj instanceof Market))
            return false;
        Market tmp = (Market) obj;
        if(!(this.marbleOnSlide.equals(tmp.marbleOnSlide)))
            return false;
        for(int i = 0; i < 3; i++)
            for(int j = 0; j < 4; j++)
                if(!(this.marketMatrix[i][j].equals(tmp.marketMatrix[i][j])))
                    return false;

        return true;
    }

    /**
     * Builder which instantiated the marbles on the grid and on the slide according with Classic Rules
     */
    @Deprecated
    public Market() throws IllegalArgumentException, IndexOutOfBoundsException {
        this(4, 1, 2, 2, 2, 2);
    }

    /**
     * This constructor creates a copy of the actual state of the Market
     *
     * @param market is the Market, which state is to be copied
     */
    public Market(Market market) throws NullPointerException {
        if (market == null) throw new NullPointerException("market can't be null");
        this.marbleOnSlide = market.marbleOnSlide;
        this.marketMatrix = new Marble[market.marketMatrix.length][market.marketMatrix[0].length];
        for (int i = 0; i < this.marketMatrix.length; i++) {
            for (int j = 0; j < this.marketMatrix[i].length; j++) {
                this.setMarbleInTheGrid(market.getMarbleInTheGrid(i, j), i, j);
            }
        }
    }

    /**
     * Builder for every configuration; it needs exactly 13 marbles because the slots in the market are from the grid 4x3 + 1 on the slide
     *
     * @param nBlue   refers the number of BlueMarbles to be added in the Market
     * @param nGrey   refers the number of GreyMarbles to be added in the Market
     * @param nPurple refers the number of PurpleMarbles to be added in the Market
     * @param nRed    refers the number of RedMarbles to be added in the Market
     * @param nWhite  refers the number of WhiteMarbles to be added in the Market
     * @param nYellow refers the number of YellowMarbles to be added in the Market
     * @throws IllegalArgumentException if a negative number of a marble is used to call this builder
     * @throws IllegalArgumentException if the sum of the parameters is not 13
     *                                  each parameter refers to the number of marble of the same colour
     * @deprecated Use Constructor public Market(File config), it's more extendable
     */
    @Deprecated
    public Market(int nWhite, int nRed, int nGrey, int nBlue, int nYellow, int nPurple) throws IllegalArgumentException {

        if (nWhite + nRed + nGrey + nBlue + nYellow + nPurple != 13)
            throw new IllegalArgumentException("Market: number of Marbles is not 13");
        if (nWhite < 0 || nRed < 0 || nGrey < 0 || nBlue < 0 || nYellow < 0 || nPurple < 0)
            throw new IllegalArgumentException("Market: number of Marbles can't be negative");

        this.marketMatrix = new Marble[3][4];

        List<Marble> configurationList = createConfigurationList(nWhite, nRed, nGrey, nBlue, nYellow, nPurple);

        for (int i = 0; i < marketMatrix.length; i++) {
            for (int j = 0; j < marketMatrix[i].length; j++) {
                setMarbleInTheGrid(configurationList.get(marketMatrix[i].length * i + j), i, j);
            }
        }


        this.marbleOnSlide = configurationList.get(configurationList.size() - 1);
    }

    /**
     * Creates a Market containing the marbles described in a xmlFile
     *
     * @param config is the xmlFile containing the description of tye market
     * @throws ParserConfigurationException when it is present an error configuration in xml file
     * @throws IOException                  if config File is impossible to read
     * @throws SAXException                 if an error appeared parsing xmlFile
     * @throws IllegalArgumentException     if a negative number of a marble is used in the xmlFile
     * @throws NegativeQuantityException    if the sum of the marbles is not 13
     */
    public Market(File config) throws ParserConfigurationException, IOException, SAXException, IllegalArgumentException, NegativeQuantityException {
        List<Marble> configurationList = createConfigurationList(config);
        if (configurationList.size() != 13)
            throw new IllegalArgumentException("Market: number of Marbles is " + configurationList.size());

        this.marketMatrix = new Marble[3][4];
        for (int i = 0; i < marketMatrix.length; i++) {
            for (int j = 0; j < marketMatrix[i].length; j++) {
                this.setMarbleInTheGrid(configurationList.get(marketMatrix[i].length * i + j), i, j);
            }
        }
        this.marbleOnSlide = configurationList.get(configurationList.size() - 1);

    }


    /**
     * Method used when buying from market and selecting 1 of the 3 rows.
     * Modify the references to the grid after buying action according to game rules.
     * the hashmap is instantiated in this Method, but it isn't modified directly
     *
     * @param rowNumber is the chosen row in the Market grid starting from 0.
     * @param effects   determines the bought resources when selecting whiteMarbles. The list must Have the size of the number of white marbles in the row
     * @return the resources purchased in an hashmap
     * @throws IllegalArgumentException if rowNumber is not valid [0...2] or if there aren't enough effects for all the white marbles
     * @throws NullPointerException     if effects is NULL
     */
    public HashMap<ResourceType, Integer> moveRow(int rowNumber, List<Effect> effects) throws IllegalArgumentException, NullPointerException {
        if (rowNumber < 0 || rowNumber >= 3) throw new IllegalArgumentException("moveRow Market: not valid rowNumber");
        if (effects == null) throw new NullPointerException("moveRow Market: not expected NULL effect");
        if (effects.size() != getNumberOfWhiteMarbleInTheRow(rowNumber)) throw new IllegalArgumentException("moverowRow market: the number of effects is not the number of whitemarble in the row");


        HashMap<ResourceType, Integer> resources = new HashMap<>();
        Marble tempMarble = marbleOnSlide;
        for (int j = 0; j < marketMatrix[rowNumber].length; j++) {
            try {
                marketMatrix[rowNumber][j].onActivate(resources, effects);
            } catch (NegativeQuantityException | NullPointerException e) {
                System.out.println("moveRow: Something extremely bad happened in the Market");
                return new HashMap<>();
            } catch (IndexOutOfBoundsException e) {
                throw new IllegalArgumentException("The effects size is not the numbers of white marbles in the column");
            }

            if (j == 0) marbleOnSlide = getMarbleInTheGrid(rowNumber, j);
            else setMarbleInTheGrid(marketMatrix[rowNumber][j], rowNumber, j - 1);
        }
        setMarbleInTheGrid(tempMarble, rowNumber, marketMatrix[rowNumber].length - 1);
        return resources;
    }

    /**
     * Method used when buying from market and selecting 1 of the 4 columns.
     * Modify the references to the grid after buying action according to game rules.
     * the hashmap is instantiated in this Method, but it isn't modified directly
     *
     * @param columnNumber is the chosen column in the Market grid starting from 0.
     * @param effects      determines the bought resources when selecting whiteMarbles. The list must Have the size of the number of white marbles in the column
     * @return the resources purchased in an hashmap
     * @throws IllegalArgumentException if columnNumber is not valid [0...3] or if there aren't enough effects for all the white marbles
     * @throws NullPointerException     if effect is NULL
     *                                  the hashmap is instantiated in this Method, but it isn't modified directly
     */
    public HashMap<ResourceType, Integer> moveColumn(int columnNumber, List<Effect> effects) throws IllegalArgumentException, NullPointerException {
        if (columnNumber < 0 || columnNumber >= 4)
            throw new IllegalArgumentException("moveColumn Market: not valid columnNumber");
        if (effects == null) throw new NullPointerException("moveColumn Market: not expected NULL effect");
        if (effects.size() != getNumberOfWhiteMarbleInTheColumn(columnNumber)) throw new IllegalArgumentException("moveColumn market: the number of effects is not the number of whitemarble in the column");

        HashMap<ResourceType, Integer> resources = new HashMap<>();
        Marble tempMarble = marbleOnSlide;
        for (int j = 0; j < marketMatrix.length; j++) {
            try {
                marketMatrix[j][columnNumber].onActivate(resources, effects);
            } catch (NegativeQuantityException | NullPointerException e) {
                System.out.println("moveColumn: Something extremely bad happened in the Market:");
                return new HashMap<>();
            } catch (IndexOutOfBoundsException e) {
                throw new IllegalArgumentException("The effects size is not the numbers of white marbles in the column");
            }

            if (j == 0) marbleOnSlide = getMarbleInTheGrid(j, columnNumber);
            else setMarbleInTheGrid(marketMatrix[j][columnNumber], j - 1, columnNumber);
        }
        setMarbleInTheGrid(tempMarble, marketMatrix.length - 1, columnNumber);
        return resources;
    }

    /**
     * Method used when comparing market resources and selecting 1 of the 3 rows.
     * the references in the grid remain unchanged.
     * the hashmap is instantiated in this Method, but it isn't modified directly
     *
     * @param rowNumber is the chosen row in the Market grid starting from 0.
     * @param effects   determines the bought resources when selecting whiteMarbles. The list must Have the size of the number of white marbles in the row
     * @return the resources purchased in an hashmap
     * @throws IllegalArgumentException if rowNumber is not valid [0...2] or if there aren't enough effects for all the white marbles
     * @throws NullPointerException     if effects is NULL
     */
    public HashMap<ResourceType, Integer> getResourcesFromRow(int rowNumber, List<Effect> effects) throws IllegalArgumentException, NullPointerException {
        if (rowNumber < 0 || rowNumber >= 3) throw new IllegalArgumentException("getRow Market: not valid rowNumber");
        if (effects == null) throw new NullPointerException("getRow Market: not expected NULL effect");
        if (effects.size() != getNumberOfWhiteMarbleInTheRow(rowNumber)) throw new IllegalArgumentException("getRow market: the number of effects is not the number of whitemarble in the row");

        HashMap<ResourceType, Integer> resources = new HashMap<>();

        for (int j = 0; j < marketMatrix[rowNumber].length; j++) {
            try {
                marketMatrix[rowNumber][j].onActivate(resources, effects);
            } catch (NegativeQuantityException | NullPointerException e) {
                System.out.println("moveRow: Something extremely bad happened in the Market");
                return new HashMap<>();
            } catch (IndexOutOfBoundsException e) {
                throw new IllegalArgumentException("The effects size is not the numbers of white marbles in the row");
            }
        }
        return resources;
    }

    /**
     * Method used when comparing market resources and selecting 1 of the 4 rows.
     * the references in the grid remain unchanged.
     * the hashmap is instantiated in this Method, but it isn't modified directly
     *
     * @param columnNumber is the chosen column in the Market grid starting from 0.
     * @param effects      determines the bought resources when selecting whiteMarbles. The list must Have the size of the number of white marbles in the column
     * @return the resources purchased in an hashmap
     * @throws IllegalArgumentException if columnNumber is not valid [0...3] or if there aren't enough effects for all the white marbles
     * @throws NullPointerException     if effect is NULL
     *                                  the hashmap is instantiated in this Method, but it isn't modified directly
     */
    public HashMap<ResourceType, Integer> getResourcesFromColumn(int columnNumber, List<Effect> effects) throws IllegalArgumentException, NullPointerException {
        if (columnNumber < 0 || columnNumber >= 4)
            throw new IllegalArgumentException("getColumn Market: not valid columnNumber");
        if (effects == null) throw new NullPointerException("getColumn Market: not expected NULL effect");
        if (effects.size() != getNumberOfWhiteMarbleInTheColumn(columnNumber)) throw new IllegalArgumentException("getColumn market: the number of effects is not the number of whitemarble in the column");


        HashMap<ResourceType, Integer> resources = new HashMap<>();
        for (int j = 0; j < marketMatrix.length; j++) {
            try {
                marketMatrix[j][columnNumber].onActivate(resources, effects);
            } catch (NegativeQuantityException | NullPointerException e) {
                System.out.println("moveColumn: Something extremely bad happened in the Market:");
                return new HashMap<>();
            } catch (IndexOutOfBoundsException e) {
                throw new IllegalArgumentException("The effects size is not the numbers of white marbles in the column");
            }
        }
        return resources;
    }

    /* same type marbles in the market refer to the same object, we instantiated only one marble for type*/
    /*create list of a certain number of references to Marbles with randomized order*/

    /**
     * Creates a List of Marbles and then Randomize the order
     * Same type Marble refers to the same object
     *
     * @param nBlue   refers the number of BlueMarble references to be added to the list
     * @param nGrey   refers the number of GreyMarbles references to be added to the list
     * @param nPurple refers the number of PurpleMarbles references to be added to the list
     * @param nRed    refers the number of RedMarbles references to be added to the list
     * @param nWhite  refers the number of WhiteMarbles references to be added to the list
     * @param nYellow refers the number of YellowMarbles references to be added to the list
     * @return a List of Marbles with randomized order
     */
    private List<Marble> createConfigurationList(int nWhite, int nRed, int nGrey, int nBlue, int nYellow, int nPurple) throws IllegalArgumentException {

        if (nWhite < 0 || nRed < 0 || nGrey < 0 || nBlue < 0 || nYellow < 0 || nPurple < 0)
            throw new IllegalArgumentException("createConfigurationList: number of Marbles can't be negative");
        List<Marble> configurationList = new ArrayList<>();
        Marble[] marbles = {new WhiteMarble(), new RedMarble(), new GreyMarble(), new BlueMarble(), new PurpleMarble(), new YellowMarble()};
        int[] numbersOfMarbles = {nWhite, nRed, nGrey, nBlue, nPurple, nYellow};

        int i = 0;
        for (Marble marble : marbles) {
            for (int c = 0; c < numbersOfMarbles[i]; c++) {
                configurationList.add(marble);
            }
            i++;
        }

        Collections.shuffle(configurationList);
        return configurationList;
    }

    private List<Marble> createConfigurationList(File config) throws ParserConfigurationException, IOException, SAXException, IllegalArgumentException, NegativeQuantityException {
        LinkedList<Marble> marbles = new LinkedList<>();
        Marble marble;
        int quantity;

        DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder docBuilder = documentBuilderFactory.newDocumentBuilder();
        Document doc = docBuilder.parse(config);
        doc.getDocumentElement().normalize();

        Node marketConfigNode = doc.getElementsByTagName("MarketConfig").item(0);
        if (marketConfigNode.getNodeType() == Node.ELEMENT_NODE) {
            Element rootElement = (Element) marketConfigNode;
            NodeList marbleNodes = rootElement.getElementsByTagName("marble");
            for (int i = 0; i < marbleNodes.getLength(); i++) {
                Node node = marbleNodes.item(i);
                if (node.getNodeType() == Node.ELEMENT_NODE) {
                    Element baseElement = (Element) node;
                    marble = MarbleType.valueOf(baseElement.getElementsByTagName("Type").item(0).getTextContent()).getMarble();
                    quantity = Integer.parseInt(baseElement.getElementsByTagName("quantity").item(0).getTextContent());
                    if (quantity < 0) throw new NegativeQuantityException("Market: negative quantity in xml File");
                    for (; quantity > 0; quantity--) {
                        marbles.add(marble);
                    }
                }
            }
        }
        Collections.shuffle(marbles);
        return marbles;
    }

    @Override
    public String toString() {
        String marketMatrixString;
        marketMatrixString = "";
        for (int i = 0; i < marketMatrix.length; i++) {
            for (int j = 0; j < marketMatrix[i].length; j++) {
                marketMatrixString = marketMatrixString.concat(marketMatrix[i][j].toString());
                marketMatrixString = marketMatrixString.concat(" ");
            }
            marketMatrixString = marketMatrixString.concat("\n");
        }

        return "Market{\n" +
                "marketMatrix=\n" +
                marketMatrixString +
                "\n, marbleOnSlide=" + marbleOnSlide +
                '}';
    }

    /**
     * get marble on the grid in a specific position
     *
     * @param row    is the chosen row of the grid starting from 0
     * @param column is the chosen column of the grid starting from 0
     * @return the chosen Marble on the grid
     * @throws IllegalArgumentException when the chosen position in the grid is not valid [0...2]x[0...3]
     */
    public Marble getMarbleInTheGrid(int row, int column) throws IllegalArgumentException {
        if (row < 0 || column < 0 || row >= 3 || column >= 4)
            throw new IllegalArgumentException("Not valid position in the grid 3x4");
        return marketMatrix[row][column];
    }

    /**
     * get marble on the slide
     *
     * @return the marble on the slide
     */
    public Marble getMarbleOnSlide() {
        return marbleOnSlide;
    }

    /**
     * set marble on the grid in a specific position
     * is private because only Market methods can change position of the Marbles in the Market
     *
     * @param row    is the chosen row of the grid starting from 0
     * @param column is the chosen column of the grid starting from 0
     * @param marble is the Marble to be set
     * @throws NullPointerException     when the marble to be set is null
     * @throws IllegalArgumentException when the chosen position in the grid is not valid [0...2]x[0...3]
     */
    private void setMarbleInTheGrid(Marble marble, int row, int column) throws IllegalArgumentException, NullPointerException {
        if (row < 0 || column < 0 || row >= 3 || column >= 4)
            throw new IllegalArgumentException("Not valid position in the grid 3x4");
        if (marble == null) throw new NullPointerException("setMarbleInTheGrid: not accepting null parameter");
        this.marketMatrix[row][column] = marble;
    }

    /**
     * set marble on the slide
     * is private because only Market methods can change position of the Marbles in the Market
     *
     * @param marbleOnSlide is the Marble to be set
     * @throws NullPointerException when the marble to be set is null
     */
    private void setMarbleOnSlide(Marble marbleOnSlide) throws NullPointerException {
        if (marbleOnSlide == null) throw new NullPointerException("setMarbleOnSlide: not accepting null parameter");
        this.marbleOnSlide = marbleOnSlide;
    }

    public int getNumberOfWhiteMarbleInTheRow(int rowNumber){
        if (rowNumber < 0 || rowNumber >= 3) throw new IllegalArgumentException("getNWhiteRow Market: not valid rowNumber");

        int c=0;
        for (int j = 0; j < marketMatrix[rowNumber].length; j++) {
            if (marketMatrix[rowNumber][j].isWhiteMarble())
                c++;
        }
        return c;
    }


    public int getNumberOfWhiteMarbleInTheColumn(int columnNumber) {
        if (columnNumber < 0 || columnNumber >= 4)
            throw new IllegalArgumentException("getNWhiteColumn Market: not valid columnNumber");

        int c = 0;
        for (int j = 0; j < marketMatrix.length; j++) {
            if (marketMatrix[j][columnNumber].isWhiteMarble())
                c++;
        }
        return c;
    }
}
