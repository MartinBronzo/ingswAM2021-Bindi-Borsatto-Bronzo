package it.polimi.ingsw.view.gui.ViewComponents.depot.depotDrop;

import it.polimi.ingsw.exceptions.IllegalActionException;
import it.polimi.ingsw.model.resources.ResourceType;
import it.polimi.ingsw.view.gui.ViewComponents.interfaces.Droppable;
import it.polimi.ingsw.view.gui.ViewComponents.interfaces.RegisterDropInterface;
import it.polimi.ingsw.view.gui.ViewComponents.resources.resourcesDrag.LimitedResourcesDrag;

import javax.swing.*;

/**
 * This class registers the drop the user makes from a panel containing finite resources (LimitedResourcesDrag object) onto a Droppable object.
 */
public class RegisterDropFromFiniteRes implements RegisterDropInterface {
    public LimitedResourcesDrag start;
    private final Droppable dropTarget;

    /**
     * Constructs a RegisterDropFromFiniteRes function
     * @param dropTarget the Droppable object where objects will be dropped onto
     * @param start a LimitedResourcesDrag panel which is the source of the dragged resources
     */
    public RegisterDropFromFiniteRes(Droppable dropTarget, LimitedResourcesDrag start){
        this.dropTarget = dropTarget;
        this.start = start;
    }

    @Override
    public void accept(Icon icon) throws IllegalActionException {
        //We update our decision counters
        dropTarget.addDecision(dropTarget.getDropInfo(), ResourceType.valueOf(((ImageIcon) icon).getDescription().split(" ")[1]));

        //If we arrive here then the drop can be made
        //We update the LimitedResourceDrag by removing the resource that has been dragged
        this.start.updateAfterDrop(((ImageIcon) icon).getDescription().split(" ")[1]);
    }
}