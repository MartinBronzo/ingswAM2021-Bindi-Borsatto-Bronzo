package it.polimi.ingsw.view.gui.ViewComponents.interfaces;

import it.polimi.ingsw.exceptions.IllegalActionException;

import javax.swing.*;

/**
 * This interface represents all the functions that can be used to check whether a drop can be made.
 */
public interface DropChecker {

    /**
     * Checks whether a drop to the specified panel can be made
     * @param jPanel the panel where an object has been dropped
     * @return true if the drop can be made, false otherwise
     * @throws IllegalActionException if the drop cannot be made. It contains a message with useful information to be show at the user
     */
    boolean test(JPanel jPanel) throws IllegalActionException;
}
