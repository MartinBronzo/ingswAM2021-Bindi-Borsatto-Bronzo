package it.polimi.ingsw.view.gui.ViewComponents.depot.depotDrop;
import it.polimi.ingsw.exceptions.IllegalActionException;
import it.polimi.ingsw.model.resources.ResourceType;
import it.polimi.ingsw.view.gui.ViewComponents.interfaces.Droppable;
import it.polimi.ingsw.view.gui.ViewComponents.interfaces.RegisterDropInterface;

import javax.swing.*;

/**
 * This class registers the drop the user makes from a panel containing infinite resources onto a Droppable object.
 */
public class RegisterDropFromInfiniteRes implements RegisterDropInterface {
    private final Droppable dropTarget;

    /**
     * Constructs a RegisterDropFromInfiniteRes function
     * @param dropTarget the Droppable object where the resources will be dropped onto
     */
    public RegisterDropFromInfiniteRes(Droppable dropTarget){
        this.dropTarget = dropTarget;
    }

    @Override
    public void accept(Icon icon) throws IllegalActionException {
        ResourceType imageType = ResourceType.valueOf(((ImageIcon) icon).getDescription().split(" ")[1]);

        //Checks whether the drop can be made into the Shelf
        ShelfDrop shelf = (ShelfDrop) dropTarget;
        if(shelf.getTypeForShelf() != null && !shelf.getTypeForShelf().equals(imageType))
            throw new IllegalActionException("You cannot drop different type of resources onto the same shelf!");

        //Updates the DropTarget about what drop has been made
        dropTarget.addDecision(dropTarget.getDropInfo(), imageType);

        /*switch (icon.toString()){
            case "src/main/resources/coins small.png":
                dropTarget.addDecision(dropTarget.getShelfNumber(), ResourceType.COIN);
                break;
            case "src/main/resources/servant small.png":
                dropTarget.addDecision(dropTarget.getShelfNumber(), ResourceType.SERVANT);
                break;
            case "src/main/resources/shield small.png":
                dropTarget.addDecision(dropTarget.getShelfNumber(), ResourceType.SHIELD);
                break;
            case "src/main/resources/stone small.png":
                dropTarget.addDecision(dropTarget.getShelfNumber(), ResourceType.STONE);
                break;
        }*/
    }
}