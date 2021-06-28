package it.polimi.ingsw.view.gui.ViewComponents.production.baseProd;

import it.polimi.ingsw.exceptions.IllegalActionException;
import it.polimi.ingsw.view.gui.ViewComponents.interfaces.DropChecker;

import javax.swing.*;

/**
 * This DropChecker is used to check whether the drop can be made onto the BaseProduction image. Right now, it always allows the drop.
 */
public class CheckBaseProd implements DropChecker {
    DragAndDropBaseProd dragAndDropBaseProd;

    /**
     * Constructs a CheckBaseProd function for the specified DragAndDropBaseProd panel
     * @param dragAndDropBaseProd a DragAndDropBaseProd whose drops this CheckBaseProd controls
     */
    public CheckBaseProd(DragAndDropBaseProd dragAndDropBaseProd) {
        this.dragAndDropBaseProd = dragAndDropBaseProd;
    }

    @Override
    public boolean test(JPanel panel) throws IllegalActionException {
        return true;
    }
}
