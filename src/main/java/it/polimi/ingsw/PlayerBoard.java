package it.polimi.ingsw;

import it.polimi.ingsw.DevCards.DevCard;
import it.polimi.ingsw.DevCards.DevSlot;
import it.polimi.ingsw.DevCards.DevSlots;
import it.polimi.ingsw.FaithTrack.*;
import it.polimi.ingsw.LeaderCard.LeaderCard;
import it.polimi.ingsw.LeaderCard.LeaderCards;
import it.polimi.ingsw.LeaderCard.leaderEffects.Effect;
import it.polimi.ingsw.LeaderCardRequirementsTests.Requirement;
import it.polimi.ingsw.exceptions.*;

import java.net.ResponseCache;
import java.util.*;

public class PlayerBoard {
    private FaithLevel playerFaithLevel;
    private final Depot depot;
    private final Strongbox strongbox;
    private final DevSlots devSlots;
    private final LeaderCards leaderCards;

    public PlayerBoard(List<LeaderCard> leaderCards) {
        this.playerFaithLevel = new FaithLevel();
        this.depot = new Depot();
        this.strongbox = new Strongbox();
        this.devSlots = new DevSlots();
        this.leaderCards = new LeaderCards(leaderCards);
    }

    public PlayerBoard() {
        this.playerFaithLevel = new FaithLevel();
        this.depot = new Depot();
        this.strongbox = new Strongbox();
        this.devSlots = new DevSlots();
        this.leaderCards = new LeaderCards();
    }


    public HashMap<ResourceType,Integer> getAllResources(){
        HashMap<ResourceType,Integer> depotMap = depot.getAllResources();
        HashMap<ResourceType,Integer> strongBoxMap = strongbox.getAllResources();
        Set<ResourceType> depotKeys = depotMap.keySet();
        Set<ResourceType> strongboxKeys = depotMap.keySet();
        Set<ResourceType> keys = new TreeSet<>();
        keys.addAll(depotKeys);
        keys.addAll(strongboxKeys);
        HashMap<ResourceType,Integer> allResourcesMap =new HashMap<>();
        keys.stream()
                .distinct()
                .forEach(key -> allResourcesMap.put(key,depotMap.getOrDefault(key,0) + strongBoxMap.getOrDefault(key,0)));
        return allResourcesMap;
    }

    public Boolean isCardBuyable(DevCard devCard){
        if (devCard==null) throw new NullPointerException("isCardBuyable: DevCardCan't be null");
        HashMap<ResourceType,Integer> cost = devCard.getCost();
        Collection<ResourceType> costKeys = cost.keySet();
        HashMap<ResourceType,Integer> allResources = getAllResources();
        return costKeys.stream().allMatch(key -> cost.getOrDefault(key,0) <= allResources.getOrDefault(key,0));
    }

    /**
     * Gets A Collection containing all the DevCards in the Slots.
     * @return a collection of Cards
     */
    public Collection<DevCard> getAllDevCards(){
        return devSlots.getAllDevCards();
    }

    /*
    #####################
    #LEADERCARDS METHODS#
    #####################
    */
    public void setNotPlayedLeaderCards(List<LeaderCard> notPlayedLeaderCards){
        List<LeaderCard> clone = new ArrayList<>();
        for(LeaderCard lD: notPlayedLeaderCards)
            clone.add(new LeaderCard(lD));
        this.leaderCards.setNotPlayedCards(clone);
    }

    /**
     * Activates the specified LeaderCard if the player meets all the requirements
     * @param leaderCard the LeaderCard to be activated
     * @return true if the card was activated
     * @throws UnmetRequirementException if the player doesn't meet the requirements
     */
    public boolean activateLeaderCard(LeaderCard leaderCard) throws UnmetRequirementException {
        PlayerResourcesAndCards playerResourcesAndCards= new PlayerResourcesAndCards(getAllResources(),getAllDevCards());
        return leaderCards.activateLeaderCard(leaderCard,playerResourcesAndCards);
    }

    /**
     * Discards the specified LeaderCard from the not-played LeaderCards the player holds and lets the player have some benefits from it
     * @param leaderCard a LeaderCard to be discarded
     * @throws IllegalArgumentException if the card can't be discarded
     * @throws LastVaticanReportException if the last Vatican Report was activated
     */
    public void discardLeaderCard(LeaderCard leaderCard) throws IllegalArgumentException, LastVaticanReportException {
        HashMap<ResourceType, Integer> outputWhenDiscarded = this.leaderCards.discardLeaderCard(leaderCard);
        this.moveForwardOnFaithTrack(outputWhenDiscarded.get(ResourceType.FAITHPOINT));
    }

    /**
     * Discards the specified LeaderCard from the not-played LeaderCards the player holds and doesn't give the player any benefits. This method is
     * used at the configuration of the game when the player is given a certain amount of LeaderCards but they can't keel all of them
     * @param leaderCard a LeaderCard to be discarded
     * @throws IllegalArgumentException if the card can't be discarded
     */
    public void discardLeaderCardAtTheBeginning(LeaderCard leaderCard) throws IllegalArgumentException{
        this.leaderCards.discardLeaderCard(leaderCard);
    }

    /**
     * Returns the effect of the specified LeaderCard if the player can use such card in the game
     * @param leaderCard a LeaderCard
     * @return the effect of such LeaderCard
     * @throws IllegalArgumentException if the player doesn't hold such card or if they haven't activated it, yet
     * @throws IllegalActionException if the player tries to get the effect of a one-shot card they have already used once
     */
    public Effect getEffectFromCard(LeaderCard leaderCard) throws IllegalArgumentException, IllegalActionException {
        return this.leaderCards.getEffectFromCard(leaderCard);
    }

    /**
     * Returns a copy of all the LeaderCards the player has not played yet
     * @return a copy of the not-played LeaderCards
     */
    public List<LeaderCard> getNotPlayedLeaderCards() {
        return this.leaderCards.getNotPlayedCards();
    }

    public List<LeaderCard> getActiveLeaderCards(){
        return this.leaderCards.getActiveCards();
    }

    public List<LeaderCard> getAlreadyUsedOneShotCard(){return this.leaderCards.getAlreadyUsedOneShotCard();}

    public List<Requirement> getLeaderCardRequirements(LeaderCard leaderCard) throws IllegalArgumentException{
        return this.leaderCards.getLeaderCardRequirements(leaderCard);
    }

    /**
     * Returns the points the player gets from their LeaderCards
     * @return the points of all the active LeaderCards the player has
     */
    public int getLeaderCardsPoints(){
        return this.leaderCards.getLeaderCardsPoints();
    }

    /**
     * Returns whether the player holds the specified LeaderCard
     * @param leaderCard a LeaderCard
     * @return true if the card is active, false if the card is not played, yet
     * @throws IllegalArgumentException if the player doesn't hold the card
     */
    public boolean isLeaderCardActive(LeaderCard leaderCard) throws IllegalArgumentException{
        return leaderCards.isLeaderCardActive(leaderCard);
    }

    /*
    ##################
    #DEvCARDS METHODS#
    ##################
    */
    /**Adds one devCard to the selected DevSlot according to GameRules
     * @param index is the DevSlot number starting from 0
     * @param devCard id DevCard to be added, can't be in one of the DevSlots
     * @return true if the DevCard is added in the desired Slot
     * @throws IndexOutOfBoundsException if index is not valid: must be between 0 and 2
     * @throws NullPointerException if devCard is null
     * @throws IllegalArgumentException if this card can't be added in the desiredSlot
     */
    public boolean addCardToDevSlot(int index, DevCard devCard) throws IndexOutOfBoundsException, NullPointerException, IllegalArgumentException{
        devSlots.addDevCard(index,devCard);
        return true;
    }

    /*
    ##################
    #DEPOT METHODS#
    ##################
    */
    /**Adds resources to the selected shelf in the depot
     * @param resourceType: the resourceType to be added
     * @param quantity: the quantity of the resource
     * @param shelf: the number of the shelf on which you want to add the resource
     * @return true if the action is performed without errors
     * @throws IllegalArgumentException if the resource is a faith point or if the quantity is negative or if the shelf isn't between 1 and 3
     * @throws NotEnoughSpaceException  if the resources to be added are more than the available space in the shelf
     */
    public boolean addResourceToDepot(ResourceType resourceType, int quantity, int shelf) throws NotEnoughSpaceException, IllegalArgumentException, AlreadyInAnotherShelfException {
        this.depot.addToShelf(resourceType,quantity,shelf);
        return true;
    }

    /**Adds resources to leaderCard Slots in the depot
     * @param resourceType: the resourceType to be added
     * @param quantity: the quantity of the resource
     * @return true if the action is performed without errors
     * @throws IllegalArgumentException if the resource is a faith point or if the quantity is negative
     * @throws NotEnoughSpaceException  if the resources to be added are more than the available space in the extra slot
     */
    public boolean addResourceToLeader(ResourceType resourceType, int quantity) throws NotEnoughSpaceException, IllegalArgumentException, NoExtraSlotException, FullExtraSlotException {
        this.depot.addToLeader(resourceType,quantity);
        return true;
    }


    /**Removes resources From the selected shelf in the depot
     * @param quantity: the quantity of the resource to be removed
     * @param shelf: the number of the shelf on which you want to remove the resources
     * @return true if the action is performed without errors
     * @throws IllegalArgumentException if the resource is a faith point or if the quantity is negative or if the shelf isn't between 1 and 3
     */
    public boolean removeResourceFromDepot(int quantity, int shelf) throws IllegalArgumentException, NotEnoughResourcesException {
        this.depot.removeFromDepot(shelf, quantity);
        return true;
    }

    /**Removes the resources from the leaderSlots in the depot
     * @param resourceType: the resourceType to be removed
     * @param quantity: the quantity of the resource
     * @return true if the action is performed without errors
     * @throws IllegalArgumentException if the resource is a faith point or if the quantity is negative
     */
    public boolean removeResourceFromLeader(ResourceType resourceType, int quantity) throws IllegalArgumentException, NotEnoughResourcesException, NoExtraSlotException {
        this.depot.removeFromLeader(resourceType,quantity);
        return true;
    }

    /**
     * Returns the number of resources of the specified type that are currently in the depot
     * @param resource the resource you want to know the quantity
     * @return the number of resources of the specified type that are currently in the depot
     * @throws IllegalArgumentException if the resource is a faith point
     */
    public int getResourceFromDepot(ResourceType resource)throws IllegalArgumentException{
        return depot.getResourceFromDepot(resource);
    }

    /**
     * Returns the type of the resource that is contained in the specified shelf
     * @param shelf the shelf of which you want to know the type of the contained resources, must be between 1 and 3
     * @return the type of the resource that is contained in the specified shelf
     * @throws IllegalArgumentException if the shelf isn't between 1 and 3
     */
    public ResourceType getResourceTypeFromShelf(int shelf) throws IllegalArgumentException{
        return depot.getShelfType(shelf);
    }

    /**
     * Switches the resources between sourceShelf and destShelf
     * @param sourceShelf the number of the first shelf, must be between 1 and 3
     * @param destShelf the number of the second shelf, must be between 1 and 3
     * @return true if the action is performed without errors
     * @throws NotEnoughSpaceException if the sourceShelf or the destShelf hasn't enough space to store the resources of the other shelf
     */
    public boolean moveBetweenShelves(int sourceShelf, int destShelf) throws NotEnoughSpaceException {
        depot.moveBetweenShelves(sourceShelf, destShelf);
        return true;
    }

    public boolean moveFromShelfToLeader(int shelfNum, int quantity) throws NotEnoughSpaceException, NoExtraSlotException, NotEnoughResourcesException, FullExtraSlotException {
        depot.moveToLeader(shelfNum, quantity);
        return true;
    }

    public boolean moveFromLeaderToShelf(ResourceType resource, int quantity, int shelfNum) throws NotEnoughSpaceException, AlreadyInAnotherShelfException, NoExtraSlotException, NotEnoughResourcesException {
        depot.moveToShelf(resource, quantity, shelfNum);
        return true;
    }

    /**
     * Addds the resources specified in the map to the strongbox
     * @param resMap the map with the resource to add
     * @return true if the action is performed without errors
     */
    public boolean addResourcesToStrongbox(HashMap<ResourceType, Integer> resMap) {
        strongbox.addResource(resMap);
        return true;
    }

    /**
     * Removes the resources specified in the map from the strongbox
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
     * @param resource the resource you want to know the quantity
     * @return the quantity of the specified resource that is in the strongbox
     */
    public int getResourceFromStrongbox(ResourceType resource){
        return strongbox.getResource(resource);
    }

    public boolean activateExtraLeaderCard(LeaderCard leaderCard) throws FullExtraSlotException {
        Effect effect = leaderCard.getEffect();
        this.depot.addExtraSlot(leaderCard.getEffect());
        return true;
    }


    /*
    ##################
    #FAITHLEVEL METHODS#
    ##################
    */

    //This two following methods are needed to set the the FaithLevel

    /**
     * Sets the FaithTrack the FaithLevel of the player belongs to if it hasn't been set, yet
     * @param faithTrack a FaithTrack
     */
    public void setPlayerFaithLevelFaithTrack(FaithTrack faithTrack){
        playerFaithLevel.setFaithTrack(faithTrack);
    }

    /**
     * Sets the PopeTiles this FaithLevel of the player has if they haven't been set, yet
     * @param popeTiles a list of PopeTiles
     */
    public void setPlayerFaithLevelPopeTiles(List<PopeTile> popeTiles){
        playerFaithLevel.setPopeTiles(popeTiles);
    }

    /**
     * Deals with a Vatican Report that's taking place
     * @param reportNum the ReportNum of the current Vatican Report
     * @throws IllegalActionException if the current Vatican Report had already been activated
     */
    public void dealWithVaticanReport(ReportNum reportNum) throws IllegalActionException{
        playerFaithLevel.dealWithVaticanReport(reportNum);
    }


    public int calculateVP(){
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
        for(ResourceType res: strongboxRes.keySet()){
            totalResources += depotRes.get(res) + strongboxRes.get(res);
        }
        vp += totalResources/5;

        return vp;
    }

    /**
     * Moves the player's marker of the specified steps
     * @param step the step the player takes on the FaithTrack
     * @throws LastVaticanReportException if the last Vatican Report was activated
     */
    public boolean moveForwardOnFaithTrack(int step) throws LastVaticanReportException{
        return playerFaithLevel.moveFaithMarker(step);
    }

    /**
     * Returns a copy of the player's FaithLevel
     * @return a copy of the player's FaithLevel
     */
    public FaithLevel getFaithLevel(){
        return new FaithLevel(this.playerFaithLevel);
    }

    /**
     * Returns the FaithTrack the player is playing in
     * @return the FaithTrack of the player
     */
    public FaithTrack getFaithTrack(){
        return playerFaithLevel.getFaithTrack();
    }

    /**
     * Returns the position of the player on the FaithTrack
     * @return the position of the player on the FaithTrack
     */
    public int getPositionOnFaithTrack(){
        return this.playerFaithLevel.getPosition();
    }

    /**
     * Returns the points the player gets from where they stand on the FaithTrack
     * @return the victory points due to the FaithTrack
     */
    public int getCellPoints(){
        return this.playerFaithLevel.getCellPoints();
    }

    /**
     * Returns the points the player gets from the PopeTiles they have
     * @return the points due to active PopeTiles
     */
    public int getPopeTilesPoints(){
        return this.playerFaithLevel.getPopeTilesPoints();
    }
}
