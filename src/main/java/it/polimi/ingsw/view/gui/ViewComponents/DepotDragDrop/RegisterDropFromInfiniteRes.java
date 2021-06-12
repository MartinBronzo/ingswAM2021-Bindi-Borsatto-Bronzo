package it.polimi.ingsw.view.gui.ViewComponents.DepotDragDrop;
import it.polimi.ingsw.exceptions.IllegalActionException;
import it.polimi.ingsw.model.ResourceType;
import it.polimi.ingsw.view.gui.ViewComponents.interfaces.RegisterDropInterface;

import javax.swing.*;

public class RegisterDropFromInfiniteRes implements RegisterDropInterface {
    private final Droppable dropTarget;

    public RegisterDropFromInfiniteRes(Droppable dropTarget){
        this.dropTarget = dropTarget;
    }
    @Override
    public void accept(Icon icon) throws IllegalActionException {
        ResourceType imageType = ResourceType.valueOf(((ImageIcon) icon).getDescription().split(" ")[1]);

        //Checks whether the drop can be made into the Shelf
        ShelfDrop shelf = (ShelfDrop) dropTarget;
        if(shelf.getTypeDropped() != null && !shelf.getTypeDropped().equals(imageType))
            throw new IllegalActionException("You cannot drop different type of resources onto the same shelf!");

        //Updates the DropTarget about what drop has been made
        dropTarget.addDecision(dropTarget.getShelfNumber(), imageType);

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