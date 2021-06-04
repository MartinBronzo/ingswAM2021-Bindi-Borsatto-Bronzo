package it.polimi.ingsw.view.gui.ViewComponents.DepotDragDrop;

import it.polimi.ingsw.exceptions.IllegalActionException;

import javax.swing.*;

public interface DropChecker {

    boolean test(JPanel jPanel) throws IllegalActionException;
}
