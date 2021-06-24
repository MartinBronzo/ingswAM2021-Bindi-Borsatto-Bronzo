package it.polimi.ingsw.view.gui.ViewComponents.interfaces;

import it.polimi.ingsw.exceptions.IllegalActionException;
import it.polimi.ingsw.model.resources.ResourceType;

public interface Droppable {

    void addDecision(Integer shelf, ResourceType res) throws IllegalActionException;

    int getShelfNumber();
}
