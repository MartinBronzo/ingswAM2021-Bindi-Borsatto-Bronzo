package it.polimi.ingsw.view.gui.ViewComponents.interfaces;

import it.polimi.ingsw.view.gui.ViewComponents.interfaces.Resettable;

import javax.swing.*;

/**
 * The objects that implement this interface can have their inner state brought back at the original state, before any drop have taken place.
 * This interface is implemented by drop target object because it has a method which can be used to pass the reference to the object of a newly added label onto the object itself.
 */
public interface DropResettable extends Resettable {

    /**
     * Communicates to this object that the specified label has been dropped onto it (this information is used by the DropResettable object
     * when it has to reset its state).
     * @param label the reference to the added label
     */
    void addDroppedLabel(JLabel label);
}
