package it.polimi.ingsw;

import it.polimi.ingsw.exceptions.IllegalActionException;
import it.polimi.ingsw.exceptions.IllegalParameterException;
import it.polimi.ingsw.exceptions.NotEnoughSpaceException;

import java.util.HashMap;

public class Depot {
    private HashMap<ResourceType, Integer> depotLevel;
    private ResourceType[] shelves;
    private HashMap<ResourceType, Integer> leaderDepot;
    private HashMap<ResourceType, Integer> depotLimit;

    public Depot() {
        depotLevel = new HashMap<>();
        leaderDepot = new HashMap<>();
        depotLimit = new HashMap<>();

        for (ResourceType resource : ResourceType.values()) {
            if (!resource.isFaithPoint()) {
                depotLevel.put(resource, 0);
                depotLimit.put(resource, 0);
            }

        }
        shelves = new ResourceType[3];
    }


    /**
     * controls if the input are legal and if the resource isn't alread on another shelf
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
     * controls if the action can be executed and if yes, performs the action
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


}


