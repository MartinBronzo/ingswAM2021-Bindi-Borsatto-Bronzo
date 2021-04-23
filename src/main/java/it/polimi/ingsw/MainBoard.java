package it.polimi.ingsw;

import it.polimi.ingsw.DevCards.DevCardColour;
import it.polimi.ingsw.DevCards.DevGrid;
import it.polimi.ingsw.FaithTrack.FaithTrack;
import it.polimi.ingsw.Interfaces.Deck;
import it.polimi.ingsw.LeaderCard.LeaderCard;
import it.polimi.ingsw.LeaderCard.leaderEffects.*;
import it.polimi.ingsw.LeaderCard.LeaderCardRequirements.CardRequirementColor;
import it.polimi.ingsw.LeaderCard.LeaderCardRequirements.CardRequirementColorAndLevel;
import it.polimi.ingsw.LeaderCard.LeaderCardRequirements.CardRequirementResource;
import it.polimi.ingsw.LeaderCard.LeaderCardRequirements.Requirement;
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
import java.util.LinkedList;
import java.util.List;

public class MainBoard {
    private static MainBoard instance;
    protected Deck leaderCardsDeck;
    protected List<PlayerBoard> playerBoardsList;
    protected int numberOfPlayers;
    protected FaithTrack faithTrack;
    protected int numberOfLeaderCardsToGive;

    //Inizializzazione FaithTrack (e anche il reportNumOrder deve essere creato) e settaggio per i vari playerBoard del faithtrack
    //Inizializzazione PopeTiles: controllare che il numero di pope tiles sia uguale e che l'ordine sia sempre lo stesso
    //Creazione delle LeaderCards e distribuzione carte ai players
    //=> creazioni con file config
    //GetPlayerBoards()

    public static MainBoard instance(int numberOfPlayers){
        if(instance == null)
            instance = new MainBoard(numberOfPlayers);
        return instance;
    }

    public MainBoard(int numberOfPlayers){
        this.leaderCardsDeck = null;
        this.numberOfPlayers = numberOfPlayers;
        this.playerBoardsList = new ArrayList<>(numberOfPlayers);
        this.numberOfLeaderCardsToGive = 4;
    }

    //In futuro questi metodi saranno private e chiamati da un unico metodo pubblico initGame objects
    public void initFaithTrack(File config) throws IOException, SAXException, ParserConfigurationException {
        this.faithTrack = FaithTrack.instance(config);
        //Configurare anche il suo reportnUm ordrer a meno che nella configurazione del faithtrack con file non venga autmaticamente fatto
    }

    public void initPlayerBoards(){
        for(int i = 0; i < this.numberOfPlayers; i++)
            this.playerBoardsList.add(new PlayerBoard());
    }

    public void initPlayerBoardsFaithTrack(){
        for(PlayerBoard pB: playerBoardsList)
            pB.setPlayerFaithLevelFaithTrack(this.faithTrack);
    }

    /**
     * Initiates all the LeaderCards and creates a deck of them
     * @param config the file where to read the description of the LeaderCards
     */
    public void initLeaderCards(File config) throws ParserConfigurationException, NegativeQuantityException, SAXException, IOException {
        this.leaderCardsDeck = new LeaderCardDeck(config);
        this.leaderCardsDeck.shuffle();
        //Collections.shuffle(this.playerBoardList);

        List<LeaderCard> tmpList;
        for(PlayerBoard pB: this.playerBoardsList){
            tmpList = new ArrayList<>();
            for(int i = 0; i < this.numberOfLeaderCardsToGive; i++)
                tmpList.add((LeaderCard) this.leaderCardsDeck.drawFromDeck());
            pB.setNotPlayedLeaderCards(tmpList);
        }
        //Da qualche parte nel controller implementare il fatto che di queste che riceve il player ne può mantenere solo una certa
    }

/*    public LinkedList<LeaderCard> initLeaderCards(File configFile) throws ParserConfigurationException, IOException, SAXException, NegativeQuantityException {
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

    private Effect readEffectFromXML(Element elementLeaderCard) {
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
    }*/
        protected DevGrid devGrid;
    //other fields...


    public MainBoard(DevGrid devGrid) {
        this.devGrid = devGrid;
    }
}
