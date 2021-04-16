package it.polimi.ingsw;

import it.polimi.ingsw.DevCards.DevCard;
import it.polimi.ingsw.DevCards.DevSlots;
import it.polimi.ingsw.FaithTrack.FaithLevel;
import it.polimi.ingsw.FaithTrack.FaithTrack;
import it.polimi.ingsw.FaithTrack.PopeTile;
import it.polimi.ingsw.FaithTrack.ReportNum;
import it.polimi.ingsw.LeaderCard.LeaderCard;
import it.polimi.ingsw.LeaderCard.LeaderCards;
import it.polimi.ingsw.LeaderCard.leaderEffects.Effect;
import it.polimi.ingsw.LeaderCard.LeaderCardRequirements.Requirement;
import it.polimi.ingsw.exceptions.*;

import java.util.*;
import java.util.stream.Collectors;

public class PlayerBoard {
    private final FaithLevel playerFaithLevel;
    private final Depot depot;
    private final Strongbox strongbox;
    private final DevSlots devSlots;
    private final LeaderCards leaderCards;
    private final BaseProduction baseProduction;

    public PlayerBoard(List<LeaderCard> leaderCards) {
        this.playerFaithLevel = new FaithLevel();
        this.depot = new Depot();
        this.strongbox = new Strongbox();
        this.devSlots = new DevSlots();
        this.leaderCards = new LeaderCards(leaderCards);
        this.baseProduction = new BaseProduction();
    }

    public PlayerBoard() {
        this.playerFaithLevel = new FaithLevel();
        this.depot = new Depot();
        this.strongbox = new Strongbox();
        this.devSlots = new DevSlots();
        this.leaderCards = new LeaderCards();
        this.baseProduction = new BaseProduction();
    }

    public FaithLevel getPlayerFaithLevel() {
        return new FaithLevel(this.playerFaithLevel);
    }

    public DevSlots getDevSlots() {
        return new DevSlots(this.devSlots);
    }

    public BaseProduction getBaseProduction() {
        return new BaseProduction(this.baseProduction);
    }


    public HashMap<ResourceType, Integer> getAllResources() {
        HashMap<ResourceType, Integer> depotMap = depot.getAllResources();
        HashMap<ResourceType, Integer> strongBoxMap = strongbox.getAllResources();
        Set<ResourceType> depotKeys = depotMap.keySet();
        Set<ResourceType> strongboxKeys = depotMap.keySet();
        Set<ResourceType> keys = new TreeSet<>();
        keys.addAll(depotKeys);
        keys.addAll(strongboxKeys);
        HashMap<ResourceType, Integer> allResourcesMap = new HashMap<>();
        keys.stream()
                .distinct()
                .forEach(key -> allResourcesMap.put(key, depotMap.getOrDefault(key, 0) + strongBoxMap.getOrDefault(key, 0)));
        return allResourcesMap;
    }

    /**
     * Gets A Collection containing all the DevCards in the Slots.
     *
     * @return a collection of Cards
     */
    public Collection<DevCard> getAllDevCards() {
        return devSlots.getAllDevCards();
    }

    public Boolean isCardBuyable(DevCard devCard) {
        if (devCard == null) throw new NullPointerException("isCardBuyable: DevCardCan't be null");
        HashMap<ResourceType, Integer> cost = devCard.getCost();
        Collection<ResourceType> costKeys = cost.keySet();
        HashMap<ResourceType, Integer> allResources = getAllResources();
        return costKeys.stream().allMatch(key -> cost.getOrDefault(key, 0) <= allResources.getOrDefault(key, 0));
    }


    public int calculateVictoryPoints() {
        HashMap<ResourceType, Integer> depotRes, strongboxRes;
        int totalResources;
        int vp = 0;

        vp += devSlots.getPoints();
        vp += playerFaithLevel.getCellPoints();
        vp += playerFaithLevel.getPopeTilesPoints();
        vp += leaderCards.getLeaderCardsPoints();
        depotRes = depot.getAllResources();
        strongboxRes = strongbox.getAllResources();
        totalResources = 0;
        for (ResourceType res : ResourceType.values()) {
            totalResources += depotRes.getOrDefault(res,0) + strongboxRes.getOrDefault(res,0);
        }
        vp += totalResources / 5;

        return vp;
    }


    /*
    #####################
    #LEADERCARDS METHODS#
    #####################
    */
    public void setNotPlayedLeaderCards(List<LeaderCard> notPlayedLeaderCards) {
        List<LeaderCard> clone = new ArrayList<>();
        for (LeaderCard lD : notPlayedLeaderCards)
            clone.add(new LeaderCard(lD));
        this.leaderCards.setNotPlayedCards(clone);
    }

    /**
     * Activates the specified LeaderCard if the player meets all the requirements
     *
     * @param leaderCard the LeaderCard to be activated
     * @return true if the card was activated
     * @throws UnmetRequirementException if the player doesn't meet the requirements
     * @throws FullExtraSlotException if the maximum number of extraSlotLeaderCards had been reached
     * @throws NullPointerException if leader card is null
     */
    public boolean activateLeaderCard(LeaderCard leaderCard) throws UnmetRequirementException, FullExtraSlotException, NullPointerException {
        if (leaderCard == null) throw new NullPointerException("LeaderCard can't be null");

        PlayerResourcesAndCards playerResourcesAndCards = new PlayerResourcesAndCards(getAllResources(), getAllDevCards());
        if (this.leaderCards.activateLeaderCard(leaderCard, playerResourcesAndCards)) {
            if (this.leaderCards.addCardToActivatedOneShotCards(leaderCard))
                this.depot.addExtraSlot(leaderCard.getEffect());
            return true;
        }
        return false;


    }

    /**
     * Discards the specified LeaderCard from the not-played LeaderCards the player holds and lets the player have some benefits from it
     *
     * @param leaderCard a LeaderCard to be discarded
     * @throws IllegalArgumentException   if the card can't be discarded
     * @throws LastVaticanReportException if the last Vatican Report was activated
     */
    public void discardLeaderCard(LeaderCard leaderCard) throws IllegalArgumentException, LastVaticanReportException {
        HashMap<ResourceType, Integer> outputWhenDiscarded = this.leaderCards.discardLeaderCard(leaderCard);
        this.moveForwardOnFaithTrack(outputWhenDiscarded.get(ResourceType.FAITHPOINT));
    }

    /**
     * Discards the specified LeaderCard from the not-played LeaderCards the player holds and doesn't give the player any benefits. This method is
     * used at the configuration of the game when the player is given a certain amount of LeaderCards but they can't keel all of them
     *
     * @param leaderCard a LeaderCard to be discarded
     * @throws IllegalArgumentException if the card can't be discarded
     */
    public void discardLeaderCardAtTheBeginning(LeaderCard leaderCard) throws IllegalArgumentException {
        this.leaderCards.discardLeaderCard(leaderCard);
    }

    /**
     * Returns the effect of the specified LeaderCard if the player can use such card in the game
     *
     * @param leaderCard a LeaderCard
     * @return the effect of such LeaderCard
     * @throws IllegalArgumentException if the player doesn't hold such card or if they haven't activated it, yet
     * @throws IllegalActionException   if the player tries to get the effect of a one-shot card they have already used once
     */
    public Effect getEffectFromCard(LeaderCard leaderCard) throws IllegalArgumentException, IllegalActionException {
        return this.leaderCards.getEffectFromCard(leaderCard);
    }

    /**
     * Returns a copy of all the LeaderCards the player has not played yet
     *
     * @return a copy of the not-played LeaderCards
     */
    public List<LeaderCard> getNotPlayedLeaderCards() {
        return this.leaderCards.getNotPlayedCards();
    }

    public List<LeaderCard> getActiveLeaderCards() {
        return this.leaderCards.getActiveCards();
    }

    public List<LeaderCard> getAlreadyUsedOneShotCard() {
        return this.leaderCards.getAlreadyUsedOneShotCard();
    }

    public List<Requirement> getLeaderCardRequirements(LeaderCard leaderCard) throws IllegalArgumentException {
        return this.leaderCards.getLeaderCardRequirements(leaderCard);
    }

    /**
     * Returns the points the player gets from their LeaderCards
     *
     * @return the points of all the active LeaderCards the player has
     */
    public int getLeaderCardsPoints() {
        return this.leaderCards.getLeaderCardsPoints();
    }

    /**
     * Returns whether the player holds the specified LeaderCard
     *
     * @param leaderCard a LeaderCard
     * @return true if the card is active, false if the card is not played, yet
     * @throws IllegalArgumentException if the player doesn't hold the card
     */
    public boolean isLeaderCardActive(LeaderCard leaderCard) throws IllegalArgumentException {
        return leaderCards.isLeaderCardActive(leaderCard);
    }

    /*
    ##################
    #DEVCARDS METHODS#
    ##################
    */

    /**
     * Adds one devCard to the selected DevSlot according to GameRules
     *
     * @param index   is the DevSlot number starting from 0
     * @param devCard id DevCard to be added, can't be in one of the DevSlots
     * @return true if the DevCard is added in the desired Slot
     * @throws IndexOutOfBoundsException if index is not valid: must be between 0 and 2
     * @throws NullPointerException      if devCard is null
     * @throws IllegalArgumentException  if this card can't be added in the desiredSlot
     */
    public boolean addCardToDevSlot(int index, DevCard devCard) throws IndexOutOfBoundsException, NullPointerException, IllegalArgumentException {
        devSlots.addDevCard(index, devCard);
        return true;
    }

    /*
    ##################
    #DEPOT METHODS#
    ##################
    */

    /**
     * Adds resources to the selected shelf in the depot
     *
     * @param resourceType: the resourceType to be added
     * @param quantity:     the quantity of the resource
     * @param shelf:        the number of the shelf on which you want to add the resource
     * @return true if the action is performed without errors
     * @throws IllegalArgumentException if the resource is a faith point or if the quantity is negative or if the shelf isn't between 1 and 3
     * @throws NotEnoughSpaceException  if the resources to be added are more than the available space in the shelf
     */
    public boolean addResourceToDepot(ResourceType resourceType, int quantity, int shelf) throws NotEnoughSpaceException, IllegalArgumentException, AlreadyInAnotherShelfException {
        this.depot.addToShelf(resourceType, quantity, shelf);
        return true;
    }

    /**
     * Adds resources to leaderCard Slots in the depot
     *
     * @param resourceType: the resourceType to be added
     * @param quantity:     the quantity of the resource
     * @return true if the action is performed without errors
     * @throws IllegalArgumentException if the resource is a faith point or if the quantity is negative
     * @throws NotEnoughSpaceException  if the resources to be added are more than the available space in the extra slot
     */
    public boolean addResourceToLeader(ResourceType resourceType, int quantity) throws NotEnoughSpaceException, IllegalArgumentException, NoExtraSlotException, FullExtraSlotException {
        this.depot.addToLeader(resourceType, quantity);
        return true;
    }


    /**
     * Removes resources From the selected shelf in the depot
     *
     * @param quantity: the quantity of the resource to be removed
     * @param shelf:    the number of the shelf on which you want to remove the resources
     * @return true if the action is performed without errors
     * @throws IllegalArgumentException if the resource is a faith point or if the quantity is negative or if the shelf isn't between 1 and 3
     */
    public boolean removeResourceFromDepot(int quantity, int shelf) throws IllegalArgumentException, NotEnoughResourcesException {
        this.depot.removeFromDepot(shelf, quantity);
        return true;
    }

    /**
     * Removes the resources from the leaderSlots in the depot
     *
     * @param resourceType: the resourceType to be removed
     * @param quantity:     the quantity of the resource
     * @return true if the action is performed without errors
     * @throws IllegalArgumentException if the resource is a faith point or if the quantity is negative
     */
    public boolean removeResourceFromLeader(ResourceType resourceType, int quantity) throws IllegalArgumentException, NotEnoughResourcesException, NoExtraSlotException {
        this.depot.removeFromLeader(resourceType, quantity);
        return true;
    }

    /**
     * Returns the number of resources of the specified type that are currently in the depot
     *
     * @param resource the resource you want to know the quantity
     * @return the number of resources of the specified type that are currently in the depot
     * @throws IllegalArgumentException if the resource is a faith point
     */
    public int getResourceFromDepot(ResourceType resource) throws IllegalArgumentException {
        return depot.getResourceFromDepot(resource);
    }

    /**
     * Returns the type of the resource that is contained in the specified shelf
     *
     * @param shelf the shelf of which you want to know the type of the contained resources, must be between 1 and 3
     * @return the type of the resource that is contained in the specified shelf
     * @throws IllegalArgumentException if the shelf isn't between 1 and 3
     */
    public ResourceType getResourceTypeFromShelf(int shelf) throws IllegalArgumentException {
        return depot.getShelfType(shelf);
    }

    /**
     * Returns the quantity of the required resource that is in the ExtraSlot of the depot
     * @param resourceType the resource of which you want to know the quantity
     * @return the quantity of the required resource that is in the ExtraSlot of the depot
     */
    public int getLeaderSlotResourceQuantity(ResourceType resourceType){
        return depot.getExtraDepotValue(resourceType);
    }

    /**
     * Returns the maximum amount of resources of the required type that can be stored in the ExtraSlot of the depot
     * @param resourceType the resource of which you want to know the maximum quantity allowed
     * @return  the maximum amount of resources that can be stored in the ExtraSlot of the depot
     */
    public int getLeaderSlotLimitQuantity(ResourceType resourceType){
        return depot.getExtraDepotLimit(resourceType);
    }

    /**
     * Switches the resources between sourceShelf and destShelf
     *
     * @param sourceShelf the number of the first shelf, must be between 1 and 3
     * @param destShelf   the number of the second shelf, must be between 1 and 3
     * @return true if the action is performed without errors
     * @throws NotEnoughSpaceException if the sourceShelf or the destShelf hasn't enough space to store the resources of the other shelf
     */
    public boolean moveBetweenShelves(int sourceShelf, int destShelf) throws NotEnoughSpaceException {
        depot.moveBetweenShelves(sourceShelf, destShelf);
        return true;
    }

    /**
     * Moves the specified amount of resources from the specified shelf of the depot to the ExtraSlot slot in the depot
     * @param shelfNum the number of the shelf from which remove the resources, must be between 1 and 3
     * @param quantity the number of resources to move to the extraSlot
     * @return true if the action is performed without errors
     * @throws NotEnoughSpaceException if there isn't enough space in the extra slot to move the resources
     * @throws NoExtraSlotException if there isn't an active extra slot for that type of resource
     * @throws NotEnoughResourcesException if there aren't enough resources to move from the shelf of the depot
     * @throws FullExtraSlotException if the etra slot is already full of resources
     */
    public boolean moveFromShelfToLeader(int shelfNum, int quantity) throws NotEnoughSpaceException, NoExtraSlotException, NotEnoughResourcesException, FullExtraSlotException {
        depot.moveToLeader(shelfNum, quantity);
        return true;
    }

    /**
     * Moves the specified amount of resources from the extra slot to the specified shelf in the depot
     * @param resource the resources you want to move
     * @param quantity the number of resources to move to the shelf
     * @param shelfNum the number of the shelf in which you want to move the resources
     * @return true if the action is performed without errors
     * @throws NotEnoughSpaceException if there isn't enough space in the specified shelf to contain the specified quantity
     * @throws AlreadyInAnotherShelfException if exists another shelf that contains the same resource
     * @throws NoExtraSlotException if there isn't an extra slot for the specified type of resource
     * @throws NotEnoughResourcesException if there aren't enough resources in the extra slot to move to the shelf
     */
    public boolean moveFromLeaderToShelf(ResourceType resource, int quantity, int shelfNum) throws NotEnoughSpaceException, AlreadyInAnotherShelfException, NoExtraSlotException, NotEnoughResourcesException {
        depot.moveToShelf(resource, quantity, shelfNum);
        return true;
    }

    /**
     * Adds the resources specified in the map to the strongbox
     *
     * @param resMap the map with the resource to add
     * @return true if the action is performed without errors
     */
    public boolean addResourcesToStrongbox(HashMap<ResourceType, Integer> resMap) {
        strongbox.addResource(resMap);
        return true;
    }

    /**
     * Removes the resources specified in the map from the strongbox
     *
     * @param resMap the map with the resources to remove
     * @return true if the action is performed without errors
     * @throws NotEnoughResourcesException if there aren't enough resources to remove
     */
    public boolean removeResourcesFromStrongbox(HashMap<ResourceType, Integer> resMap) throws NotEnoughResourcesException {
        strongbox.removeResource(resMap);
        return true;
    }

    /**
     * Returns the quantity of the specified resource that is in the strongbox
     *
     * @param resource the resource you want to know the quantity
     * @return the quantity of the specified resource that is in the strongbox
     */
    public int getResourceFromStrongbox(ResourceType resource) {
        return strongbox.getResource(resource);
    }


    /*
    ##################
    #FAITHLEVEL METHODS#
    ##################
    */

    //This two following methods are needed to set the the FaithLevel

    /**
     * Sets the FaithTrack the FaithLevel of the player belongs to if it hasn't been set, yet
     *
     * @param faithTrack a FaithTrack
     */
    public void setPlayerFaithLevelFaithTrack(FaithTrack faithTrack) {
        playerFaithLevel.setFaithTrack(faithTrack);
    }

    /**
     * Sets the PopeTiles this FaithLevel of the player has if they haven't been set, yet
     *
     * @param popeTiles a list of PopeTiles
     */
    public void setPlayerFaithLevelPopeTiles(List<PopeTile> popeTiles) {
        playerFaithLevel.setPopeTiles(popeTiles);
    }

    /**
     * Deals with a Vatican Report that's taking place
     *
     * @param reportNum the ReportNum of the current Vatican Report
     * @throws IllegalActionException if the current Vatican Report had already been activated
     */
    public void dealWithVaticanReport(ReportNum reportNum) throws IllegalActionException {
        playerFaithLevel.dealWithVaticanReport(reportNum);
    }

    /**
     * Moves the player's marker of the specified steps
     *
     * @param step the step the player takes on the FaithTrack
     * @throws LastVaticanReportException if the last Vatican Report was activated
     */
    public boolean moveForwardOnFaithTrack(int step) throws LastVaticanReportException {
        return playerFaithLevel.moveFaithMarker(step);
    }

    /**
     * Returns a copy of the player's FaithLevel
     *
     * @return a copy of the player's FaithLevel
     */
    public FaithLevel getFaithLevel() {
        return new FaithLevel(this.playerFaithLevel);
    }

    /**
     * Returns the FaithTrack the player is playing in
     *
     * @return the FaithTrack of the player
     */
    public FaithTrack getFaithTrack() {
        return playerFaithLevel.getFaithTrack();
    }

    /**
     * Returns the position of the player on the FaithTrack
     *
     * @return the position of the player on the FaithTrack
     */
    public int getPositionOnFaithTrack() {
        return this.playerFaithLevel.getPosition();
    }

    /**
     * Returns the points the player gets from where they stand on the FaithTrack
     *
     * @return the victory points due to the FaithTrack
     */
    public int getCellPoints() {
        return this.playerFaithLevel.getCellPoints();
    }

    /**
     * Returns the points the player gets from the PopeTiles they have
     *
     * @return the points due to active PopeTiles
     */
    public int getPopeTilesPoints() {
        return this.playerFaithLevel.getPopeTilesPoints();
    }
    /*
    ####################
    #PRODUCTION METHODS#
    ####################
    */

    /**
     * Checks if parameters can activate production and if they are ok the production is added to the strongBox and FaithPoints move forward faithtrack;
     *
     * @param alsoBaseProduction       is true is BaseProduction must produce
     * @param ProductiveDevCards       is the Collection of devCards owned, which will produces resources.
     * @param productiveLeaderCardsMap is the Map of LeaderCards active, which will produces extra resources. the values represents the desired extra resource, can't be a faithpoint
     * @return true if everything went well
     * @throws NullPointerException     if one parameter is null
     * @throws IllegalArgumentException if ProductiveDevCards contains one or more not Usable dev Cards or if one or more productiveLeaderCardsMap key is not one the owned active leaderCards or the desired production for a leader card is faithpoint
     */
    public boolean ActivateProduction(Collection<DevCard> ProductiveDevCards, HashMap<LeaderCard, ResourceType> productiveLeaderCardsMap, boolean alsoBaseProduction) throws NullPointerException, IllegalArgumentException, LastVaticanReportException {
        Collection<DevCard> usableDevCards;
        List<LeaderCard> activeLeaderCards;
        HashMap<ResourceType, Integer> productionMap;
        Set<LeaderCard> productiveLeaderCards;

        if (ProductiveDevCards == null || productiveLeaderCardsMap == null)
            throw new NullPointerException("devCards or leaderCards are null");

        usableDevCards = this.devSlots.getUsableDevCards();
        if (!usableDevCards.containsAll(ProductiveDevCards))
            throw new IllegalArgumentException("One or more devCards are not activatable cards in the Slot");

        productiveLeaderCards = productiveLeaderCardsMap.keySet();
        activeLeaderCards = this.leaderCards.getActiveCards();
        if (!activeLeaderCards.containsAll(productiveLeaderCards))
            throw new IllegalArgumentException("One or more leaderCards are not activated player cards");
        if (productiveLeaderCards.stream().anyMatch(card -> productiveLeaderCardsMap.get(card).isFaithPoint()))
            throw new IllegalArgumentException("A leader card can't produce faithPoints");

        List<HashMap<ResourceType, Integer>> devCardsMaps = ProductiveDevCards.stream().map(DevCard::getProductionOutput).collect(Collectors.toList());
        List<HashMap<ResourceType, Integer>> leaderCardsMaps = productiveLeaderCards.stream().map(leaderCard -> leaderCard.getEffect().extraProductionEffect(productiveLeaderCardsMap.get(leaderCard))).collect(Collectors.toList());
        devCardsMaps.addAll(leaderCardsMaps);
        if (alsoBaseProduction)
            devCardsMaps.add(this.baseProduction.getOutputHashMap());

        productionMap = this.sumReduceHashMaps2HashMap(devCardsMaps);
        int numberOfProducedFaithPoints = productionMap.getOrDefault(ResourceType.FAITHPOINT, 0);
        if (numberOfProducedFaithPoints != 0) {
            productionMap.remove(ResourceType.FAITHPOINT);
            this.playerFaithLevel.moveFaithMarker(numberOfProducedFaithPoints);
        }
        strongbox.addResource(productionMap);
        return true;
    }

    /**
     * checks if these cards can produces resources and return the total cost to activate production with these cards
     *
     * @param ProductiveDevCards    is the Collection of devCards owned, which will produces resources.
     * @param productiveLeaderCards is the Collection of leaderCards active, which ypu want to activate the production
     * @param alsoBaseProduction    if you want to activate base production
     * @return the total cost to activate these cards
     * @throws NullPointerException     if one or more parameter is null
     * @throws IllegalArgumentException if ProductiveDevCards contains one or more not Usable dev Cards or if one or more productiveLeaderCard is not in active leaderCards
     * @throws IllegalActionException   if the cost is bigger than the total resources owned.
     */
    public HashMap<ResourceType, Integer> getProductionCost(Collection<DevCard> ProductiveDevCards, Collection<LeaderCard> productiveLeaderCards, boolean alsoBaseProduction) throws NullPointerException, IllegalArgumentException, IllegalActionException {
        Collection<DevCard> usableDevCards;
        List<LeaderCard> activeLeaderCards;

        if (ProductiveDevCards == null || productiveLeaderCards == null)
            throw new NullPointerException("devCards or leaderCards are null");

        usableDevCards = this.devSlots.getUsableDevCards();
        if (!usableDevCards.containsAll(ProductiveDevCards))
            throw new IllegalArgumentException("One or more devCards are not activatable cards in the Slot");

        activeLeaderCards = this.leaderCards.getActiveCards();
        if (!activeLeaderCards.containsAll(productiveLeaderCards))
            throw new IllegalArgumentException("One or more leaderCards are not activated player cards");

        List<HashMap<ResourceType, Integer>> devCardsMaps = ProductiveDevCards.stream().map(DevCard::getProductionInput).collect(Collectors.toList());
        List<HashMap<ResourceType, Integer>> leaderCardsMaps = productiveLeaderCards.stream().map(leaderCard -> leaderCard.getEffect().getRequiredInput()).collect(Collectors.toList());
        devCardsMaps.addAll(leaderCardsMaps);
        if (alsoBaseProduction)
            devCardsMaps.add(this.baseProduction.getInputHashMap());

        HashMap<ResourceType, Integer> costMap = this.sumReduceHashMaps2HashMap(devCardsMaps);
        Collection<ResourceType> costKeys = costMap.keySet();
        HashMap<ResourceType, Integer> allResources = this.getAllResources();
        if (costKeys.stream().anyMatch(key -> costMap.getOrDefault(key, 0) > allResources.getOrDefault(key, 0)))
            throw new IllegalActionException("playerboard_getProductionCost:there is not enough resources to activate these cards");
        return costMap;


    }


    private HashMap<ResourceType, Integer> sumReduceHashMaps2HashMap(List<HashMap<ResourceType, Integer>> hashMaps) {
        if (hashMaps == null) throw new NullPointerException("sumReduceHashMaps2HashMap: hashMaps are null");
        HashMap<ResourceType, Integer> finalMap = new HashMap<>();
        Set<ResourceType> actualKeys;
        for (HashMap<ResourceType, Integer> actualMap : hashMaps) {
            actualKeys = actualMap.keySet();
            actualKeys.stream()
                    .distinct()
                    .forEach(key -> finalMap.put(key, finalMap.getOrDefault(key, 0) + actualMap.get(key)));
        }
        return finalMap;
    }

    /**
     * this class sets Parameters in input HashMap and output HashMap which represents the input Cost of the BaseProduction and the output production.
     * Also performs analysis to see if parameters are can be valid for this configuration Base Production
     *
     * @param inputResources  the List which represents the desired cost HashMap
     * @param outputResources the List which represents the desired output HashMap
     * @return true if the lists are Valid for this BaseProduction
     * @throws IllegalArgumentException if one or more parameters are not Valid
     * @throws NullPointerException     if inputResources or outputResources are null
     */
    public boolean SetBaseProduction(List<ResourceType> inputResources, List<ResourceType> outputResources) throws IllegalArgumentException, NullPointerException{
        return this.baseProduction.setBaseProduction(inputResources,outputResources);
    }

}
