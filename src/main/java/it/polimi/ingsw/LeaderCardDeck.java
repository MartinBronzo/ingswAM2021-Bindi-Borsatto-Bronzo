package it.polimi.ingsw;

import it.polimi.ingsw.DevCards.DevCardColour;
import it.polimi.ingsw.Interfaces.Deck;
import it.polimi.ingsw.LeaderCard.LeaderCard;
import it.polimi.ingsw.LeaderCard.leaderEffects.*;
import it.polimi.ingsw.LeaderCardRequirementsTests.CardRequirementColor;
import it.polimi.ingsw.LeaderCardRequirementsTests.CardRequirementColorAndLevel;
import it.polimi.ingsw.LeaderCardRequirementsTests.CardRequirementResource;
import it.polimi.ingsw.LeaderCardRequirementsTests.Requirement;
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
import java.util.*;

/**
 * This card holds all the LeaderCards the MainBoard has at the beginning of the game.
 */
public class LeaderCardDeck implements Deck {
    List<LeaderCard> leaderCards;

    /**
     * Constructs an empty deck of LeaderCards
     */
    public LeaderCardDeck(){
        this.leaderCards = new ArrayList<>();
    }

    /**
     * Constructs a deck of the specified LeaderCards
     * @param leaderCards the LeaderCards to be stored in the deck
     */
    public LeaderCardDeck(List<LeaderCard> leaderCards){
        this.leaderCards = leaderCards;
    }

    public LeaderCardDeck(File configFile) throws ParserConfigurationException, NegativeQuantityException, SAXException, IOException {
        this.leaderCards = this.initLeaderCards(configFile);
        this.shuffle();
    }


    /**
     * Adds the specified LeaderCard to the deck
     * @param leaderCard a LeaderCard to be added
     */
    public void addLeaderCard(LeaderCard leaderCard){
        this.leaderCards.add(leaderCard);
    }

    /**
     * Draws a LeaderCard from the deck (it removes it)
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

    public List<LeaderCard> getCopyLeaderCards(){
        List<LeaderCard> result = new ArrayList<>();
        for(LeaderCard lD: this.leaderCards)
            result.add(new LeaderCard(lD));
        return result;
    }

    public LinkedList<LeaderCard> initLeaderCards(File configFile) throws ParserConfigurationException, IOException, SAXException, NegativeQuantityException {
        LinkedList<LeaderCard> deck = new LinkedList<>();

        NodeList nodeLeaderCardList;
        Element elementLeaderConfig, elementLeaderCard;
        Node cardNode, requirementsListNode;
        int numLeaderCards;

        LeaderCard leaderCard;
        int victoryPoints;
        ArrayList<Requirement> requirementList;
        Effect effect;

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
                    victoryPoints = Integer.parseInt(elementLeaderCard.getElementsByTagName("VictoryPoints").item(0).getTextContent());

                    //Reads the requirements
                    requirementsListNode = elementLeaderCard.getElementsByTagName("Requirementlist").item(0);
                    requirementList = readRequirementsFromXML(requirementsListNode);

                    //Reads the effect
                    effect = readEffectFromXML(elementLeaderCard);

                    leaderCard = new LeaderCard(victoryPoints, requirementList, effect);
                    deck.add(leaderCard);
                }
            }
        }
        return deck;
    }

    private Effect readEffectFromXML(Element elementLeaderCard){
        Node effectNode;
        Element elementEffect;

        ResourceType resourceType;
        int quantity;

        if(elementLeaderCard.getElementsByTagName("DiscountEffect").getLength() != 0){
            effectNode = elementLeaderCard.getElementsByTagName("DiscountEffect").item(0);
            if (effectNode.getNodeType() == Node.ELEMENT_NODE) {
                elementEffect = (Element) effectNode;
                resourceType = ResourceType.valueOf(elementEffect.getElementsByTagName("Resource").item(0).getTextContent());
                quantity = Integer.parseInt(elementEffect.getElementsByTagName("Discount").item(0).getTextContent());
                return new DiscountLeaderEffect(resourceType, quantity);
            }
        }
        else if(elementLeaderCard.getElementsByTagName("ExtraSlotEffect").getLength() != 0){
            effectNode = elementLeaderCard.getElementsByTagName("ExtraSlotEffect").item(0);
            if (effectNode.getNodeType() == Node.ELEMENT_NODE) {
                elementEffect = (Element) effectNode;
                resourceType = ResourceType.valueOf(elementEffect.getElementsByTagName("Resource").item(0).getTextContent());
                quantity = Integer.parseInt(elementEffect.getElementsByTagName("Quantity").item(0).getTextContent());
                return new ExtraSlotLeaderEffect(resourceType, quantity);
            }
        }
        else if(elementLeaderCard.getElementsByTagName("WhiteMarbleEffect").getLength() != 0){
            effectNode = elementLeaderCard.getElementsByTagName("WhiteMarbleEffect").item(0);
            if (effectNode.getNodeType() == Node.ELEMENT_NODE) {
                elementEffect = (Element) effectNode;
                resourceType = ResourceType.valueOf(elementEffect.getElementsByTagName("Resource").item(0).getTextContent());
                return new WhiteMarbleLeaderEffect(resourceType);
            }
        }
        else if(elementLeaderCard.getElementsByTagName("ExtraProductionEffect").getLength() != 0){
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

    private ArrayList<Requirement> readRequirementsFromXML(Node requirementsListNode) throws NegativeQuantityException {
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
