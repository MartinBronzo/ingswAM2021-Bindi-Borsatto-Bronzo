package it.polimi.ingsw.view.gui.ViewComponents.baseProdDnD;

import it.polimi.ingsw.exceptions.IllegalActionException;
import it.polimi.ingsw.model.ResourceType;
import it.polimi.ingsw.view.gui.ViewComponents.DepotDragDrop.DropChecker;

import javax.swing.*;
import java.util.List;

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
