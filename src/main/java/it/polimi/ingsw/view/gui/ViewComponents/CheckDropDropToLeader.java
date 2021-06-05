package it.polimi.ingsw.view.gui.ViewComponents;

import it.polimi.ingsw.exceptions.IllegalActionException;
import it.polimi.ingsw.view.gui.ViewComponents.DepotDragDrop.DropChecker;

import javax.swing.*;
import java.util.ArrayList;
import java.util.List;

public class CheckDropDropToLeader implements DropChecker, Resettable{
    private List<LeaderCardDrop> leaders;
    private LeaderCardDrop destination;

    public CheckDropDropToLeader() {
        this.leaders = new ArrayList<>();
        this.destination = null;
    }

    public void addLeaderCard(LeaderCardDrop leader){
        this.leaders.add(leader);
    }

    public void setLeaders(List<LeaderCardDrop> leaders) {
        this.leaders = leaders;
    }

    public CheckDropDropToLeader(List<LeaderCardDrop> leaders) {
        this.leaders = leaders;
        this.destination = null;
    }

    @Override
    public boolean test(JPanel jPanel) throws IllegalActionException {
        LeaderCardDrop leader = (LeaderCardDrop) jPanel;
        if(destination == null)
            destination = leader;
        else if(destination != leader)
            throw new IllegalActionException("You cannot move resources to two different LeaderCards at the same time");
        if(leader.getOriginalPresent() + leader.getQuantity() == leader.getMaximumCapacity())
           throw new IllegalActionException("This LeaderCard is full!");
        leader.setDroppedHere(true);
        return true;
    }

    @Override
    public void resetState() {
        this.destination = null;
    }
}
