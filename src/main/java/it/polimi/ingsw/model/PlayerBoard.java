package it.polimi.ingsw.model;

import it.polimi.ingsw.exceptions.*;
import it.polimi.ingsw.model.DevCards.DevCard;
import it.polimi.ingsw.model.DevCards.DevSlots;
import it.polimi.ingsw.model.FaithTrack.FaithLevel;
import it.polimi.ingsw.model.FaithTrack.FaithTrack;
import it.polimi.ingsw.model.FaithTrack.PopeTile;
import it.polimi.ingsw.model.FaithTrack.ReportNum;
import it.polimi.ingsw.model.LeaderCard.LeaderCard;
import it.polimi.ingsw.model.LeaderCard.LeaderCardRequirements.Requirement;
import it.polimi.ingsw.model.LeaderCard.LeaderCards;
import it.polimi.ingsw.model.LeaderCard.leaderEffects.Effect;

import java.util.*;
import java.util.stream.Collectors;

public class PlayerBoard {
    private final FaithLevel playerFaithLevel;
    private final Depot depot;
    private final Strongbox strongbox;
    private final DevSlots devSlots;
    private final LeaderCards leaderCards;
    private final BaseProduction baseProduction;

    /**
     * Returns a copy of the player's FaithLevel
     *
     * @return a copy of the player's FaithLevel
     */
    public FaithLevel getFaithLevel() {
        return new FaithLevel(this.playerFaithLevel);
    }

    public DevSlots getDevSlots() {
        return new DevSlots(this.devSlots);
    }


    /**
     * Sum all resources owned by the player
     *
     * @return the Sum of all resources owned by the player
     */
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
     * Compares the cost of the passed DevCard with all resources owned
     *
     * @param devCard is the DevCard to be checked
     * @return true if devCard can be bought
     * @throws NullPointerException if devCard is Null
     */
    public Boolean isCardBuyable(DevCard devCard) {
        if (devCard == null) throw new NullPointerException("isCardBuyable: DevCardCan't be null");
        HashMap<ResourceType, Integer> cost = devCard.getCost();
        Collection<ResourceType> costKeys = cost.keySet();
        HashMap<ResourceType, Integer> allResources = getAllResources();
        return costKeys.stream().allMatch(key -> cost.getOrDefault(key, 0) <= allResources.getOrDefault(key, 0));
    }


    /**
     * Calculate the actual Victory Points of the Player
     *
     * @return the actual Victory Points
     */
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
            totalResources += depotRes.getOrDefault(res, 0) + strongboxRes.getOrDefault(res, 0);
        }
        vp += totalResources / 5;

        return vp;
    }

    /**
     * returns the amount of victory points, excluding the ones coming from the amount of resources in Depot and Strongbox
     * @return the amount of victory points, excluding the ones coming from the amount of resources in Depot and Strongbox
     */
    public int partialVictoryPoints(){
        int vp = 0;

        vp += devSlots.getPoints();
        vp += playerFaithLevel.getCellPoints();
        vp += playerFaithLevel.getPopeTilesPoints();
        vp += leaderCards.getLeaderCardsPoints();

        return vp;
    }


    /*
    #####################
    #LEADERCARDS METHODS#
    #####################
    */


    /**
     * Sets the LeaderCards this player has at the beginning of the game
     *
     * @param notPlayedLeaderCards this player's LeaderCards
     */
    public void setNotPlayedLeaderCardsAtGameBeginning(List<LeaderCard> notPlayedLeaderCards) {
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
     * @throws IllegalActionException if the player doesn't meet all the requirements in order to activate the specified card or the ExtraSlot effect of the card cannot be applied
     * @throws IllegalArgumentException if the LeaderCard is a null pointer
     */
    public boolean activateLeaderCard(LeaderCard leaderCard) throws IllegalActionException, IllegalArgumentException {
        try {
            if (leaderCard == null) throw new IllegalArgumentException("LeaderCard can't be null");

            PlayerResourcesAndCards playerResourcesAndCards = new PlayerResourcesAndCards(getAllResources(), devSlots.getAllDevCards());
            if (this.leaderCards.activateLeaderCard(leaderCard, playerResourcesAndCards)) {
                if (this.leaderCards.addCardToActivatedOneShotCards(leaderCard))
                    this.depot.addExtraSlot(leaderCard.getEffect());
                return true;
            }
            return false;
        } catch (UnmetRequirementException | FullExtraSlotException e){
            throw new IllegalActionException(e.getMessage());
        }
    }

    /**
     * Discards the specified LeaderCard from the not-played LeaderCards the player holds and lets the player have some benefits from it
     *
     * @param leaderCard a LeaderCard to be discarded
     * @throws IllegalArgumentException   if the card can't be discarded
     * @throws LastVaticanReportException if the last Vatican Report was activated
     */
    public boolean discardLeaderCard(LeaderCard leaderCard) throws IllegalArgumentException, LastVaticanReportException {
        HashMap<ResourceType, Integer> outputWhenDiscarded = this.leaderCards.discardLeaderCard(leaderCard);
        //By default, when the player discards a LeaderCard they get one FaithPoint
        this.moveForwardOnFaithTrack(outputWhenDiscarded.get(ResourceType.FAITHPOINT));
        return true;
    }

    /**
     * Discards the specified LeaderCards from the not-played LeaderCards the player holds and doesn't give the player any benefits. This method is
     * used at the configuration of the game when the player is given a certain amount of LeaderCards but they can't keep all of them
     *
     * @param cards the LeaderCards to be discarded
     * @throws IllegalActionException   if the player wants to discard more cards than they have
     * @throws IllegalArgumentException if one card can't be discarded
     */
    public void discardLeaderCardsAtTheBeginning(List<LeaderCard> cards) throws IllegalActionException, IllegalArgumentException {
        if (cards.size() > this.leaderCards.getNotPlayedCards().size())
            throw new IllegalActionException("There are not enough cards to discard!");
        for (LeaderCard lC : cards)
            this.leaderCards.discardLeaderCard(lC);
    }

    /**
     * Returns the effect of the specified LeaderCard if the player can use such card in the game
     *
     * @param leaderCard a LeaderCard
     * @return the effect of such LeaderCard
     * @throws IllegalArgumentException if the player doesn't hold such card or if they haven't activated it, yet
     * @throws IllegalActionException   if the player tries to get the effect of a one-shot card they have already used once
     */
    @Deprecated
    public Effect getEffectFromCard(LeaderCard leaderCard) throws IllegalArgumentException, IllegalActionException {
        return this.leaderCards.getEffectFromCard(leaderCard);
    }

    /**
     * Returns a list of effects whose active LeaderCard is specified via the position they have in the PlayerBoard
     *
     * @param cardIndexes the list of indexes of some LeaderCards (indexes are non-negative integers)
     * @return the effects of the specified LeaderCards
     * @throws IllegalArgumentException if one of the specified indexes is out of bound
     */
    public List<Effect> getEffectsFromCards(List<Integer> cardIndexes) throws IllegalArgumentException {
        List<Effect> result = new ArrayList<>();
        for (Integer i : cardIndexes)
            result.add(leaderCards.getEffectFromCard(i));

        return result;
    }

    /**
     * Returns a list of LeaderCards who are specified via their position they have in the PlayerBoard
     * @param cardIndexes the list of indexes of some LeaderCards (indexes are non-negative integers)
     * @return the list of the desired LeaderCards
     * @throws IllegalArgumentException if one of the specified indexes is out of bound
     */
    public List<LeaderCard> getNotPlayedLeaderCardsFromIndex(List<Integer> cardIndexes) throws IllegalArgumentException {
        List<LeaderCard> result = new ArrayList<>();
        for (Integer i : cardIndexes)
            result.add(leaderCards.getNotPlayedLeaderCardFromIndex(i));

        return result;
    }

    /**
     * Returns the not played LeaderCard whose position it holds in the PlayerBoard is specified as a parameter
     * @param cardIndex the position inside the ordered collection of not player LeaderCards of the desired LeaderCard (it is a non-negative integer)
     * @return the desired not played LeaderCard
     * @throws IllegalArgumentException if the given index is out of bound
     */
    public LeaderCard getNoPlayedLeaderCardFromIndex(int cardIndex) throws IllegalArgumentException {
        return this.leaderCards.getNotPlayedLeaderCardFromIndex(cardIndex);
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

    @Deprecated
    public List<Requirement> getLeaderCardRequirements(LeaderCard leaderCard) throws IllegalArgumentException {
        return this.leaderCards.getLeaderCardRequirements(leaderCard);
    }

    /**
     * Returns the points the player gets from their LeaderCards
     *
     * @return the points of all the active LeaderCards the player has
     */
    @Deprecated
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
    @Deprecated
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
     * @throws IllegalArgumentException if index is not valid: must be between 0 and 2
     * @throws IllegalArgumentException if devCard is null
     * @throws IllegalArgumentException if this card can't be added in the desiredSlot
     * @throws EndOfGameException if the player adds the 7th card in the slots
     */
    public boolean addCardToDevSlot(int index, DevCard devCard) throws IllegalArgumentException, EndOfGameException {
        try {
            if (this.devSlots.addDevCard(index, devCard)) {
                if (this.devSlots.getAllDevCards().size() == 7)
                    throw new EndOfGameException("Added the seventh card in devSlots; it's the last turn");
                return true;
            }
        } catch (IndexOutOfBoundsException | NullPointerException e) {
            throw new IllegalArgumentException(e.getMessage());
        }
        return false;
    }

    public DevCard getUsableDevCardFromDevSlotIndex(int index){
        return devSlots.getDevSlot(index).getLastDevCard();
    }

    /**
     * Returns a collection containing all the DevCards in the this player's slots.
     *
     * @return a collection of Cards
     */
    public Collection<DevCard> getAllDevCards() {
        return devSlots.getAllDevCards();
    }



    /*
    #################
    #DEPOT METHODS#
    ##################
    */

    /**
     * Returns the number of resources on the specified shelf
     * @param index the index of the shelf (must be between 1 and 3)
     * @return the number of resources in the specified shelf
     */
    public int getNumberOfResInShelf(int index){
        return this.depot.getNumberOfResOnShelf(index);
    }

    /**
     * Adds resources to the selected shelf in the depot
     *
     * @param resourceType: the resourceType to be added
     * @param quantity:     the quantity of the resource
     * @param shelf:        the number of the shelf on which you want to add the resource
     * @return true if the action is performed without errors
     * @throws IllegalArgumentException if the inputs the player gives are not suitable for the game
     * @throws IllegalActionException   if what the player wants to do is not suitable for the game
     */
    public boolean addResourceToDepot(ResourceType resourceType, int quantity, int shelf) throws IllegalArgumentException, IllegalActionException {
        try {
            return this.depot.addToShelf(resourceType, quantity, shelf);
        } catch (AlreadyInAnotherShelfException | NotEnoughSpaceException e) {
            throw new IllegalActionException(e.getMessage());
        }
    }

    /**
     * Adds resources to leaderCard Slots in the depot
     *
     * @param resourceType: the resourceType to be added
     * @param quantity:     the quantity of the resource
     * @return true if the action is performed without errors
     * @throws IllegalArgumentException if the inputs the player gives are not suitable for the game
     * @throws IllegalActionException   if what the player wants to do is not suitable for the game
     */
    public boolean addResourceToLeader(ResourceType resourceType, int quantity) throws IllegalArgumentException, IllegalActionException {
        try {
            return this.depot.addToLeader(resourceType, quantity);
        } catch (NotEnoughSpaceException | FullExtraSlotException e) {
            throw new IllegalActionException(e.getMessage());
        } catch (NoExtraSlotException e) {
            throw new IllegalArgumentException(e.getMessage());
        }
    }


    /**
     * Removes resources From the selected shelf in the depot
     *
     * @param quantity: the quantity of the resource to be removed
     * @param shelf:    the number of the shelf on which you want to remove the resources
     * @return true if the action is performed without errors
     * @throws IllegalArgumentException if the resource is a faith point or if the quantity is negative or if the shelf isn't between 1 and 3
     * @throws IllegalActionException   if there aren't enough resources to remove
     */
    public boolean removeResourceFromDepot(int quantity, int shelf) throws IllegalArgumentException, IllegalActionException {
        try {
            return this.depot.removeFromDepot(shelf, quantity);
        } catch (NotEnoughResourcesException e) {
            throw new IllegalActionException(e.getMessage());
        }
    }

    /**
     * Removes the resources from the leaderSlots in the depot
     *
     * @param resourceType: the resourceType to be removed
     * @param quantity:     the quantity of the resource
     * @return true if the action is performed without errors
     * @throws IllegalArgumentException if the resource is a faith point or if the quantity is negative
     */
    public boolean removeResourceFromLeader(ResourceType resourceType, int quantity) throws IllegalArgumentException, IllegalActionException {
        try {
            return this.depot.removeFromLeader(resourceType, quantity);
        }catch (NoExtraSlotException | NotEnoughResourcesException e){
            throw new IllegalActionException(e.getMessage());
        }
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
     *
     * @param resourceType the resource of which you want to know the quantity
     * @return the quantity of the required resource that is in the ExtraSlot of the depot
     */
    public int getLeaderSlotResourceQuantity(ResourceType resourceType) {
        return depot.getExtraDepotValue(resourceType);
    }

    /**
     * Returns a copy of the Extra Slots this player has
     * @return a copy of the Extra Slots
     */
    public HashMap<ResourceType, Integer> getLeaderDepot(){
        return this.depot.getLeaderDepot();
    }

    /**
     * Returns the maximum amount of resources of the required type that can be stored in the ExtraSlot of the depot
     *
     * @param resourceType the resource of which you want to know the maximum quantity allowed
     * @return the maximum amount of resources that can be stored in the ExtraSlot of the depot
     */
    @Deprecated
    public int getLeaderSlotLimitQuantity(ResourceType resourceType) {
        return depot.getExtraDepotLimit(resourceType);
    }

    /**
     * Switches the resources between sourceShelf and destShelf
     *
     * @param sourceShelf the number of the first shelf, must be between 1 and 3
     * @param destShelf   the number of the second shelf, must be between 1 and 3
     * @return true if the action is performed without errors
     * @throws IllegalActionException if the sourceShelf or the destShelf hasn't enough space to store the resources of the other shelf
     */
    public boolean moveBetweenShelves(int sourceShelf, int destShelf) throws IllegalActionException {
        try {
            return depot.moveBetweenShelves(sourceShelf, destShelf);
        }catch (NotEnoughSpaceException e){
            throw new IllegalActionException(e.getMessage());
        }
    }

    /**
     * Moves the specified amount of resources from the specified shelf of the depot to the ExtraSlot slot in the depot
     *
     * @param shelfNum the number of the shelf from which remove the resources, must be between 1 and 3
     * @param quantity the number of resources to move to the extraSlot
     * @return true if the action is performed without errors
     * @throws IllegalActionException     if there isn't enough space in the extra slot to move the resources
     * @throws IllegalActionException        if there isn't an active extra slot for that type of resource
     * @throws IllegalActionException if there aren't enough resources to move from the shelf of the depot
     * @throws IllegalActionException      if the extra slot is already full of resources
     */
    public boolean moveFromShelfToLeader(int shelfNum, int quantity) throws IllegalActionException {
        try {
            return depot.moveToLeader(shelfNum, quantity);
        }catch (NotEnoughSpaceException | NoExtraSlotException | NotEnoughResourcesException | FullExtraSlotException e){
            throw new IllegalActionException(e.getMessage());
        }
    }

    /**
     * Moves the specified amount of resources from the extra slot to the specified shelf in the depot
     *
     * @param resource the resources you want to move
     * @param quantity the number of resources to move to the shelf
     * @param shelfNum the number of the shelf in which you want to move the resources
     * @return true if the action is performed without errors
     * @throws IllegalActionException        if there isn't enough space in the specified shelf to contain the specified quantity
     * @throws IllegalActionException if exists another shelf that contains the same resource
     * @throws IllegalArgumentException           if there isn't an extra slot for the specified type of resource
     * @throws IllegalActionException    if there aren't enough resources in the extra slot to move to the shelf
     */
    public boolean moveFromLeaderToShelf(ResourceType resource, int quantity, int shelfNum) throws IllegalActionException, IllegalArgumentException {
        try {
            return depot.moveToShelf(resource, quantity, shelfNum);
        }catch (NotEnoughSpaceException | AlreadyInAnotherShelfException | NotEnoughResourcesException e){
            throw new IllegalActionException(e.getMessage());
        }catch(NoExtraSlotException e){
            throw new IllegalArgumentException(e.getMessage());
        }
    }

    /**
     * Adds the resources specified in the map to the strongbox
     *
     * @param resMap the map with the resource to add
     * @return true if the action is performed without errors
     * @deprecated resources to strongbox can be added exclusively using activate production method
     */
    @Deprecated
    public boolean addResourcesToStrongbox(HashMap<ResourceType, Integer> resMap) {
        return strongbox.addResource(resMap);
    }

    /**
     * Removes the resources specified in the map from the strongbox
     *
     * @param resMap the map with the resources to remove
     * @return true if the action is performed without errors
     * @throws IllegalActionException if there aren't enough resources to remove
     */
    public boolean removeResourcesFromStrongbox(HashMap<ResourceType, Integer> resMap) throws IllegalActionException {
        try {
            return strongbox.removeResource(resMap);
        }catch(NotEnoughResourcesException e){
            throw new IllegalActionException(e.getMessage());
        }
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

    public HashMap<ResourceType, Integer> getStrongboxMap(){
        return strongbox.getAllResources();
    }

    /*
    ##################
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
    @Deprecated
    public int getCellPoints() {
        return this.playerFaithLevel.getCellPoints();
    }

    /**
     * Returns the points the player gets from the PopeTiles they have
     *
     * @return the points due to active PopeTiles
     */
    @Deprecated
    public int getPopeTilesPoints() {
        return this.playerFaithLevel.getPopeTilesPoints();
    }

    public List<PopeTile> getPopeTile() {
        return playerFaithLevel.getPopeTilesSafe();
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
    public boolean activateProduction(Collection<DevCard> ProductiveDevCards, HashMap<LeaderCard, ResourceType> productiveLeaderCardsMap, boolean alsoBaseProduction) throws NullPointerException, IllegalArgumentException, LastVaticanReportException {
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
        return strongbox.addResource(productionMap);
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
            throw new IllegalArgumentException("One or more devCards are not activable cards in the Slot");

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
            throw new IllegalActionException("playerboard_getProductionCost:there are not enough resources to activate these cards");
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
    public boolean setBaseProduction(List<ResourceType> inputResources, List<ResourceType> outputResources) throws IllegalArgumentException, NullPointerException {
        return this.baseProduction.setBaseProduction(inputResources, outputResources);
    }

    /**
     * Returns a copy of the player's BaseProduction
     *
     * @return a copy of the BaseProduction
     */
    public BaseProduction getBaseProduction() {
        return new BaseProduction(this.baseProduction);
    }

    public HashMap<ResourceType, Integer> getBaseProductionInput(){
        return new HashMap<>(this.baseProduction.getInputHashMap());
    }

    public HashMap<ResourceType, Integer> getBaseProductionOutput(){
        return new HashMap<>(this.baseProduction.getOutputHashMap());
    }


     /*
    ###############################
    #BEGINNING OF THE GAME METHODS#
    ###############################
    */

    /**
     * Adds to the depot all the extra resources players might get at the beginning of the game and moves forward the player's faith marker
     *
     * @param extraResources  a map of all the extra resources and where to put them
     * @param extraFaithPoint the extra steps on the FaithTrack this player gets to take
     * @throws IllegalActionException     if the specified configuration in the depot is invalid
     * @throws LastVaticanReportException if the player already finished the FaithTrack (they must be very lucky)
     */
    public void setBeginningExtraResources(Map<ResourceType, ArrayList<Integer>> extraResources, int extraFaithPoint) throws IllegalActionException, LastVaticanReportException {
        try {
            for (Map.Entry<ResourceType, ArrayList<Integer>> e : extraResources.entrySet()) {
                this.addResourceToDepot(e.getKey(), e.getValue().get(0), e.getValue().get(1));
            }
        } catch (Exception e) {
            throw new IllegalActionException("Can't add to depot in this way!");
        }
        this.moveForwardOnFaithTrack(extraFaithPoint);
    }

    @Deprecated
    public PlayerBoard(List<LeaderCard> leaderCards) {
        this.playerFaithLevel = new FaithLevel();
        this.depot = new Depot();
        this.strongbox = new Strongbox();
        this.devSlots = new DevSlots();
        this.leaderCards = new LeaderCards(leaderCards);
        this.baseProduction = new BaseProduction();
    }

    /**
     * Constructs an empty PlayerBoard (no FaithTrack, no initial LeaderCards are set)
     */
    public PlayerBoard() {
        this.playerFaithLevel = new FaithLevel();
        this.depot = new Depot();
        this.strongbox = new Strongbox();
        this.devSlots = new DevSlots();
        this.leaderCards = new LeaderCards();
        this.baseProduction = new BaseProduction();
    }

    /**
     * Creates a copy of the specified PlayerBoard
     * @param original the PlayerBoard to be cloned
     */
    public PlayerBoard(PlayerBoard original){
        this.playerFaithLevel = new FaithLevel(original.playerFaithLevel);
        this.depot = new Depot(original.depot);
        this.strongbox = new Strongbox(original.strongbox);
        this.devSlots = new DevSlots(original.devSlots);
        this.leaderCards = new LeaderCards(original.leaderCards);
        this.baseProduction = new BaseProduction(original.baseProduction);
    }

    /**
     * Constructs a new PlayerBoard: every parameter but the FaithTrack is copied from the specified PlayerBoard, the FaithTrack of this new PlayerBoard is the same as the one specified
     * (this is needed in order to make sure that the new PlayerBoard references to the specified PlayerBoard).
     * @param original the PlayerBoard whose every parameter but the FaithTrack is to be cloned
     * @param faithTrack the FaithTrack the new PlayerBoard will reference once constructed
     */
    public PlayerBoard(PlayerBoard original, FaithTrack faithTrack){
        this.playerFaithLevel = original.playerFaithLevel.getClone(original.playerFaithLevel, faithTrack);
        this.depot = new Depot(original.depot);
        this.strongbox = new Strongbox(original.strongbox);
        this.devSlots = new DevSlots(original.devSlots);
        this.leaderCards = new LeaderCards(original.leaderCards);
        this.baseProduction = new BaseProduction(original.baseProduction);
    }

    /**
     * Checks whether this player has discarded all the cards they are supposed to
     * @param numCardsAtBeginning how many cards the player gets at the beginning of the game
     * @param numCardsToDiscard how many cards the player is supposed to discard
     * @return true if the player has discarded the card they are supposed to, false otherwise
     */
    public boolean checkDiscardedLeaderCard(int numCardsAtBeginning, int numCardsToDiscard){
        if(this.leaderCards.getNotActiveCardsSize() + this.leaderCards.getActiveCardsSize() == numCardsAtBeginning - numCardsToDiscard)
            return true;
        return false;
    }

}
