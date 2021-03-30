package it.polimi.ingsw;

import it.polimi.ingsw.exceptions.NegativeQuantityException;

import java.util.HashMap;

public class Strongbox {
    private HashMap<ResourceType, Integer> strongBoxResources;

    //The constructor initialize the HashMap with all the ResourceTypes with value of 0. FaithPoint isn't added
    public Strongbox() {
        strongBoxResources = new HashMap<>();

        for (ResourceType resource : ResourceType.values()) {
            if (!resource.isFaithPoint())
                strongBoxResources.put(resource, 0);
        }
    }

    //The method adds all the resources inside resMap to strongBoxResources, adding them to the previous value
    public boolean addResource(HashMap<ResourceType, Integer> resMap) throws NegativeQuantityException {
        int actualValue;
        int addValue;
        int newValue;

        for (ResourceType resource : resMap.keySet()) {
            addValue = resMap.get(resource);

            if(addValue < 0)
                throw new NegativeQuantityException("Negative input quantity");

            if(addValue > 0) {
                actualValue = strongBoxResources.get(resource);
                newValue = actualValue + addValue;
                strongBoxResources.put(resource, newValue);
            }
        }
        return true;
    }

    /*The method removes all the resources inside resMap from strongBoxResources, subtracting the value of the
     * resMap's resource from strongBoxResources. If the result is < 0 then throws an exception*/
    public boolean removeResource(HashMap<ResourceType, Integer> resMap) throws NegativeQuantityException {
        int actualValue;
        int removeValue;
        int newValue;

        for (ResourceType resource : resMap.keySet()) {
            removeValue = resMap.get(resource);
            if(removeValue < 0)
                throw new NegativeQuantityException("Negative input quantity");

            if (removeValue > 0) {
                actualValue = strongBoxResources.get(resource);
                newValue = actualValue - removeValue;
                if (newValue < 0) {
                    throw new NegativeQuantityException("Not enough resources to remove");
                }
                strongBoxResources.put(resource, newValue);
            }
        }
        return true;
    }

    public int getResource(ResourceType resource){
        return strongBoxResources.get(resource);
    }


}
