package it.polimi.ingsw.view.gui.ViewComponents.leaderCards.leaderCardsDrop;

import it.polimi.ingsw.exceptions.IllegalActionException;
import it.polimi.ingsw.view.gui.ViewComponents.interfaces.DropChecker;
import it.polimi.ingsw.view.gui.ViewComponents.interfaces.Resettable;

import javax.swing.*;
import java.util.ArrayList;
import java.util.List;

/**
 * This DropChecker function is used to check whether a drop can be made onto a collection of LeaderCardDrop panels.
 */
public class CheckMoveToLeader implements DropChecker, Resettable {
    private List<LeaderCardDrop> leaders;
    private LeaderCardDrop destination;

  /*  public CheckMoveToLeader() {
        this.leaders = new ArrayList<>();
        this.destination = null;
    }

    public void addLeaderCard(LeaderCardDrop leader){
        this.leaders.add(leader);
    }
*/

    /**
     * Constructs a CheckMoveToLeader function which checks the drop onto the specified LeaderCardDrops
     * @param leaders a list of LeaderCardDrop panels
     */
    public CheckMoveToLeader(List<LeaderCardDrop> leaders) {
        this.leaders = leaders;
        this.destination = null;
    }

    /**
     * Sets the list of LeaderCardDrop this DropChecker function controls the drop onto
     * @param leaders a list of LeaderCardDrop panels
     */
    public void setLeaders(List<LeaderCardDrop> leaders) {
        this.leaders = leaders;
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
