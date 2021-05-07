package it.polimi.ingsw.model;

import it.polimi.ingsw.exceptions.EmptyDeckException;
import it.polimi.ingsw.exceptions.IllegalActionException;
import it.polimi.ingsw.exceptions.NegativeQuantityException;
import it.polimi.ingsw.model.DevCards.DevCard;
import it.polimi.ingsw.model.DevCards.DevCardColour;
import it.polimi.ingsw.model.DevCards.DevGrid;
import it.polimi.ingsw.model.FaithTrack.FaithTrack;
import it.polimi.ingsw.model.FaithTrack.ReportNum;
import it.polimi.ingsw.model.Interfaces.Deck;
import it.polimi.ingsw.model.LeaderCard.LeaderCard;
import it.polimi.ingsw.model.LeaderCard.leaderEffects.Effect;
import it.polimi.ingsw.model.Market.Market;
import it.polimi.ingsw.model.marble.Marble;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;

public class MainBoard {
    private static MainBoard instance;
    protected Deck leaderCardsDeck;
    protected List<PlayerBoard> playerBoardsList;
    protected int numberOfPlayers;
    protected FaithTrack faithTrack;
    protected int numberOfLeaderCardsToGive;
    protected DevGrid devGrid;
    protected Market market;
    //other fields...


    /*
    ###########################################################################################################
     MARKET METHODS
    ###########################################################################################################
     */

    /**
     * Moves the specified row in the market
     * @param rowNumber the row to be moved
     * @param effects the effects of LeaderCards the player wants to use
     * @return the resources the player gets
     * @throws IllegalArgumentException if this move can't be made (the specified row number is invalid or there are not enough effects)
     * @throws NullPointerException if there are no specified effects
     */
    //The NullPointerException must be catched in the controller because it's the controller which must remember to at least send an empty list
    public HashMap<ResourceType, Integer> moveRowInMarket(int rowNumber, List<Effect> effects) throws IllegalArgumentException, NullPointerException {
        return this.market.moveRow(rowNumber, effects);
    }

    /**
     * Moves the specified column in the market
     * @param columnNumber the column to be moved
     * @param effects the effects of LeaderCards the player wants to use
     * @return the resources the player gets
     * @throws IllegalArgumentException if this move can't be made (the specified column number is invalid or there are not enough effects)
     * @throws NullPointerException if there are no specified effects
     */
    //The NullPointerException must be caught in the controller because it's the controller which must remember to at least send an empty list
    public HashMap<ResourceType, Integer> moveColumnInMarket(int columnNumber, List<Effect> effects) throws IllegalArgumentException, NullPointerException {
        return this.market.moveColumn(columnNumber, effects);
    }

    /**
     * Returns the resources the player would get if the chose to move the specified row
     *
     * @param rowNumber the row the player may want to change in the future
     * @param effects the effects of LeaderCards the player wants to use
     * @return the resources this move would generate
     * @throws IllegalArgumentException if this move can't be made (the specified row number is invalid or there are not enough effects)
     * @throws NullPointerException     if there are no specified effects
     */
    //The NullPointerException must be caught in the controller because it's the controller which must remember to at least send an empty list
    public HashMap<ResourceType, Integer> getResourcesFromRowInMarket(int rowNumber, List<Effect> effects) throws IllegalArgumentException, NullPointerException {
        return this.market.getResourcesFromRow(rowNumber, effects);
    }

    /**
     * Returns the resources the player would get if the chose to move the specified column
     *
     * @param columnNumber the column the player may want to change in the future
     * @param effects the effects of LeaderCards the player wants to use
     * @return the resources this move would generate
     * @throws IllegalArgumentException if this move can't be made (the specified column number is invalid or there are not enough effects)
     * @throws NullPointerException     if there are no specified effects
     */
    //The NullPointerException must be caught in the controller because it's the controller which must remember to at least send an empty list
    public HashMap<ResourceType, Integer> getResourcesFromColumnInMarket(int columnNumber, List<Effect> effects) throws IllegalArgumentException, NullPointerException {
        return this.market.getResourcesFromColumn(columnNumber, effects);
    }

    /**
     * Returns the marble in the specific position
     *
     * @param row    the row the marble is at
     * @param column the column the marble is at
     * @return the chosen Marble on the grid
     * @throws IllegalArgumentException when the position is invalid
     */
    public Marble getMarbleInTheGrid(int row, int column) throws IllegalArgumentException{
        return this.market.getMarbleInTheGrid(row, column);
    }

    /*
    ###########################################################################################################
     DEVGRID METHODS
    ###########################################################################################################
     */

    /**
     * Returns the DevCard at the specified position
     *
     * @param row    is the chosen row of the grid
     * @param column is the chosen column of the grid
     * @return the first DevCard at the chosen DevDeck on the grid (the Card is not Removed From the deck) or NULL if devDeck is Empty
     * @throws IllegalArgumentException when the chosen position in the grid is not valid
     */
    public DevCard getDevCardFromDeckInDevGrid(int row, int column) throws IllegalArgumentException{
        return this.devGrid.getDevCardFromDeck(row, column);
    }

    /**
     * Returns the DevCard with the specified level and color
     *
     * @param level  the level of the card
     * @param colour the color of the card
     * @return the first DevCard in the chosen DevDeck on the grid (the Card is not removed from the deck) or NULL if devDeck is Empty
     * @throws IllegalArgumentException when the chosen level or color are not valid
     */
    public DevCard getDevCardFromDeckInDevGrid(int level, DevCardColour colour) throws IllegalArgumentException{
        return this.devGrid.getDevCardFromDeck(level, colour);
    }

    /**
     * draws the DevCard on the grid at the specified position
     *
     * @param row    the row of the DevCard
     * @param column the column of the DevCard
     * @return the first DevCard in the chosen DevDeck on the grid. The Card is Removed From the deck.
     * @throws IllegalArgumentException when the chosen position on the grid is not valid
     * @throws IllegalActionException if the specified DevDeck is empty
     */
    public DevCard drawDevCardFromDeckInDevGrid(int row, int column) throws IllegalArgumentException, IllegalActionException{
        DevCard result;

        try{
            result = this.devGrid.drawDevCardFromDeck(row, column);
        }catch(EmptyDeckException e){
            throw new IllegalActionException(e.getMessage());
        }

        return result;
    }

    /**
     * draws a DevCard with the specified level and color
     *
     * @param level  the level of the card
     * @param colour the color of the card
     * @return the first DevCard in the chosen DevDeck on the grid. The Card is Removed From the deck.
     * @throws IllegalArgumentException when the chosen position in the grid is not valid
     * @throws IllegalActionException      if devDeck is Empty;
     */
    public DevCard drawDevCardFromDeckInDevGrid(int level, DevCardColour colour) throws IllegalArgumentException, IllegalActionException{
        DevCard result;

        try{
            result = this.devGrid.drawDevCardFromDeck(level, colour);
        }catch(EmptyDeckException e){
            throw new IllegalActionException(e.getMessage());
        }

        return result;
    }

    /**
     * @return a list of the uncovered DevCard in the Gris
     */
    public Collection<DevCard> getDrawableCardsInDevGrid(){
        return this.devGrid.getDrawableCards();
    }

    /**
     * Returns the size of the devDeck containing devCards of the specified color and with the max level
     *
     * @param colour the color of the devDeck
     * @return the size of the devDeck containing devCards of the specified color and with the max level
     */
    public int getDevDeckSizeInDevGrid(DevCardColour colour) {
        return this.devGrid.getDevDeckSize(colour);
    }



    /***************************** OLD STUFF TO BE CHECKED ****************************************/


    public MainBoard(DevGrid devGrid) {
        this.devGrid = devGrid;
    }

    //Inizializzazione FaithTrack (e anche il reportNumOrder deve essere creato) e settaggio per i vari playerBoard del faithtrack
    //Inizializzazione PopeTiles: controllare che il numero di pope tiles sia uguale e che l'ordine sia sempre lo stesso
    //Creazione delle LeaderCards e distribuzione carte ai players
    //=> creazioni con file config
    //GetPlayerBoards()

    public static MainBoard instance(int numberOfPlayers) {
        if (instance == null)
            instance = new MainBoard(numberOfPlayers);
        return instance;
    }

    public MainBoard(int numberOfPlayers) {
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

    public void initPlayerBoards() {
        for (int i = 0; i < this.numberOfPlayers; i++)
            this.playerBoardsList.add(new PlayerBoard());
    }

    public void initPlayerBoardsFaithTrack() {
        for (PlayerBoard pB : playerBoardsList)
            pB.setPlayerFaithLevelFaithTrack(this.faithTrack);
    }

    /**
     * Initiates all the LeaderCards and creates a deck of them
     *
     * @param config the file where to read the description of the LeaderCards
     */
    public void initLeaderCards(File config) throws ParserConfigurationException, NegativeQuantityException, SAXException, IOException {
        this.leaderCardsDeck = new LeaderCardDeck(config);
        this.leaderCardsDeck.shuffle();
        //Collections.shuffle(this.playerBoardList);

        List<LeaderCard> tmpList;
        for (PlayerBoard pB : this.playerBoardsList) {
            tmpList = new ArrayList<>();
            for (int i = 0; i < this.numberOfLeaderCardsToGive; i++)
                tmpList.add((LeaderCard) this.leaderCardsDeck.drawFromDeck());
            pB.setNotPlayedLeaderCardsAtGameBeginning(tmpList);
        }
        //Da qualche parte nel controller implementare il fatto che di queste che riceve il player ne può mantenere solo una certa
    }

    public void dealWithVaticanReportAllPlayers(ReportNum reportNum) throws IllegalActionException {
        for (PlayerBoard pB : this.playerBoardsList)
            pB.dealWithVaticanReport(reportNum);
    }

    public MainBoard() throws NegativeQuantityException, ParserConfigurationException, IOException, SAXException {
        this.playerBoardsList = new ArrayList<>();
        this.market = new Market(new File("MarketConfig.xsd.xml"));
        this.devGrid = new DevGrid(new File("DevCardConfig.xsd.xml"));
    }

    public void addPlayerBoard(PlayerBoard playerBoard) {
        this.playerBoardsList.add(playerBoard);
    }
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

