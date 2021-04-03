package it.polimi.ingsw;

import it.polimi.ingsw.exceptions.IllegalActionException;
import it.polimi.ingsw.exceptions.IllegalParameterException;
import it.polimi.ingsw.exceptions.NotEnoughSpaceException;
import it.polimi.ingsw.leaderEffects.ExtraSlotLeaderEffect;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Depot {
    private final HashMap<ResourceType, Integer> depotLevel;
    private final ResourceType[] shelves;
    private final HashMap<ResourceType, Integer> leaderDepot;
    private final List<ExtraSlotLeaderEffect> depotLimit;

    public Depot() {
        depotLevel = new HashMap<>();
        leaderDepot = new HashMap<>();
        depotLimit = new ArrayList<>();

        for (ResourceType resource : ResourceType.values()) {
            if (!resource.isFaithPoint()) {
                depotLevel.put(resource, 0);
                leaderDepot.put(resource, 0);
            }

        }
        shelves = new ResourceType[3];
    }


    /**
     * Controls if the input are legal and if the resource isn't alread on another shelf
     *
     * @param resource: the resource to be added
     * @param quantity: the quantity of the resource
     * @param shelfNum: the number of the shelf on which you want to add the resource
     * @return true if there are no illegal parameters
     * @throws IllegalParameterException if the resource is a faith point or if the quantity is negative or if the shelf isn't between 1 and 3
     * @throws IllegalActionException    if the resource to be added is already in the depot in another position
     */
    private boolean canAddToShelf(ResourceType resource, int quantity, int shelfNum) throws IllegalParameterException, IllegalActionException {
        if (resource == ResourceType.FAITHPOINT)
            throw new IllegalParameterException("Can't add FaithPoints");

        if (quantity < 0)
            throw new IllegalParameterException("Negative quantity");

        if (shelfNum < 1 || shelfNum > 3)
            throw new IllegalParameterException("ShelfNum out of bound");

        for (int i = 0; i < shelves.length; i++) {
            if (shelves[i] == resource && i != shelfNum - 1)
                throw new IllegalActionException("Resource already in another shelf");
        }

        return true;
    }

    /**
     * controls if the action of add resources to a shelf can be executed and if yes, performs the action
     *
     * @param resource: the resource to be added
     * @param quantity: the quantity of the resource
     * @param shelfNum: the number of the shelf on which you want to add the resource
     * @return true if the action is performed without errors
     * @throws IllegalParameterException if the resource is a faith point or if the quantity is negative or if the shelf isn't between 1 and 3
     * @throws IllegalActionException    if the resource to be added is already in the depot in another position
     * @throws NotEnoughSpaceException   if the resources to be added are more than the available space in the shelf
     */
    public boolean addToShelf(ResourceType resource, int quantity, int shelfNum) throws IllegalParameterException, NotEnoughSpaceException, IllegalActionException {
        boolean canAdd;
        int actualValue, newValue, availableSpace;

        canAdd = canAddToShelf(resource, quantity, shelfNum);
        if (canAdd) {
            if (quantity == 0)
                return true;

            actualValue = depotLevel.get(resource);
            newValue = actualValue + quantity;
            if (newValue > shelfNum) {
                availableSpace = shelfNum - actualValue;
                throw new NotEnoughSpaceException("Not enought space in depot", availableSpace);
            } else {
                depotLevel.put(resource, newValue);
                shelfNum--;
                if (actualValue == 0) {
                    shelves[shelfNum] = resource;
                }
                return true;
            }
        }
        return false;
    }

    /**
     * Controls if the parameters are valid and if the action can be performed
     *
     * @param shelfNum: the num of the shelf from which to remove the resource
     * @param quantity: the quantity of resources to remove
     * @return true if there are no illegal parameters and the action can be performed
     * @throws IllegalParameterException: if shelfNum isn't between 1 and 3, if quantity is < 0 and if the shelf is already empty
     */
    private boolean canRemoveFromDepot(int shelfNum, int quantity) throws IllegalParameterException {
       if(shelfNum < 1 || shelfNum > 3)
           throw new IllegalParameterException("ShelfNum out of bound");

        if(quantity < 0)
            throw new IllegalParameterException("Negative quantity");

        if(shelves[shelfNum - 1] == null)
            throw new IllegalParameterException("Resource not in depot");

        return true;
    }

    /**
     * Controls if the remove action can be performed and if yes, perform the action
     *
     * @param shelfNum: the number of the shelf to remove resources
     * @param quantity: the quantity of the resource to remove
     * @return true if the action is performed without errors
     * @throws IllegalParameterException: if at least one parameter is illegal
     * @throws IllegalActionException: if there aren't enough resources to be removed from the depot
     */
    public boolean removeFromDepot(int shelfNum, int quantity) throws IllegalParameterException, IllegalActionException {
        int actualValue, newValue;
        ResourceType resourceType;
        boolean canRemove;

        canRemove = canRemoveFromDepot(shelfNum, quantity);
        if(canRemove) {
            if(quantity == 0)
                return true;

            shelfNum --;
            resourceType = shelves[shelfNum];
            actualValue = depotLevel.get(resourceType);
            newValue = actualValue - quantity;
            if (newValue < 0)
                throw new IllegalActionException("Not enough resources to remove");
            depotLevel.put(resourceType, newValue);

            if(newValue == 0){
                shelves[shelfNum] = null;
            }
        }
        return true;
    }

    /**
     * Controls if the parameters are valid and if the action can be performed
     *
     * @param sourceShelf: the position of the resource to move
     * @param destShelf: the final position of the resource
     * @return true if there are no illegal parameters and if the action can be performed
     * @throws IllegalParameterException: if sourceShelf or destShelf aren't between 1 and 3; if in sourceShelf position there aren't any resources
     * @throws IllegalActionException: if the source shelf contains more resources than the max allowed in the destination shelf, or vice versa
     */
    private boolean canMoveBetweenShelves(int sourceShelf, int destShelf) throws IllegalParameterException, IllegalActionException {
        int sourceValue, destValue;
        if(sourceShelf < 1 || sourceShelf > 3)
            throw new IllegalParameterException("sourceShelf out of bound");

        if(destShelf < 1 || destShelf > 3)
            throw new IllegalParameterException("destinationShelf out of bound");

        if(shelves[sourceShelf - 1] == null)
            throw new IllegalActionException("No source resource to move");

        sourceValue = depotLevel.get(shelves[sourceShelf - 1]);
        if(sourceValue > destShelf)
            throw new IllegalActionException("Not enough space in destination shelf");

        if(shelves[destShelf - 1] != null) {
            destValue = depotLevel.get(shelves[destShelf - 1]);
            if(destValue > sourceShelf)
                throw new IllegalActionException("Not enough space in source shelf");
        }
        return true;
    }

    /**
     * Controls if parameters are legal and if yes, move the resources
     *
     * @param sourceShelf: the position of the resource to move
     * @param destShelf: the final position of the resource
     * @return true if the action is performed without errors
     * @throws IllegalParameterException: if at least one parameter is illegal
     * @throws IllegalActionException: if the source shelf contains more resources than the max allowed in the destination shelf, or vice versa
     */
    public boolean moveBetweenShelves(int sourceShelf, int destShelf) throws IllegalParameterException, IllegalActionException {
        ResourceType resourceHolder;
        boolean canMove;

        canMove = canMoveBetweenShelves(sourceShelf, destShelf);
        if(canMove){
            sourceShelf --;
            destShelf --;
            resourceHolder = shelves[destShelf];
            shelves[destShelf] = shelves[sourceShelf];
            shelves[sourceShelf] = resourceHolder;
        }

        return true;
    }

    /**
     * Control if the parameters are valid and if the action can be performed
     *
     * @param effect: the effect of the leader card that you want to activate
     * @return true if there are no illegal parameters and the action can be performed
     * @throws IllegalParameterException if the type of the resource of the effect is faith point and if the number of resources of the effect is negative
     * @throws IllegalActionException if the maximum number of active leader cards has been already reached
     */
    private boolean canAddExtraSlot(ExtraSlotLeaderEffect effect) throws IllegalParameterException, IllegalActionException {
        final int maxLeaderCards = 2;
        if(effect.getType() == ResourceType.FAITHPOINT)
            throw new  IllegalParameterException("Can't add FaithPoints");

        if(effect.getResourceNumber() < 0)
            throw new IllegalParameterException("Negative quantity");

        if(depotLimit.size() == maxLeaderCards)
            throw new IllegalActionException("Can't add more leader cards");
        return true;
    }

    /**
     * Activate the effect of a new LeaderCard with effect ExtraSlotLeaderEffect
     *
     * @param effect: the effect of the leaderCard
     * @return true if the action in performed without errors
     * @throws IllegalParameterException if the effect has non valid values
     * @throws IllegalActionException if the maximum number of active leader cards ha been already reached
     */
    public boolean addExtraSolt(ExtraSlotLeaderEffect effect) throws IllegalParameterException, IllegalActionException {
        boolean canAdd;

        canAdd = canAddExtraSlot(effect);
        if(canAdd){
            depotLimit.add(effect);
        }

        return true;
    }

    private boolean canAddToLeaderDepot(ResourceType resource, int quantity) throws IllegalParameterException, IllegalActionException, NotEnoughSpaceException {
        int resLimit, actualValue, newValue;

        if(resource == ResourceType.FAITHPOINT)
            throw new IllegalParameterException("Can't add faith points");

        if(quantity < 0)
            throw new IllegalParameterException("Negative quantity");

        resLimit = getExtraDepotLimit(resource);
        if(resLimit == 0)
            throw new IllegalActionException("No existing extra slot for this resource");

        actualValue = leaderDepot.get(resource);
        if(actualValue == resLimit)
            throw new IllegalActionException("Extra slot is full");

        newValue = actualValue + quantity;
        if(newValue > resLimit)
            throw new NotEnoughSpaceException("Not enough space in extra slot", resLimit - actualValue);

        return true;
    }

    public boolean addToLeaderDepot(ResourceType resource, int quantity) throws IllegalParameterException, IllegalActionException, NotEnoughSpaceException {
        boolean canAdd;
        int actualValue, newValue;

        canAdd = canAddToLeaderDepot(resource, quantity);
        if(canAdd){
            if(quantity == 0)
                return true;
            actualValue = leaderDepot.get(resource);
            newValue = actualValue + quantity;
            leaderDepot.put(resource, newValue);
        }

        return true;
    }

    /**
     * @param resource: the resource you want to know the value
     * @return the value of the resource
     * @throws IllegalParameterException if resource is a faithPoint
     */
    public int getResource(ResourceType resource) throws IllegalParameterException {
        if (resource == ResourceType.FAITHPOINT)
            throw new IllegalParameterException("Faith Points are not stored in depot");

        return depotLevel.get(resource);
    }

    /**
     * @param shelfNum: the number of the shelf you want to know the type
     * @return the ResourceType of that shelf
     * @throws IllegalParameterException if shelf num isn't between 1 and 3
     */
    public ResourceType getShelfType(int shelfNum) throws IllegalParameterException {
        if (shelfNum < 1 || shelfNum > 3)
            throw new IllegalParameterException("ShelfNum out of bound");

        shelfNum--;
        return shelves[shelfNum];
    }

    /**
     * Returns the maximum number of a certain type of resource that can be stored in leaderDepot
     *
     * @param resoruce: the resourceType of which you want to know the limit
     * @return the maximum number of resources of the selected type that can be stored in leaderDepot
     */
    int getExtraDepotLimit(ResourceType resoruce){
        int size, limit;

        limit = 0;
        size = depotLimit.size();
        for(int i = 0; i < size; i++){
            if(depotLimit.get(i).getType() == resoruce)
                limit = limit + depotLimit.get(i).getResourceNumber();
        }
        return limit;
    }

    /**
     * Returns the actual number of resources stored in the ExtraSlotLeaderCard
     *
     * @param resource: the type of the resource
     * @return the actual number of resources stored in the ExtraSlotLeaderCard
     * @throws IllegalParameterException if the resource is faith point
     */
    int getExtraDepotValue(ResourceType resource) throws IllegalParameterException {
        if(resource == ResourceType.FAITHPOINT)
            throw new IllegalParameterException("Faith Point aren't stored in extraSlot");
        return leaderDepot.get(resource);
    }

}


