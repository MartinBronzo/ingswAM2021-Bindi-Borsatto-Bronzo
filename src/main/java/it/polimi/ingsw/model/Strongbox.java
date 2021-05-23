package it.polimi.ingsw.model;

import it.polimi.ingsw.exceptions.NotEnoughResourcesException;

import java.util.HashMap;

/**
 * Strongbox class represents the strongbox of the game where resources can be stored
 */
public class Strongbox {
    private final HashMap<ResourceType, Integer> strongBoxResources;

    /**
     * The constructor initialize the HashMap with all the ResourceTypes with value of 0. FaithPoint isn't added
     */
    public Strongbox() {
        strongBoxResources = new HashMap<>();

        for (ResourceType resource : ResourceType.values()) {
            if (!resource.isFaithPoint())
                strongBoxResources.put(resource, 0);
        }
    }

    /**
     * Constructs a copy of the specified Strongbox
     *
     * @param strongbox the Strongbox to be cloned
     */
    public Strongbox(Strongbox strongbox) {
        this.strongBoxResources = new HashMap<>(strongbox.strongBoxResources);
    }

    /**
     * The method adds all the resources inside resMap to strongBoxResources, adding them to the previous value
     *
     * @param resMap: is an HashMap that contains all the resources to be stored in the strongbox
     * @return true if the action is performed without errors
     * @throws IllegalArgumentException if a value in resMap is negative; if resMap contains faithPoints
     */
    public boolean addResource(HashMap<ResourceType, Integer> resMap) throws IllegalArgumentException {
        int actualValue;
        int addValue;
        int newValue;

        for (ResourceType resource : resMap.keySet()) {
            if (resource == ResourceType.FAITHPOINT)
                throw new IllegalArgumentException("Can't add Faith point");

            addValue = resMap.get(resource);

            if (addValue < 0)
                throw new IllegalArgumentException("Negative quantity");

            if (addValue > 0) {
                actualValue = strongBoxResources.get(resource);
                newValue = actualValue + addValue;
                strongBoxResources.put(resource, newValue);
            }
        }
        return true;
    }

    /**
     * The method removes all the resources inside resMap from strongBoxResources, subtracting the value of the
     * resMap's resource from strongBoxResources. If the result is < 0 then throws an exception
     *
     * @param resMap: is an HashMap that contains all the resources to be removed from the strongbox
     *                return true if the action is performed without errors
     * @throws NotEnoughResourcesException if there are not enough resources to remove
     * @throws IllegalArgumentException    if a value in resMap is negative; if resMap contains faithPoints
     */
    public boolean removeResource(HashMap<ResourceType, Integer> resMap) throws IllegalArgumentException, NotEnoughResourcesException {
        int actualValue;
        int removeValue;
        int newValue;

        for (ResourceType resource : resMap.keySet()) {
            removeValue = resMap.get(resource);

            if (resource == ResourceType.FAITHPOINT)
                throw new IllegalArgumentException("Can't add Faith point");

            if (removeValue < 0)
                throw new IllegalArgumentException("Negative quantity");

            if (removeValue > 0) {
                actualValue = strongBoxResources.get(resource);
                newValue = actualValue - removeValue;
                if (newValue < 0) {
                    throw new NotEnoughResourcesException("Not enough resources to remove");
                }
                strongBoxResources.put(resource, newValue);
            }
        }
        return true;
    }

    /**
     * @param resource: the resource you want to know the value
     * @return the value of the specified resource in the strongbox
     */
    public int getResource(ResourceType resource) {
        return strongBoxResources.get(resource);
    }

    /**
     * Returns all the resources in the strongbox in a copy of the hashmap
     *
     * @return returns all the resources in the strongbox in a copy of the hashmap
     */
    public HashMap<ResourceType, Integer> getAllResources() {
        return new HashMap<>(strongBoxResources);
    }

}
