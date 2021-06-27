package it.polimi.ingsw.view.gui.ViewComponents.resources.resourcesDrop;

import it.polimi.ingsw.exceptions.IllegalActionException;
import it.polimi.ingsw.model.resources.ResourceType;
import it.polimi.ingsw.view.gui.ViewComponents.interfaces.DropChecker;
import it.polimi.ingsw.view.gui.ViewComponents.interfaces.Resettable;

import javax.swing.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * This DropChecker is used to checks whether drops to a panel which can only hold a limited amount of resources can be made. The
 * player is supposed to drop onto the panel only certain resources with a certain quantity.
 */
public class CheckLimitedDrop implements DropChecker, Resettable {
    private HashMap<ResourceType, Integer> resToBeTaken;
    private List<ResourceType> typeList;
    private HashMap<ResourceType, Integer> copyOriginalResToBeTaken;

    /**
     * Constructs a CheckLimitedDrop function
     * @param resToBeTaken the resources that can be dropped
     */
    public CheckLimitedDrop(HashMap<ResourceType, Integer> resToBeTaken) {
        this.resToBeTaken = resToBeTaken;
        typeList = new ArrayList<>();
        for(Map.Entry<ResourceType, Integer> e : resToBeTaken.entrySet()){
            typeList.add(e.getKey());
        }
        this.copyOriginalResToBeTaken = new HashMap<>(resToBeTaken);
    }

    @Override
    public boolean test(JPanel panel) throws IllegalActionException {
        PanelDrop pDrop = (PanelDrop) panel;
        if(resToBeTaken.size() == 0)
            throw new IllegalActionException("You have already specified all the resources!");
        return true;
    }

    /**
     * Updates this function after a drop
     * @param type the type of the resource that has been dropped
     * @return true if the player can still make drops, false if the player has made the last drop
     * @throws IllegalActionException if the drop couldn't have taken place because either there was no need to drop this kind of resource
     * or the player dropped all the resources they needed for the specified type
     */
    public boolean updateResCounter(ResourceType type) throws IllegalActionException{
        if(!typeList.contains(type))
            throw new IllegalActionException("You are not required to add a " + type + " resource!");
        if(resToBeTaken.get(type) == null)
            throw new IllegalActionException("You have already specified all the " + type + " resources you need!");
        int oldNum = resToBeTaken.get(type);
        oldNum--;
        if(oldNum == 0){
            resToBeTaken.remove(type);
            return false;
        }
        resToBeTaken.put(type, oldNum);
        return true;
    }

    /**
     * Returns whether the player has specified all the drops they were supposed to
     * @return true if the player has specified all the drops they were supposed to, false otherwise
     */
    public boolean hasPlayerSpecifiedEverything(){
        return this.resToBeTaken.isEmpty();
    }

    @Override
    public void resetState() {
        this.resToBeTaken = new HashMap<>(copyOriginalResToBeTaken);
    }
}
