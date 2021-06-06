package it.polimi.ingsw.view.gui.ViewComponents;

import it.polimi.ingsw.exceptions.IllegalActionException;
import it.polimi.ingsw.model.ResourceType;
import it.polimi.ingsw.view.gui.ViewComponents.interfaces.RegisterDropInterface;

import javax.swing.*;
import java.util.List;

public class RegisterMoveToLeader implements RegisterDropInterface {
    private List<LeaderCardDrop> leaders;
    private DepotDrag depotDrag;

    public RegisterMoveToLeader(List<LeaderCardDrop> leaders, DepotDrag depotDrag) {
        this.leaders = leaders;
        this.depotDrag = depotDrag;
    }

    @Override
    public void accept(Icon icon) throws IllegalActionException {
        //Preparing using parameters
        String info = ((ImageIcon) icon).getDescription();
        String infos[] = info.split(" ");
        ResourceType resDropped = ResourceType.valueOf(infos[1]);

        //Finding the LeaderCard where the resource was dropped
        LeaderCardDrop leaderDest = this.leaders.stream().filter(LeaderCardDrop::isDroppedHere).findAny().get();

        //The player cannot drop a resource which is different in type from the ones the LeaderCard can store
        if(!(resDropped.equals(leaderDest.getResStored())))
            throw new IllegalActionException("You can only store " + leaderDest.getResStored() + "s here!");

        depotDrag.updateAfterDrop(infos[1] + " " + infos[2]);
        leaderDest.addDecision(infos[2]);
        for(LeaderCardDrop l : this.leaders)
            l.setDroppedHere(false);
    }
}
