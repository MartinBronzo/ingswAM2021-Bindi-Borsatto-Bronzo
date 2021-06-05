package it.polimi.ingsw.view.gui.ViewComponents;

import it.polimi.ingsw.exceptions.IllegalActionException;
import it.polimi.ingsw.model.ResourceType;
import it.polimi.ingsw.view.gui.ViewComponents.DepotDragDrop.DropChecker;
import it.polimi.ingsw.view.gui.ViewComponents.interfaces.Resettable;

import javax.swing.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CheckLimitedDrop implements DropChecker, Resettable {
    private HashMap<ResourceType, Integer> resToBeTaken;
    private List<ResourceType> typeList;
    private HashMap<ResourceType, Integer> copyOriginalResToBeTaken;

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

    public boolean hasPlayerSpecifiedEverything(){
        return this.resToBeTaken.isEmpty();
    }

    @Override
    public void resetState() {
        this.resToBeTaken = new HashMap<>(copyOriginalResToBeTaken);
    }
}
