package it.polimi.ingsw.view.gui.ViewComponents;

import it.polimi.ingsw.exceptions.IllegalActionException;
import it.polimi.ingsw.view.gui.ViewComponents.DepotDragDrop.DropChecker;

import javax.swing.*;

//This class has been used for testing purposes so far but it may be used for checking whether a drop in the trash can can be done
public class CheckTrashCanDrop implements DropChecker {
    @Override
    public boolean test(JPanel panel) throws IllegalActionException {
        return true;
    }
}
