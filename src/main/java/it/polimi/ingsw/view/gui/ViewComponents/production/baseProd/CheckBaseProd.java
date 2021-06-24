package it.polimi.ingsw.view.gui.ViewComponents.production.baseProd;

import it.polimi.ingsw.exceptions.IllegalActionException;
import it.polimi.ingsw.view.gui.ViewComponents.interfaces.DropChecker;

import javax.swing.*;

public class CheckBaseProd implements DropChecker {
    DragAndDropBaseProd dragAndDropBaseProd;

    public CheckBaseProd(DragAndDropBaseProd dragAndDropBaseProd) {
        this.dragAndDropBaseProd = dragAndDropBaseProd;
    }

    @Override
    public boolean test(JPanel panel) throws IllegalActionException {
        return true;
    }
}
