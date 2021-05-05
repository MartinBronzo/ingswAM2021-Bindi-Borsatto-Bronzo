package it.polimi.ingsw.model.soloGame;

import it.polimi.ingsw.model.DevCards.DevCardColour;
import it.polimi.ingsw.model.Interfaces.Deck;
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
import java.util.Collections;
import java.util.LinkedList;

/**
 * This class represents the deck of SoloActionTokens that is used in a SoloGame
 */

public class SoloActionDeck implements Deck {
    private final LinkedList<SoloActionToken> soloDeck;

    public SoloActionDeck(File configFile) throws ParserConfigurationException, IOException, SAXException {
        soloDeck = createDeck(configFile);
        Collections.shuffle(soloDeck);
    }

    public SoloActionDeck(SoloActionDeck soloActionDeck) {
        this.soloDeck = new LinkedList<>(soloActionDeck.soloDeck);
    }

    /**
     * Creates the deck from an xml file and returns it
     *
     * @param configFile the file that represents the xml file to be read
     * @return a LinkedList that represents the deck of token
     * @throws ParserConfigurationException if the read of the xml file has errors
     * @throws IOException                  if the read of the xml file has errors
     * @throws SAXException                 if the read of the xml file has errors
     */
    private LinkedList<SoloActionToken> createDeck(File configFile) throws ParserConfigurationException, IOException, SAXException {
        LinkedList<SoloActionToken> deck = new LinkedList<>();

        //variables for xml read
        Element tokenConfigElement, tokenElement;
        NodeList nodeDiscardTokens, nodeFaithToken;
        Node node;
        int length;

        //attributes for the tokens
        DiscardToken discardToken;
        FaithPointToken faithPointToken;
        DevCardColour color;
        int numCards, numPoints;
        boolean shuffle;


        //Prepare the reading of the file
        DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder docBuilder = documentBuilderFactory.newDocumentBuilder();
        Document doc = docBuilder.parse(configFile);
        doc.getDocumentElement().normalize();

        //Reads the root of the file
        Node tokenConfigNode = doc.getElementsByTagName("SoloTokenConfig").item(0);
        if (tokenConfigNode.getNodeType() == Node.ELEMENT_NODE) {
            tokenConfigElement = (Element) tokenConfigNode;
            //reads discardToken nodes
            nodeDiscardTokens = tokenConfigElement.getElementsByTagName("DiscardToken");
            length = nodeDiscardTokens.getLength();
            //creates a token for every element in xml
            for (int i = 0; i < length; i++) {
                node = nodeDiscardTokens.item(i);
                if (node.getNodeType() == Node.ELEMENT_NODE) {
                    tokenElement = (Element) node;
                    color = DevCardColour.valueOf(tokenElement.getElementsByTagName("Color").item(0).getTextContent());
                    numCards = Integer.parseInt(tokenElement.getElementsByTagName("NumCards").item(0).getTextContent());

                    discardToken = new DiscardToken(color, numCards);
                    deck.add(discardToken);
                }
            }
            // reads faithPointToken nodes
            nodeFaithToken = tokenConfigElement.getElementsByTagName("FaithPointToken");
            length = nodeFaithToken.getLength();
            //creates a token for every element in xml
            for (int i = 0; i < length; i++) {
                node = nodeFaithToken.item(i);
                if (node.getNodeType() == Node.ELEMENT_NODE) {
                    tokenElement = (Element) node;
                    numPoints = Integer.parseInt(tokenElement.getElementsByTagName("NumPoints").item(0).getTextContent());
                    shuffle = Boolean.parseBoolean(tokenElement.getElementsByTagName("Shuffle").item(0).getTextContent());

                    if (shuffle)
                        faithPointToken = new FaithPointToken(numPoints);
                    else
                        faithPointToken = new ShuffleToken(numPoints);

                    deck.add(faithPointToken);
                }
            }
        }

        return deck;
    }

    /**
     * Draws the first card of the token deck and add it to the end of the deck because before arriving to the end of the deck, the deck will be shuffled
     *
     * @return the drew card
     */
    @Override
    public Object drawFromDeck() {
        SoloActionToken drewToken;
        drewToken = soloDeck.removeFirst();
        soloDeck.addLast(drewToken);
        return drewToken;
    }

    /**
     * Returns the first card of the deck, without drawing it
     *
     * @return the first card of the deck, without drawing it
     */
    @Override
    public Object getFirst() {
        return soloDeck.getFirst();
    }

    /**
     * Shuffles the deck
     *
     * @return true if the action is performed without errors
     */
    @Override
    public boolean shuffle() {
        Collections.shuffle(soloDeck);
        return true;
    }

    /**
     * Returns true if the deck is empty
     *
     * @return true if the deck is empty
     */
    @Override
    public boolean isEmpty() {
        return soloDeck.isEmpty();
    }

    /**
     * Returns the size of the complete deck
     *
     * @return the size of the complete deck
     */
    @Override
    public int size() {
        return soloDeck.size();
    }
}