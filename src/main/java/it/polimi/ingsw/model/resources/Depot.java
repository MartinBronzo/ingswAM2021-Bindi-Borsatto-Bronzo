package it.polimi.ingsw.model.resources;

import it.polimi.ingsw.exceptions.*;
import it.polimi.ingsw.model.leaderCard.leaderEffects.Effect;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Depot {
    private final HashMap<ResourceType, Integer> depotLevel;
    private final ResourceType[] shelves;
    private final HashMap<ResourceType, Integer> leaderDepot;
    private final List<Effect> depotLimit;

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
     * Constructs a copy of the specified Deport
     *
     * @param depot the Depot to be cloned
     */
    public Depot(Depot depot) {
        this.depotLevel = new HashMap<>(depot.depotLevel);
        shelves = new ResourceType[3];
        System.arraycopy(depot.shelves, 0, this.shelves, 0, depot.shelves.length);
        this.leaderDepot = new HashMap<>(depot.leaderDepot);
        this.depotLimit = new ArrayList<>(depot.depotLimit);
    }

    /**
     * Controls if the input are legal and if the resource isn't alread on another shelf
     *
     * @param resource: the resource to be added
     * @param quantity: the quantity of the resource
     * @param shelfNum: the number of the shelf, the value is between 1 and 3: 1 is the smallest shelf and 3 is the largest
     * @return true if there are no illegal parameters
     * @throws IllegalArgumentException       if the resource is a faith point or if the quantity is negative or if the shelf isn't between 1 and 3
     * @throws AlreadyInAnotherShelfException if the resource to be added is already in the depot in another position
     * @throws NotEnoughSpaceException        if the resources to be added are more than the available space in the shelf
     */
    private boolean canAddToShelf(ResourceType resource, int quantity, int shelfNum) throws IllegalArgumentException, NotEnoughSpaceException, AlreadyInAnotherShelfException {
        int actualValue, newValue, availableSpace;

        isFaithPoint(resource);
        ctrlQuantity(quantity);
        controlShelfNum(shelfNum);

        for (int i = 0; i < shelves.length; i++) {
            if (shelves[i] == resource && i != shelfNum - 1)
                throw new AlreadyInAnotherShelfException("Resource already in another shelf");
        }

        actualValue = depotLevel.get(resource);
        newValue = actualValue + quantity;
        if (newValue > shelfNum) {
            availableSpace = shelfNum - actualValue;
            throw new NotEnoughSpaceException("Not enough space in depot", availableSpace);
        }

        return true;
    }

    /**
     * controls if the action of add resources to a shelf can be executed and if yes, performs the action
     *
     * @param resource: the resource to be added
     * @param quantity: the quantity of the resource
     * @param shelfNum: the number of the shelf, the value is between 1 and 3: 1 is the smallest shelf and 3 is the largest
     * @return true if the action is performed without errors
     * @throws IllegalArgumentException       if the resource is a faith point or if the quantity is negative or if the shelf isn't between 1 and 3
     * @throws AlreadyInAnotherShelfException if the resource to be added is already in the depot in another position
     * @throws NotEnoughSpaceException        if the resources to be added are more than the available space in the shelf
     */
    public boolean addToShelf(ResourceType resource, int quantity, int shelfNum) throws IllegalArgumentException, NotEnoughSpaceException, AlreadyInAnotherShelfException {
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
     * @param shelfNum: the number of the shelf, the value is between 1 and 3: 1 is the smallest shelf and 3 is the largest
     * @param quantity: the quantity of resources to remove
     * @return true if there are no illegal parameters and the action can be performed
     * @throws IllegalArgumentException    : if shelfNum isn't between 1 and 3, if quantity is < 0 and if the shelf is already empty
     * @throws NotEnoughResourcesException : if there aren't enough resources to be removed from the depot
     */
    private boolean canRemoveFromDepot(int shelfNum, int quantity) throws IllegalArgumentException, NotEnoughResourcesException {
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
            throw new NotEnoughResourcesException("Not enough resources to remove");

        return true;
    }

    /**
     * Controls if the remove action can be performed and if yes, perform the action
     *
     * @param shelfNum: the number of the shelf, the value is between 1 and 3: 1 is the smallest shelf and 3 is the largest
     * @param quantity: the quantity of the resource to remove
     * @return true if the action is performed without errors
     * @throws IllegalArgumentException     : if at least one parameter is illegal
     * @throws NotEnoughResourcesException: if there aren't enough resources to be removed from the depot
     */
    public boolean removeFromDepot(int shelfNum, int quantity) throws IllegalArgumentException, NotEnoughResourcesException {
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
     * @param sourceShelf: the position of the resource to move; the value is between 1 and 3: 1 is the smallest shelf and 3 is the largest
     * @param destShelf:   the final position of the resource; the value is between 1 and 3: 1 is the smallest shelf and 3 is the largest
     * @return true if there are no illegal parameters and if the action can be performed
     * @throws IllegalArgumentException : if sourceShelf or destShelf aren't between 1 and 3; if in sourceShelf position there aren't any resources
     * @throws NotEnoughSpaceException: if the source shelf contains more resources than the max allowed in the destination shelf, or vice versa
     */
    private boolean canMoveBetweenShelves(int sourceShelf, int destShelf) throws IllegalArgumentException, NotEnoughSpaceException {
        int sourceValue, destValue;

        controlShelfNum(sourceShelf);
        controlShelfNum(destShelf);

        if (shelves[sourceShelf - 1] == null)
            throw new NullPointerException("No source resource to move");

        sourceValue = depotLevel.get(shelves[sourceShelf - 1]);
        if (sourceValue > destShelf)
            throw new IllegalArgumentException("Not enough space in destination shelf");


        if (shelves[destShelf - 1] != null) {
            destValue = depotLevel.get(shelves[destShelf - 1]);
            if (destValue > sourceShelf)
                throw new NotEnoughSpaceException("Not enough space in source shelf", sourceShelf - depotLevel.get(shelves[sourceShelf - 1]));
        }
        return true;
    }

    /**
     * Controls if parameters are legal and if yes, move the resources
     *
     * @param sourceShelf: the position of the resource to move; the value is between 1 and 3: 1 is the smallest shelf and 3 is the largest
     * @param destShelf:   the final position of the resource; the value is between 1 and 3: 1 is the smallest shelf and 3 is the largest
     * @return true if the action is performed without errors
     * @throws IllegalArgumentException : if at least one parameter is illegal
     * @throws NotEnoughSpaceException: if the source shelf contains more resources than the max allowed in the destination shelf, or vice versa
     */
    public boolean moveBetweenShelves(int sourceShelf, int destShelf) throws IllegalArgumentException, NotEnoughSpaceException {
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
     * @throws FullExtraSlotException   if the maximum number of active leader cards has been already reached
     * @deprecated after update of extraSlotLeaderEffect class
     */
    private boolean canAddExtraSlot(Effect effect) throws IllegalArgumentException, FullExtraSlotException {
        final int maxLeaderCards = 2;

        if (depotLimit.size() == maxLeaderCards)
            throw new FullExtraSlotException("Can't add more leader cards");
        return true;
    }

    /**
     * Activate the effect of a new LeaderCard
     *
     * @param effect: the effect of the leaderCard
     * @return true if the action in performed without errors
     * @throws IllegalArgumentException if the effect has non valid values
     * @throws FullExtraSlotException   if the maximum number of active leader cards has been already reached
     */
    public boolean addExtraSlot(Effect effect) throws IllegalArgumentException, FullExtraSlotException {
        final int maxLeaderCards = 2;

        if (depotLimit.size() == maxLeaderCards)
            throw new FullExtraSlotException("Can't add more leader cards");

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
     * @throws NoExtraSlotException     :    if the resource to remove doesn't have an extra slot
     * @throws FullExtraSlotException:  if the extra slot is already full of resources
     * @throws NotEnoughSpaceException: if the resources to be added are more than the available space in the extra slot
     */
    private boolean canAddToLeaderDepot(ResourceType resource, int quantity) throws IllegalArgumentException, NotEnoughSpaceException, NoExtraSlotException, FullExtraSlotException {
        int resLimit, actualValue, newValue;

        isFaithPoint(resource);
        ctrlQuantity(quantity);

        resLimit = getExtraDepotLimit(resource);
        if (resLimit == 0)
            throw new NoExtraSlotException("No existing extra slot for this resource");

        actualValue = leaderDepot.get(resource);
        if (actualValue == resLimit)
            throw new FullExtraSlotException("Extra slot is full");

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
     * @throws IllegalArgumentException : if the resource is a faith point and if the quantity is negative
     * @throws NoExtraSlotException:    if the resource to remove doesn't have an extra slot
     * @throws FullExtraSlotException:  if the extra slot is already full of resources
     * @throws NotEnoughSpaceException  if the resources to be added are more than the available space in the extra slot
     */
    public boolean addToLeader(ResourceType resource, int quantity) throws IllegalArgumentException, NotEnoughSpaceException, NoExtraSlotException, FullExtraSlotException {
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
     * @throws IllegalArgumentException    if the resource is a faith point and if the quantity is negative
     * @throws NoExtraSlotException        if the resource to remove doesn't have an extra slot
     * @throws NotEnoughResourcesException if there aren't enough resources to remove from leaderExtraSlot
     */
    private boolean canRemoveFromLeader(ResourceType resource, int quantity) throws IllegalArgumentException, NoExtraSlotException, NotEnoughResourcesException {
        int resLimit;
        int actualValue, newValue;

        isFaithPoint(resource);
        ctrlQuantity(quantity);

        resLimit = getExtraDepotLimit(resource);
        if (resLimit == 0)
            throw new NoExtraSlotException("No existing extra slot for this resource");

        actualValue = leaderDepot.get(resource);
        newValue = actualValue - quantity;

        if (newValue < 0)
            throw new NotEnoughResourcesException("Not enough resources to remove");

        return true;
    }

    /**
     * Controls if all the parameters are legal and if yes, performs the action
     *
     * @param resource: the resource to remove
     * @param quantity: the quantity to remove
     * @return true if all the parameters are legal and if the action can be performed
     * @throws IllegalArgumentException    if the resource is a faith point and if the quantity is negative
     * @throws NoExtraSlotException        if the resource to remove doesn't have an extra slot
     * @throws NotEnoughResourcesException if there aren't enough resources to remove from leaderExtraSlot
     */
    public boolean removeFromLeader(ResourceType resource, int quantity) throws IllegalArgumentException, NoExtraSlotException, NotEnoughResourcesException {
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
     * @param shelfNum: the  number of the shelf on which move the resources; the value is between 1 and 3: 1 is the smallest shelf and 3 is the largest
     * @param quantity: the number of resources to move; the value is between 1 and 3: 1 is the smallest shelf and 3 is the largest
     * @return true if all the parameters are legal and if the action can be performed
     * @throws IllegalArgumentException    if the resources can't be removed from the shelf and if they can't be added to de extra slot
     * @throws NotEnoughResourcesException if there aren't enough resources to move
     * @throws NoExtraSlotException        if there isn't an active extra slot for the resource
     * @throws FullExtraSlotException      if the extra slot is already full
     * @throws NotEnoughSpaceException     if the number of moved resources is greater than the available space
     */
    private boolean canMoveToLeader(int shelfNum, int quantity) throws IllegalArgumentException, NotEnoughSpaceException, NotEnoughResourcesException, NoExtraSlotException, FullExtraSlotException {
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
     * @param shelfNum: the  number of the shelf on which move the resources; the value is between 1 and 3: 1 is the smallest shelf and 3 is the largest
     * @param quantity: the number of resources to move; the value is between 1 and 3: 1 is the smallest shelf and 3 is the largest
     * @return true if all the parameters are legal and if the action can be performed
     * @throws IllegalArgumentException    if the resources can't be removed from the shelf and if they can't be added to de extra slot
     * @throws NotEnoughResourcesException if there aren't enough resources to move
     * @throws NoExtraSlotException        if there isn't an active extra slot for the resource
     * @throws FullExtraSlotException      if the extra slot is already full
     * @throws NotEnoughSpaceException     if the number of moved resources is greater than the available space
     */
    public boolean moveToLeader(int shelfNum, int quantity) throws IllegalArgumentException, NotEnoughSpaceException, NotEnoughResourcesException, NoExtraSlotException, FullExtraSlotException {
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
     * @param shelfNum: the  number of the shelf on which move the resources; the value is between 1 and 3: 1 is the smallest shelf and 3 is the largest
     * @return true if all the parameters are legal and if the action can be performed
     * @throws IllegalArgumentException       if the resources can't be removed from the extra slot and if they can't be added to the shelf
     * @throws NotEnoughResourcesException    if there aren't enough resources to move
     * @throws NoExtraSlotException           if there isn't an active extra slot for the resource
     * @throws AlreadyInAnotherShelfException if the resource is already in the depot in another shelf
     * @throws NotEnoughSpaceException        if the number of moved resources is greater than the available space
     */
    private boolean canMoveToShelf(ResourceType resource, int quantity, int shelfNum) throws IllegalArgumentException, NotEnoughSpaceException, AlreadyInAnotherShelfException, NoExtraSlotException, NotEnoughResourcesException {
        canRemoveFromLeader(resource, quantity);
        canAddToShelf(resource, quantity, shelfNum);
        return true;
    }

    /**
     * Controls if all the parameters are legal and if yes, performs the action
     *
     * @param resource: the type of the resource to move to the shelf
     * @param quantity: the quantity to move
     * @param shelfNum: the  number of the shelf on which move the resources; the value is between 1 and 3: 1 is the smallest shelf and 3 is the largest
     * @return true if all the parameters are legal and if the action can be performed
     * @throws IllegalArgumentException       if the resources can't be removed from the extra slot and if they can't be added to the shelf
     * @throws IllegalArgumentException       if the resources can't be removed from the extra slot and if they can't be added to the shelf
     * @throws NotEnoughResourcesException    if there aren't enough resources to move
     * @throws NoExtraSlotException           if there isn't an active extra slot for the resource
     * @throws AlreadyInAnotherShelfException if the resource is already in the depot in another shelf
     * @throws NotEnoughSpaceException        if the number of moved resources is greater than the available space
     */
    public boolean moveToShelf(ResourceType resource, int quantity, int shelfNum) throws IllegalArgumentException, NotEnoughSpaceException, AlreadyInAnotherShelfException, NoExtraSlotException, NotEnoughResourcesException {
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
     * @param shelfNum: the number of the shelf you want to know the type; the value is between 1 and 3: 1 is the smallest shelf and 3 is the largest
     * @return the ResourceType of that shelf; null if the shelf is empty
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
    public int getExtraDepotLimit(ResourceType resoruce) {
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
    public int getExtraDepotValue(ResourceType resource) throws IllegalArgumentException {
        isFaithPoint(resource);
        return leaderDepot.get(resource);
    }

    /**
     * Returns the number of resources on the specified shelf
     *
     * @param index the index of the shelf (must be between 1 and 3)
     * @return the number of resources in the specified shelf
     */
    public int getNumberOfResOnShelf(int index) {
        int qt;
        ResourceType resourceType;

        controlShelfNum(index);

        resourceType = getShelfType(index);
        if (resourceType == null)
            return 0;

        qt = getResourceFromDepot(resourceType);

        return qt;
    }

    /**
     * Returns all the resources in the depot in a copy of the hashmap
     *
     * @return returns all the resources in the depot in a copy of the hashmap
     */
    public HashMap<ResourceType, Integer> getAllResources() {
        int resNum;
        HashMap<ResourceType, Integer> depotCopy;
        depotCopy = new HashMap<>(depotLevel);

        //TODO: provare a farlo con map.keySet
        for (ResourceType resource : ResourceType.values()) {
            if (!resource.isFaithPoint()) {
                resNum = depotCopy.get(resource);
                resNum += getExtraDepotValue(resource);
                depotCopy.put(resource, resNum);
            }
        }
        return depotCopy;
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
     * @param shelfNum: the number of the shelf; the value is between 1 and 3: 1 is the smallest shelf and 3 is the largest
     * @throws IllegalArgumentException if shelfNum isn't between 1 and 3
     */
    private void controlShelfNum(int shelfNum) throws IllegalArgumentException {
        if (shelfNum < 1 || shelfNum > 3)
            throw new IllegalArgumentException("ShelfNum out of bound");
    }

    /**
     * Returns a copy of the Extra Slots this player has
     *
     * @return a copy of the Extra Slots
     */
    public HashMap<ResourceType, Integer> getLeaderDepot() {
        return new HashMap<>(leaderDepot);
    }
}


