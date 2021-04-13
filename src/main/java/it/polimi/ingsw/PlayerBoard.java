package it.polimi.ingsw;

import it.polimi.ingsw.DevCards.DevCard;
import it.polimi.ingsw.DevCards.DevSlot;
import it.polimi.ingsw.DevCards.DevSlots;
import it.polimi.ingsw.FaithTrack.FaithLevel;
import it.polimi.ingsw.FaithTrack.FaithTrack;
import it.polimi.ingsw.FaithTrack.PopeTile;
import it.polimi.ingsw.FaithTrack.ReportNum;
import it.polimi.ingsw.LeaderCard.LeaderCard;
import it.polimi.ingsw.LeaderCard.LeaderCards;
import it.polimi.ingsw.LeaderCard.leaderEffects.Effect;
import it.polimi.ingsw.LeaderCardRequirementsTests.Requirement;
import it.polimi.ingsw.exceptions.IllegalActionException;
import it.polimi.ingsw.exceptions.LastVaticanReportException;
import it.polimi.ingsw.exceptions.NotEnoughSpaceException;
import it.polimi.ingsw.exceptions.UnmetRequirementException;

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
     * Discards the specified LeaderCard from the not-played LeaderCards the player holds
     * @param leaderCard a LeaderCard to be discarded
     * @throws IllegalArgumentException if the card can't be discarded
     * @throws LastVaticanReportException if the last Vatican Report was activated
     */
    public void discardLeaderCard(LeaderCard leaderCard) throws IllegalArgumentException, LastVaticanReportException {
        HashMap<ResourceType, Integer> outputWhenDiscarded = this.leaderCards.discardLeaderCard(leaderCard);
        this.moveForwardOnFaithTrack(outputWhenDiscarded.get(ResourceType.FAITHPOINT));
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
     * @throws IllegalActionException   if the resource to be added is already in the depot in another position
     * @throws NotEnoughSpaceException  if the resources to be added are more than the available space in the shelf
     */
    public boolean addResourceToDepot(ResourceType resourceType, int quantity, int shelf) throws IllegalActionException, NotEnoughSpaceException, IllegalArgumentException {
        this.depot.addToShelf(resourceType,quantity,shelf);
        return true;
    }

    /**Adds resources to leaderCard Slots in the depot
     * @param resourceType: the resourceType to be added
     * @param quantity: the quantity of the resource
     * @return true if the action is performed without errors
     * @throws IllegalArgumentException if the resource is a faith point or if the quantity is negative
     * @throws IllegalActionException   if the resource to remove doesn't have an extra slot and if the extra slot is already full of resources
     * @throws NotEnoughSpaceException  if the resources to be added are more than the available space in the extra slot
     */
    public boolean addResourceToDepot(ResourceType resourceType, int quantity) throws IllegalActionException, NotEnoughSpaceException, IllegalArgumentException {
        this.depot.addToLeader(resourceType,quantity);
        return true;
    }


    /**Removes resources From the selected shelf in the depot
     * @param quantity: the quantity of the resource to be removed
     * @param shelf: the number of the shelf on which you want to remove the resources
     * @return true if the action is performed without errors
     * @throws IllegalArgumentException if the resource is a faith point or if the quantity is negative or if the shelf isn't between 1 and 3
     * @throws IllegalActionException   f there aren't enough resources to be removed from the depot
     */
    public boolean removeResourceFromDepot(int quantity, int shelf) throws IllegalActionException, IllegalArgumentException {
        this.depot.removeFromDepot(shelf, quantity);
        return true;
    }

    /**Removes the resources from the leaderSlots in the depot
     * @param resourceType: the resourceType to be removed
     * @param quantity: the quantity of the resource
     * @return true if the action is performed without errors
     * @throws IllegalArgumentException if the resource is a faith point or if the quantity is negative
     * @throws IllegalActionException if the resource to remove doesn't have an extra slot
     */
    public boolean removeResourceFromDepot(ResourceType resourceType, int quantity) throws IllegalActionException, IllegalArgumentException {
        this.depot.removeFromLeader(resourceType,quantity);
        return true;
    }


    /*
    public boolean activateExtraLeaderCard(LeaderCard leaderCard){
        Effect effect = leaderCard.getEffect();
        this.depot.addExtraSlot(leaderCard.getEffect());
        return true;
    }
    */

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
