package it.polimi.ingsw;

import it.polimi.ingsw.exceptions.IllegalActionException;
import it.polimi.ingsw.exceptions.NotEnoughSpaceException;
import it.polimi.ingsw.LeaderCard.leaderEffects.ExtraSlotLeaderEffect;

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
     * @throws IllegalArgumentException if the resource is a faith point or if the quantity is negative or if the shelf isn't between 1 and 3
     * @throws IllegalActionException   if the resource to be added is already in the depot in another position
     * @throws NotEnoughSpaceException  if the resources to be added are more than the available space in the shelf
     */
    private boolean canAddToShelf(ResourceType resource, int quantity, int shelfNum) throws IllegalArgumentException, IllegalActionException, NotEnoughSpaceException {
        int actualValue, newValue, availableSpace;

        isFaithPoint(resource);
        ctrlQuantity(quantity);
        controlShelfNum(shelfNum);

        for (int i = 0; i < shelves.length; i++) {
            if (shelves[i] == resource && i != shelfNum - 1)
                throw new IllegalActionException("Resource already in another shelf");
        }

        actualValue = depotLevel.get(resource);
        newValue = actualValue + quantity;
        if (newValue > shelfNum) {
            availableSpace = shelfNum - actualValue;
            throw new NotEnoughSpaceException("Not enought space in depot", availableSpace);
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
     * @throws IllegalArgumentException if the resource is a faith point or if the quantity is negative or if the shelf isn't between 1 and 3
     * @throws IllegalActionException   if the resource to be added is already in the depot in another position
     * @throws NotEnoughSpaceException  if the resources to be added are more than the available space in the shelf
     */
    public boolean addToShelf(ResourceType resource, int quantity, int shelfNum) throws IllegalArgumentException, NotEnoughSpaceException, IllegalActionException {
        boolean canAdd;
        int actualValue, newValue;

        canAdd = canAddToShelf(resource, quantity, shelfNum);
        if (canAdd) {
            actualValue = depotLevel.get(resource);
            newValue = actualValue + quantity;
            depotLevel.put(resource, newValue);
            shelfNum--;
            if (actualValue == 0 && newValue != 0)
                shelves[shelfNum] = resource;

            return true;
        }
        return false;
    }

    /**
     * Controls if the parameters are valid and if the action can be performed
     *
     * @param shelfNum: the num of the shelf from which to remove the resource
     * @param quantity: the quantity of resources to remove
     * @return true if there are no illegal parameters and the action can be performed
     * @throws IllegalArgumentException : if shelfNum isn't between 1 and 3, if quantity is < 0 and if the shelf is already empty
     * @throws IllegalActionException:  if there aren't enough resources to be removed from the depot
     */
    private boolean canRemoveFromDepot(int shelfNum, int quantity) throws IllegalArgumentException, IllegalActionException {
        int actualValue, newValue;
        ResourceType resourceType;

        controlShelfNum(shelfNum);
        ctrlQuantity(quantity);

        if (shelves[shelfNum - 1] == null)
            throw new IllegalArgumentException("Resource not in depot");

        shelfNum--;
        resourceType = shelves[shelfNum];
        actualValue = depotLevel.get(resourceType);
        newValue = actualValue - quantity;
        if (newValue < 0)
            throw new IllegalActionException("Not enough resources to remove");

        return true;
    }

    /**
     * Controls if the remove action can be performed and if yes, perform the action
     *
     * @param shelfNum: the number of the shelf to remove resources
     * @param quantity: the quantity of the resource to remove
     * @return true if the action is performed without errors
     * @throws IllegalArgumentException : if at least one parameter is illegal
     * @throws IllegalActionException:  if there aren't enough resources to be removed from the depot
     */
    public boolean removeFromDepot(int shelfNum, int quantity) throws IllegalArgumentException, IllegalActionException {
        int actualValue, newValue;
        ResourceType resourceType;
        boolean canRemove;

        canRemove = canRemoveFromDepot(shelfNum, quantity);
        if (canRemove) {
            shelfNum--;
            resourceType = shelves[shelfNum];
            actualValue = depotLevel.get(resourceType);
            newValue = actualValue - quantity;
            depotLevel.put(resourceType, newValue);

            if (newValue == 0) {
                shelves[shelfNum] = null;
            }
        }
        return true;
    }

    /**
     * Controls if the parameters are valid and if the action can be performed
     *
     * @param sourceShelf: the position of the resource to move
     * @param destShelf:   the final position of the resource
     * @return true if there are no illegal parameters and if the action can be performed
     * @throws IllegalArgumentException : if sourceShelf or destShelf aren't between 1 and 3; if in sourceShelf position there aren't any resources
     * @throws IllegalActionException:  if the source shelf contains more resources than the max allowed in the destination shelf, or vice versa
     */
    private boolean canMoveBetweenShelves(int sourceShelf, int destShelf) throws IllegalArgumentException, IllegalActionException {
        int sourceValue, destValue;

        controlShelfNum(sourceShelf);
        controlShelfNum(destShelf);

        if (shelves[sourceShelf - 1] == null)
            throw new IllegalActionException("No source resource to move");

        sourceValue = depotLevel.get(shelves[sourceShelf - 1]);
        if (sourceValue > destShelf)
            throw new IllegalActionException("Not enough space in destination shelf");

        if (shelves[destShelf - 1] != null) {
            destValue = depotLevel.get(shelves[destShelf - 1]);
            if (destValue > sourceShelf)
                throw new IllegalActionException("Not enough space in source shelf");
        }
        return true;
    }

    /**
     * Controls if parameters are legal and if yes, move the resources
     *
     * @param sourceShelf: the position of the resource to move
     * @param destShelf:   the final position of the resource
     * @return true if the action is performed without errors
     * @throws IllegalArgumentException : if at least one parameter is illegal
     * @throws IllegalActionException:  if the source shelf contains more resources than the max allowed in the destination shelf, or vice versa
     */
    public boolean moveBetweenShelves(int sourceShelf, int destShelf) throws IllegalArgumentException, IllegalActionException {
        ResourceType resourceHolder;
        boolean canMove;

        canMove = canMoveBetweenShelves(sourceShelf, destShelf);
        if (canMove) {
            sourceShelf--;
            destShelf--;
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
     * @throws IllegalArgumentException if the type of the resource of the effect is faith point and if the number of resources of the effect is negative
     * @throws IllegalActionException   if the maximum number of active leader cards has been already reached
     * @deprecated for updating extraSlotLeaderEffect class
     */
    private boolean canAddExtraSlot(ExtraSlotLeaderEffect effect) throws IllegalArgumentException, IllegalActionException {
        final int maxLeaderCards = 2;

        if (depotLimit.size() == maxLeaderCards)
            throw new IllegalActionException("Can't add more leader cards");
        return true;
    }

    /**
     * Activate the effect of a new LeaderCard with effect ExtraSlotLeaderEffect
     *
     * @param effect: the effect of the leaderCard
     * @return true if the action in performed without errors
     * @throws IllegalArgumentException if the effect has non valid values
     * @throws IllegalActionException   if the maximum number of active leader cards ha been already reached
     */
    public boolean addExtraSolt(ExtraSlotLeaderEffect effect) throws IllegalArgumentException, IllegalActionException {
        final int maxLeaderCards = 2;

        if (depotLimit.size() == maxLeaderCards)
            throw new IllegalActionException("Can't add more leader cards");

        depotLimit.add(effect);

        return true;
    }

    /**
     * Controls if there are illegal parameter and if the action can be performed
     *
     * @param resource: the resource to add
     * @param quantity: the quantity to add
     * @return true if all the parameters are legal and if the action can be performed
     * @throws IllegalArgumentException : if the resource is a faith point and if the quantity is negative
     * @throws IllegalActionException:  if the resource to remove doesn't have an extra slot and if the extra slot is already full of resources
     * @throws NotEnoughSpaceException: if the resources to be added are more than the available space in the extra slot
     */
    private boolean canAddToLeaderDepot(ResourceType resource, int quantity) throws IllegalArgumentException, IllegalActionException, NotEnoughSpaceException {
        int resLimit, actualValue, newValue;

        isFaithPoint(resource);
        ctrlQuantity(quantity);

        resLimit = getExtraDepotLimit(resource);
        if (resLimit == 0)
            throw new IllegalActionException("No existing extra slot for this resource");

        actualValue = leaderDepot.get(resource);
        if (actualValue == resLimit)
            throw new IllegalActionException("Extra slot is full");

        newValue = actualValue + quantity;
        if (newValue > resLimit)
            throw new NotEnoughSpaceException("Not enough space in extra slot", resLimit - actualValue);

        return true;
    }

    /**
     * Controls if all the parameters are legal and if yes, performs the action
     *
     * @param resource: the resource to add
     * @param quantity: the quantity to add
     * @return true if the action is performed without errors
     * @throws IllegalArgumentException if the resource is a faith point and if the quantity is negative
     * @throws IllegalActionException   if the resource to remove doesn't have an extra slot and if the extra slot is already full of resources
     * @throws NotEnoughSpaceException  if the resources to be added are more than the available space in the extra slot
     */
    public boolean addToLeader(ResourceType resource, int quantity) throws IllegalArgumentException, IllegalActionException, NotEnoughSpaceException {
        boolean canAdd;
        int actualValue, newValue;

        canAdd = canAddToLeaderDepot(resource, quantity);
        if (canAdd) {
            actualValue = leaderDepot.get(resource);
            newValue = actualValue + quantity;
            leaderDepot.put(resource, newValue);
        }

        return true;
    }

    /**
     * Controls if there are illegal parameter and if the action can be performed
     *
     * @param resource: the resource to remove
     * @param quantity: the quantity to remove
     * @return true if all the parameters are legal and if the action can be performed
     * @throws IllegalArgumentException if the resource is a faith point and if the quantity is negative
     * @throws IllegalActionException   if the resource to remove doesn't have an extra slot
     */
    private boolean canRemoveFromLeader(ResourceType resource, int quantity) throws IllegalArgumentException, IllegalActionException {
        int resLimit;
        int actualValue, newValue;

        isFaithPoint(resource);
        ctrlQuantity(quantity);

        resLimit = getExtraDepotLimit(resource);
        if (resLimit == 0)
            throw new IllegalActionException("No existing extra slot for this resource");

        actualValue = leaderDepot.get(resource);
        newValue = actualValue - quantity;

        if (newValue < 0)
            throw new IllegalActionException("Not enough resources to remove");

        return true;
    }

    /**
     * Controls if all the parameters are legal and if yes, performs the action
     *
     * @param resource: the resource to remove
     * @param quantity: the quantity to remove
     * @return true if all the parameters are legal and if the action can be performed
     * @throws IllegalArgumentException if the resource is a faith point and if the quantity is negative
     * @throws IllegalActionException   if the resource to remove doesn't have an extra slot
     */
    public boolean removeFromLeader(ResourceType resource, int quantity) throws IllegalArgumentException, IllegalActionException {
        boolean canRemove;
        int actualValue, newValue;

        canRemove = canRemoveFromLeader(resource, quantity);
        if (canRemove) {
            actualValue = leaderDepot.get(resource);
            newValue = actualValue - quantity;
            leaderDepot.put(resource, newValue);
        }
        return true;
    }

    /**
     * Controls if there are illegal parameter and if the action can be performed
     *
     * @param shelfNum: the  number of the shelf on which move the resources
     * @param quantity: the number of resources to move
     * @return true if all the parameters are legal and if the action can be performed
     * @throws IllegalArgumentException if the resources can't be removed from the shelf and if they can't be added to de extra slot
     * @throws IllegalActionException   if the resources can't be removed from the shelf and if they can't be added to de extra slot
     * @throws NotEnoughSpaceException  if the number of moved resources is greater than the available space
     */
    private boolean canMoveToLeader(int shelfNum, int quantity) throws IllegalArgumentException, IllegalActionException, NotEnoughSpaceException {
        ResourceType resourceType;

        controlShelfNum(shelfNum);
        ctrlQuantity(quantity);

        resourceType = getShelfType(shelfNum);
        canRemoveFromDepot(shelfNum, quantity);
        canAddToLeaderDepot(resourceType, quantity);

        return true;
    }

    /**
     * Controls if all the parameters are legal and if yes, performs the action
     *
     * @param shelfNum: the  number of the shelf on which move the resources
     * @param quantity: the number of resources to move
     * @return true if all the parameters are legal and if the action can be performed
     * @throws IllegalArgumentException if the resources can't be removed from the shelf and if they can't be added to de extra slot
     * @throws IllegalActionException   if the resources can't be removed from the shelf and if they can't be added to de extra slot
     * @throws NotEnoughSpaceException  if the number of moved resources is greater than the available space
     */
    public boolean moveToLeader(int shelfNum, int quantity) throws IllegalArgumentException, IllegalActionException, NotEnoughSpaceException {
        boolean canMove;
        ResourceType resourceType;

        canMove = canMoveToLeader(shelfNum, quantity);
        if (canMove) {
            resourceType = getShelfType(shelfNum);
            removeFromDepot(shelfNum, quantity);
            addToLeader(resourceType, quantity);
        }

        return true;
    }

    /**
     * Controls if there are illegal parameter and if the action can be performed
     *
     * @param resource: the type of the resource to move to the shelf
     * @param quantity: the quantity to move
     * @param shelfNum: the  number of the shelf on which move the resources
     * @return true if all the parameters are legal and if the action can be performed
     * @throws IllegalArgumentException if the resources can't be removed from the extra slot and if they can't be added to the shelf
     * @throws IllegalActionException   if the resources can't be removed from the extra slot and if they can't be added to the shelf
     * @throws NotEnoughSpaceException  if the number of moved resources is greater than the available space
     */
    private boolean canMoveToShelf(ResourceType resource, int quantity, int shelfNum) throws IllegalArgumentException, IllegalActionException, NotEnoughSpaceException {
        canRemoveFromLeader(resource, quantity);
        canAddToShelf(resource, quantity, shelfNum);
        return true;
    }

    /**
     * Controls if all the parameters are legal and if yes, performs the action
     *
     * @param resource: the type of the resource to move to the shelf
     * @param quantity: the quantity to move
     * @param shelfNum: the  number of the shelf on which move the resources
     * @return true if all the parameters are legal and if the action can be performed
     * @throws IllegalArgumentException if the resources can't be removed from the extra slot and if they can't be added to the shelf
     * @throws IllegalActionException   if the resources can't be removed from the extra slot and if they can't be added to the shelf
     * @throws NotEnoughSpaceException  if the number of moved resources is greater than the available space
     */
    public boolean moveToShelf(ResourceType resource, int quantity, int shelfNum) throws IllegalArgumentException, IllegalActionException, NotEnoughSpaceException {
        boolean canMove;

        canMove = canMoveToShelf(resource, quantity, shelfNum);
        if (canMove) {
            removeFromLeader(resource, quantity);
            addToShelf(resource, quantity, shelfNum);
        }
        return true;
    }

    /**
     * Returns the quantity of the selected resource
     *
     * @param resource: the resource you want to know the value
     * @return the value of the resource
     * @throws IllegalArgumentException if resource is a faithPoint
     */
    public int getResourceFromDepot(ResourceType resource) throws IllegalArgumentException {
        isFaithPoint(resource);

        return depotLevel.get(resource);
    }

    /**
     * Returns the type of the selected shelf
     *
     * @param shelfNum: the number of the shelf you want to know the type
     * @return the ResourceType of that shelf
     * @throws IllegalArgumentException if shelf num isn't between 1 and 3
     */
    public ResourceType getShelfType(int shelfNum) throws IllegalArgumentException {
        controlShelfNum(shelfNum);

        shelfNum--;
        return shelves[shelfNum];
    }

    /**
     * Returns the maximum number of a certain type of resource that can be stored in leaderDepot
     *
     * @param resoruce: the resourceType of which you want to know the limit
     * @return the maximum number of resources of the selected type that can be stored in leaderDepot
     */
    int getExtraDepotLimit(ResourceType resoruce) {
        int size, limit;

        limit = 0;
        size = depotLimit.size();
        for (int i = 0; i < size; i++) {
            if (depotLimit.get(i).extraSlotGetType() == resoruce)
                limit = limit + depotLimit.get(i).extraSlotGetResourceNumber();
        }
        return limit;
    }

    /**
     * Returns the actual number of resources stored in the ExtraSlotLeaderCard
     *
     * @param resource: the type of the resource
     * @return the actual number of resources stored in the ExtraSlotLeaderCard
     * @throws IllegalArgumentException if the resource is faith point
     */
    int getExtraDepotValue(ResourceType resource) throws IllegalArgumentException {
        isFaithPoint(resource);
        return leaderDepot.get(resource);
    }

    /**
     * Returns all the resources in the depot in a copy of the hashmap
     * @return returns all the resources in the depot in a copy of the hashmap
     */
    public HashMap<ResourceType, Integer> getAllResources(){
        return new HashMap<>(depotLevel);
    }

    /**
     * Controls if the resource passed is a faith point
     *
     * @param resourceType: the resource to control
     * @throws IllegalArgumentException if the resource is a faith point
     */
    private void isFaithPoint(ResourceType resourceType) throws IllegalArgumentException {
        if (resourceType == ResourceType.FAITHPOINT)
            throw new IllegalArgumentException("Depot can't handle faith points");
    }

    /**
     * Controls if the quantity passed is negative
     *
     * @param quantity: the quantity to control
     * @throws IllegalArgumentException if the quantoty is negative
     */
    private void ctrlQuantity(int quantity) throws IllegalArgumentException {
        if (quantity < 0)
            throw new IllegalArgumentException("Negative quantity");
    }

    /**
     * Controls if the number of the shelf selected is a valid number
     *
     * @param shelfNum: the number of the shelf
     * @throws IllegalArgumentException if shelfNum isn't between 1 and 3
     */
    private void controlShelfNum(int shelfNum) throws IllegalArgumentException {
        if (shelfNum < 1 || shelfNum > 3)
            throw new IllegalArgumentException("ShelfNum out of bound");
    }
}


