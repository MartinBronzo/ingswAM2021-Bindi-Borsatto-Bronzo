package it.polimi.ingsw.model.leaderCard;

import it.polimi.ingsw.exceptions.NegativeQuantityException;
import it.polimi.ingsw.model.devCards.DevCardColour;
import it.polimi.ingsw.model.Interfaces.Deck;
import it.polimi.ingsw.model.leaderCard.LeaderCardRequirements.CardRequirementColor;
import it.polimi.ingsw.model.leaderCard.LeaderCardRequirements.CardRequirementColorAndLevel;
import it.polimi.ingsw.model.leaderCard.LeaderCardRequirements.CardRequirementResource;
import it.polimi.ingsw.model.leaderCard.LeaderCardRequirements.Requirement;
import it.polimi.ingsw.model.leaderCard.leaderEffects.*;
import it.polimi.ingsw.model.resources.ResourceType;
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
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

/**
 * This card holds all the LeaderCards the MainBoard has at the beginning of the game.
 */
public class LeaderCardDeck implements Deck {
    List<LeaderCard> leaderCards;

    /**
     * Constructs an empty deck of LeaderCards
     */
    public LeaderCardDeck() {
        this.leaderCards = new ArrayList<>();
    }

    /**
     * Constructs a deck of the specified LeaderCards
     *
     * @param leaderCards the LeaderCards to be stored in the deck
     */
    public LeaderCardDeck(List<LeaderCard> leaderCards) {
        this.leaderCards = leaderCards;
    }

    /**
     * Constructs a LeaderCardDeck by reading the LeaderCards description in the specified file and by constructing them, too
     *
     * @param configFile the file where to read the description of the LeaderCards
     * @throws NegativeQuantityException    if the described LeaderCard aren't valid because of a negative number used as a quantity for some requirement
     * @throws ParserConfigurationException if there are problems in the parsing
     * @throws SAXException                 if there is a general SAX error or warning
     * @throws IOException                  if an IO operations fails
     */
    public LeaderCardDeck(File configFile) throws NegativeQuantityException, ParserConfigurationException, SAXException, IOException {
        this.leaderCards = this.initLeaderCards(configFile);
        this.shuffle();
    }

    /**
     * Constructs a copy of the specified LeaderCardDeck
     *
     * @param original the LeaderCard to be cloned
     */
    public LeaderCardDeck(LeaderCardDeck original) {
        this();
        for (LeaderCard lC : original.leaderCards)
            this.leaderCards.add(new LeaderCard(lC));
    }


    /**
     * Adds the specified LeaderCard to the deck
     *
     * @param leaderCard a LeaderCard to be added
     */
    public void addLeaderCard(LeaderCard leaderCard) {
        this.leaderCards.add(leaderCard);
    }

    /**
     * Draws a LeaderCard from the deck (it removes it)
     *
     * @return the drawn LeaderCard
     */
    @Override
    public Object drawFromDeck() {
        LeaderCard leaderCard = this.leaderCards.get(0);
        this.leaderCards.remove(leaderCard);
        return leaderCard;
    }

    /**
     * Draws the LeaderCard at the top of the deck (it removes it)
     *
     * @return the drawn LeaderCard
     */
    @Override
    public Object getFirst() {
        LeaderCard leaderCard = this.leaderCards.get(0);
        this.leaderCards.remove(leaderCard);
        return leaderCard;
    }

    @Override
    public boolean shuffle() {
        Collections.shuffle(this.leaderCards);
        return true;
    }

    @Override
    public boolean isEmpty() {
        return this.leaderCards.isEmpty();
    }

    @Override
    public int size() {
        return this.leaderCards.size();
    }

    public List<LeaderCard> getCopyLeaderCards() {
        List<LeaderCard> result = new ArrayList<>();
        for (LeaderCard lD : this.leaderCards)
            result.add(new LeaderCard(lD));
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null)
            return false;
        if (obj == this)
            return true;
        if (!(obj instanceof LeaderCardDeck))
            return false;
        LeaderCardDeck tmp = (LeaderCardDeck) obj;
        //return this.leaderCards.containsAll(tmp.leaderCards) && tmp.leaderCards.containsAll(this.leaderCards);
        return this.containsAllNoDuplicates(this.leaderCards, tmp.leaderCards) && this.containsAllNoDuplicates(tmp.leaderCards, this.leaderCards);
    }

    /**
     * Checks whether the first list is contained by the second list
     *
     * @param l1 the list to be contained by the other
     * @param l2 the list to contain the other
     * @return true if the first list is contained by the second one, false otherwise
     */
    private boolean containsAllNoDuplicates(List<LeaderCard> l1, List<LeaderCard> l2) {
        List<LeaderCard> l1Copy = new ArrayList<>(l1);
        for (LeaderCard lD : l2)
            l1Copy.remove(lD);
        return l1Copy.isEmpty();
    }

    public static LinkedList<LeaderCard> initLeaderCards(File configFile) throws ParserConfigurationException, IOException, SAXException, NegativeQuantityException {
        LinkedList<LeaderCard> deck = new LinkedList<>();

        NodeList nodeLeaderCardList;
        Element elementLeaderConfig, elementLeaderCard;
        Node cardNode, requirementsListNode;
        int numLeaderCards;

        LeaderCard leaderCard;
        int victoryPoints;
        ArrayList<Requirement> requirementList;
        Effect effect;
        String url;

        //Prepare the reading of the file
        DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder docBuilder = documentBuilderFactory.newDocumentBuilder();
        Document doc = docBuilder.parse(configFile);
        doc.getDocumentElement().normalize();

        //Reads the root of the file
        Node leaderConfigNode = doc.getElementsByTagName("LeaderCardConfig").item(0);
        if (leaderConfigNode.getNodeType() == Node.ELEMENT_NODE) {
            elementLeaderConfig = (Element) leaderConfigNode;
            nodeLeaderCardList = elementLeaderConfig.getElementsByTagName("LeaderCard");

            //Reads every leaderCard in the file
            numLeaderCards = nodeLeaderCardList.getLength();
            for (int i = 0; i < numLeaderCards; i++) {
                cardNode = nodeLeaderCardList.item(i);
                if (cardNode.getNodeType() == Node.ELEMENT_NODE) {
                    elementLeaderCard = (Element) cardNode;

                    //Reads the VictoryPoints
                    victoryPoints = Integer.parseInt(elementLeaderCard.getElementsByTagName("VictoryPoints").item(0).getTextContent());

                    //Reads the requirements
                    requirementsListNode = elementLeaderCard.getElementsByTagName("Requirementlist").item(0);
                    requirementList = readRequirementsFromXML(requirementsListNode);

                    //Reads the effect
                    effect = readEffectFromXML(elementLeaderCard);

                    //Reads the Url
                    url = elementLeaderCard.getElementsByTagName("Url").item(0).getTextContent();

                    //leaderCard = new LeaderCard(victoryPoints, requirementList, effect);
                    leaderCard = new LeaderCard(victoryPoints, requirementList, effect, url);
                    deck.add(leaderCard);
                }
            }
        }
        return deck;
    }

    private static Effect readEffectFromXML(Element elementLeaderCard) {
        Node effectNode;
        Element elementEffect;

        ResourceType resourceType;
        int quantity;

        if (elementLeaderCard.getElementsByTagName("DiscountEffect").getLength() != 0) {
            effectNode = elementLeaderCard.getElementsByTagName("DiscountEffect").item(0);
            if (effectNode.getNodeType() == Node.ELEMENT_NODE) {
                elementEffect = (Element) effectNode;
                resourceType = ResourceType.valueOf(elementEffect.getElementsByTagName("Resource").item(0).getTextContent());
                quantity = Integer.parseInt(elementEffect.getElementsByTagName("Discount").item(0).getTextContent());
                return new DiscountLeaderEffect(resourceType, quantity);
            }
        } else if (elementLeaderCard.getElementsByTagName("ExtraSlotEffect").getLength() != 0) {
            effectNode = elementLeaderCard.getElementsByTagName("ExtraSlotEffect").item(0);
            if (effectNode.getNodeType() == Node.ELEMENT_NODE) {
                elementEffect = (Element) effectNode;
                resourceType = ResourceType.valueOf(elementEffect.getElementsByTagName("Resource").item(0).getTextContent());
                quantity = Integer.parseInt(elementEffect.getElementsByTagName("Quantity").item(0).getTextContent());
                return new ExtraSlotLeaderEffect(resourceType, quantity);
            }
        } else if (elementLeaderCard.getElementsByTagName("WhiteMarbleEffect").getLength() != 0) {
            effectNode = elementLeaderCard.getElementsByTagName("WhiteMarbleEffect").item(0);
            if (effectNode.getNodeType() == Node.ELEMENT_NODE) {
                elementEffect = (Element) effectNode;
                resourceType = ResourceType.valueOf(elementEffect.getElementsByTagName("Resource").item(0).getTextContent());
                return new WhiteMarbleLeaderEffect(resourceType);
            }
        } else if (elementLeaderCard.getElementsByTagName("ExtraProductionEffect").getLength() != 0) {
            effectNode = elementLeaderCard.getElementsByTagName("ExtraProductionEffect").item(0);
            if (effectNode.getNodeType() == Node.ELEMENT_NODE) {
                elementEffect = (Element) effectNode;
                resourceType = ResourceType.valueOf(elementEffect.getElementsByTagName("Resource").item(0).getTextContent());
                quantity = Integer.parseInt(elementEffect.getElementsByTagName("Quantity").item(0).getTextContent());

                return new ExtraProductionLeaderEffect(resourceType, quantity);
            }
        }
        return null;
    }

    private static ArrayList<Requirement> readRequirementsFromXML(Node requirementsListNode) throws NegativeQuantityException {
        NodeList colorRequirementList, resourceRequirementList, colorAndLevelRequirementsList;
        Node requirementNode;
        Element elementRequirementsList, elementRequirement;
        int numRequirements;
        ArrayList<Requirement> requirementList = new ArrayList<>();

        DevCardColour cardColour;
        ResourceType resourceType;
        int quantity, level;

        if (requirementsListNode.getNodeType() == Node.ELEMENT_NODE) {
            elementRequirementsList = (Element) requirementsListNode;

            //gets all ColorRequirements of the leaderCard
            colorRequirementList = elementRequirementsList.getElementsByTagName("ColorRequirement");
            numRequirements = colorRequirementList.getLength();
            for (int i = 0; i < numRequirements; i++) {
                requirementNode = colorRequirementList.item(i);
                if (requirementNode.getNodeType() == Node.ELEMENT_NODE) {
                    elementRequirement = (Element) requirementNode;
                    cardColour = DevCardColour.valueOf(elementRequirement.getElementsByTagName("Color").item(0).getTextContent());
                    quantity = Integer.parseInt(elementRequirement.getElementsByTagName("Quantity").item(0).getTextContent());
                    requirementList.add(new CardRequirementColor(cardColour, quantity));
                }
            }

            //gets all ResourceRequirements of the leaderCard
            resourceRequirementList = elementRequirementsList.getElementsByTagName("ResourceRequirement");
            numRequirements = resourceRequirementList.getLength();
            for (int i = 0; i < numRequirements; i++) {
                requirementNode = resourceRequirementList.item(i);
                if (requirementNode.getNodeType() == Node.ELEMENT_NODE) {
                    elementRequirement = (Element) requirementNode;
                    resourceType = ResourceType.valueOf(elementRequirement.getElementsByTagName("Resource").item(0).getTextContent());
                    quantity = Integer.parseInt(elementRequirement.getElementsByTagName("Quantity").item(0).getTextContent());
                    requirementList.add(new CardRequirementResource(resourceType, quantity));
                }
            }

            //gets all colorAndLevelRequirements of the leaderCard
            colorAndLevelRequirementsList = elementRequirementsList.getElementsByTagName("ColorAndLevelRequirement");
            numRequirements = colorAndLevelRequirementsList.getLength();
            for (int i = 0; i < numRequirements; i++) {
                requirementNode = colorAndLevelRequirementsList.item(i);
                if (requirementNode.getNodeType() == Node.ELEMENT_NODE) {
                    elementRequirement = (Element) requirementNode;
                    cardColour = DevCardColour.valueOf(elementRequirement.getElementsByTagName("Color").item(0).getTextContent());
                    level = Integer.parseInt(elementRequirement.getElementsByTagName("Level").item(0).getTextContent());
                    quantity = Integer.parseInt(elementRequirement.getElementsByTagName("Quantity").item(0).getTextContent());
                    requirementList.add(new CardRequirementColorAndLevel(level, cardColour, quantity));
                }
            }
        }
        return requirementList;
    }
}
