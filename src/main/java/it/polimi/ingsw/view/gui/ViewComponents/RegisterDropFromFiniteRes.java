package it.polimi.ingsw.view.gui.ViewComponents;

import it.polimi.ingsw.exceptions.IllegalActionException;
import it.polimi.ingsw.model.ResourceType;
import it.polimi.ingsw.view.gui.ViewComponents.DepotDragDrop.Droppable;
import it.polimi.ingsw.view.gui.ViewComponents.interfaces.RegisterDropInterface;

import javax.swing.*;

public class RegisterDropFromFiniteRes implements RegisterDropInterface {
    public LimitedResourcesDrag start;
    private final Droppable dropTarget;

    public RegisterDropFromFiniteRes(Droppable dropTarget, LimitedResourcesDrag start){
        this.dropTarget = dropTarget;
        this.start = start;
    }

    @Override
    public void accept(Icon icon) throws IllegalActionException {
        //We update the LimitedResourceDrag by removing the resource that has been dragged
        this.start.updateAfterDrop(((ImageIcon) icon).getDescription().split(" ")[1]);

        //We update our decision counters
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
        dropTarget.addDecision(dropTarget.getShelfNumber(), ResourceType.valueOf(((ImageIcon) icon).getDescription().split(" ")[1]));


    }
}