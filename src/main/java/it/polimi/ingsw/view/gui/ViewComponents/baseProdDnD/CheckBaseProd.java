package it.polimi.ingsw.view.gui.ViewComponents.baseProdDnD;

import it.polimi.ingsw.exceptions.IllegalActionException;
import it.polimi.ingsw.model.ResourceType;
import it.polimi.ingsw.view.gui.ViewComponents.DepotDragDrop.DropChecker;
import it.polimi.ingsw.view.gui.ViewComponents.DepotDragDrop.MyDepotPanel;
import it.polimi.ingsw.view.gui.panels.PanelManager;

import javax.swing.*;
import java.util.List;

public class CheckBaseProd implements DropChecker {
    DragAndDropBaseProd dragAndDropBaseProd;

    public CheckBaseProd(DragAndDropBaseProd dragAndDropBaseProd) {
        this.dragAndDropBaseProd = dragAndDropBaseProd;
    }

    @Override
    public boolean test(JPanel panel) throws IllegalActionException {

        //TODO: THIS IS THE CHECK AFTER THE PRESSION OF THE BUTTON
        /*//Checks whether the user has already specified all the resources they are supposed to
        if (dragAndDropBaseProd.getOutput() == null)
            throw new IllegalActionException("Specify the resource for the output");

        List<ResourceType> inputs = dragAndDropBaseProd.getInputs();
        if (inputs.size() != 2)
            throw new IllegalActionException("Specify all the resources for the input");

        for (ResourceType res : inputs) {
            if (res == null)
                throw new IllegalActionException("Specify all the resources for the input");
        }*/

        return true;
    }
}
