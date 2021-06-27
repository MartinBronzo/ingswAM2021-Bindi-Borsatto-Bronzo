package it.polimi.ingsw.view.gui.ViewComponents.interfaces;

import it.polimi.ingsw.exceptions.IllegalActionException;

import javax.swing.*;

/**
 * This interface is represented by the functions that are used to take track of the drops made onto an object.
 */
public interface RegisterDropInterface {

    /**
     * Takes not that a drop has been made of the specified icon
     * @param icon the dropped icon
     * @throws IllegalActionException if the drop couldn't have been made
     */
    void accept(Icon icon) throws IllegalActionException;
}
