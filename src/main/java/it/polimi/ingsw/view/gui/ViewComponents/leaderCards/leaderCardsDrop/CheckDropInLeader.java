package it.polimi.ingsw.view.gui.ViewComponents.leaderCards.leaderCardsDrop;

import it.polimi.ingsw.exceptions.IllegalActionException;
import it.polimi.ingsw.view.gui.ViewComponents.interfaces.DropChecker;
import it.polimi.ingsw.view.gui.mainViews.PanelManager;

import javax.swing.*;

/**
 * This class represents the CheckDrop function for any LeaderCard.
 */
//We can use the same CheckDrop function for every LeaderCard present
public class CheckDropInLeader implements DropChecker {

    @Override
    public boolean test(JPanel jPanel) throws IllegalActionException {
        PanelManager manager = PanelManager.getInstance();
        LeaderCardDrop dropped = (LeaderCardDrop) jPanel;

        //Checks whether there is still room onto the shelf
        if(dropped.getDroppedHereAmount() + dropped.getOriginalPresent() == dropped.getMaximumCapacity())
            throw new IllegalActionException("There is no room on this LeaderCard!");

        return true;
    }
}
