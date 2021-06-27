package it.polimi.ingsw.view.gui.ViewComponents.interfaces;

import it.polimi.ingsw.exceptions.IllegalActionException;
import it.polimi.ingsw.model.resources.ResourceType;

/**
 * This interfaces represents drop target that can be updated after a drop has been made onto them.
 */
public interface Droppable {

    /**
     * Updates the drop target with the specified information
     * @param intInfo an integer number that can be significant to the drop target
     * @param res the Resource type that has been dropped
     * @throws IllegalActionException if the drop cannot occur
     */
    void addDecision(Integer intInfo, ResourceType res) throws IllegalActionException;

    /**
    * Returns an integer information about the drop target (for example, if the drop target is a shelf, then it returns the number of the shelf
    * @return an integer representing an information about the drop target
    */
    int getDropInfo();
}
