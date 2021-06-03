package it.polimi.ingsw.view.gui.ViewComponents;

import it.polimi.ingsw.exceptions.IllegalActionException;
import it.polimi.ingsw.view.gui.ViewComponents.DepotDragDrop.DropChecker;

import javax.swing.*;

public class CheckLimitedDrop implements DropChecker {
    @Override
    public boolean test(JPanel panel) throws IllegalActionException {
        return false;
    }
}
