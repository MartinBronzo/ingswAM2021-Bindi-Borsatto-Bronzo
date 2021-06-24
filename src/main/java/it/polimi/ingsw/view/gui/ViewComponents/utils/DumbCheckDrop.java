package it.polimi.ingsw.view.gui.ViewComponents.utils;

import it.polimi.ingsw.exceptions.IllegalActionException;
import it.polimi.ingsw.view.gui.ViewComponents.interfaces.DropChecker;

import javax.swing.*;

/**
 * This DropChecker always allows the drop.
 */
public class DumbCheckDrop implements DropChecker {
    @Override
    public boolean test(JPanel panel) throws IllegalActionException {
        return true;
    }
}
